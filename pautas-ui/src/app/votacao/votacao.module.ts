import { NgModule } from '@angular/core';
import { VotacaoComponent } from './component/votacao.component';
import { SharedModule } from '../shared/shared.module';
import { PautaService } from '../pauta/pauta.service';
import { PautaVotadaComponent } from "./pauta-votada/pauta-votada.component";
import { PainelVotacaoComponent } from './painel-votacao/painel-votacao.component';
import { DatePipe } from '@angular/common';
import { CronometroComponent } from './cronometro/cronometro.component';
import { NgxMaskModule } from 'ngx-mask';
import { VotoService } from '../voto/service/voto.service';

@NgModule({
  declarations: [
    VotacaoComponent,
    PautaVotadaComponent,
    PainelVotacaoComponent,
    CronometroComponent,
  ],
  imports: [
    SharedModule,
    NgxMaskModule,
],
  exports: [
    VotacaoComponent,
    PautaVotadaComponent,
    PainelVotacaoComponent,
  ],
  providers: [
    PautaService,
    VotoService,
    DatePipe,
  ],
})
export class VotacaoModule { }
