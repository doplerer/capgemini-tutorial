package com.ccsw.tutorial.category.service;

import com.ccsw.tutorial.category.model.Category;
import com.ccsw.tutorial.category.model.CategoryDto;
import com.ccsw.tutorial.category.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author ccsw
 *
 */
@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    CategoryRepository categoryRepository;

    /**
     * {@inheritDoc}
     */
    @Override
    public Category get(Long id) {

        return this.categoryRepository.findById(id).orElse(null);
    }

    /**
     * {@inheritDoc}
     */
    public List<Category> findAll() {

        return (List<Category>) this.categoryRepository.findAll();
    }

    /**
     * {@inheritDoc}
     */
    public void save(Long id, CategoryDto dto) {

        Category category;

        if (id == null) {
            category = new Category();
        } else {
            category = this.get(id);
        }

        category.setName(dto.getName());
        this.categoryRepository.save(category);
    }

    /**
     * {@inheritDoc}
     */
    public void delete(Long id) throws Exception {

        if (this.get(id) == null) {
            throw new Exception("Not Exists");
        }

        this.categoryRepository.deleteById(id);
    }

}
