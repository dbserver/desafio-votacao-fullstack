import { NgModule } from "@angular/core";
import { RouterModule, Routes } from "@angular/router";
import { PautaFormComponent } from "./form/pauta-form.component";

const routes: Routes = [
  {
    path: 'form',
    component: PautaFormComponent
  }
];

@NgModule({
  imports: [ RouterModule.forChild(routes) ],
  exports: [ RouterModule ]
})
export class PautaRoutingModule {}