'use client';

import { useGetAllSections } from '@/app/api/hookService/useSectionService';
import { ISection } from '@/app/api/types/section';
import Container from '@/app/components/Container';
import Spinner from '@/app/components/Spinner';
import React from 'react';
import SectionCard from './components/SectionCard';

export default function ListAllSessions() {
  const { data: allSections, isLoading } = useGetAllSections({
    isSectionsOPen: false,
  });

  return (
    <div className="my-10">
      <p>
        Aqui você pode visualizar todas as sessões criadas a partir de uma
        pauta, bem como os resultados das votações.
      </p>
      <div>
        {isLoading ? (
          <div className="flex justify-center">
            <Spinner size={120} />
          </div>
        ) : (
          <div className="mt-5 grid grid-cols-1 gap-4 md:grid-cols-3 lg:grid-cols-4">
            {!allSections ? (
              <p>Não foi possível carregar as sessões</p>
            ) : allSections.length > 0 ? (
              allSections?.map((section: ISection) => (
                <Container key={section.id}>
                  <SectionCard section={section} countVote={true} />
                </Container>
              ))
            ) : (
              <p>Não há sessões cadastradas</p>
            )}
          </div>
        )}
      </div>
    </div>
  );
}
