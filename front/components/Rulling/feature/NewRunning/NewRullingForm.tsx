import { Controller, useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import { Input } from "@heroui/input";
import { DatePicker } from "@heroui/date-picker";
import {DateValue, getLocalTimeZone, now, ZonedDateTime} from "@internationalized/date";
import { Button } from "@heroui/button";
import clsx from "clsx";
import { Alert } from "@heroui/alert";
import { useEffect } from "react";

import NewRullingSchema from "@/domain/Rulling/Schemas/NewRulling.schema";
import TNewRulling from "@/domain/Rulling/Types/TNewRulling";
import useApiHookRegisterRulling from "@/domain/Rulling/Api/useApiHookRegisterRulling";

type Props = {
  fallback?: VoidFunction;
};

export default function NewRullingForm({ fallback }: Props) {
  const {
    control,
    setValue,
    formState: { isValid },
    handleSubmit,
    reset,
  } = useForm<TNewRulling>({
    resolver: zodResolver(NewRullingSchema),
    defaultValues: {
      description: "",
      expiration: "",
      title: "",
    },
  });
  const { isLoading, fetch, response } = useApiHookRegisterRulling();

  useEffect(() => {
    if (response && fallback) {
      reset();
      fallback();
    }
  }, [response]);


  return (
    <>
      <form
        onSubmit={handleSubmit((data) => {
          fetch(data);
        })}
      >
        <div className={"mb-2"}>
          <Controller
            control={control}
            name={"title"}
            render={({ field }) => <Input {...field} size={"sm"} />}
          />
        </div>
        <div className={"mb-2"}>
          <Controller
            control={control}
            name={"description"}
            render={({ field }) => <Input {...field} size={"sm"} />}
          />
        </div>
        <div className={"mb-2"}>
          <DatePicker
            showMonthAndYearPickers
            granularity={"minute"}
            hourStep={1}
            label="Expira em:"
            //@ts-ignore
            minValue={
              now(getLocalTimeZone()).add({
                minutes: 1,
              })
            }
            onChange={(dateValue) => {
              if (!dateValue) {
                setValue("expiration", "");

                return;
              }
              const isoDate = dateValue
                .toDate(getLocalTimeZone())
                .toISOString();

              setValue("expiration", isoDate);
            }}
          />
          <Alert
            className={"mt-2"}
            color={"warning"}
            description={
              "A data deve ser maior que agora, caso está não seja fornecida o sistema irá setar a data atual + 1 minuto!"
            }
            title={"Atenção!"}
            variant={"bordered"}
          />
        </div>
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
            Salvar
          </Button>
        </footer>
      </form>
    </>
  );
}
