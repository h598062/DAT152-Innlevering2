/**
 *
 */
package no.hvl.dat152.rest.ws.controller;

import no.hvl.dat152.rest.ws.exceptions.OrderNotFoundException;
import no.hvl.dat152.rest.ws.model.Order;
import no.hvl.dat152.rest.ws.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

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
	public ResponseEntity<List<Order>> getAllBorrowOrders(
			@RequestParam(name = "expiry", required = false) LocalDate expiry,
			@RequestParam(name = "page", required = false) Integer page,
			@RequestParam(name = "size", required = false) Integer size) {
		if (expiry != null && page != null && size != null) {
			PageRequest pageRequest = PageRequest.of(page, size, Sort.by("expiry"));
			return ResponseEntity.ok(orderService.findByExpiryDate(expiry, pageRequest));
		} else {
			return ResponseEntity.ok(orderService.findAllOrders());
		}
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
