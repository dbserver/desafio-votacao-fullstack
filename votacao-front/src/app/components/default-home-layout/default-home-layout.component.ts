import { Component, Input } from '@angular/core';

@Component({
  selector: 'app-default-home-layout',
  standalone: true,
  imports: [],
  templateUrl: './default-home-layout.component.html',
  styleUrl: './default-home-layout.component.scss'
})
export class DefaultHomeLayoutComponent {
  @Input() topicBtnText: string = "";
  @Input() sessionNome: string = ""
  @Input() sessionDescription: string = "";
  @Input() sessionVotos: string = "";
  @Input() btVote: string = "";


}
