import { Component, OnInit } from '@angular/core';
import { Pauta } from '../../pauta/model/pauta';
import { PautaService } from '../../pauta/pauta.service';
import { DialogComponent, DialogData } from '../../shared/components/dialog/dialog.component';
import { MatDialog } from '@angular/material/dialog';

@Component({
  selector: 'app-votacao',
  standalone: false,
  templateUrl: './votacao.component.html',
  styleUrl: './votacao.component.scss'
})
export class VotacaoComponent implements OnInit {

  pauta!: Pauta;

  constructor(private service: PautaService, private dialogRef: MatDialog) {
    this.ouvirUltimaPauta();    
  }

  ngOnInit(): void {
    this.loadUltimaPauta();
  }

  private ouvirUltimaPauta(): void {
    this.service.ouvirCarregarUltimaPauta().subscribe(() => this.loadUltimaPauta());
  }

  private loadUltimaPauta(): void {
    this.service.getLastRecord().subscribe({
      next: (res) => {
        this.pauta = res?.data;
        console.log('VotacaoComponent.loadUltimaPauta', res);
      },
      error: (err) => this.msgError(err.error.error)
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

  tempoAcabou(): void {
    this.service.emitirCarregarPautas();
    this.service.emitirCarregarUltimaPauta();
  }

}
