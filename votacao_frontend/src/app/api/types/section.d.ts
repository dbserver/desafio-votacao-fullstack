import { IAgenda } from './agenda';
import { IVote } from './vote';

export interface ISection {
  id: number;
  agendaId: IAgenda;
  duration: number;
  end: Date;
  start: Date;
  votes: IVote[];
}

export type TSectionBody = Pick<ISection, 'agendaId', 'duration'>;
