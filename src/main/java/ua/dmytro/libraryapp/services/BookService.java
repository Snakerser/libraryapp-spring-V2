package ua.dmytro.libraryapp.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.dmytro.libraryapp.models.Book;
import ua.dmytro.libraryapp.models.Person;
import ua.dmytro.libraryapp.repositories.BookRepository;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class BookService {
    private final BookRepository bookRepository;

    @Autowired
    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public List<Book> findAll() {
        return bookRepository.findAll();
    }

    public List<Book> findAll(int page, int itemPerPage){
        return bookRepository.findAll(PageRequest.of(page,itemPerPage)).getContent();
    }

    public Book findOne(int id){
        Optional<Book> foundBook = bookRepository.findById(id);
        return foundBook.orElse(null);
    }

    public Person findOwner(int id){
        return bookRepository.findById(id).map(Book::getOwner).orElse(null);
    }

    public List<Book> searchByTitle(String searchReguest) {

        return bookRepository.findByNameStartingWith(searchReguest);
    }

    @Transactional
    public void save(Book book){
        bookRepository.save(book);
    }

    @Transactional
    public void update(int id, Book updatedBook){
        updatedBook.setId(id);
        bookRepository.save(updatedBook);
    }

    @Transactional
    public void delete(int id){
        bookRepository.deleteById(id);
    }
    @Transactional
    public void release(int id){
        bookRepository.findById(id).ifPresent(book -> book.setOwner(null));
    }
    @Transactional
    public void take(int id, Person selectedPerson){
        bookRepository.findById(id).ifPresent(book -> book.setOwner(selectedPerson));
    }

}
