/**
 *
 */
package no.hvl.dat152.rest.ws.service;

import no.hvl.dat152.rest.ws.exceptions.OrderNotFoundException;
import no.hvl.dat152.rest.ws.exceptions.UnauthorizedOrderActionException;
import no.hvl.dat152.rest.ws.model.Order;
import no.hvl.dat152.rest.ws.repository.OrderRepository;
import no.hvl.dat152.rest.ws.security.service.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

/**
 * @author tdoy
 */
@Service
public class OrderService {

	@Autowired
	private OrderRepository orderRepository;

	/**
	 * @param order
	 *
	 * @return
	 */
	public Order saveOrder(Order order) {
		order = orderRepository.save(order);
		return order;
	}

	/**
	 * @param id
	 *
	 * @return
	 *
	 * @throws OrderNotFoundException
	 */
	public Order findOrder(Long id) throws OrderNotFoundException {
		// verify who is making this request - Only ADMIN or SUPER_ADMIN can access any order
		return orderRepository.findById(id)
		                      .orElseThrow(() -> new OrderNotFoundException(
				                      "Order with id: " + id + " not found in the order list!"));
	}

	/**
	 * @param id
	 */
	public void deleteOrder(Long id) throws OrderNotFoundException {
		Order order = orderRepository.findById(id).orElseThrow(() -> new OrderNotFoundException(
				"Order with id: " + id + " not found in the order list!"));
		orderRepository.delete(order);
	}

	/**
	 * @return
	 */
	public List<Order> findAllOrders() {
		return (List<Order>) orderRepository.findAll();
	}

	/**
	 * @param expiry
	 * @param page
	 *
	 * @return
	 */
	public List<Order> findByExpiryDate(LocalDate expiry, Pageable page) {
		return orderRepository.findByExpiryBefore(expiry, page).get().toList();
	}

	/**
	 * @param order
	 * @param id
	 *
	 * @return
	 *
	 * @throws OrderNotFoundException
	 */
	public Order updateOrder(Order order, Long id) throws OrderNotFoundException {
		findOrder(id);
		return saveOrder(order);
	}

	private boolean verifyPrincipalOfOrder(Long id) throws UnauthorizedOrderActionException {

		UserDetailsImpl userPrincipal = (UserDetailsImpl) SecurityContextHolder.getContext()
		                                                                       .getAuthentication().getPrincipal();
		// verify if the user sending request is an ADMIN or SUPER_ADMIN
		for (GrantedAuthority authority : userPrincipal.getAuthorities()) {
			if (authority.getAuthority().equals("ADMIN") ||
			    authority.getAuthority().equals("SUPER_ADMIN")) {
				return true;
			}
		}

		// otherwise, make sure that the user is the one who initially made the order
		String email = orderRepository.findEmailByOrderId(id);

		if (email.equals(userPrincipal.getEmail())) {
			return true;
		}

		throw new UnauthorizedOrderActionException("Unauthorized order action!");

	}
}
