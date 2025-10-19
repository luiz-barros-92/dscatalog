package com.luizbarros.dscatalog.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.luizbarros.dscatalog.entities.Product;

public interface ProductRepository extends JpaRepository<Product, Long>{

}
