import { useMemo, useState } from "react";
import { debounce } from "@heroui/shared-utils";
import { AxiosError } from "axios";

import ToastError from "@/components/Commons/UI/Toast/ToastError";
import { RullingService } from "@/domain/Rulling/Services/Rulling.service";
import { Rulling } from "@/domain/Rulling/Model/Rulling";

const useApiHookRullings = (): TApiHooks<Rulling[], number> => {
  const [response, setResponse] = useState<Rulling[] | undefined>(undefined);
  const [error, setError] = useState<Error | undefined>(undefined);
  const [isLoading, setIsLoading] = useState<boolean>(false);

  const handleFetch = debounce((page) => {
    if (isLoading) return;
    setIsLoading(true);

    RullingService.getRullingsByPage(page)
      .then(({ data, status }) => {
        if (status !== 200) {
          ToastError("Erro ao obter as Pautas!");
          setError(new Error("Erro ao obter as Pautas"));

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

  return useMemo<TApiHooks<Rulling[], number>>(
    () => ({
      error,
      response,
      isLoading,
      fetch: handleFetch,
    }),
    [isLoading, response, error],
  );
};

export default useApiHookRullings;
