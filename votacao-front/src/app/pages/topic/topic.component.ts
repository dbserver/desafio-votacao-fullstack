import { Component, Input } from '@angular/core';
import { TopicFormComponent } from '../../components/topic-form/topic-form.component';

@Component({
  selector: 'app-topic',
  standalone: true,
  imports: [
    TopicFormComponent
  ],
  templateUrl: './topic.component.html',
  styleUrl: './topic.component.scss'
})
export class TopicComponent {
  @Input() topicBtnText: string = "";

}
