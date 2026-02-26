import { useMemo, useState } from "react";
import { debounce } from "@heroui/shared-utils";
import { AxiosError } from "axios";

import { AuthService } from "@/domain/User/Services/Auth.service";
import ToastSuccess from "@/components/Commons/UI/Toast/ToastSuccess";
import ToastError from "@/components/Commons/UI/Toast/ToastError";
import { User } from "@/domain/User/Model/User";
import TUserSignup from "@/domain/User/Types/TUserSignup";

const useApiHookRegister = (): TApiHooks<User, TUserSignup> => {
  const [response, setResponse] = useState<User | undefined>(undefined);
  const [error, setError] = useState<Error | undefined>(undefined);
  const [isLoading, setIsLoading] = useState<boolean>(false);

  const handleFetch = debounce((data: TUserSignup) => {
    if (isLoading) return;
    setIsLoading(true);

    AuthService.register(data)
      .then(({ data, status }) => {
        if (status !== 200) {
          ToastError("Erro ao realizar cadastro!");
          setError(new Error("Erro ao realizar cadastro!"));

          return;
        }
        ToastSuccess("Cadastro realizado com sucesso!");
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

  return useMemo<TApiHooks<User, TUserSignup>>(
    () => ({
      error,
      response,
      isLoading,
      fetch: handleFetch,
    }),
    [isLoading, response, error],
  );
};

export default useApiHookRegister;
