import { Component, EventEmitter, Input, OnDestroy, OnInit, Output } from '@angular/core';
import { Subscription, interval } from 'rxjs';

@Component({
  selector: 'app-cronometro',
  standalone: false,
  templateUrl: './cronometro.component.html',
  styleUrl: './cronometro.component.scss'
})
export class CronometroComponent implements OnInit, OnDestroy {
  @Input() dataFechamento!: Date;
  @Output() tempoFinalizado = new EventEmitter<void>();

  tempoRestante = { days: 0, hours: 0, minutes: 0, seconds: 0 };
  private timerSubscription!: Subscription;

  ngOnInit(): void {
    this.iniciaContagem();
  }

  iniciaContagem(): void {
    this.atualizaTempo();

    this.timerSubscription = interval(1000).subscribe(() => {
      this.atualizaTempo();
    });
  }

  atualizaTempo(): void {
    const agora = new Date().getTime();
    let diferenca = new Date(this.dataFechamento).getTime() - agora;

    if (diferenca <= 0) {
      this.tempoRestante = { days: 0, hours: 0, minutes: 0, seconds: 0 };
      this.timerSubscription.unsubscribe();
      this.tempoFinalizado.emit();
      return;
    }

    const totalSeconds = Math.floor(diferenca / 1000);
    this.tempoRestante = {
      days: Math.floor(totalSeconds / (3600 * 24)),
      hours: Math.floor((totalSeconds % (3600 * 24)) / 3600),
      minutes: Math.floor((totalSeconds % 3600) / 60),
      seconds: totalSeconds % 60
    };
  }

  ngOnDestroy(): void {
    this.timerSubscription?.unsubscribe();
  }
}
