package com.ccsw.tutorial.prestamo.model;

import com.ccsw.tutorial.client.model.ClientDto;
import com.ccsw.tutorial.game.model.GameDto;
import java.util.Date;

/**
 * @author ccsw
 *
 */
public class PrestamoDto {

    private Long id;

    private GameDto game;

    private ClientDto client;

    private Date initialDate;

    private Date finalDate;

    /**
     * @return id
     */
    public Long getId() {
        return this.id;
    }

    /**
     * @param id new value of {@link #getId}.
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return game
     */
    public GameDto getGame() {
        return this.game;
    }

    /**
     * @param game new value of {@link #getGame}.
     */
    public void setGame(GameDto game) {
        this.game = game;
    }

    /**
     * @return client
     */
    public ClientDto getClient() {
        return this.client;
    }

    /**
     * @param client new value of {@link #getClient}.
     */
    public void setClient(ClientDto client) {
        this.client = client;
    }

    /**
     * @return initialDate
     */
    public Date getInitialDate() {
        return this.initialDate;
    }

    /**
     * @param initialDate new value of {@link #getInitialDate}.
     */
    public void setInitialDate(Date initialDate) {
        this.initialDate = initialDate;
    }

    /**
     * @return finalDate
     */
    public Date getFinalDate() {
        return this.finalDate;
    }

    /**
     * @param finalDate new value of {@link #getFinalDate}.
     */
    public void setFinalDate(Date finalDate) {
        this.finalDate = finalDate;
    }

}