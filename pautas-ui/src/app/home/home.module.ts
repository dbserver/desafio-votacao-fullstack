import { NgModule } from "@angular/core";
import { HomeComponent } from "./component/home.component";
import { HomeRoutingModule } from "./home-routing.module";
import { SharedModule } from "../shared/shared.module";
import { PautasModule } from "../pauta/pauta.module";
import { VotacaoModule } from "../votacao/votacao.module";

@NgModule({
    declarations: [
        HomeComponent,
    ],
    imports: [
        SharedModule,
        HomeRoutingModule,
        PautasModule,
        VotacaoModule,
    ]
})
export class HomeModule {}