import Title from '@/app/components/Title';
import ListAllSessions from '@/app/templates/SessionsTemplate/ListAllSessions';
import React from 'react';

export default function AllSessionPage() {
  return (
    <>
      <Title title="Todas sessões" />
      <ListAllSessions />
    </>
  );
}
