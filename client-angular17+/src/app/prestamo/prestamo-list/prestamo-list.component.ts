import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatTableDataSource, MatTableModule } from '@angular/material/table';
import { Prestamo } from '../model/Prestamo';
import { PrestamoService } from '../prestamo.service';
import { MatDialog } from '@angular/material/dialog';
import { PageEvent } from '@angular/material/paginator';
import { Pageable } from '../../core/model/page/Pageable';
import { DialogConfirmationComponent } from '../../core/dialog-confirmation/dialog-confirmation.component';
import { MatPaginatorModule } from '@angular/material/paginator';
import { PrestamoEditComponent } from '../prestamo-edit/prestamo-edit.component';

import { FormsModule } from '@angular/forms';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { Game } from '../../game/model/Game';
import { Client } from '../../client/model/client';
import { GameService } from '../../game/game.service';
import { ClientService } from '../../client/client.service';

@Component({
  selector: 'app-prestamo-list',
  standalone: true,
  imports: [MatButtonModule, MatIconModule, MatTableModule, CommonModule, MatPaginatorModule, FormsModule, MatFormFieldModule, MatInputModule, MatSelectModule],
  templateUrl: './prestamo-list.component.html',
  styleUrl: './prestamo-list.component.scss',
})
export class PrestamoListComponent implements OnInit{
  pageNumber: number = 0;
  pageSize: number = 5;
  totalElements: number = 0;

  filterGame?: Game;
  filterClient?: Client;
  filterDate?: Date;

  games: Game[] = [];
  clients: Client[] = [];

  dataSource = new MatTableDataSource<Prestamo>();
    displayedColumns: string[] = ['id', 'game_title', 'client_name', 'first_date', 'final_date', 'action'];

  constructor(
      private prestamoService: PrestamoService, 
      private gameService: GameService,
      private clientService: ClientService,
      public dialog: MatDialog
  ) {}

  ngOnInit(): void {
        this.gameService.getGames().subscribe(games => this.games = games);
        this.clientService.getClients().subscribe(clients => this.clients = clients);
        this.loadPage();
    }

  onCleanFilter(): void {
      this.filterGame = null;
      this.filterClient = null;
      this.filterDate = null;

      this.onSearch();
  }

  onSearch(): void {
      this.pageNumber = 0;
      this.loadPage();
  }

  loadPage(event?: PageEvent) {
      const pageable: Pageable = {
          pageNumber: this.pageNumber,
          pageSize: this.pageSize,
          sort: [
              {
                  property: 'id',
                  direction: 'ASC',
              },
          ],
      };

      if (event != null) {
          pageable.pageSize = event.pageSize;
          pageable.pageNumber = event.pageIndex;
      }

      this.prestamoService.getPrestamos(
              pageable, 
              this.filterGame != null ? this.filterGame.id : null,
              this.filterClient != null ? this.filterClient.id : null,
              this.filterDate
          ).subscribe((data) => {
          this.dataSource.data = data.content;
          this.pageNumber = data.pageable.pageNumber;
          this.pageSize = data.pageable.pageSize;
          this.totalElements = data.totalElements;
      });
  }

  createPrestamo() {
        const dialogRef = this.dialog.open(PrestamoEditComponent, {
            data: {},
        });

        dialogRef.afterClosed().subscribe((result) => {
            this.ngOnInit();
        });
    }

    editPrestamo(prestamo: Prestamo) {
        const dialogRef = this.dialog.open(PrestamoEditComponent, {
            data: { prestamo: prestamo },
        });

        dialogRef.afterClosed().subscribe((result) => {
            this.ngOnInit();
        });
    }

    deletePrestamo(prestamo: Prestamo) {
        const dialogRef = this.dialog.open(DialogConfirmationComponent, {
            data: {
                title: 'Eliminar préstamo',
                description:
                    'Atención si borra el préstamo se perderán sus datos.<br> ¿Desea eliminar el préstamo?',
            },
        });

        dialogRef.afterClosed().subscribe((result) => {
            if (result) {
                this.prestamoService.deletePrestamo(prestamo.id).subscribe((result) => {
                    this.ngOnInit();
                });
            }
        });
    }
}
