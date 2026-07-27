import http from "k6/http";
import { check } from "k6";

export const options = {
  vus: 1,
  iterations: 10,
  summaryTrendStats: ["min", "avg", "med", "p(90)", "p(95)", "p(99)", "max"],
  thresholds: {
    checks: ["rate==1"],
    http_req_failed: ["rate==0"],
    http_req_duration: ["p(95)<500"],
  },
};

const BASE_URL = __ENV.BASE_URL || "http://localhost:8080";

export function setup() {
  if (!__ENV.PAUTA_ID) {
    throw new Error(
      "Informe o ID da pauta: k6 run -e PAUTA_ID=1 performance/scripts/resultado-smoke.js",
    );
  }

  return {
    resultadoUrl: `${BASE_URL}/api/v1/resultados/${__ENV.PAUTA_ID}`,
  };
}

export default function (data) {
  const response = http.get(data.resultadoUrl, {
    tags: { endpoint: "consultar_resultado" },
  });

  check(response, {
    "status é 200": (res) => res.status === 200,
    "resposta é JSON": (res) => {
      try {
        res.json();
        return true;
      } catch {
        return false;
      }
    },
  });
}
