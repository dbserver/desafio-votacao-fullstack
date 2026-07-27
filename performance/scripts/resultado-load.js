import http from "k6/http";
import { check } from "k6";
import { Counter, Rate, Trend } from "k6/metrics";

const LOAD_VUS = Number.parseInt(__ENV.VUS || "10", 10);
const LOAD_DURATION = __ENV.DURATION || "10m";

const baselineRequests = new Counter("baseline_requests");

const baselineFailures = new Rate("baseline_req_failed");

const baselineChecks = new Rate("baseline_checks");

const baselineDuration = new Trend("baseline_req_duration", true);

export const options = {
  scenarios: {
    warmup: {
      executor: "constant-vus",
      exec: "warmup",
      vus: Math.min(5, LOAD_VUS),
      duration: "30s",
      tags: {
        phase: "warmup",
      },
    },

    baseline: {
      executor: "constant-vus",
      exec: "baseline",
      vus: LOAD_VUS,
      duration: LOAD_DURATION,
      startTime: "35s",
      gracefulStop: "10s",
      tags: {
        phase: "baseline",
      },
    },
  },

  summaryTrendStats: ["min", "avg", "med", "p(90)", "p(95)", "p(99)", "max"],

  thresholds: {
    baseline_req_failed: ["rate==0"],
    baseline_checks: ["rate==1"],
  },
};

const BASE_URL = __ENV.BASE_URL || "http://localhost:8080";

export function setup() {
  if (!__ENV.PAUTA_ID) {
    throw new Error("Informe PAUTA_ID ao executar o teste");
  }

  return {
    resultadoUrl: `${BASE_URL}/api/v1/resultados/${__ENV.PAUTA_ID}`,
  };
}

function isJson(response) {
  try {
    response.json();
    return true;
  } catch {
    return false;
  }
}

export function warmup(data) {
  http.get(data.resultadoUrl, {
    tags: {
      endpoint: "consultar_resultado",
      phase: "warmup",
    },
  });
}

export function baseline(data) {
  const response = http.get(data.resultadoUrl, {
    tags: {
      endpoint: "consultar_resultado",
      phase: "baseline",
    },
  });

  const validResponse = response.status === 200 && isJson(response);

  baselineRequests.add(1);
  baselineFailures.add(!validResponse);
  baselineChecks.add(validResponse);
  baselineDuration.add(response.timings.duration);

  check(response, {
    "baseline: status é 200": (res) => res.status === 200,

    "baseline: resposta é JSON": (res) => isJson(res),
  });
}
