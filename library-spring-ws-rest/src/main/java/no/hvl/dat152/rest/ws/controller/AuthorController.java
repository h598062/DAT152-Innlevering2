/**
 *
 */
    package no.hvl.dat152.rest.ws.controller;


    import no.hvl.dat152.rest.ws.exceptions.AuthorNotFoundException;
    import no.hvl.dat152.rest.ws.model.Author;
    import no.hvl.dat152.rest.ws.service.AuthorService;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.http.HttpStatus;
    import org.springframework.http.ResponseEntity;
    import org.springframework.web.bind.annotation.*;

    import java.util.List;

    /**
     *
     */
    @RestController
    @RequestMapping("/elibrary/api/v1")
    public class AuthorController {


        // TODO - getBooksByAuthorId (@Mappings, URI, and method)

        @Autowired
        private AuthorService authorService;

        @GetMapping("/authors")
        public ResponseEntity<Object> getAllAuthors() {

            List<Author> authors = authorService.findAllAuthor();

            if (authors.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(authors, HttpStatus.OK);

        }

        @GetMapping("/authors/{id]")
        public ResponseEntity<Author> getAuthorById(@PathVariable("id") Long id) throws AuthorNotFoundException {
            Author author = authorService.getAuthorById(id);

            if(author == null){
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(author,HttpStatus.OK);

        }

        @PostMapping("/authors")
        public ResponseEntity<Author> createAuthor(@RequestBody Author author){

            Author newAuthor = authorService.saveAuthor(author);

            return new ResponseEntity<>(newAuthor, HttpStatus.OK);
        }

        @PutMapping("/authors/{id}")
        public ResponseEntity<Author> updateAuthor(@PathVariable ("id") Long id, @RequestBody Author author) throws AuthorNotFoundException {
            author.setAuthorId(id.intValue());
            Author updateAuthor = authorService.updateAuthor(author);

            return new ResponseEntity<>(updateAuthor, HttpStatus.OK);
        }

        @DeleteMapping("/authors/{id}")
        public ResponseEntity<Author> deleteAuthor(@PathVariable("id")Long id) throws AuthorNotFoundException {
            authorService.deleteAuthor(id);
            return ResponseEntity.noContent().build();

        }




    }
