package com.luizbarros.dscatalog.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.luizbarros.dscatalog.entities.Category;

public interface CategoryRepository extends JpaRepository<Category, Long>{

}
