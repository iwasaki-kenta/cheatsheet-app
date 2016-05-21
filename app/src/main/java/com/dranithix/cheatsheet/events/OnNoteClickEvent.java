package com.dranithix.cheatsheet.events;

import com.dranithix.cheatsheet.entities.Note;

/**
 * Created by user on 5/22/2016.
 */

public class OnNoteClickEvent {
    public final Note note;

    public OnNoteClickEvent(Note note) {
        this.note = note;
    }
}
