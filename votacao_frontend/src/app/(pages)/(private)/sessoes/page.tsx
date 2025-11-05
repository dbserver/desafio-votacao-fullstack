import Title from '@/app/components/Title';
import ListSessionOpenTemplate from '@/app/templates/SessionsTemplate/ListSessionOpenTemplate';
import React from 'react';

export default function SessionPage() {
  return (
    <>
      <Title title="Sessões abertas" />
      <ListSessionOpenTemplate />
    </>
  );
}
