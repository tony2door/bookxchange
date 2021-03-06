package com.bookxchange.service.it;

import com.bookxchange.BookExchangeApplication;
import com.bookxchange.model.AuthorEntity;
import com.bookxchange.model.BookEntity;
import com.bookxchange.repository.BooksRepository;
import com.bookxchange.service.AuthorsService;
import com.bookxchange.service.BookService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import static org.junit.Assert.assertNotNull;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK, classes = BookExchangeApplication.class)
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
@PropertySource("classpath:application-test.properties")
public class BookServiceDataBaseRetrieveingTests {

    @Autowired
    private BooksRepository bookRepo;

    @Autowired
    private AuthorsService workingAuthorService;

    @Autowired
    private BookService workingBookService;

    @Before
     public void setUpTestData(){
       AuthorEntity authorOne = new AuthorEntity("NameOne", "Surname One");
       AuthorEntity authorTwo = new AuthorEntity("NameTwo", "Surname Two");
       AuthorEntity authorThree = new AuthorEntity("NameThree", "Surname Three");
       AuthorEntity authorFour = new AuthorEntity("NameFour", "Surname Four");

       ArrayList<AuthorEntity> authorsSetOne = new ArrayList<>();
       authorsSetOne.add(authorOne);

       ArrayList<AuthorEntity> authorsSetTwo = new ArrayList<>();
       authorsSetTwo.add(authorTwo);

       ArrayList<AuthorEntity> authorsSetThree = new ArrayList<>();
       authorsSetThree.add(authorThree);
       authorsSetThree.add(authorFour);

       BookEntity bookOne = new BookEntity("1234567890", "Book One Title", "This is book one description 1234567890987654321", 0,authorsSetOne);
       BookEntity bookTwo = new BookEntity("1231231231", "Book Two Title", "This is book two description 1234567890987654321", 0,authorsSetTwo);
       BookEntity bookThree = new BookEntity("0987654321", "Book Three Title", "This is book three description 1234567890987654321", 0,authorsSetThree);

       bookRepo.save(bookOne);
       bookRepo.save(bookTwo);
       bookRepo.save(bookThree);
   }

   @Test
   public void addABookToTheDataBaseWithNewAuthor() {
       AuthorEntity authorFive = new AuthorEntity("NameFive", "Surname Five");
       ArrayList<AuthorEntity> authorsSetFour = new ArrayList<>();
       authorsSetFour.add(authorFive);

       BookEntity bookFour = new BookEntity("9879879871", "Book Four Title", "This is book four description 1234567890987654321", 0,authorsSetFour);

       workingBookService.addNewBookToDB(bookFour);

       BookEntity savedBook = bookRepo.getByIsbn("9879879871");

       Assert.assertNotNull(savedBook);
       Assert.assertTrue(savedBook.getAuthors().contains(authorFive));
   }


   @Test
   public void userGetsABookBasedOnISBN() {
        assertNotNull(workingBookService.retrieveBookFromDB("1234567890"));
   }

    @Test
    public void userGetsAllBooksInTheDataBase() {
        Assert.assertTrue(0<workingBookService.userRetrievesBookList().size());
    }

    @Test
    public void userAddsABook() {
        AuthorEntity authorSix = new AuthorEntity("NameSix", "Surname Six");
        ArrayList<AuthorEntity> authorsSetSix = new ArrayList<>();
        authorsSetSix.add(authorSix);

        BookEntity bookSix = new BookEntity("4564564561", "Book Five Title", "This is book five description 1234567890987654321", 0,authorsSetSix);

        workingBookService.addNewBookToDB(bookSix);
        Assert.assertEquals(bookSix, workingBookService.retrieveBookFromDB(bookSix.getIsbn()));
    }

}
