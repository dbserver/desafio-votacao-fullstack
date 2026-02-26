import { useMemo, useState } from "react";
import { debounce } from "@heroui/shared-utils";
import { AxiosError } from "axios";

import ToastSuccess from "@/components/Commons/UI/Toast/ToastSuccess";
import ToastError from "@/components/Commons/UI/Toast/ToastError";
import { Rulling } from "@/domain/Rulling/Model/Rulling";
import TNewRulling from "@/domain/Rulling/Types/TNewRulling";
import { RullingService } from "@/domain/Rulling/Services/Rulling.service";

const useApiHookRegisterRulling = (): TApiHooks<Rulling, TNewRulling> => {
  const [response, setResponse] = useState<Rulling | undefined>(undefined);
  const [error, setError] = useState<Error | undefined>(undefined);
  const [isLoading, setIsLoading] = useState<boolean>(false);

  const handleFetch = debounce((data: TNewRulling) => {
    if (isLoading) return;
    setIsLoading(true);

    RullingService.registerRulling(data)
      .then(({ data, status }) => {
        if (status !== 200) {
          ToastError("Erro ao registrar essa Pauta!");
          setError(new Error("Erro ao registrar essa Pauta"));

          return;
        }
        ToastSuccess("Pauta registrada com sucesso!");
        setResponse(data);
      })
      .catch((e: unknown) => {
        const err = e as AxiosError;
        const newError = new Error(err.message);

        ToastError(newError.message);
        setError(newError);
      })
      .finally(() => setIsLoading(false));
  }, 200);

  return useMemo<TApiHooks<Rulling, TNewRulling>>(
    () => ({
      error,
      response,
      isLoading,
      fetch: handleFetch,
    }),
    [isLoading, response, error],
  );
};

export default useApiHookRegisterRulling;
