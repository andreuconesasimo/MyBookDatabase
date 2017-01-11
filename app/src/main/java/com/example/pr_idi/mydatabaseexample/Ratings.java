package com.example.pr_idi.mydatabaseexample;

/**
 * Created by cmarchal on 1/9/17.
 */

public final class Ratings {
    private Ratings () {}

    public static int valueOf(String rating) {
        switch (rating) {
            case "molt dolent": return 0;
            case "dolent": return 1;
            case "regular": return 2;
            case "bo": return 3;
            case "molt bo": return 4;
        }
        throw new IllegalArgumentException();
    }

    public static String toText(float rating) {
        switch ((int) rating) {
            case 0: return "molt dolent";
            case 1: return "dolent";
            case 2: return "regular";
            case 3: return "bo";
            case 4: return "molt bo";
        }
        throw new IllegalArgumentException();
    }
}
