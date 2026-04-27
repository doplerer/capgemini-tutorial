import { Game } from "../../game/model/Game";
import { Client } from "../../client/model/client";

export class Prestamo {
    id: number;
    game: Game;
    client: Client;
    initialDate: Date;
    finalDate: Date;
}