import { SimNaoEnum } from "../../util/enum/sim-nao.enum";
import { Voto } from "../../voto/model/voto";
import { Pauta } from "./pauta";

export class SessaoVotacao {
    id!: number;
    duracao!: number;
    dataAbertura!: Date;
    dataFechamento!: Date;
    pauta!: Pauta;
    votos!: Voto[];

    get totalVotos(): number {
        return this.votos ? this.votos.length : 0;
    }

    get totalVotosSim(): number {
        return this.votos ? this.votos.filter(voto => voto.voto === SimNaoEnum.SIM).length : 0;
    }
}
