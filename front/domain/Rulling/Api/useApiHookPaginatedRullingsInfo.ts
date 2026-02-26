import { useMemo, useState } from "react";
import { debounce } from "@heroui/shared-utils";
import { AxiosError } from "axios";

import ToastError from "@/components/Commons/UI/Toast/ToastError";
import { RullingService } from "@/domain/Rulling/Services/Rulling.service";
import TRullingPaginationInfo from "@/domain/Rulling/Types/TRullingPaginationInfo";

const useApiHookPaginatedRullingsInfo = (): TApiHooks<
  TRullingPaginationInfo,
  unknown
> => {
  const [response, setResponse] = useState<TRullingPaginationInfo | undefined>(
    undefined,
  );
  const [error, setError] = useState<Error | undefined>(undefined);
  const [isLoading, setIsLoading] = useState<boolean>(false);

  const handleFetch = debounce(() => {
    if (isLoading) return;
    setIsLoading(true);

    RullingService.getPaginatedRullingsInfo()
      .then(({ data, status }) => {
        if (status !== 200) {
          ToastError("Erro ao obter informações das Pautas!");
          setError(new Error("Erro ao obter informações das Pautas"));

          return;
        }
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

  return useMemo<TApiHooks<TRullingPaginationInfo, unknown>>(
    () => ({
      error,
      response,
      isLoading,
      fetch: handleFetch,
    }),
    [isLoading, response, error],
  );
};

export default useApiHookPaginatedRullingsInfo;
