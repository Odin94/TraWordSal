package TraWordSal.Graph;

import java.util.concurrent.ThreadLocalRandom;

public class Utils {
    public static String capitalize(String str) {
        return str.substring(0, 1).toUpperCase() + str.substring(1).toLowerCase();
    }


    // Assumes x and y are non-empty strings of same length (could assert this and
    // throw if precondition is violated)
    public static int levenshteinDistance(String x, String y) {
        if (x.isEmpty()) {
            return y.length();
        }

        if (y.isEmpty()) {
            return x.length();
        }

        try {
            int substitution = levenshteinDistance(x.substring(1), y.substring(1))
                    + costOfSubstitution(x.charAt(0), y.charAt(0));

            return substitution;
        } catch (Exception e) {
            System.err.println("Words: " + x + ", " + y);
            throw e;
        }
    }

    public static int costOfSubstitution(char a, char b) {
        return a == b ? 0 : 1;
    }

    public static int rnd(int min, int max) {
        return ThreadLocalRandom.current().nextInt(min, max + 1);
    }
}

