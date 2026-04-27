import { Component, Inject, OnInit } from '@angular/core';
import { Prestamo } from '../model/Prestamo';
import { Game } from '../../game/model/Game';
import { Client } from '../../client/model/client';
import { PrestamoService } from '../prestamo.service';
import { MAT_DIALOG_DATA, MatDialogRef, MatDialogModule } from '@angular/material/dialog';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatSelectModule } from '@angular/material/select';
import { GameService } from '../../game/game.service';
import { ClientService } from '../../client/client.service';

@Component({
  selector: 'app-prestamo-edit',
  standalone: true,
  imports: [FormsModule, ReactiveFormsModule, MatFormFieldModule, MatInputModule, MatButtonModule, MatDialogModule, MatSelectModule ],
  templateUrl: './prestamo-edit.component.html',
  styleUrl: './prestamo-edit.component.scss',
})
export class PrestamoEditComponent implements OnInit{

    prestamo!: Prestamo;
    games: Game[] = [];
    clients: Client[] = [];

    constructor(
        public dialogRef: MatDialogRef<PrestamoEditComponent>,
        @Inject(MAT_DIALOG_DATA) public data: any,
        private prestamoService: PrestamoService,
        private gameService: GameService,
        private clientService: ClientService
    ) {}

    ngOnInit(): void {
        this.prestamo = this.data.prestamo ? Object.assign({}, this.data.prestamo) : new Prestamo();

        this.gameService.getGames().subscribe(games => {
            this.games = games;
            
            if (this.prestamo.game != null) {
                const gameFilter: Game[] = games.filter(
                    (game) => game.id == this.data.prestamo.game.id
                );
                if (gameFilter != null) {
                    this.prestamo.game = gameFilter[0];
                }
            }
        });

        this.clientService.getClients().subscribe(clients => {
            this.clients = clients;

            if (this.prestamo.client != null) {
                const clientFilter: Client[] = clients.filter(
                    (client) => client.id == this.data.prestamo.client.id
                );
                if (clientFilter != null) {
                    this.prestamo.client = clientFilter[0];
                }
            }
        });
    }

    onSave() {
        if (!this.prestamo.game || !this.prestamo.client || !this.prestamo.initialDate || !this.prestamo.finalDate) {
            alert('Todos los campos son obligatorios');
            return;
        }
        if (this.prestamo.initialDate > this.prestamo.finalDate) {
            alert('La fecha de fin no puede ser anterior a la fecha de inicio');
            return;
        }

        const date1 = new Date(this.prestamo.initialDate);
        const date2 = new Date(this.prestamo.finalDate);
        const diffTime = Math.abs(date2.getTime() - date1.getTime());
        const diffDays = Math.ceil(diffTime / (1000 * 60 * 60 * 24)); 

        if (diffDays > 14) {
            alert('El periodo de préstamo máximo solo podrá ser de 14 días');
            return;
        }

        this.prestamoService.savePrestamo(this.prestamo).subscribe({
            next: () => {
                this.dialogRef.close();
            },
            error: (err) => {
                alert(err.error.message || 'Ha ocurrido un error al guardar el préstamo');
            }
        });
    }

    onClose() {
        this.dialogRef.close();
    }


}
