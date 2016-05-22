package com.dranithix.cheatsheet.util;

import com.dranithix.cheatsheet.entities.Category;
import com.dranithix.cheatsheet.entities.Note;
import com.dranithix.cheatsheet.entities.Question;
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

    public static List<Question> testQuestionData() {
        List<Question> questions = new ArrayList<>();
        questions.add(new Question("\\textrm{Find the domain of the vector function. }\\\\\\\\\n" +
                "\\mathbf r(t) = \\langle \\ln(6 t), \\sqrt{t  + 19}, \\frac{1}{\\sqrt{9 - t}}\n" +
                "\\rangle\n", "\\textrm{Answer:} \\left ( 0,9 \\right )"));
        questions.add(new Question("\\textrm{ Find a vector function that represents the curve of intersection of \n" +
                "the paraboloid }\n" +
                "z = 9 x^2 + 3 y^2 \\textrm{ and the cylinder } y = 6 x^2.\n", "\\textrm{Answer: } r(t) = 9*t*t + (3*6*6)*t^4"));
        questions.add(new Question("\\textrm{ Evaluate the triple integral }\n" +
                "\\displaystyle \\iiint_{E} z \\, dV \n" +
                "\\textrm{ where E is the solid bounded by the cylinder } \n" +
                "y^2+  z^2 = 1024\n" +
                "\\textrm{ and the planes }\n" +
                "x = 0, y = 8 x \\textup{ and }z =0\n" +
                "\\textrm{ in the first octant.}\n", "\\textup{ Answer: } 16384"));
        questions.add(new Question("\\textrm{ Starting from the point }\n" +
                "\\left( 5, 0, -5 \\right)\n" +
                "\\textrm{ reparametrize the curve }\n" +
                "\\mathbf{r} \\left( t \\right) = \\left( 5 + 3 t \\right) \\mathbf{i} +\n" +
                "  \\left( 0 + 3 t \\right) \\mathbf{j} + \\left( -5  - 3 t \\right) \\mathbf{k}\n" +
                "\\textrm{ in terms of arclength. }", "\\textup{Answer: }r(t (s)) = 5i+ (3s)j+ 5.196k\n"));
        questions.add(new Question("\\textrm{ Find the partial derivatives of the function } \\\\\\\\\n" +
                "f(x,y) = \\int_y^x \\cos(-6 t^2 + 2 t  - 4)\\, dt\n", "\\textrm{ Answer: } \\\\\n" +
                "f_x(x,y) = cos(-6x^2 + 2x + -4)\t\\\\\n" +
                "f_y(x,y) = -cos(-6y^2 + 2y + -4)\n"));
        return questions;
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
