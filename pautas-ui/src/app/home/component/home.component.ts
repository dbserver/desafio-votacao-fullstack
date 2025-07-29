import { Component } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-home-component',
  templateUrl: './home.component.html',
  styleUrl: './home.component.scss',
  standalone: false,
})
export class HomeComponent {

  constructor(private router: Router) {}

  add(): void {
    this.router.navigate(['pauta/form']);
  }

}
