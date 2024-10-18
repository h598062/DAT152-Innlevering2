/**
 *
 */
package no.hvl.dat152.rest.ws.service;


import no.hvl.dat152.rest.ws.exceptions.AuthorNotFoundException;
import no.hvl.dat152.rest.ws.model.Author;
import no.hvl.dat152.rest.ws.model.Book;
import no.hvl.dat152.rest.ws.repository.AuthorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

/**
 *
 */
@Service
public class AuthorService {
	@Autowired
	private AuthorRepository authorRepository;

	/**
	 *
	 * @param author
	 * @return
	 */
	public Author saveAuthor(Author author) {
		return authorRepository.save(author);
	}

	/**
	 *
	 * @return
	 */
	public List<Author> findAllAuthor() {
		return (List<Author>) authorRepository.findAll();
	}

	/**
	 *
	 * @param authorId
	 * @return
	 * @throws AuthorNotFoundException
	 */
	public Author getAuthorById(long authorId) throws AuthorNotFoundException {
		return authorRepository.findById(authorId)
		                       .orElseThrow(() -> new AuthorNotFoundException(
				                       "Author with id = " + authorId + " not found!"));
	}

	/**
	 *
	 * @param authorId
	 * @return
	 * @throws AuthorNotFoundException
	 */
	public Author findById(long authorId) throws AuthorNotFoundException {
		return getAuthorById(authorId);
	}

	/**
	 *
	 * @param author
	 * @return
	 * @throws AuthorNotFoundException
	 */
	public Author updateAuthor(Author author) throws AuthorNotFoundException {
		Author existingAuthor = getAuthorById(author.getAuthorId());

		existingAuthor.setBooks(author.getBooks());
		existingAuthor.setFirstname(author.getFirstname());
		existingAuthor.setLastname(author.getLastname());

		return saveAuthor(existingAuthor);
	}

	/**
	 *
	 * @param id
	 * @throws AuthorNotFoundException
	 */
	public void deleteAuthor(Long id) throws AuthorNotFoundException {
		Author author = getAuthorById(id);
		authorRepository.delete(author);
	}

	/**
	 *
	 * @param id
	 * @return
	 * @throws AuthorNotFoundException
	 */
	public Set<Book> findBooksByAuthorId(Long id) throws AuthorNotFoundException {
		return authorRepository.findById(id)
		                       .orElseThrow(() -> new AuthorNotFoundException(
				                       "Author with id = " + id + " not found!")).getBooks();
	}
}
