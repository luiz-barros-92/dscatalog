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
	
	@Value("${email.password-recover.uri}")
	private String recoverUri;
	
	private final UserRepository userRepository;
	private final PasswordRecoverRepository passwordRecoverRepository;
	private final EmailService emailService;
	
	public AuthService(UserRepository userRepository, PasswordRecoverRepository passwordRecoverRepository, EmailService emailService) {
		this.userRepository = userRepository;
		this.passwordRecoverRepository = passwordRecoverRepository;
		this.emailService = emailService;
	}

	@Transactional
	public void createRecoverToken(EmailDTO body) {
		User user = userRepository.findByEmail(body.email());
		if(user == null) {
			throw new ResourceNotFoundException("Email not found");			
		}
		String token = UUID.randomUUID().toString();
		PasswordRecover entity = new PasswordRecover();
		entity.setEmail(body.email());
		entity.setToken(token);
		entity.setExpiration(Instant.now().plusSeconds(tokenMinutes * 60L));
		entity = passwordRecoverRepository.save(entity);
		
		String message = "Acess link to set anew password:\n"
				+ recoverUri + token + "Expires in " + tokenMinutes + " minutes.";
		emailService.sendEmail(body.email(), "Password recover", message);
	}
}
