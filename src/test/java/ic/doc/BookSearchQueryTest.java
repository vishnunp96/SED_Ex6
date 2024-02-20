package ic.doc;

import ic.doc.catalogues.LibraryCatalogue;
import org.jmock.Expectations;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Rule;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class BookSearchQueryTest {


    @Rule
    public JUnitRuleMockery context = new JUnitRuleMockery();
    LibraryCatalogue libraryCatalogue = context.mock(LibraryCatalogue.class);


    @Test
    public void searchesForBooksInLibraryCatalogueByAuthorSurname() {

        BookSearchQuery bookSearchQuery = new BookSearchQueryBuilder()
                .withLastName("dickens")
                .build();

        context.checking(new Expectations() {{
            oneOf(libraryCatalogue).searchFor("LASTNAME='dickens' ");
            will(returnValue(List.of(
                    new Book("Oliver Twist", "Charles Dickens", 1838),
                    new Book("Oliver Twist", "Charles Dickens", 1838)
            )));
        }});
        List<Book> books = bookSearchQuery.execute(libraryCatalogue);

        assertThat(books.size(), is(2));
        assertTrue(books.get(0).matchesAuthor("dickens"));
    }

    @Test
    public void searchesForBooksInLibraryCatalogueByAuthorFirstname() {

        BookSearchQuery bookSearchQuery = new BookSearchQueryBuilder()
                .withFirstName("Jane")
                .build();
        context.checking(new Expectations() {{
            oneOf(libraryCatalogue).searchFor("FIRSTNAME='Jane' ");
            will(returnValue(List.of(
                    new Book("Pride and Prejudice", "Jane Austen", 1813),
                    new Book("Pride and Prejudice", "Jane Austen", 1813)
            )));
        }});
        List<Book> books = bookSearchQuery.execute(libraryCatalogue);

        assertThat(books.size(), is(2));
        assertTrue(books.get(0).matchesAuthor("Austen"));
    }

    @Test
    public void searchesForBooksInLibraryCatalogueByTitle() {

        BookSearchQuery bookSearchQuery = new BookSearchQueryBuilder()
                .withTitleContaining("Two Cities")
                .build();
        context.checking(new Expectations() {{
            oneOf(libraryCatalogue).searchFor("TITLECONTAINS(Two Cities) ");
            will(returnValue(List.of(
                    new Book("A Tale of Two Cities", "Charles Dickens", 1859)
            )));
        }});
        List<Book> books = bookSearchQuery.execute(libraryCatalogue);

        assertThat(books.size(), is(1));
        assertTrue(books.get(0).matchesAuthor("dickens"));
    }

    @Test
    public void searchesForBooksInLibraryCatalogueBeforeGivenPublicationYear() {

        BookSearchQuery bookSearchQuery = new BookSearchQueryBuilder()
                .wasPublishedBefore(1700)
                .build();
        context.checking(new Expectations() {{
            oneOf(libraryCatalogue).searchFor("PUBLISHEDBEFORE(1700) ");
            will(returnValue(List.of(
                    new Book("Hamlet", "William Shakespeare", 1603)
            )));
        }});
        List<Book> books = bookSearchQuery.execute(libraryCatalogue);

        assertThat(books.size(), is(1));
        assertTrue(books.get(0).matchesAuthor("Shakespeare"));
    }

    @Test
    public void searchesForBooksInLibraryCatalogueAfterGivenPublicationYear() {

        BookSearchQuery bookSearchQuery = new BookSearchQueryBuilder()
                .wasPublishedAfter(1950)
                .build();
        context.checking(new Expectations() {{
            oneOf(libraryCatalogue).searchFor("PUBLISHEDAFTER(1950) ");
            will(returnValue(List.of(
                    new Book("Lord of the Flies", "William Golding", 1954)
            )));
        }});
        List<Book> books = bookSearchQuery.execute(libraryCatalogue);

        assertThat(books.size(), is(1));
        assertTrue(books.get(0).matchesAuthor("Golding"));
    }

    @Test
    public void searchesForBooksInLibraryCatalogueWithCombinationOfParameters() {

        BookSearchQuery bookSearchQuery = new BookSearchQueryBuilder()
                .withLastName("dickens")
                .wasPublishedBefore(1840)
                .build();
        context.checking(new Expectations() {{
            oneOf(libraryCatalogue).searchFor("LASTNAME='dickens' PUBLISHEDBEFORE(1840) ");
            will(returnValue(List.of(
                    new Book("Oliver Twist", "Charles Dickens", 1838)
            )));
        }});
        List<Book> books = bookSearchQuery.execute(libraryCatalogue);

        assertThat(books.size(), is(1));
        assertTrue(books.get(0).matchesAuthor("charles dickens"));
    }

    @Test
    public void searchesForBooksInLibraryCatalogueWithCombinationOfTitleAndOtherParameters() {

        BookSearchQuery bookSearchQuery = new BookSearchQueryBuilder()
                .withTitleContaining("of")
                .wasPublishedAfter(1800)
                .wasPublishedBefore(2000)
                .build();
        context.checking(new Expectations() {{
            oneOf(libraryCatalogue)
                    .searchFor("TITLECONTAINS(of) PUBLISHEDAFTER(1800) PUBLISHEDBEFORE(2000) ");
            will(returnValue(List.of(
                    new Book("A Tale of Two Cities", "Charles Dickens", 1859),
                    new Book("The Life and Opinions", "Laurence Sterne", 1759),
                    new Book("The Life and Opinions", "Laurence Sterne", 1759)
            )));
        }});
        List<Book> books = bookSearchQuery.execute(libraryCatalogue);

        assertThat(books.size(), is(3));
        assertTrue(books.get(0).matchesAuthor("charles dickens"));
    }
}
