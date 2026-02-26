import { Controller, useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import { Input } from "@heroui/input";
import { Button } from "@heroui/button";
import clsx from "clsx";
import { useRouter } from "next/router";
import { useEffect } from "react";

import TUserLogin from "@/domain/User/Types/TUserLogin";
import UserLoginSchema from "@/domain/User/Schemas/UserLogin.schema";
import useApiHookLogin from "@/domain/User/Api/useApiHookLogin";

export default function Login() {
  const { response, isLoading, fetch } = useApiHookLogin();
  const { push } = useRouter();
  const {
    control,
    handleSubmit,
    formState: { isValid, errors },
  } = useForm<TUserLogin>({
    resolver: zodResolver(UserLoginSchema),
    defaultValues: {
      email: "",
    },
  });

  useEffect(() => {
    if (response && response.token) {
      push("/profile/rulling/1");
    }
  }, [response]);

  return (
    <form
      className={"w-full"}
      onSubmit={handleSubmit((data) => {
        fetch(data);
      })}
    >
      <Controller
        control={control}
        name={"email"}
        render={({ field }) => (
          <Input
            {...field}
            errorMessage={errors.email?.message}
            isInvalid={!!(errors && errors.email)}
            label={"E-mail:"}
            size={"sm"}
          />
        )}
      />
      <footer className={"pt-3 flex justify-end"}>
        <Button
          className={clsx({
            "opacity-50": !isValid,
            "cursor-not-allowed": !isValid,
          })}
          disabled={!isValid}
          isLoading={isLoading}
          size={"sm"}
          type={"submit"}
        >
          Entrar
        </Button>
      </footer>
    </form>
  );
}
