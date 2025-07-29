import { Component, EventEmitter, Input, Output } from '@angular/core';

@Component({
  selector: 'app-button',
  standalone: false,
  templateUrl: './button.component.html',
})
export class ButtonComponent {
  @Output() onClick = new EventEmitter<void>();

  @Input() icon!: string;
  @Input() label!: string;
  @Input() color: string = 'white';
  @Input() textColor: string = 'black';

  click(): void {
    this.onClick.emit();
  }
}
