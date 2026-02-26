import { Controller, useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import { Input } from "@heroui/input";
import { Button } from "@heroui/button";
import clsx from "clsx";
import { useEffect } from "react";
import { useRouter } from "next/router";

import UserSignupSchema from "@/domain/User/Schemas/UserSignup.schema";
import useApiHookRegister from "@/domain/User/Api/useApiHookRegister";

export default function Register() {
  const { push } = useRouter();
  const {
    control,
    handleSubmit,
    formState: { isValid, errors },
  } = useForm({
    resolver: zodResolver(UserSignupSchema),
    defaultValues: {
      name: "",
      email: "",
    },
  });

  const { isLoading, fetch, response } = useApiHookRegister();

  useEffect(() => {
    if (response) {
      push("/profile/rulling/1");
    }
  }, [response]);

  return (
    <>
      <form onSubmit={handleSubmit((data) => fetch(data))}>
        <div className={"mb-2"}>
          <Controller
            control={control}
            name={"name"}
            render={({ field }) => (
              <Input
                size={"sm"}
                {...field}
                errorMessage={errors?.name?.message}
                isInvalid={!!(errors && errors.name)}
                label={"Nome"}
              />
            )}
          />
        </div>
        <div className={"mb-2"}>
          <Controller
            control={control}
            name={"email"}
            render={({ field }) => (
              <Input
                size={"sm"}
                {...field}
                errorMessage={errors?.email?.message}
                isInvalid={!!(errors && errors.email)}
                label={"E-mail"}
              />
            )}
          />
        </div>
        <footer className={"pt-1 flex justify-end"}>
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
            Cadastrar
          </Button>
        </footer>
      </form>
    </>
  );
}
