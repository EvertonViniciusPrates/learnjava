package com.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.backend.entity.User;
import com.backend.repository.UserRepository;

@RestController
@RequestMapping("/user")
public class UserController {
	
	@Autowired
	UserRepository userRepository;
	
	@Secured({"ROLE_ADMIN"})
	@RequestMapping(value = "", method = RequestMethod.GET)
	public Page<User> listar(@RequestParam("page") int page, @RequestParam("size") int size){
		return userRepository.findAll(PageRequest.of(page, size));
	}
	
	@RequestMapping(value = "/salvar", method = RequestMethod.POST)
	public User salvar(@RequestBody User user){		
		return userRepository.save(user);
	}
	
	@RequestMapping(value = "/salvar", method = RequestMethod.PUT)
	public User alterar(@RequestBody User user){		
		return userRepository.save(user);
	}
	
	@RequestMapping(value = "/remover", method = RequestMethod.DELETE)
	public void remover(@RequestBody User user){		
		userRepository.delete(user);
	}
}
