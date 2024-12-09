import { Routes } from '@angular/router';
import { HomeComponent } from './pages/home/home.component';
import { TopicComponent } from './pages/topic/topic.component';

export const routes: Routes = [

  {
    path: "home",
    component: HomeComponent
  },
  {
    path: "topic",
    component: TopicComponent
  }
];
