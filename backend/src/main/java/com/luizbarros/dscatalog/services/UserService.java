package com.luizbarros.dscatalog.services;

import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.luizbarros.dscatalog.dto.RoleDTO;
import com.luizbarros.dscatalog.dto.UserDTO;
import com.luizbarros.dscatalog.dto.UserInsertDTO;
import com.luizbarros.dscatalog.dto.UserUpdateDTO;
import com.luizbarros.dscatalog.entities.Role;
import com.luizbarros.dscatalog.entities.User;
import com.luizbarros.dscatalog.projections.UserDetailsProjection;
import com.luizbarros.dscatalog.repositories.RoleRepository;
import com.luizbarros.dscatalog.repositories.UserRepository;
import com.luizbarros.dscatalog.services.exceptions.DatabaseException;
import com.luizbarros.dscatalog.services.exceptions.ResourceNotFoundException;

import jakarta.persistence.EntityNotFoundException;

@Service
public class UserService implements UserDetailsService{	
	
	private final UserRepository userRepository;
	private final RoleRepository roleRepository;
	private final PasswordEncoder passwordEncoder;
		
	public UserService(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.roleRepository = roleRepository;
		this.passwordEncoder = passwordEncoder;
	}

	@Transactional(readOnly = true)
	public Page<UserDTO> findAll(Pageable pageable){
		Page<User> page = userRepository.findAll(pageable);
		return page.map(UserDTO::new);
	}

	@Transactional(readOnly = true)
	public UserDTO findById(Long id) {
		return userRepository
				.findById(id)
				.map(UserDTO::new)
				.orElseThrow(() -> new ResourceNotFoundException("Resource not found"));		
	}

	@Transactional
	public UserDTO insert(UserInsertDTO dto) {
		User entity = new User();
		dtoToEntity(dto, entity);
		entity.setPassword(passwordEncoder.encode(dto.getPassword()));
		entity = userRepository.save(entity);
		return new UserDTO(entity);
	}

	@Transactional
	public UserDTO update(Long id, UserUpdateDTO dto) {
		try {
			User entity = userRepository.getReferenceById(id);
			dtoToEntity(dto, entity);
			entity = userRepository.save(entity);
			return new UserDTO(entity);
		}
		catch (EntityNotFoundException e) {
			throw new ResourceNotFoundException("Id - " + id + ": not found");
		}		
	}

	@Transactional(propagation = Propagation.SUPPORTS)
	public void delete(Long id) {
		if (!userRepository.existsById(id)) {
			throw new ResourceNotFoundException("Resource not found");
		}
		try {
			userRepository.deleteById(id);    		
		}
	    	catch (DataIntegrityViolationException e) {
	        	throw new DatabaseException("Referential integrity violation");
	   	}
	}
	
	private void dtoToEntity(UserDTO dto, User entity) {
		entity.setFirstName(dto.getFirstName());
		entity.setLastName(dto.getLastName());
		entity.setEmail(dto.getEmail());		
		entity.getRoles().clear();
        for (RoleDTO roleDto : dto.getRoles()) {
        	Role role = roleRepository.getReferenceById(roleDto.getId());
        	entity.getRoles().add(role);
        }
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		List<UserDetailsProjection> result = userRepository.searchUserAndRolesByEmail(username);
		if(result.size() == 0) {
			throw new UsernameNotFoundException("User not found");
		}
		User user = new User();
		user.setEmail(username);
		user.setPassword(result.get(0).getPassword());
		for(UserDetailsProjection projection : result) {
			user.addRole(new Role(projection.getRoleId(), projection.getAuthority()));
		}
		return user;
	}
}
