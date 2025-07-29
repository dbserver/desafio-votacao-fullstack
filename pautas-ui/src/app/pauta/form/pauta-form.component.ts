import { ChangeDetectionStrategy, Component } from '@angular/core';

import { Router } from '@angular/router';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatDialog } from '@angular/material/dialog';

import { PautaService } from '../pauta.service';
import { DialogData, DialogComponent } from '../../shared/components/dialog/dialog.component';

@Component({
  selector: 'app-pauta-form',
  standalone: false,
  changeDetection: ChangeDetectionStrategy.OnPush,
  templateUrl: './pauta-form.component.html',
  styleUrl: './pauta-form.component.scss',
})
export class PautaFormComponent {
  form!: FormGroup;

  constructor(
    private router: Router,
    private fb: FormBuilder,
    private dialogRef: MatDialog,
    private service: PautaService,
  ) {
    this.buildForm();
  }

  buildForm(): void {
    this.form = this.fb.group({
      titulo: ['', Validators.required],
      descricao: ['', Validators.required],
      sessaoVotacao: this.fb.group({
        duracao: [''],
      }),
    });
  }

  save(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      this.msgError('Preencha os campos obrigatórios.');
      return;
    }
    this.service.save(this.form.value).subscribe({
      next: (res) => {
        this.msgOk(`Pauta '${res?.data.titulo}' criada com sucesso!`);
        console.log('PautaListComponent.loadPautas', res);
      },
      complete: () => this.voltar(),
      error: (err) => this.msgError(err.error.error)
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

  voltar(): void {
    this.service.emitirCarregarPautas();
    this.service.emitirCarregarUltimaPauta();
    this.router.navigate(['../..']);
  }
}
