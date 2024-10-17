/**
 *
 */
package no.hvl.dat152.rest.ws.service;


import no.hvl.dat152.rest.ws.exceptions.AuthorNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import no.hvl.dat152.rest.ws.model.Author;
import no.hvl.dat152.rest.ws.repository.AuthorRepository;

import java.util.List;

/**
 *
 */
@Service
public class AuthorService {

	// TODO public Set<Book> findBooksByAuthorId(Long id)

	@Autowired
	private AuthorRepository authorRepository;


	public Author saveAuthor(Author author) {
		return authorRepository.save(author);

	}

	public List<Author> findAllAuthor() {
		return (List<Author>) authorRepository.findAll();
	}


	public Author getAuthorById(long authorId) throws AuthorNotFoundException {
		return authorRepository.findById(authorId)
				.orElseThrow(() -> new AuthorNotFoundException("Author with id = " + authorId + " not found!"));
	}

	public Author updateAuthor(Author author) throws AuthorNotFoundException {

		Author existingAuthor = getAuthorById(author.getAuthorId());


		existingAuthor.setBooks(author.getBooks());
		existingAuthor.setFirstname(author.getFirstname());
		existingAuthor.setLastname(author.getLastname());

		return saveAuthor(existingAuthor);


	}

	public void deleteAuthor(Long id) throws AuthorNotFoundException {
		Author author = getAuthorById(id);
		authorRepository.delete(author);
	}
}

