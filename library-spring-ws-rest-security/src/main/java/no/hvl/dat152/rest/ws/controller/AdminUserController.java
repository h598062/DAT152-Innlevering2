/**
 * 
 */
package no.hvl.dat152.rest.ws.controller;


import no.hvl.dat152.rest.ws.model.Role;
import no.hvl.dat152.rest.ws.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import no.hvl.dat152.rest.ws.exceptions.UserNotFoundException;
import no.hvl.dat152.rest.ws.model.User;
import no.hvl.dat152.rest.ws.service.AdminUserService;

/**
 * @author tdoy
 */
@PreAuthorize("hasAuthority('ADMIN')")
@RestController
@RequestMapping("/elibrary/api/v1/admin")
public class AdminUserController {

	@Autowired
	private AdminUserService userService;
	@Autowired
	private RoleService      roleService;

	@PutMapping("/users/{id}")
	public ResponseEntity<Object> updateUserRole(@PathVariable("id") Long id, @RequestParam("role") String roleName)
			throws UserNotFoundException{
		
		User user = userService.findUser(id);
		Role role = roleService.findRoleByName(roleName);

		user.addRole(role);
		return ResponseEntity.ok(userService.saveUser(user));
	}
	
	@DeleteMapping("/users/{id}")
	public ResponseEntity<Object> deleteUserRole(@PathVariable("id") Long id,
			@RequestParam("role") String role) throws UserNotFoundException{

		User user = userService.findUser(id);
		Role foundRole = roleService.findRoleByName(role);

		user.removeRole(foundRole);
		return ResponseEntity.ok(userService.saveUser(user));
	}
	
}
