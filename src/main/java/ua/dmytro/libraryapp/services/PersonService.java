package ua.dmytro.libraryapp.services;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.dmytro.libraryapp.models.Book;
import ua.dmytro.libraryapp.models.Person;
import ua.dmytro.libraryapp.repositories.PersonRepository;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class PersonService {
    private final PersonRepository personRepository;

    @Autowired
    public PersonService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    public List<Person> findAll() {
        return personRepository.findAll();
    }

    public Person findOne(int id){
        Optional<Person> foundPerson = personRepository.findById(id);
        return foundPerson.orElse(null);
    }
    public List<Book> findOwnedBooks(int id) {
        Optional<Person> person = personRepository.findById(id);

        if (person.isPresent()) {
            Hibernate.initialize(person.get().getBook());
            return person.get().getBook();
        }
        else {
            return Collections.emptyList();
        }
    }

    @Transactional
    public void save(Person person){
        person.setCreateAt(new Date());
        personRepository.save(person);
    }

    @Transactional
    public void update(int id, Person updatedPerson){
        updatedPerson.setId(id);
        updatedPerson.setCreateAt(new Date());
        personRepository.save(updatedPerson);
    }

    @Transactional
    public void delete(int id){
        personRepository.deleteById(id);
    }

}
