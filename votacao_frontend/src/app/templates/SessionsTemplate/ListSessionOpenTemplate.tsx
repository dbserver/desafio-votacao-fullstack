'use client';

import { ISection } from '@/app/api/types/section';
import Button from '@/app/components/Button';
import Container from '@/app/components/Container';
import Modal from '@/app/components/Modal';
import Spinner from '@/app/components/Spinner';
import { useRouter } from 'next/navigation';
import React, { useState } from 'react';
import AgendaForm from './components/AgendaForm';
import SectionCard from './components/SectionCard';
import VoteForm from './components/VoteForm';
import { useGetAllSections } from '@/app/api/hookService/useSectionService';
import Link from 'next/link';
import { FaArrowRight } from 'react-icons/fa';

export default function ListSessionOpenTemplate() {
  const [openModalAgenda, setOpenModalAgenda] = useState<boolean>(false);
  const [openModalVote, setOpenModalVote] = useState<boolean>(false);
  const [selectedSection, setSelectedSection] = useState<number | null>(null);

  const router = useRouter();

  const { data: allOpenSections, isLoading } = useGetAllSections({
    isSectionsOPen: true,
  });

  const onVoteClick = (id: number) => {
    setSelectedSection(id);
    setOpenModalVote(true);
  };

  return (
    <>
      <div className="my-10 flex flex-col items-start gap-4">
        <Link
          className="flex items-center justify-center gap-4 hover:text-green-700"
          href="/sessoes/visualizarsessoes"
        >
          {' '}
          <FaArrowRight /> Visualizar todas sessões
        </Link>
        <Button onClick={() => setOpenModalAgenda(true)}>
          Criar nova pauta
        </Button>
        <Button onClick={() => router.push('/sessoes/novasessao')}>
          Criar nova seção
        </Button>
      </div>
      {isLoading ? (
        <div className="flex justify-center">
          <Spinner size={120} />
        </div>
      ) : (
        <div className="mt-5 grid grid-cols-1 gap-4 md:grid-cols-3 lg:grid-cols-4">
          {!allOpenSections ? (
            <p>Não foi possível carregar as sessões</p>
          ) : allOpenSections.length > 0 ? (
            allOpenSections?.map((section: ISection) => (
              <Container key={section.id}>
                <SectionCard onVoteClick={onVoteClick} section={section} />
              </Container>
            ))
          ) : (
            <p>Não há sessões abertas</p>
          )}
        </div>
      )}
      {openModalAgenda && (
        <Modal onCancel={() => setOpenModalAgenda(false)}>
          <AgendaForm setOpenModalAgenda={setOpenModalAgenda} />
        </Modal>
      )}
      {openModalVote && (
        <Modal onCancel={() => setOpenModalVote(false)}>
          <VoteForm
            setOpenModalVote={setOpenModalVote}
            selectedSection={selectedSection}
          />
        </Modal>
      )}
    </>
  );
}
