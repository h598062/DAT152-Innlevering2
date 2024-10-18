/**
 *
 */
package no.hvl.dat152.rest.ws.controller;

import no.hvl.dat152.rest.ws.exceptions.BookNotFoundException;
import no.hvl.dat152.rest.ws.model.Author;
import no.hvl.dat152.rest.ws.model.Book;
import no.hvl.dat152.rest.ws.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

/**
 * @author tdoy
 */
@PreAuthorize("hasAuthority('ADMIN')")
@RestController
@RequestMapping("/elibrary/api/v1")
public class BookController {

	@Autowired
	private BookService bookService;

	/**
	 *
	 * @return
	 */
	@PreAuthorize("hasAuthority('USER')")
	@GetMapping("/books")
	public ResponseEntity<Object> getAllBooks() {

		List<Book> books = bookService.findAll();

		if (books.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}

		return new ResponseEntity<>(books, HttpStatus.OK);
	}

	/**
	 *
	 * @param isbn
	 * @return
	 * @throws BookNotFoundException
	 */
	@PreAuthorize("hasAuthority('USER')")
	@GetMapping("/books/{isbn}")
	public ResponseEntity<Object> getBook(@PathVariable("isbn") String isbn) throws BookNotFoundException {

		Book book = bookService.findByISBN(isbn);

		return new ResponseEntity<>(book, HttpStatus.OK);

	}

	/**
	 *
	 * @param book
	 * @return
	 */
	@PostMapping("/books")
	public ResponseEntity<Book> createBook(@RequestBody Book book) {

		Book nbook = bookService.saveBook(book);

		return new ResponseEntity<>(nbook, HttpStatus.CREATED);
	}

	/**
	 *
	 * @param book
	 * @param isbn
	 * @return
	 */
	@PutMapping("/books/{isbn}")
	public ResponseEntity<Book> updateBook(@RequestBody Book book, @PathVariable("isbn") String isbn) {
		book.setIsbn(isbn);

		try {
			return ResponseEntity.ok(bookService.updateBook(book));
		} catch (BookNotFoundException e) {
			return ResponseEntity.notFound().build();
		}
	}

	/**
	 *
	 * @param isbn
	 * @return
	 * @throws BookNotFoundException
	 */
	@DeleteMapping("/books/{isbn}")
	public ResponseEntity<Void> deleteBook(@PathVariable("isbn") String isbn) {
		try {
			bookService.deleteBookByISBN(isbn);
			return ResponseEntity.ok().build();
		} catch (BookNotFoundException e) {
			return ResponseEntity.notFound().build();
		}
	}

	/**
	 *
	 * @param isbn
	 * @return
	 */
	@PreAuthorize("hasAuthority('USER')")
	@GetMapping("/books/{isbn}/authors")
	public ResponseEntity<Set<Author>> getAuthorsOfBookByISBN(@PathVariable("isbn") String isbn) {
		try {
			return ResponseEntity.ok(bookService.findAuthorsOfBookByISBN(isbn));
		} catch (BookNotFoundException e) {
			return ResponseEntity.notFound().build();
		}
	}


}
