package com.dranithix.cheatsheet.entities;

/**
 * Created by user on 5/21/2016.
 */

public class Subcategory {
    private String id;
    private String name;

    public Subcategory() {}

    public Subcategory(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
