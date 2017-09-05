package com.library.app.author.repository;

import com.library.app.author.model.Author;
import com.library.app.author.model.filter.AuthorFilter;
import com.library.app.common.model.PaginatedData;
import com.library.app.common.model.filter.PaginationData;
import com.library.app.commontests.utils.TestBaseRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static com.library.app.commontests.author.AuthorForTestsRepository.erichGamma;
import static com.library.app.commontests.author.AuthorForTestsRepository.jamesGosling;
import static com.library.app.commontests.author.AuthorForTestsRepository.martinFowler;
import static com.library.app.commontests.author.AuthorForTestsRepository.robertMartin;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

/**
 * Unit tests for Author Repository.
 * Created by wilferaciolli on 21/02/2017.
 */
public class AuthorRepositoryUTest extends TestBaseRepository {

    //Declare entity manager
    private AuthorRepository authorRepository;

    /**
     * Set up entity manager and data.
     */
    @Before
    public void initTestsCase() {
        initializeTestDB();

        authorRepository = new AuthorRepository();
        authorRepository.em = em;
    }

    /**
     * Clear down and close entity manager.
     */
    @After
    public void setDownTestCase() {
        closeEntityManager();
    }

    /**
     * Test add author and find it on the databse.
     */
    @Test
    public void addAuthorAndFindIt() {
        final Long authorAddedId = dbCommandExecutor.executeCommand(() -> {
            return authorRepository.add(robertMartin()).getId();
        });
        assertThat(authorAddedId, is(notNullValue()));

        final Author author = authorRepository.findById(authorAddedId);
        assertThat(author, is(notNullValue()));
        assertThat(author.getName(), is(equalTo(robertMartin().getName())));
    }

    /**
     * Test find author with not known id.
     */
    @Test
    public void findAuthorByIdNotFound() {
        final Author author = authorRepository.findById(999L);
        assertThat(author, is(nullValue()));
    }

    /**
     * test update author.
     */
    @Test
    public void updateAuthor() {
        final Long authorAddedId = dbCommandExecutor.executeCommand(() -> {
            return authorRepository.add(robertMartin()).getId();
        });
        assertThat(authorAddedId, is(notNullValue()));

        final Author author = authorRepository.findById(authorAddedId);
        assertThat(author.getName(), is(equalTo(robertMartin().getName())));

        author.setName("Uncle Bob");
        dbCommandExecutor.executeCommand(() -> {
            authorRepository.update(author);
            return null;
        });

        final Author authorAfterUpdate = authorRepository.findById(authorAddedId);
        assertThat(authorAfterUpdate.getName(), is(equalTo("Uncle Bob")));
    }

    /**
     * Test does author id exists.
     */
    @Test
    public void existsById() {
        final Long authorAddedId = dbCommandExecutor.executeCommand(() -> {
            return authorRepository.add(robertMartin()).getId();
        });

        assertThat(authorRepository.existsById(authorAddedId), is(equalTo(true)));
        assertThat(authorRepository.existsById(999l), is(equalTo(false)));
    }

    /**
     * Test find by filter by not passing any filter.
     */
    @Test
    public void findByFilterNoFilter() {
        //load some authors
        loadDataForFindByFilter();

        final PaginatedData<Author> result = authorRepository.findByFilter(new AuthorFilter());

        //assert that every result came back as there was no filter
        assertThat(result.getNumberOfRows(), is(equalTo(4)));
        assertThat(result.getRows().size(), is(equalTo(4)));

        assertThat(result.getRow(0).getName(), is(equalTo(erichGamma().getName())));
        assertThat(result.getRow(1).getName(), is(equalTo(jamesGosling().getName())));
        assertThat(result.getRow(2).getName(), is(equalTo(martinFowler().getName())));
        assertThat(result.getRow(3).getName(), is(equalTo(robertMartin().getName())));
    }

    /**
     * Test get authors paginated with filters.
     */
    @Test
    public void findByFilterFilteringByNameAndPaginatingAndOrderingDescending() {
        loadDataForFindByFilter();

        //set the pagination attributes
        final AuthorFilter authorFilter = new AuthorFilter();
        authorFilter.setName("o");
        authorFilter.setPaginationData(new PaginationData(0, 2, "name", PaginationData.OrderMode.DESCENDING));

        //call the service and check that only authors that contains the letter o on their names have come back
        PaginatedData<Author> result = authorRepository.findByFilter(authorFilter);
        assertThat(result.getNumberOfRows(), is(equalTo(3)));
        assertThat(result.getRows().size(), is(equalTo(2)));
        assertThat(result.getRow(0).getName(), is(equalTo(robertMartin().getName())));
        assertThat(result.getRow(1).getName(), is(equalTo(martinFowler().getName())));

        authorFilter.setPaginationData(new PaginationData(2, 2, "name", PaginationData.OrderMode.DESCENDING));
        result = authorRepository.findByFilter(authorFilter);

        assertThat(result.getNumberOfRows(), is(equalTo(3)));
        assertThat(result.getRows().size(), is(equalTo(1)));
        assertThat(result.getRow(0).getName(), is(equalTo(jamesGosling().getName())));


    }

    /**
     * Load data for find by filter. This method add a few authors to help on testing
     */
    private void loadDataForFindByFilter() {
        dbCommandExecutor.executeCommand(() -> {
            authorRepository.add(robertMartin());
            authorRepository.add(jamesGosling());
            authorRepository.add(martinFowler());
            authorRepository.add(erichGamma());

            return null;
        });
    }


}
