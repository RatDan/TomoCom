package com.danrat.tomocom.Model;

import java.util.List;

public class Interest {
    private final String name;
    private final List<String> subcategories;

    public Interest(String name, List<String> subcategories) {
        this.name = name;
        this.subcategories = subcategories;
    }

    public String getCategory() {
        return name;
    }

    public List<String> getSubcategories() {
        return subcategories;
    }
}
