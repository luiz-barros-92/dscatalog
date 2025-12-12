package com.luizbarros.dscatalog.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.luizbarros.dscatalog.entities.PasswordRecover;

public interface PasswordRecoverRepository extends JpaRepository<PasswordRecover, Long>{

}
