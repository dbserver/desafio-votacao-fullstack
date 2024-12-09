import { Component } from '@angular/core';
import { DefaultHomeLayoutComponent } from '../../components/default-home-layout/default-home-layout.component';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [DefaultHomeLayoutComponent],
  templateUrl: './home.component.html',
  styleUrl: './home.component.scss'
})
export class HomeComponent {

}
