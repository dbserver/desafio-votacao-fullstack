import { NgModule } from "@angular/core";
import { App } from "./app";
import { BrowserModule } from "@angular/platform-browser";
import { HttpClientModule } from "@angular/common/http";
import { BrowserAnimationsModule } from "@angular/platform-browser/animations";
import { RouterModule } from "@angular/router";
import { routes } from "./app.routes";
import { SharedModule } from "./shared/shared.module";
import { NgxMaskModule } from "ngx-mask";

@NgModule({
  declarations: [ App ],
  imports: [
    BrowserModule,
    BrowserAnimationsModule,
    HttpClientModule,
    SharedModule,
    NgxMaskModule.forRoot(),
    RouterModule.forRoot(routes)
  ],
  bootstrap: [ App ]
})
export class AppModule {}