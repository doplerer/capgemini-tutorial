import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Pageable } from '../core/model/page/Pageable';
import { Prestamo } from './model/Prestamo';
import { PrestamoPage } from './model/PrestamoPage';

@Injectable({
  providedIn: 'root',
})
export class PrestamoService {
    constructor(private http: HttpClient) {}

    getPrestamos(pageable: Pageable, gameId?: number, clientId?: number, date?: Date): Observable<PrestamoPage> {
        return this.http.post<PrestamoPage>('http://localhost:8080/prestamo', { 
            pageable: pageable,
            gameId: gameId,
            clientId: clientId,
            date: date
        });
    }

    savePrestamo(prestamo: Prestamo): Observable<void> {
        let url = 'http://localhost:8080/prestamo';

        if (prestamo.id != null) {
            url += '/' + prestamo.id;
        }

        return this.http.put<void>(url, prestamo);
    }

    deletePrestamo(idPrestamo: number): Observable<void> {
        return this.http.delete<void>('http://localhost:8080/prestamo/' + idPrestamo);
    }
}