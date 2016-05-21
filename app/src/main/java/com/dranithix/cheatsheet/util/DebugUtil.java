package com.dranithix.cheatsheet.util;

import com.dranithix.cheatsheet.entities.Category;
import com.dranithix.cheatsheet.entities.Subcategory;

import java.util.ArrayList;
import java.util.List;

public class DebugUtil {
    public static List<Category> testCategoryData() {
        List<Category> testData = new ArrayList<Category>();
        testData.add(new Category("abc", "English", "http://www.thehindu.com/multimedia/dynamic/00136/nets_136621f.jpg"));
        testData.add(new Category("def", "Math", "https://www.topsknives.com/media/catalog/product/cache/1/image/464x/040ec09b1e35df139433887a97daa66f/h/o/hofhar01_1_.jpg"));
        return testData;
    }

    public static List<Subcategory> testSubcategoryData() {
        List<Subcategory> testData = new ArrayList<Subcategory>();
        testData.add(new Subcategory("", "Calculus 1"));
        testData.add(new Subcategory("", "Calculus 2"));
        return testData;
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
