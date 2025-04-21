'use client';

import Label from '@/app/components/Label';
import React, { SetStateAction, useState } from 'react';
import { useForm } from 'react-hook-form';
import { Toast, ToastTypeEnum } from '@/app/components/Toast';
import Button from '@/app/components/Button';
import Select from '@/app/components/Select';
import Cookies from 'js-cookie';
import { useCreateVote } from '@/app/api/hookService/useVoteService';
import { VoteOptionEnum } from '@/app/api/types/vote';

type TVoteFormProps = {
  setOpenModalVote: React.Dispatch<SetStateAction<boolean>>;
  selectedSection: number | null;
};

type TDataFormVote = {
  voteOption: VoteOptionEnum;
};

const voteOptions = [
  { value: 'SIM', label: 'Sim' },
  { value: 'NAO', label: 'Não' },
];

export default function VoteForm({
  setOpenModalVote,
  selectedSection,
}: TVoteFormProps) {
  const [toastMessage, setToastMessage] = useState<string>('');
  const { handleSubmit, register } = useForm<TDataFormVote>();
  const cpf = Cookies.get('token');

  const { mutate: createVote } = useCreateVote();

  const onSubmit = (data: TDataFormVote) => {
    const payload = {
      voteOption: data.voteOption,
      sectionId: selectedSection as number,
      cpf: cpf as string,
    };

    const handleSuccess = () => {
      setOpenModalVote(false);
    };

    const handleError = (error: Error) => {
      setToastMessage(error.message);
    };

    createVote(payload, {
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
        <p>Escolha a opção para registrar seu voto nesta sessão.</p>
        <Label text="Qual seu voto?">
          <Select
            field={register('voteOption')}
            options={voteOptions}
            required={true}
          />
        </Label>
        <div className="w-max">
          <Button type="submit">Votar</Button>
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
