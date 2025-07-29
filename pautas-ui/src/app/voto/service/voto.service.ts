import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Voto } from "../model/voto";
import { Observable } from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class VotoService {

    back_api_url = 'http://localhost:3841/voto';

    constructor(private http: HttpClient) {}

    public votar(voto: Voto): Observable<void> {
        return this.http.post<void>(`${this.back_api_url}/votar`, voto);
    }

}