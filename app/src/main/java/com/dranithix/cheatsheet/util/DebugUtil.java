package com.dranithix.cheatsheet.util;

import com.dranithix.cheatsheet.entities.Category;
import com.dranithix.cheatsheet.entities.Note;
import com.dranithix.cheatsheet.entities.Subcategory;

import java.util.ArrayList;
import java.util.List;

public class DebugUtil {

    public static List<Note> testNoteData() {
        List<Note> notes = new ArrayList<>();
        notes.add(new Note("Epsilon-Delta Limits", "https://i.gyazo.com/50fba511977dcfba20b6fca858d45ffd.png"));
        notes.add(new Note("Harmonic Motion", "https://i.gyazo.com/ab05054f432cd47e9ac6b99fb0d51943.png"));
        notes.add(new Note("Parametric Equations", "https://i.gyazo.com/485350f88cb305f41fca5d7d5603e11b.png"));
        notes.add(new Note("Homogeneous Equations", "https://i.gyazo.com/8a3c08e565d9d1bba719e67147e46f59.png"));
        notes.add(new Note("N'th Integral", "https://i.gyazo.com/9efa973833357fac511427f44035839f.png"));
        notes.add(new Note("Fourier Series", "https://i.gyazo.com/cd9201e1af8ab10f25948c13c7bff2fa.png"));
        return notes;
    }

    public static String getFormattedLocationInDegree(double latitude, double longitude) {
        try {
            int latSeconds = (int) Math.round(latitude * 3600);
            int latDegrees = latSeconds / 3600;
            latSeconds = Math.abs(latSeconds % 3600);
            int latMinutes = latSeconds / 60;
            latSeconds %= 60;

            int longSeconds = (int) Math.round(longitude * 3600);
            int longDegrees = longSeconds / 3600;
            longSeconds = Math.abs(longSeconds % 3600);
            int longMinutes = longSeconds / 60;
            longSeconds %= 60;
            String latDegree = latDegrees >= 0 ? "N" : "S";
            String lonDegrees = latDegrees >= 0 ? "E" : "W";

            return Math.abs(latDegrees) + "°" + latMinutes + "'" + latSeconds
                    + "\"" + latDegree + " " + Math.abs(longDegrees) + "°" + longMinutes
                    + "'" + longSeconds + "\"" + lonDegrees;
        } catch (Exception e) {

            return "" + String.format("%8.5f", latitude) + "  "
                    + String.format("%8.5f", longitude);
        }


    }
}
