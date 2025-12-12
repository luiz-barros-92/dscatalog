package com.luizbarros.dscatalog.services;

import java.time.Instant;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.luizbarros.dscatalog.dto.EmailDTO;
import com.luizbarros.dscatalog.entities.PasswordRecover;
import com.luizbarros.dscatalog.entities.User;
import com.luizbarros.dscatalog.repositories.PasswordRecoverRepository;
import com.luizbarros.dscatalog.repositories.UserRepository;
import com.luizbarros.dscatalog.services.exceptions.ResourceNotFoundException;

@Service
public class AuthService {

	@Value("${email.password-recover.token.minutes}")
	private Long tokenMinutes;
	
	private final UserRepository userRepository;
	private final PasswordRecoverRepository passwordRecoverRepository;
	
	public AuthService(UserRepository userRepository, PasswordRecoverRepository passwordRecoverRepository) {
		this.userRepository = userRepository;
		this.passwordRecoverRepository = passwordRecoverRepository;
	}

	@Transactional
	public void createRecoverToken(EmailDTO body) {
		User user = userRepository.findByEmail(body.email());
		if(user == null) {
			throw new ResourceNotFoundException("Email not found");			
		}
		PasswordRecover entity = new PasswordRecover();
		entity.setEmail(body.email());
		entity.setToken(UUID.randomUUID().toString());
		entity.setExpiration(Instant.now().plusSeconds(tokenMinutes * 60L));
		entity = passwordRecoverRepository.save(entity);
	}
}
