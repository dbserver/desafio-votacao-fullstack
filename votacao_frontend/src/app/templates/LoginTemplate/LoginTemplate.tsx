'use client';

import React from 'react';
import { useForm } from 'react-hook-form';

import { useRouter } from 'next/navigation';
import Input from '@/app/components/Input';
import Button from '@/app/components/Button';
import Label from '@/app/components/Label';
import { login } from '@/app/utils/auth';
import { formatMaskCPF, removeMask } from '@/app/utils/formaters/masks';

type TFormValues = {
  cpf: string;
};

export default function LoginTemplate() {
  const { register, handleSubmit, watch, setValue } = useForm<TFormValues>();
  const router = useRouter();
  const cpfValue = watch('cpf');

  const onSubmit = (data: TFormValues) => {
    const { cpf } = data;
    const removedMask = removeMask(cpf);
    login(removedMask);
    router.replace('/sessoes');
  };

  const handleCpfChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const formatted = formatMaskCPF(e.target.value);
    setValue('cpf', formatted);
  };

  return (
    <main className="h-full gap-9 p-10 md:flex md:items-center md:justify-center">
      <div className="flex flex-col gap-4 md:max-w-[500px] md:items-center md:justify-center">
        <h1 className="text-5xl md:text-7xl">Sessão Digital</h1>
        <p className="indent-8">
          Participe ativamente das decisões da sua cooperativa! Acesse a
          plataforma para consultar pautas, votar e acompanhar os resultados das
          sessões.
        </p>
        <p className="indent-8">
          Seu voto faz a diferença. Juntos, decidimos o futuro!
        </p>
      </div>
      <form
        onSubmit={handleSubmit(onSubmit)}
        className="mt-10 flex flex-col items-center justify-center gap-4 rounded-lg border border-green-700 p-10"
      >
        <Label text="Digite seu cpf" isRequired={true}>
          <Input
            field={register('cpf')}
            required={true}
            value={cpfValue}
            onChange={handleCpfChange}
            type="text"
            placeholder="Digite seu cpf"
          />
        </Label>
        <Button type="submit"> Entrar</Button>
      </form>
    </main>
  );
}
