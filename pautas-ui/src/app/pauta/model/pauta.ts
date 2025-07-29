import { SessaoVotacao } from "./sessao-votacao";

export class Pauta {
    id!: number;
    titulo!: string;
    descricao!: string;
    sessaoVotacao!: SessaoVotacao;
}
