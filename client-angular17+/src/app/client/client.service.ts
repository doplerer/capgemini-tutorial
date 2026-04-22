import { Injectable } from '@angular/core';
import { Client } from './model/client';
import { Observable, of} from 'rxjs';
import { CLIENT_DATA } from './model/mock-clients';

@Injectable({
  providedIn: 'root',
})
export class ClientService {

  constructor() {}

  getClients(): Observable<Client[]> {
    return of(CLIENT_DATA);
  }

  saveClient(client: Client): Observable<Client> {
    return of(client);
  }

  deleteClient(clientId: number): Observable<any> {
    return of(null);
  }
}

