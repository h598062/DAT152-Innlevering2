package no.hvl.dat152.rest.ws.service;

import no.hvl.dat152.rest.ws.exceptions.RoleNotFoundException;
import no.hvl.dat152.rest.ws.model.Role;
import no.hvl.dat152.rest.ws.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleService {
	@Autowired
	RoleRepository roleRepository;

	public Role findRoleByName(String name) {
		Role role = roleRepository.findByNameIgnoreCase(name);
		if (role == null) {
			throw new RoleNotFoundException("Could not find a role with name " + name);
		} else {
			return role;
		}
	}
}
