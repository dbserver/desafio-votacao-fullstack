import { Component,  OnInit } from '@angular/core';
import { PautaService } from '../pauta.service';
import { Pauta } from '../model/pauta';
import { DialogComponent, DialogData } from '../../shared/components/dialog/dialog.component';
import { MatDialog } from '@angular/material/dialog';
import { Voto } from '../../voto/model/voto';
import { SimNaoEnum } from '../../util/enum/sim-nao.enum';

@Component({
  selector: 'app-pauta-list',
  templateUrl: './pauta-list.component.html',
  standalone: false,
})
export class PautaListComponent implements OnInit {

  dataSource: Pauta[] = [];

  displayedColumns: string[] = ['id', 'titulo', 'votos', 'aceitacao'];

  constructor(private service: PautaService, private dialogRef: MatDialog) {
    this.ouvirPautas();
  }

  ngOnInit(): void {
    this.loadPautas();
  }

  private ouvirPautas(): void {
    this.service.ouvirCarregarPautas().subscribe(() => this.loadPautas());
  }

  private loadPautas(): void {
    this.service.getAll().subscribe({
      next: (res) => {
        this.dataSource = res?.data;
        console.log('PautaListComponent.loadPautas', res);
      },
      error: (err) => this.msgError(err.error.error),
    });
  }
  
  private msgError(msg: string): void {
    const data: DialogData = {
      title: 'Erro',
      msgDialog: msg,
      msgBtnClose: 'Fechar'
    };
    this.dialogRef.open(DialogComponent, { data });
  }

  public getVotosLabel(votos: Voto[]): string {
    const totalVotos = votos ? votos.length : 0;
    const totalVotosSim = votos ? votos.filter(voto => voto.voto === SimNaoEnum.SIM).length : 0;
    return `${totalVotosSim.toString()}/${totalVotos.toString()}`;
  }

  public getAceitacao(votos: Voto[]): string {
    const totalVotos = votos ? votos.length : 0;
    const totalVotosSim = votos ? votos.filter(voto => voto.voto === SimNaoEnum.SIM).length : 0;
    const porcentagem = (totalVotos === 0) ? 0 : totalVotosSim * 100 / totalVotos;
    return `${porcentagem.toFixed(2)}%`;
  }

}
