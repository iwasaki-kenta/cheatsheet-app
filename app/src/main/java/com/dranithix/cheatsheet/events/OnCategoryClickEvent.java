package com.dranithix.cheatsheet.events;

import com.dranithix.cheatsheet.entities.Category;

/**
 * Created by user on 5/21/2016.
 */

public class OnCategoryClickEvent {
    public final Category category;

    public OnCategoryClickEvent(Category category) {
        this.category = category;
    }
}
