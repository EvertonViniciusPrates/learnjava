package com.backend.config;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.backend.repository.RoleRepository;
import com.backend.repository.UserRepository;
import com.backend.entity.Role;
import com.backend.entity.User;

@Component
public class DataInitializer implements ApplicationListener<ContextRefreshedEvent> {

	@Autowired
	UserRepository userRepository;

	@Autowired
	RoleRepository roleRepository;

	@Autowired
	PasswordEncoder passwordEncoder;
	
	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		List<User> users = userRepository.findAll();
		if(users.isEmpty()) {
			this.createUsers("Everton", "admin", passwordEncoder.encode("123456"), "ROLE_ADMIN");
			this.createUsers("Vinicius", "vini.everton2@hotmail.com", passwordEncoder.encode("rambo31mil"), "ROLE_USER");
			this.createUsers("Prates", "vini.everton3@hotmail.com", passwordEncoder.encode("rambo30mil"), "ROLE_CLIENT");
		}
	}
	
	public void createUsers(String nome, String email, String senha, String role) {
		Role roleObj = new Role();
		roleObj.setName(role);
		
		this.roleRepository.save(roleObj);

		User user = new User(nome, senha, email, Arrays.asList(roleObj));
		userRepository.save(user);
	}
	
}
