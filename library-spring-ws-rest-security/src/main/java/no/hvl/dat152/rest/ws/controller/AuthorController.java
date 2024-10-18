/**
 *
 */
package no.hvl.dat152.rest.ws.controller;


import no.hvl.dat152.rest.ws.exceptions.AuthorNotFoundException;
import no.hvl.dat152.rest.ws.model.Author;
import no.hvl.dat152.rest.ws.model.Book;
import no.hvl.dat152.rest.ws.service.AuthorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

/**
 *
 */
@PreAuthorize("hasAuthority('ADMIN')")
@RestController
@RequestMapping("/elibrary/api/v1")
public class AuthorController {

	@Autowired
	private AuthorService authorService;

	@PreAuthorize("hasAuthority('USER')")
	@GetMapping("/authors")
	public ResponseEntity<Object> getAllAuthors() {

		List<Author> authors = authorService.findAllAuthor();

		if (authors.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}

		return new ResponseEntity<>(authors, HttpStatus.OK);

	}

	@PreAuthorize("hasAuthority('USER')")
	@GetMapping("/authors/{id}")
	public ResponseEntity<Author> getAuthorById(@PathVariable("id") Long id) throws AuthorNotFoundException {
		Author author = authorService.getAuthorById(id);

		if (author == null) {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}

		return new ResponseEntity<>(author, HttpStatus.OK);
	}

	@PostMapping("/authors")
	public ResponseEntity<Author> createAuthor(@RequestBody Author author) {

		Author newAuthor = authorService.saveAuthor(author);

		return new ResponseEntity<>(newAuthor, HttpStatus.CREATED);
	}

	@PutMapping("/authors/{id}")
	public ResponseEntity<Author> updateAuthor(@PathVariable("id") Long id, @RequestBody Author author) throws
			AuthorNotFoundException {
		author.setAuthorId(id.intValue());
		Author updateAuthor = authorService.updateAuthor(author);

		return new ResponseEntity<>(updateAuthor, HttpStatus.OK);
	}

	@DeleteMapping("/authors/{id}")
	public ResponseEntity<Author> deleteAuthor(@PathVariable("id") Long id) {
		try {
			authorService.deleteAuthor(id);
			return ResponseEntity.ok().build();
		} catch (AuthorNotFoundException e) {
			return ResponseEntity.notFound().build();
		}
	}

	@PreAuthorize("hasAuthority('USER')")
	@GetMapping("/authors/{id}/books")
	public ResponseEntity<Set<Book>> getBooksByAuthorId(@PathVariable("id") Long id) {
		try {
			return ResponseEntity.ok(authorService.findBooksByAuthorId(id));
		} catch (AuthorNotFoundException e) {
			return ResponseEntity.notFound().build();
		}
	}


}
