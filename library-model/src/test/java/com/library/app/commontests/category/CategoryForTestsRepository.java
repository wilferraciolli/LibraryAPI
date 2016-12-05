package com.library.app.commontests.category;

import com.library.app.category.model.Category;
import org.junit.Ignore;

import java.util.Arrays;
import java.util.List;

/**
 * Helper class to test categories on the repository. This generate dummy categories to help on unit tests.
 * Created by wilferaciolli on 25/11/2016.
 */
@Ignore
public class CategoryForTestsRepository {

    //create a set of categories for tests
    public static Category java() {
        return new Category("Java");
    }

    public static Category cleanCode() {
        return new Category("Clean Code");
    }

    public static Category architecture() {
        return new Category("Architecture");
    }

    public static Category networks() {
        return new Category("Networks");
    }

    // Category to return an Id, as every other categories return just names
    public static Category categoryWithId(final Category category, final Long id) {
        category.setId(id);
        return category;
    }

    //get all categories
    public static List<Category> allCategories() {
        return Arrays.asList(java(), cleanCode(), architecture(), networks());
    }

}