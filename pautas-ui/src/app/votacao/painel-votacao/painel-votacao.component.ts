import { Component, Input } from '@angular/core';
import { Pauta } from '../../pauta/model/pauta';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { PautaService } from '../../pauta/pauta.service';
import { VotoService } from '../../voto/service/voto.service';
import { SimNaoEnum } from '../../util/enum/sim-nao.enum';
import { Voto } from '../../voto/model/voto';
import { DialogComponent, DialogData } from '../../shared/components/dialog/dialog.component';
import { MatDialog } from '@angular/material/dialog';

@Component({
  selector: 'app-painel-votacao',
  standalone: false,
  templateUrl: './painel-votacao.component.html',
  styleUrl: './painel-votacao.component.scss'
})
export class PainelVotacaoComponent {
  form!: FormGroup;
  private _pauta!: Pauta;
  
  constructor(
    private fb: FormBuilder,
    private service: PautaService,
    private votoService: VotoService,
    private dialogRef: MatDialog
  ) {
    this.buildForm();
  }

  buildForm(): void {
    this.form = this.fb.group({
      cpf: ['', Validators.required],
    });
  }

  @Input() set pauta(pauta: Pauta) {
    this._pauta = new Pauta();
    Object.assign(this._pauta, pauta);
  }

  quandoTempoAcabar(): void {
    this.service.fecharVotos(this._pauta.id).subscribe({
      next: (res) => {
        console.log('PainelVotacaoComponent.quandoTempoAcabar', res);
        this._pauta = res?.data;
        this.msgOk(`
          Votação encerrada para a pauta: '${res.data.titulo}'.<br>
          Votos Sim/Total: ${this.getVotosLabel(res.data.sessaoVotacao.votos)}.
        `);
        this.resetForm();
        this.service.emitirCarregarPautas();
        this.service.emitirCarregarUltimaPauta();
      },
      error: (err) => this.msgError(err.error.error),
    });
  }

  private getVotosLabel(votos: Voto[]): string {
    const totalVotos = votos ? votos.length : 0;
    const totalVotosSim = votos ? votos.filter(voto => voto.voto === SimNaoEnum.SIM).length : 0;
    return `${totalVotosSim.toString()}/${totalVotos.toString()}`;
  }

  votarSim(): void {
    this.votar(SimNaoEnum.SIM);
  }

  votarNao(): void {
    this.votar(SimNaoEnum.NAO);
  }

  votar(votoTipo: SimNaoEnum): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      this.msgError('Por favor, preencha o CPF corretamente.');
      return;
    }

    const voto: Voto = new Voto();
    voto.cpfAssociado = this.form.get('cpf')?.value;
    voto.voto = votoTipo;

    this.votoService.votar(voto).subscribe({
      next: () => {
        this.msgOk(`Voto registrado com sucesso!`);
        this.resetForm()
      },
      error: (err) => this.msgError(err.error.error),
    });
  }

  private msgOk(msg: string): void {
    const data: DialogData = {
      title: 'Sucesso',
      msgDialog: msg,
      msgBtnClose: 'Fechar'
    };
    this.dialogRef.open(DialogComponent, { data });
  }
  
  private msgError(msg: string): void {
    const data: DialogData = {
      title: 'Erro',
      msgDialog: msg,
      msgBtnClose: 'Fechar'
    };
    this.dialogRef.open(DialogComponent, { data });
  }

  private resetForm(): void {
    this.form.reset();
    this.form.markAsPristine();
    this.form.markAsUntouched();
  }

  get pauta(): Pauta {
    return this._pauta;
  }
}
