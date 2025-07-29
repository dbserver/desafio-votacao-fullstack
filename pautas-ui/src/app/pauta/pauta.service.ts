import { Injectable } from "@angular/core";
import { HttpClient } from "@angular/common/http";
import { Pauta } from "./model/pauta";
import { Observable, Subject } from "rxjs";
import { ApiResponse } from "../core/dto/api-response";

@Injectable({
  providedIn: 'root'
})
export class PautaService {

  private carregarPautasSubject: Subject<void> = new Subject<void>();
  private carregarUltimaPautaSubject: Subject<void> = new Subject<void>();

  back_api_url = 'http://localhost:3841/pauta';

  constructor(private http: HttpClient) {}

  save(pauta: Pauta): Observable<ApiResponse<Pauta>> {
    return this.http.post<ApiResponse<Pauta>>(this.back_api_url, pauta);
  }

  getAll(): Observable<ApiResponse<Pauta[]>> {
    return this.http.get<ApiResponse<Pauta[]>>(this.back_api_url);
  }

  getLastRecord(): Observable<ApiResponse<Pauta>> {
    return this.http.get<ApiResponse<Pauta>>(`${this.back_api_url}/last-record`);
  }

  fecharVotos(idPauta: number): Observable<ApiResponse<Pauta>> {
    return this.http.put<ApiResponse<Pauta>>(`${this.back_api_url}/fechar-votos/${idPauta}`, null);
  }

  public emitirCarregarPautas(): void {
    this.carregarPautasSubject.next();
  }

  public ouvirCarregarPautas(): Observable<void> {
    return this.carregarPautasSubject.asObservable();
  }

  public emitirCarregarUltimaPauta(): void {
    this.carregarUltimaPautaSubject.next();
  }

  public ouvirCarregarUltimaPauta(): Observable<void> {
    return this.carregarUltimaPautaSubject.asObservable();
  }

}
