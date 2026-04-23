import { Component, OnInit, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { ClientService } from '../client.service';
import { Client } from '../model/client';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';

@Component({
  selector: 'app-client-edit',
  standalone: true,
  imports: [
    FormsModule,
    ReactiveFormsModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule
  ],
  templateUrl: './client-edit.component.html',
  styleUrl: './client-edit.component.scss',
})
export class ClientEditComponent implements OnInit{
  client!: Client;
  errorMessage: string | null = null;

  constructor(
    public dialogRef: MatDialogRef<ClientEditComponent>,
    @Inject(MAT_DIALOG_DATA) public data: {client : Client},
    private clientService: ClientService
  ) {}

  ngOnInit(): void {
    this.client = this.data.client ? Object.assign({}, this.data.client) : new Client();
  }

  onSave() {
        this.errorMessage = null;
        this.clientService.saveClient(this.client).subscribe({
            next: () => {
                this.dialogRef.close();
            },
            error: (err) => {
                if (err.status === 409) {
                    this.errorMessage = "El nombre del cliente ya existe.";
                } else {
                    this.errorMessage = "Ha ocurrido un error inesperado.";
                }
            }
        });
    }

  onClose() {
      this.dialogRef.close();
  }
}