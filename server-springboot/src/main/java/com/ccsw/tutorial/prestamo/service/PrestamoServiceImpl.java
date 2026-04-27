package com.ccsw.tutorial.prestamo.service;

import com.ccsw.tutorial.client.service.ClientService;
import com.ccsw.tutorial.game.service.GameService;
import com.ccsw.tutorial.prestamo.model.Prestamo;
import com.ccsw.tutorial.prestamo.model.PrestamoDto;
import com.ccsw.tutorial.prestamo.model.PrestamoSearchDto;
import com.ccsw.tutorial.prestamo.repository.PrestamoRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

/**
 * @author ccsw
 *
 */
@Service
@Transactional
public class PrestamoServiceImpl implements PrestamoService {

    @Autowired
    PrestamoRepository prestamoRepository;

    @Autowired
    GameService gameService;

    @Autowired
    ClientService clientService;

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Prestamo> findAll() {
        return (List<Prestamo>) this.prestamoRepository.findAll();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Prestamo get(Long id) {
        return this.prestamoRepository.findById(id).orElse(null);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Page<Prestamo> findPage(PrestamoSearchDto dto) {
        org.springframework.data.jpa.domain.Specification<Prestamo> spec = org.springframework.data.jpa.domain.Specification.where(null);

        if (dto.getGameId() != null) {
            spec = spec.and(new com.ccsw.tutorial.prestamo.repository.specification.PrestamoSpecification(new com.ccsw.tutorial.common.criteria.SearchCriteria("game.id", ":", dto.getGameId())));
        }

        if (dto.getClientId() != null) {
            spec = spec.and(new com.ccsw.tutorial.prestamo.repository.specification.PrestamoSpecification(new com.ccsw.tutorial.common.criteria.SearchCriteria("client.id", ":", dto.getClientId())));
        }

        if (dto.getDate() != null) {
            spec = spec.and(new com.ccsw.tutorial.prestamo.repository.specification.PrestamoSpecification(new com.ccsw.tutorial.common.criteria.SearchCriteria("finalDate", ">=", dto.getDate())));
            spec = spec.and(new com.ccsw.tutorial.prestamo.repository.specification.PrestamoSpecification(new com.ccsw.tutorial.common.criteria.SearchCriteria("initialDate", "<=", dto.getDate())));
        }

        return this.prestamoRepository.findAll(spec, dto.getPageable().getPageable());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void save(Long id, PrestamoDto dto) {
        if (dto.getFinalDate().before(dto.getInitialDate())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La fecha de fin no puede ser anterior a la fecha de inicio");
        }

        long diffInMillies = Math.abs(dto.getFinalDate().getTime() - dto.getInitialDate().getTime());
        long diff = java.util.concurrent.TimeUnit.DAYS.convert(diffInMillies, java.util.concurrent.TimeUnit.MILLISECONDS);
        if (diff > 14) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El periodo de préstamo máximo solo podrá ser de 14 días");
        }

        List<Prestamo> gameOverlaps = this.prestamoRepository.findByGameIdAndInitialDateLessThanEqualAndFinalDateGreaterThanEqual(
                dto.getGame().getId(), dto.getFinalDate(), dto.getInitialDate());

        for (Prestamo overlap : gameOverlaps) {
            if (id == null || !overlap.getId().equals(id)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El mismo juego no puede estar prestado a dos clientes distintos en un mismo día");
            }
        }

        List<Prestamo> clientOverlaps = this.prestamoRepository.findByClientIdAndInitialDateLessThanEqualAndFinalDateGreaterThanEqual(
                dto.getClient().getId(), dto.getFinalDate(), dto.getInitialDate());

        java.util.Calendar cal = java.util.Calendar.getInstance();
        java.util.Date start = dto.getInitialDate();
        java.util.Date end = dto.getFinalDate();
        
        cal.setTime(start);
        while (!cal.getTime().after(end)) {
            java.util.Date currentDay = cal.getTime();
            int count = 0;
            for (Prestamo overlap : clientOverlaps) {
                if (id != null && overlap.getId().equals(id)) continue;
                
                // Si el prestamo solapado incluye a currentDay
                if (!overlap.getInitialDate().after(currentDay) && !overlap.getFinalDate().before(currentDay)) {
                    count++;
                }
            }
            if (count >= 2) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Un mismo cliente no puede tener prestados más de 2 juegos en un mismo día");
            }
            cal.add(java.util.Calendar.DATE, 1);
        }

        Prestamo prestamo;

        if (id == null) {
            prestamo = new Prestamo();
        } else {
            prestamo = this.get(id);
        }

        BeanUtils.copyProperties(dto, prestamo, "id", "game", "client");

        prestamo.setGame(gameService.get(dto.getGame().getId()));
        prestamo.setClient(clientService.get(dto.getClient().getId()));

        this.prestamoRepository.save(prestamo);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void delete(Long id) throws Exception {
        if (this.get(id) == null) {
            throw new Exception("Not exists");
        }

        this.prestamoRepository.deleteById(id);
    }

}