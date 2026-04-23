import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Client } from './model/client';
import { Observable, of} from 'rxjs';
import { CLIENT_DATA } from './model/mock-clients';
import { Category } from '../category/model/category';

@Injectable({
  providedIn: 'root',
})
export class ClientService {

  constructor(private http: HttpClient) {}

  private baseUrl = 'http://localhost:8080/client';

  getClients(): Observable<Client[]> {
    return this.http.get<Client[]>(this.baseUrl);
  }

  saveClient(client: Client): Observable<Client> {
    const { id } = client;
    const url = id ? `${this.baseUrl}/${id}` : this.baseUrl;
    return this.http.put<Client>(url, client);
  }

  deleteClient(clientId: number): Observable<any> {
    return this.http.delete(`${this.baseUrl}/${clientId}`);
  }
}

