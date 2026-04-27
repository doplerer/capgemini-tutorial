package com.ccsw.tutorial.prestamo;

import com.ccsw.tutorial.client.model.Client;
import com.ccsw.tutorial.client.service.ClientService;
import com.ccsw.tutorial.game.model.Game;
import com.ccsw.tutorial.game.service.GameService;
import com.ccsw.tutorial.prestamo.model.Prestamo;
import com.ccsw.tutorial.prestamo.model.PrestamoDto;
import com.ccsw.tutorial.prestamo.model.PrestamoSearchDto;
import com.ccsw.tutorial.prestamo.repository.PrestamoRepository;
import com.ccsw.tutorial.prestamo.service.PrestamoServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PrestamoTest {

    @Mock
    private PrestamoRepository prestamoRepository;

    @Mock
    private GameService gameService;

    @Mock
    private ClientService clientService;

    @InjectMocks
    private PrestamoServiceImpl prestamoService;

    public static final Long EXISTS_PRESTAMO_ID = 1L;
    public static final Long NOT_EXISTS_PRESTAMO_ID = 0L;
    public static final Long GAME_ID = 1L;
    public static final Long CLIENT_ID = 1L;

    @Test
    public void findAllShouldReturnAllPrestamos() {

        List<Prestamo> list = new ArrayList<>();
        list.add(mock(Prestamo.class));

        when(prestamoRepository.findAll()).thenReturn(list);

        List<Prestamo> prestamos = prestamoService.findAll();

        assertNotNull(prestamos);
        assertEquals(1, prestamos.size());
    }

    @Test
    public void findPageShouldReturnPageOfPrestamos() {

        PrestamoSearchDto dto = new PrestamoSearchDto();
        Prestamo prestamo = mock(Prestamo.class);
        List<Prestamo> list = new ArrayList<>();
        list.add(prestamo);

        Page<Prestamo> page = new PageImpl<>(list, PageRequest.of(0, 10), 1);
        when(prestamoRepository.findAll(org.mockito.ArgumentMatchers.any(org.springframework.data.jpa.domain.Specification.class), org.mockito.ArgumentMatchers.eq(PageRequest.of(0, 10)))).thenReturn(page);

        PrestamoSearchDto searchDto = new PrestamoSearchDto();
        searchDto.setPageable(new com.ccsw.tutorial.common.pagination.PageableRequest(0, 10));

        Page<Prestamo> result = prestamoService.findPage(searchDto);

        assertNotNull(result);
        assertEquals(1, result.getContent().size());
    }

    @Test
    public void getExistsPrestamoIdShouldReturnPrestamo() {

        Prestamo prestamo = mock(Prestamo.class);
        when(prestamo.getId()).thenReturn(EXISTS_PRESTAMO_ID);
        when(prestamoRepository.findById(EXISTS_PRESTAMO_ID)).thenReturn(Optional.of(prestamo));

        Prestamo prestamoResponse = prestamoService.get(EXISTS_PRESTAMO_ID);

        assertNotNull(prestamoResponse);
        assertEquals(EXISTS_PRESTAMO_ID, prestamo.getId());
    }

    @Test
    public void getNotExistsPrestamoIdShouldReturnNull() {

        when(prestamoRepository.findById(NOT_EXISTS_PRESTAMO_ID)).thenReturn(Optional.empty());

        Prestamo prestamo = prestamoService.get(NOT_EXISTS_PRESTAMO_ID);

        assertNull(prestamo);
    }

    @Test
    public void saveNotExistsPrestamoIdShouldInsert() {

        PrestamoDto prestamoDto = new PrestamoDto();
        prestamoDto.setInitialDate(new Date());
        prestamoDto.setFinalDate(new Date());

        Game game = mock(Game.class);
        game.setId(GAME_ID);
        prestamoDto.setGame(new com.ccsw.tutorial.game.model.GameDto());
        prestamoDto.getGame().setId(GAME_ID);

        Client client = mock(Client.class);
        client.setId(CLIENT_ID);
        prestamoDto.setClient(new com.ccsw.tutorial.client.model.ClientDto());
        prestamoDto.getClient().setId(CLIENT_ID);

        when(gameService.get(GAME_ID)).thenReturn(game);
        when(clientService.get(CLIENT_ID)).thenReturn(client);

        ArgumentCaptor<Prestamo> prestamo = ArgumentCaptor.forClass(Prestamo.class);

        prestamoService.save(null, prestamoDto);

        verify(prestamoRepository).save(prestamo.capture());
        verify(gameService).get(GAME_ID);
        verify(clientService).get(CLIENT_ID);

        assertNotNull(prestamo.getValue());
        assertEquals(game, prestamo.getValue().getGame());
        assertEquals(client, prestamo.getValue().getClient());
    }

    @Test
    public void saveExistsPrestamoIdShouldUpdate() {

        PrestamoDto prestamoDto = new PrestamoDto();
        prestamoDto.setInitialDate(new Date());
        prestamoDto.setFinalDate(new Date());

        Game game = mock(Game.class);
        game.setId(GAME_ID);
        prestamoDto.setGame(new com.ccsw.tutorial.game.model.GameDto());
        prestamoDto.getGame().setId(GAME_ID);

        Client client = mock(Client.class);
        client.setId(CLIENT_ID);
        prestamoDto.setClient(new com.ccsw.tutorial.client.model.ClientDto());
        prestamoDto.getClient().setId(CLIENT_ID);

        Prestamo prestamo = mock(Prestamo.class);
        when(prestamoRepository.findById(EXISTS_PRESTAMO_ID)).thenReturn(Optional.of(prestamo));
        when(gameService.get(GAME_ID)).thenReturn(game);
        when(clientService.get(CLIENT_ID)).thenReturn(client);

        prestamoService.save(EXISTS_PRESTAMO_ID, prestamoDto);

        verify(prestamoRepository).save(prestamo);
        verify(gameService).get(GAME_ID);
        verify(clientService).get(CLIENT_ID);
    }

    @Test
    public void deleteExistsPrestamoIdShouldDelete() throws Exception {

        Prestamo prestamo = mock(Prestamo.class);
        when(prestamoRepository.findById(EXISTS_PRESTAMO_ID)).thenReturn(Optional.of(prestamo));

        prestamoService.delete(EXISTS_PRESTAMO_ID);

        verify(prestamoRepository).deleteById(EXISTS_PRESTAMO_ID);
    }

    @Test
    public void deleteNotExistsPrestamoIdShouldThrowException() {

        when(prestamoRepository.findById(NOT_EXISTS_PRESTAMO_ID)).thenReturn(Optional.empty());

        assertThrows(Exception.class, () -> {
            prestamoService.delete(NOT_EXISTS_PRESTAMO_ID);
        });

        verify(prestamoRepository, never()).deleteById(NOT_EXISTS_PRESTAMO_ID);
    }

}