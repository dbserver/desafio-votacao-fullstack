export interface IVote {
  voteId: number;
  cpf: string;
  voteOption: VoteOptionEnum;
}

export enum VoteOptionEnum {
  SIM = 'SIM',
  NAO = 'NAO',
}

export type TVoteBody = Pick<IVote, 'cpf' | 'voteOption'> & {
  sectionId: number;
};
