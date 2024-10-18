/**
 *
 */
package no.hvl.dat152.rest.ws.service;

import java.util.List;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import no.hvl.dat152.rest.ws.exceptions.OrderNotFoundException;
import no.hvl.dat152.rest.ws.model.Order;
import no.hvl.dat152.rest.ws.repository.OrderRepository;

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
		return orderRepository.findAll();
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

}
