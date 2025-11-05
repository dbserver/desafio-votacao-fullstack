import Input from '@/app/components/Input';
import Label from '@/app/components/Label';
import React, { SetStateAction, useState } from 'react';
import { useForm } from 'react-hook-form';
import { useCreateAgenda } from '@/app/api/hookService/useAgendaService';
import { Toast, ToastTypeEnum } from '@/app/components/Toast';
import Button from '@/app/components/Button';

type TAgendaFormProps = {
  setOpenModalAgenda: React.Dispatch<SetStateAction<boolean>>;
};

type TDataFormAgenda = {
  name: string;
};

export default function AgendaForm({ setOpenModalAgenda }: TAgendaFormProps) {
  const [toastMessage, setToastMessage] = useState<string>('');
  const { handleSubmit, register } = useForm<{ name: string }>();
  const { mutate: createAgenda } = useCreateAgenda();

  const onSubmit = (data: TDataFormAgenda) => {
    const handleSuccess = () => {
      setOpenModalAgenda(false);
    };

    const handleError = (error: Error) => {
      setToastMessage(error.message);
    };

    createAgenda(data, {
      onSuccess: handleSuccess,
      onError: handleError,
    });
  };

  return (
    <>
      <form
        onSubmit={handleSubmit(onSubmit)}
        className="flex flex-col items-center gap-4"
      >
        <p>
          Use este espaço para criar uma nova pauta e disponibilizá-la para
          votação em uma nova sessão.
        </p>
        <Label text="Nome da pauta">
          <Input
            field={register('name')}
            required={true}
            placeholder="Digite o nome da pauta para votação"
          />
        </Label>
        <div className="w-max">
          <Button type="submit">Criar pauta</Button>
        </div>
      </form>
      {toastMessage && (
        <Toast
          type={ToastTypeEnum.ERROR}
          message={toastMessage}
          onClose={() => setToastMessage('')}
        />
      )}
    </>
  );
}
