package ru.ythree.blog.factory;


import org.junit.jupiter.api.Test;
import ru.ythree.blog.model.SearchFilter;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class SearchFilterFactoryTest {

    @Test
    public void testSearch_inputIsEmpty() {
        SearchFilter searchFilter = SearchFilterFactory.create("");
        assertNull( searchFilter.getTags());
        assertNull(searchFilter.getSearchStr());

        searchFilter = SearchFilterFactory.create("       ");
        assertNull( searchFilter.getTags());
        assertNull(searchFilter.getSearchStr());
    }

    @Test
    public void testSearch() {
        SearchFilter searchFilter = SearchFilterFactory.create("   #tag1 #tag2 one two     #tag3 four  ");
        assertEquals(3, searchFilter.getTags().size());
        assertEquals("tag1", searchFilter.getTags().get(0));
        assertEquals("tag2", searchFilter.getTags().get(1));
        assertEquals("tag3", searchFilter.getTags().get(2));

        assertEquals("one two four", searchFilter.getSearchStr());


        searchFilter = SearchFilterFactory.create("   #tag1 #tag2    #tag3 ");
        assertEquals(3, searchFilter.getTags().size());
        assertNull(searchFilter.getSearchStr());


        searchFilter = SearchFilterFactory.create("   one two      four  ");
        assertNull( searchFilter.getTags());
        assertEquals("one two four", searchFilter.getSearchStr());



        searchFilter = SearchFilterFactory.create("  first #tag1 #tag2 one two     #tag3 four  #tag5");
        assertEquals(4, searchFilter.getTags().size());
        assertEquals("tag1", searchFilter.getTags().get(0));
        assertEquals("tag2", searchFilter.getTags().get(1));
        assertEquals("tag3", searchFilter.getTags().get(2));
        assertEquals("tag5", searchFilter.getTags().get(3));

        assertEquals("first one two four", searchFilter.getSearchStr());
    }

    @Test
    public void testSearch_1() {
        SearchFilter searchFilter = SearchFilterFactory.create("1");
        assertNull(searchFilter.getTags());

        assertEquals("1", searchFilter.getSearchStr());

    }

    @Test
    public void test() {
        List<String> list = Arrays.asList("'tag1", "'tag2'");
        System.out.println(String.join(", ", list));
    }
}