package com.ccsw.tutorial.prestamo.repository;

import com.ccsw.tutorial.prestamo.model.Prestamo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

/**
 * @author ccsw
 *
 */
public interface PrestamoRepository extends CrudRepository<Prestamo, Long>, JpaSpecificationExecutor<Prestamo> {

    /**
     * Método para recuperar un listado paginado de {@link Prestamo}
     *
     * @param pageable pageable
     * @return {@link Page} de {@link Prestamo}
     */
    Page<Prestamo> findAll(Pageable pageable);

    @Override
    @EntityGraph(attributePaths = {"client", "game"})
    Page<Prestamo> findAll(Specification<Prestamo> spec, Pageable pageable);

    java.util.List<Prestamo> findByGameIdAndInitialDateLessThanEqualAndFinalDateGreaterThanEqual(Long gameId, java.util.Date finalDate, java.util.Date initialDate);

    java.util.List<Prestamo> findByClientIdAndInitialDateLessThanEqualAndFinalDateGreaterThanEqual(Long clientId, java.util.Date finalDate, java.util.Date initialDate);

}