import { Component, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { MatButtonModule } from '@angular/material/button';

@Component({
  selector: 'app-dialog-confirmation',
  imports: [MatButtonModule],
  templateUrl: './dialog-confirmation.component.html',
  styleUrl: './dialog-confirmation.component.scss',
})
export class DialogConfirmationComponent {
  title: string;
  description: string;

  constructor(
    public dialogRef: MatDialogRef<DialogConfirmationComponent>,
    @Inject(MAT_DIALOG_DATA) public data: { title: string, description: string }
  ) {
    this.title = data.title;
    this.description = data.description;
  }

  onClose(result?: boolean): void {
    this.dialogRef.close(result);
  }
}
