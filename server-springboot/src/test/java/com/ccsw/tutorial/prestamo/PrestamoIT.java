package com.ccsw.tutorial.prestamo;

import com.ccsw.tutorial.prestamo.model.PrestamoDto;
import com.ccsw.tutorial.prestamo.model.PrestamoSearchDto;
import com.ccsw.tutorial.game.model.GameDto;
import com.ccsw.tutorial.client.model.ClientDto;
import com.ccsw.tutorial.common.pagination.PageableRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.ccsw.tutorial.config.ResponsePage;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class PrestamoIT {

    public static final String LOCALHOST = "http://localhost:";
    public static final String SERVICE_PATH = "/prestamo";

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    ParameterizedTypeReference<List<PrestamoDto>> responseType = new ParameterizedTypeReference<List<PrestamoDto>>() {
    };

    ParameterizedTypeReference<ResponsePage<PrestamoDto>> responsePageType = new ParameterizedTypeReference<ResponsePage<PrestamoDto>>() {
    };

    @Test
    public void findAllShouldReturnAllPrestamos() {

        ResponseEntity<List<PrestamoDto>> response = restTemplate.exchange(LOCALHOST + port + SERVICE_PATH, HttpMethod.GET, null, responseType);

        assertNotNull(response);
        assertEquals(0, response.getBody().size());
    }

    @Test
    public void findPageShouldReturnPageOfPrestamos() {

        PrestamoSearchDto dto = new PrestamoSearchDto();
        PageableRequest pageableRequest = new PageableRequest(0, 10);
        dto.setPageable(pageableRequest);

        ResponseEntity<ResponsePage<PrestamoDto>> response = restTemplate.exchange(
            LOCALHOST + port + SERVICE_PATH,
            HttpMethod.POST,
            new HttpEntity<>(dto),
            responsePageType
        );

        assertNotNull(response);
        // Initially empty
        assertEquals(0, response.getBody().getContent().size());
    }

    public static final Long NEW_PRESTAMO_ID = 1L;

    @Test
    public void saveWithoutIdShouldCreateNewPrestamo() {

        PrestamoDto dto = new PrestamoDto();
        dto.setInitialDate(new Date());
        dto.setFinalDate(new Date());

        GameDto gameDto = new GameDto();
        gameDto.setId(1L);
        dto.setGame(gameDto);

        ClientDto clientDto = new ClientDto();
        clientDto.setId(1L);
        dto.setClient(clientDto);

        restTemplate.exchange(LOCALHOST + port + SERVICE_PATH, HttpMethod.PUT, new HttpEntity<>(dto), Void.class);

        ResponseEntity<List<PrestamoDto>> response = restTemplate.exchange(LOCALHOST + port + SERVICE_PATH, HttpMethod.GET, null, responseType);
        assertNotNull(response);
        assertEquals(1, response.getBody().size());

        PrestamoDto prestamoSearch = response.getBody().stream().filter(item -> item.getId().equals(NEW_PRESTAMO_ID)).findFirst().orElse(null);
        assertNotNull(prestamoSearch);
    }

    public static final Long MODIFY_PRESTAMO_ID = 1L;

    @Test
    public void modifyWithExistIdShouldModifyPrestamo() {

        // First create a prestamo
        PrestamoDto dto = new PrestamoDto();
        dto.setInitialDate(new Date());
        dto.setFinalDate(new Date());

        GameDto gameDto = new GameDto();
        gameDto.setId(1L);
        dto.setGame(gameDto);

        ClientDto clientDto = new ClientDto();
        clientDto.setId(1L);
        dto.setClient(clientDto);

        restTemplate.exchange(LOCALHOST + port + SERVICE_PATH, HttpMethod.PUT, new HttpEntity<>(dto), Void.class);

        // Modify it
        PrestamoDto modifyDto = new PrestamoDto();
        modifyDto.setInitialDate(new Date());
        modifyDto.setFinalDate(new Date());

        GameDto gameDto2 = new GameDto();
        gameDto2.setId(2L);
        modifyDto.setGame(gameDto2);

        ClientDto clientDto2 = new ClientDto();
        clientDto2.setId(2L);
        modifyDto.setClient(clientDto2);

        restTemplate.exchange(LOCALHOST + port + SERVICE_PATH + "/" + MODIFY_PRESTAMO_ID, HttpMethod.PUT, new HttpEntity<>(modifyDto), Void.class);

        ResponseEntity<List<PrestamoDto>> response = restTemplate.exchange(LOCALHOST + port + SERVICE_PATH, HttpMethod.GET, null, responseType);
        assertNotNull(response);
        assertEquals(1, response.getBody().size());

        PrestamoDto prestamoSearch = response.getBody().stream().filter(item -> item.getId().equals(MODIFY_PRESTAMO_ID)).findFirst().orElse(null);
        assertNotNull(prestamoSearch);
    }

    @Test
    public void modifyWithNotExistIdShouldInternalError() {

        PrestamoDto dto = new PrestamoDto();
        dto.setInitialDate(new Date());
        dto.setFinalDate(new Date());

        GameDto gameDto = new GameDto();
        gameDto.setId(1L);
        dto.setGame(gameDto);

        ClientDto clientDto = new ClientDto();
        clientDto.setId(1L);
        dto.setClient(clientDto);

        ResponseEntity<?> response = restTemplate.exchange(LOCALHOST + port + SERVICE_PATH + "/999", HttpMethod.PUT, new HttpEntity<>(dto), Void.class);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    public static final Long DELETE_PRESTAMO_ID = 1L;

    @Test
    public void deleteWithExistsIdShouldDeletePrestamo() {

        // First create a prestamo
        PrestamoDto dto = new PrestamoDto();
        dto.setInitialDate(new Date());
        dto.setFinalDate(new Date());

        GameDto gameDto = new GameDto();
        gameDto.setId(1L);
        dto.setGame(gameDto);

        ClientDto clientDto = new ClientDto();
        clientDto.setId(1L);
        dto.setClient(clientDto);

        restTemplate.exchange(LOCALHOST + port + SERVICE_PATH, HttpMethod.PUT, new HttpEntity<>(dto), Void.class);

        // Delete it
        restTemplate.exchange(LOCALHOST + port + SERVICE_PATH + "/" + DELETE_PRESTAMO_ID, HttpMethod.DELETE, null, Void.class);

        ResponseEntity<List<PrestamoDto>> response = restTemplate.exchange(LOCALHOST + port + SERVICE_PATH, HttpMethod.GET, null, responseType);
        assertNotNull(response);
        assertEquals(0, response.getBody().size());
    }

    @Test
    public void deleteWithNotExistsIdShouldInternalError() {

        ResponseEntity<?> response = restTemplate.exchange(LOCALHOST + port + SERVICE_PATH + "/999", HttpMethod.DELETE, null, Void.class);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

}