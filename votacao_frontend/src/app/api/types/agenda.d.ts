export interface IAgenda {
  id: number;
  name: string;
}

export type TAgendaBody = Pick<IAgenda, 'name'>;
