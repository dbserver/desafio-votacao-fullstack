import { ISection } from '@/app/api/types/section';
import Button from '@/app/components/Button';

import React, { useMemo } from 'react';

type TSectionCard = {
  onVoteClick?: (id: number) => void;
  section: ISection;
  countVote?: boolean;
};

export default function SectionCard({
  section,
  onVoteClick,
  countVote = false,
}: TSectionCard) {
  const voteCount = useMemo(() => {
    return section.votes.reduce(
      (acc, vote) => {
        acc[vote.voteOption] = (acc[vote.voteOption] || 0) + 1;
        return acc;
      },
      { SIM: 0, NAO: 0 }
    );
  }, [section.votes]);

  return (
    <div className="flex h-full w-full flex-col items-start justify-center gap-2 p-4 text-center shadow-lg">
      <p className="w-full truncate text-left" title={section?.agendaId?.name}>
        <strong>Pauta:</strong> {section?.agendaId?.name}
      </p>
      <p>
        <strong>Duração:</strong> {section?.duration} minutos
      </p>
      <p>
        <strong>Início:</strong> {new Date(section?.start).toLocaleString()}
      </p>
      <p>
        <strong>Fim:</strong> {new Date(section?.end).toLocaleString()}
      </p>

      {countVote && (
        <div>
          <strong>Contagem dos votos:</strong>
          <ul className="mt-1 ml-4 list-disc text-left">
            <li>Sim: {voteCount.SIM}</li>
            <li>Não: {voteCount.NAO} </li>
          </ul>
        </div>
      )}

      {onVoteClick && (
        <div className="self-center">
          <Button onClick={() => onVoteClick(section.id)}>Votar</Button>
        </div>
      )}
    </div>
  );
}
