import { useMemo, useState } from "react";
import { debounce } from "@heroui/shared-utils";
import { AxiosError } from "axios";

import TUserAuthToken from "@/domain/User/Types/TUserAuthToken";
import TUserLogin from "@/domain/User/Types/TUserLogin";
import { AuthService } from "@/domain/User/Services/Auth.service";
import ToastSuccess from "@/components/Commons/UI/Toast/ToastSuccess";
import ToastError from "@/components/Commons/UI/Toast/ToastError";

const useApiHookLogin = (): TApiHooks<TUserAuthToken, TUserLogin> => {
  const [response, setResponse] = useState<TUserAuthToken | undefined>(
    undefined,
  );
  const [error, setError] = useState<Error | undefined>(undefined);
  const [isLoading, setIsLoading] = useState<boolean>(false);

  const handleFetch = debounce((data: TUserLogin) => {
    if (isLoading) return;
    setIsLoading(true);

    AuthService.login(data)
      .then(({ data, status }) => {
        if (status !== 200) {
          ToastError("Erro ao realizar login!");
          setError(new Error("Erro ao realizar login!"));

          return;
        }
        ToastSuccess("Login realizado com sucesso!");
        setResponse(data);
        AuthService.saveSession(data.token);
      })
      .catch((e: unknown) => {
        const err = e as AxiosError;
        const newError = new Error(err.message);

        ToastError(newError.message);
        setError(newError);
      })
      .finally(() => setIsLoading(false));
  }, 200);

  return useMemo<TApiHooks<TUserAuthToken, TUserLogin>>(
    () => ({
      error,
      response,
      isLoading,
      fetch: handleFetch,
    }),
    [isLoading, response, error],
  );
};

export default useApiHookLogin;
