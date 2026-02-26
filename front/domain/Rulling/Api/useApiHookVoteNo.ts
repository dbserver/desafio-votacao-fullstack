import { useMemo, useState } from "react";
import { debounce } from "@heroui/shared-utils";
import { AxiosError } from "axios";

import ToastError from "@/components/Commons/UI/Toast/ToastError";
import { RullingService } from "@/domain/Rulling/Services/Rulling.service";
import { Rulling } from "@/domain/Rulling/Model/Rulling";
import ToastSuccess from "@/components/Commons/UI/Toast/ToastSuccess";

const useApiHookVoteNo = (): TApiHooks<Rulling, string> => {
  const [response, setResponse] = useState<Rulling | undefined>(undefined);
  const [error, setError] = useState<Error | undefined>(undefined);
  const [isLoading, setIsLoading] = useState<boolean>(false);

  const handleFetch = debounce((rullingId) => {
    if (isLoading) return;
    setIsLoading(true);

    RullingService.voteRullingNo(rullingId)
      .then(({ data, status }) => {
        if (status !== 200) {
          ToastError("Erro ao registrar o seu voto!");
          setError(new Error("Erro ao registrar o seu voto!"));

          return;
        }

        ToastSuccess("Voto registrado com sucesso! Obrigado por participar!");
        setResponse(data);
      })
      .catch((e: unknown) => {
        const err = e as AxiosError;
        const newError = new Error(err.message);

        if (err.status === 409) {
          ToastError("Um momento!", "Você já votou nessa Pauta!");
        } else if (err.status === 404) {
          ToastError("Essa pauta expirou ou não existe mais!");
        } else {
          ToastError(newError.message);
        }
        setError(newError);
      })
      .finally(() => setIsLoading(false));
  }, 200);

  return useMemo<TApiHooks<Rulling, string>>(
    () => ({
      error,
      response,
      isLoading,
      fetch: handleFetch,
    }),
    [isLoading, response, error],
  );
};

export default useApiHookVoteNo;
