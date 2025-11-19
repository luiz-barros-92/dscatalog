package com.luizbarros.dscatalog.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.luizbarros.dscatalog.entities.User;

public interface UserRepository extends JpaRepository<User, Long>{

	User findByEmail(String email);
}
