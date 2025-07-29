import { NgModule } from "@angular/core";
import { SharedModule } from "../shared/shared.module";
import { PautaFormComponent } from "./form/pauta-form.component";
import { PautaRoutingModule } from "./pauta-routing.module";
import { PautaListComponent } from "./list/pauta-list.component";
import { MatTableModule } from "@angular/material/table";

@NgModule({
    declarations: [
        PautaFormComponent,
        PautaListComponent,
    ],
    imports: [
        SharedModule,
        MatTableModule,
        PautaRoutingModule,
    ],
    exports: [
        PautaListComponent,
        MatTableModule,
    ]
})
export class PautasModule {}