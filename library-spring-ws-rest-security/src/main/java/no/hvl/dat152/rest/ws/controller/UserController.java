/**
 *
 */
package no.hvl.dat152.rest.ws.controller;

import no.hvl.dat152.rest.ws.exceptions.OrderNotFoundException;
import no.hvl.dat152.rest.ws.exceptions.UserNotFoundException;
import no.hvl.dat152.rest.ws.model.Order;
import no.hvl.dat152.rest.ws.model.User;
import no.hvl.dat152.rest.ws.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * @author tdoy
 */
@RestController
@RequestMapping("/elibrary/api/v1")
public class UserController {

	@Autowired
	private UserService userService;

	@GetMapping("/users")
	public ResponseEntity<Object> getUsers() {

		List<User> users = userService.findAllUsers();

		if (users.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} else {
			return new ResponseEntity<>(users, HttpStatus.OK);
		}
	}

	@GetMapping(value = "/users/{id}")
	public ResponseEntity<Object> getUser(@PathVariable("id") Long id) throws
			UserNotFoundException,
			OrderNotFoundException {

		User user = userService.findUser(id);

		return new ResponseEntity<>(user, HttpStatus.OK);

	}

	@PostMapping("/users")
	public ResponseEntity<User> createUser(@RequestBody User user) {
		User savedUser = userService.saveUser(user);
		return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
	}

	@PutMapping("/users/{id}")
	public ResponseEntity<User> updateUser(@PathVariable("id") Long id, @RequestBody User user) {
		try {
			return ResponseEntity.ok(userService.updateUser(user, id));
		} catch (UserNotFoundException e) {
			return ResponseEntity.notFound().build();
		}
	}

	@DeleteMapping("/users/{id}")
	public ResponseEntity<User> deleteUser(@PathVariable("id") Long id) throws UserNotFoundException {
		userService.deleteUser(id);
		return ResponseEntity.ok().build();
	}


	@GetMapping("/users/{id}/orders")
	public ResponseEntity<Set<Order>> getUserOrders(@PathVariable("id") Long id) throws UserNotFoundException {
		return ResponseEntity.ok(userService.getUserOrders(id));
	}

	@GetMapping("/users/{uid}/orders/{oid}")
	public ResponseEntity<Order> getUserOrder(@PathVariable("uid") Long uid, @PathVariable("oid") Long oid) {
		try {
			return ResponseEntity.ok(userService.getUserOrder(uid, oid));
		} catch (UserNotFoundException | OrderNotFoundException e) {
			return ResponseEntity.notFound().build();
		}
	}

	@DeleteMapping("/users/{uid}/orders/{oid}")
	public ResponseEntity<Order> deleteUserOrder(@PathVariable("uid") Long uid, @PathVariable("oid") Long oid) {
		try {
			userService.deleteOrderForUser(uid, oid);
			return ResponseEntity.ok().build();
		} catch (UserNotFoundException | OrderNotFoundException e) {
			return ResponseEntity.notFound().build();
		}
	}

	// TODO - HATEOAS links
	@PostMapping("/users/{uid}/orders")
	public ResponseEntity<User> createUserOrder(@PathVariable("uid") Long uid, @RequestBody Order order) {
		try {
			User user = userService.createOrdersForUser(uid, order);
			for (final Order o : user.getOrders()) {
				Link ln1 = linkTo(methodOn(OrderController.class)
						.updateOrder(o.getId(), order)).withRel("update");
				Link ln2 = linkTo(methodOn(UserController.class)
						.deleteUserOrder(user.getUserid(), o.getId())).withRel("delete");
				Link ln3 = linkTo(methodOn(UserController.class)
						.getUserOrder(user.getUserid(), o.getId())).withSelfRel();
				Link ln4 = linkTo(methodOn(OrderController.class)
						.getBorrowOrder(o.getId())).withSelfRel();
				o.add(ln1);
				o.add(ln2);
				o.add(ln3);
				o.add(ln4);
			}
			return new ResponseEntity<>(user, HttpStatus.CREATED);
		} catch (UserNotFoundException | OrderNotFoundException e) {
			return ResponseEntity.notFound().build();
		}
	}

}
