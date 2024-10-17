/**
 *
 */
package no.hvl.dat152.rest.ws.service;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import no.hvl.dat152.rest.ws.exceptions.BookNotFoundException;
import no.hvl.dat152.rest.ws.exceptions.UpdateBookFailedException;
import no.hvl.dat152.rest.ws.model.Author;
import no.hvl.dat152.rest.ws.model.Book;
import no.hvl.dat152.rest.ws.repository.BookRepository;

/**
 * @author tdoy
 */
@Service
public class BookService {

	@Autowired
	private BookRepository bookRepository;

	/**
	 * Saves a book
	 *
	 * @param book The book to save
	 *
	 * @return Referance to the saved book
	 */
	public Book saveBook(Book book) {
		return bookRepository.save(book);
	}

	/**
	 * Finds all books
	 *
	 * @return A list containing all books (or empty list if applicable)
	 */
	public List<Book> findAll() {
		return (List<Book>) bookRepository.findAll();
	}

	/**
	 * Finds a book with the given ISBN
	 *
	 * @param isbn The ISBN of the book to find
	 *
	 * @return The book, never null
	 *
	 * @throws BookNotFoundException If the book is not found
	 */
	public Book findByISBN(String isbn) throws BookNotFoundException {
		return bookRepository.findByIsbn(isbn)
		                     .orElseThrow(() -> new BookNotFoundException("Book with isbn = " + isbn + " not found!"));
	}

	/**
	 * Updates a book
	 *
	 * @param book The book to update
	 *
	 * @throws BookNotFoundException If the book is not found
	 */
	public Book updateBook(Book book) throws BookNotFoundException {
		Book existingBook = findByISBN(book.getIsbn());

		existingBook.setTitle(book.getTitle());
		existingBook.setAuthors(book.getAuthors());

		return saveBook(existingBook);
	}

	/**
	 * Deletes a book with the given ISBN
	 *
	 * @param isbn The books ISBN
	 *
	 * @throws BookNotFoundException If the book is not found
	 */
	public void deleteBookByISBN(String isbn) throws BookNotFoundException {
		Book book = findByISBN(isbn);
		if (book != null) {
			bookRepository.delete(book);
		} else {
			throw new BookNotFoundException("Book with ISBN= " + isbn + "not found");
		}
	}

	/**
	 * Finds a set of Authors from the given book ISBN
	 *
	 * @param isbn The isbn for teh book
	 *
	 * @return A Set of Authors
	 *
	 * @throws BookNotFoundException If the book is not found
	 */
	public Set<Author> findAuthorsOfBookByISBN(String isbn) throws BookNotFoundException {
		return findByISBN(isbn).getAuthors();
	}

	/**
	 * Deletes a given book by id
	 *
	 * @param id The books id
	 *
	 * @throws BookNotFoundException If the book is not found
	 */
	public void deleteBookById(long id) throws BookNotFoundException {
		Book book = bookRepository.findById(id).orElseThrow(
				() -> new BookNotFoundException("Book with id = " + id + " not found!"));

		bookRepository.delete(book);
	}

	// TODO public List<Book> findAllPaginate(Pageable page)


}
