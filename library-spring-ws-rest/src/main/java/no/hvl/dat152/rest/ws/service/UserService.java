/**
 *
 */
package no.hvl.dat152.rest.ws.service;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import no.hvl.dat152.rest.ws.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import no.hvl.dat152.rest.ws.exceptions.OrderNotFoundException;
import no.hvl.dat152.rest.ws.exceptions.UserNotFoundException;
import no.hvl.dat152.rest.ws.model.Order;
import no.hvl.dat152.rest.ws.model.User;
import no.hvl.dat152.rest.ws.repository.UserRepository;

/**
 * @author tdoy
 */
@Service
public class UserService {

	@Autowired
	private UserRepository  userRepository;
	@Autowired
	private OrderRepository orderRepository;
	@Autowired
	private OrderService    orderService;

	/**
	 * @return
	 */
	public List<User> findAllUsers() {
		return (List<User>) userRepository.findAll();
	}

	/**
	 * @param userid
	 *
	 * @return
	 *
	 * @throws UserNotFoundException
	 */
	public User findUser(Long userid) throws UserNotFoundException {
		return userRepository.findById(userid)
		                     .orElseThrow(() -> new UserNotFoundException("User with id: " + userid + " not found"));
	}

	/**
	 * @param user
	 *
	 * @return
	 */
	public User saveUser(User user) {
		return userRepository.save(user);
	}

	/**
	 * @param id
	 *
	 * @throws UserNotFoundException
	 */
	public void deleteUser(Long id) throws UserNotFoundException {
		User user = findUser(id);
		userRepository.delete(user);
	}

	/**
	 * @param user
	 * @param id
	 *
	 * @return
	 */
	public User updateUser(User user, Long id) throws UserNotFoundException {
		findUser(id);
		return saveUser(user);
	}

	/**
	 * @param userid
	 *
	 * @return
	 *
	 * @throws UserNotFoundException
	 */
	public Set<Order> getUserOrders(Long userid) throws UserNotFoundException {
		return findUser(userid).getOrders();
	}

	/**
	 * @param userid
	 * @param oid
	 *
	 * @return
	 */
	public Order getUserOrder(Long userid, Long oid) throws UserNotFoundException, OrderNotFoundException {
		return findUser(userid).getOrders()
		                       .stream().filter(o -> o.getId().equals(oid))
		                       .findFirst().orElseThrow(() -> new OrderNotFoundException(
						("User with id = " + userid + " does not have an order with id = " + oid)));
	}

	/**
	 * Removes the order from the user.
	 * NB! This does not delete the order from database.
	 *
	 * @param userid The user id
	 * @param oid    The order id
	 *
	 * @throws UserNotFoundException  If the user is not found
	 * @throws OrderNotFoundException If the order is not found
	 */
	public void deleteOrderForUser(Long userid, Long oid) throws UserNotFoundException, OrderNotFoundException {
		User user = findUser(userid);
		Order order = user.getOrders().stream().filter(o -> o.getId().equals(oid)).findFirst()
		                  .orElseThrow(() -> new OrderNotFoundException(
				                  ("User with id = " + userid + " does not have an order with id = " + oid)));
		user.getOrders().remove(order);
		saveUser(user);
		// We are going out from that the order is not supposed to be deleted from db, just from the users own order list
	}

	/**
	 * Saves the order to database and creates link to the user
	 *
	 * @param userid The user
	 * @param order  the new order
	 *
	 * @return The user with the order attached
	 *
	 * @throws UserNotFoundException If the user is not found
	 */
	public User createOrdersForUser(Long userid, Order order) throws UserNotFoundException {
		User  user       = findUser(userid);
		Order savedOrder = orderService.saveOrder(order);
		user.addOrder(savedOrder);
		return userRepository.save(user);
	}
}
