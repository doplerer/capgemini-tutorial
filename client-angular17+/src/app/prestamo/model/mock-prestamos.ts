import { Game } from '../../game/model/Game';
import { PrestamoPage } from './PrestamoPage';

export const PRESTAMO_DATA: PrestamoPage = {
    content: [
        { id: 1, game: { id: 1, title: 'Catan' } as Game, client: { id: 1, name: 'John Doe' }, initialDate: new Date(), finalDate: new Date() },
        { id: 2, game: { id: 2, title: 'Ticket to Ride' } as Game, client: { id: 2, name: 'Jane Smith' }, initialDate: new Date(), finalDate: new Date() }],
    
        pageable: {
        pageSize: 5,
        pageNumber: 0,
        sort: [{ property: 'id', direction: 'ASC' }],
    },
    totalElements: 2,
};