/**
 *
 */
package no.hvl.dat152.rest.ws.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import no.hvl.dat152.rest.ws.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import no.hvl.dat152.rest.ws.exceptions.OrderNotFoundException;
import no.hvl.dat152.rest.ws.exceptions.UserNotFoundException;
import no.hvl.dat152.rest.ws.model.Order;
import no.hvl.dat152.rest.ws.service.OrderService;

/**
 * @author tdoy
 */
@RestController
@RequestMapping("/elibrary/api/v1")
public class OrderController {

	@Autowired
	OrderService orderService;

	// TODO - getAllBorrowOrders (@Mappings, URI=/orders, and method) + filter by expiry and paginate
	@GetMapping("/orders")
	public ResponseEntity<List<Order>> getAllBorrowOrders() {
		return ResponseEntity.ok(orderService.findAllOrders());
	}

	// TODO - getBorrowOrder (@Mappings, URI=/orders/{id}, and method)
	@GetMapping("/orders/{id}")
	public ResponseEntity<Order> getBorrowOrder(@PathVariable Long id) throws OrderNotFoundException {
		return ResponseEntity.ok(orderService.findOrder(id));
	}

	@PutMapping("/orders/{id}")
	public ResponseEntity<Order> updateOrder(@PathVariable Long id, @RequestBody Order order) {
		try {
			return ResponseEntity.ok(orderService.updateOrder(order, id));
		} catch (OrderNotFoundException e) {
			return ResponseEntity.notFound().build();
		}
	}

	// TODO - deleteBookOrder (@Mappings, URI=/orders/{id}, and method)
	@DeleteMapping("/orders/{id}")
	public ResponseEntity<Order> deleteBookOrder(@PathVariable Long id) {
		try {
			orderService.deleteOrder(id);
			return ResponseEntity.ok().build();
		} catch (OrderNotFoundException e) {
			return ResponseEntity.notFound().build();
		}
	}
}
