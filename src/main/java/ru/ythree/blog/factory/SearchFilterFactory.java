package ru.ythree.blog.factory;

import ru.ythree.blog.model.SearchFilter;

import java.util.ArrayList;
import java.util.List;

public class SearchFilterFactory {
    public static SearchFilter create(String input) {
        if (input == null || input.isEmpty())
            return new SearchFilter();

        String[] tokens = input.split(" ");

        List<String> tags = new ArrayList<>();
        StringBuffer sb = new StringBuffer();
        for (String token : tokens) {
            if (token == null || token.isEmpty()) continue;

            if (token.startsWith("#"))
                tags.add(token.substring(1));
            else
                sb.append(token).append(" ");

        }
        String subStr = sb.toString().trim();

        return new SearchFilter(tags.isEmpty() ? null : tags, subStr.isEmpty() ? null : subStr);
    }
}
