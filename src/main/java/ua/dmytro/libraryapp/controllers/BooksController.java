package ua.dmytro.libraryapp.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ua.dmytro.libraryapp.models.Book;
import ua.dmytro.libraryapp.models.Person;
import ua.dmytro.libraryapp.services.BookService;
import ua.dmytro.libraryapp.services.PersonService;

import javax.validation.Valid;
import java.util.Optional;

@Controller
@RequestMapping("/books")
public class BooksController {
    private final BookService bookService;
    private final PersonService personService;

    @Autowired
    public BooksController(BookService bookService, PersonService personService) {

        this.bookService = bookService;
        this.personService = personService;
    }

    @GetMapping()
    public String index(@RequestParam(defaultValue = "") String page,
                        @RequestParam(defaultValue = "") String books_per_page,
                        Model model) {
        if(page.equals("") || books_per_page.equals("")){
            model.addAttribute("books", bookService.findAll());
            return "books/index";
        }
        model.addAttribute("books", bookService.findAll(Integer.parseInt(page), Integer.parseInt(books_per_page)));

        return "books/index";
    }
    @GetMapping("/search")
    public String searchPage(){
        return "books/search";
    }
    @PostMapping("/search")
        public String search(@RequestParam String searchReguest, Model model){
        model.addAttribute("books", bookService.searchByTitle(searchReguest));

        return "books/search";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable("id") int id, Model model, @ModelAttribute("person") Person person) {
        model.addAttribute("book", bookService.findOne(id));

        Person bookOwner = bookService.findOwner(id);

        if (bookOwner != null)
            model.addAttribute("owner", bookOwner);
        else
            model.addAttribute("people", personService.findAll());

        return "books/show";
    }

    @GetMapping("/new")
    public String newBook(@ModelAttribute("book") Book book) {
        return "books/new";
    }

    @PostMapping()
    public String create(@ModelAttribute("book") @Valid Book book, BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return "books/new";

        bookService.save(book);
        return "redirect:/books";
    }

    @GetMapping("/{id}/edit")
    public String edit(Model model, @PathVariable("id") int id) {
        model.addAttribute("book", bookService.findOne(id));
        return "books/edit";
    }

    @PatchMapping("/{id}")
    public String update(@ModelAttribute("book") @Valid Book book, BindingResult bindingResult, @PathVariable("id") int id) {
        if (bindingResult.hasErrors())
            return "books/edit";

        bookService.update(id, book);
        return "redirect:/books";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id) {
        bookService.delete(id);
        return "redirect:/books";
    }

    @PatchMapping("{id}/release")
    public String release(@PathVariable("id") int id){
        bookService.release(id);
        return "redirect:/books/" + id;
    }

    @PatchMapping("{id}/take")
    public String take(@PathVariable("id") int id, @ModelAttribute("person") Person selectedPerson) {
        bookService.take(id, selectedPerson);
        return "redirect:/books/" + id;
    }

}
