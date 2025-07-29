import { Component, Input } from '@angular/core';
import { Pauta } from '../../pauta/model/pauta';

@Component({
  selector: 'app-pauta-votada',
  standalone: false,
  templateUrl: './pauta-votada.component.html',
  styleUrl: './pauta-votada.component.scss'
})
export class PautaVotadaComponent {
  
  private _pauta!: Pauta;
  
  @Input() set pauta(pauta: Pauta) {
    this._pauta = new Pauta();
    Object.assign(this._pauta, pauta);
  }

  get pauta(): Pauta {
    return this._pauta;
  }

}
