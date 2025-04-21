import Title from '@/app/components/Title';
import CreateSessionTemplate from '@/app/templates/SessionsTemplate/CreateSessionTemplate';
import React from 'react';

export default function CreateSessionPage() {
  return (
    <>
      <Title title="Criar nova sessão" />
      <CreateSessionTemplate />
    </>
  );
}
