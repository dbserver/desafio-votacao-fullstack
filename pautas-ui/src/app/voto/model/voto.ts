import { SessaoVotacao } from "../../pauta/model/sessao-votacao";
import { SimNaoEnum } from "../../util/enum/sim-nao.enum";

export class Voto {
    id!: number;
    cpfAssociado!: number;
    voto!: SimNaoEnum;
    sessaoVotacao!: SessaoVotacao;
}
