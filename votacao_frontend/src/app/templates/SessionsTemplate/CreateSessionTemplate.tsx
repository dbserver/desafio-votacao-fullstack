'use client';

import { useGetAllAvaiableAgendas } from '@/app/api/hookService/useAgendaService';
import { useCreateSection } from '@/app/api/hookService/useSectionService';
import Button from '@/app/components/Button';
import Container from '@/app/components/Container';
import Input from '@/app/components/Input';
import Label from '@/app/components/Label';
import Select from '@/app/components/Select';
import Spinner from '@/app/components/Spinner';
import { useRouter } from 'next/navigation';
import React, { useState } from 'react';
import { useForm } from 'react-hook-form';
import { Toast, ToastTypeEnum } from '@/app/components/Toast';

type TDataForm = {
  duration: number | null;
  agendaId: number | null;
};

const defaultValueForm: TDataForm = {
  duration: null,
  agendaId: null,
};

export default function CreateSessionTemplate() {
  const [errorMessage, setErrorMessage] = useState<string>('');

  const router = useRouter();

  const { data: allAgendas } = useGetAllAvaiableAgendas({ sortByName: true });
  const { mutate: createSection, isPending } = useCreateSection();

  const agendaOptions = React.useMemo(() => {
    return (
      allAgendas?.map((agenda) => ({
        value: agenda.id,
        label: agenda.name,
      })) ?? []
    );
  }, [allAgendas]);

  const { handleSubmit, register } = useForm<TDataForm>({
    defaultValues: defaultValueForm,
  });

  const onSubmit = (data: TDataForm) => {
    const handleSuccess = () => {
      router.push('/sessoes');
    };

    const handleError = (error: Error) => {
      setErrorMessage(error.message);
    };

    createSection(data, {
      onSuccess: handleSuccess,
      onError: handleError,
    });
  };
  return (
    <div className="mt-10 md:mx-auto md:w-96">
      <Container>
        <form
          onSubmit={handleSubmit(onSubmit)}
          className="flex flex-col items-center justify-center p-4"
        >
          <Label text="Selecione uma pauta" isRequired>
            <Select
              field={register('agendaId')}
              options={agendaOptions}
              required={true}
            />
          </Label>
          <Label text="Tempo de duração, em minutos">
            <Input
              field={register('duration')}
              type="number"
              placeholder="Digite o tempo de duração da sessão"
            />
          </Label>
          <Button>{isPending ? <Spinner size={32} /> : 'Salvar'}</Button>
        </form>
      </Container>
      {errorMessage && (
        <Toast
          type={ToastTypeEnum.ERROR}
          message={errorMessage}
          onClose={() => setErrorMessage('')}
        />
      )}
    </div>
  );
}
