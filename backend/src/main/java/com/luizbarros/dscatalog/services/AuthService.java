package com.luizbarros.dscatalog.services;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.luizbarros.dscatalog.dto.EmailDTO;
import com.luizbarros.dscatalog.dto.NewPasswordDTO;
import com.luizbarros.dscatalog.entities.PasswordRecover;
import com.luizbarros.dscatalog.entities.User;
import com.luizbarros.dscatalog.repositories.PasswordRecoverRepository;
import com.luizbarros.dscatalog.repositories.UserRepository;
import com.luizbarros.dscatalog.services.exceptions.ResourceNotFoundException;

import jakarta.validation.Valid;

@Service
public class AuthService {

	@Value("${email.password-recover.token.minutes}")
	private Long tokenMinutes;
	
	@Value("${email.password-recover.uri}")
	private String recoverUri;
	
	private final UserRepository userRepository;
	private final PasswordRecoverRepository passwordRecoverRepository;
	private final EmailService emailService;
	private final PasswordEncoder passwordEncoder;
	
	public AuthService(UserRepository userRepository, PasswordRecoverRepository passwordRecoverRepository, EmailService emailService, PasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.passwordRecoverRepository = passwordRecoverRepository;
		this.emailService = emailService;
		this.passwordEncoder = passwordEncoder;
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

	@Transactional
	public void saveNewPassword(@Valid NewPasswordDTO body) {
		List<PasswordRecover> result = passwordRecoverRepository.searchValidTokens(body.token(), Instant.now());
		if(result.size() == 0){
			throw new ResourceNotFoundException("Invalid token");	
		}		
		User user = userRepository.findByEmail(result.get(0).getEmail());
		user.setPassword(passwordEncoder.encode(body.password()));
		user = userRepository.save(user);
	}
}
