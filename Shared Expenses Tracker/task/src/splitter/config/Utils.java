package splitter.config;

import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.regex.Matcher;

@Slf4j
public class Utils {

    public static int DEBUG_LEVEL = 0;
    public static String OUTPUT_PATH = "output.txt";

    public static BigDecimal createBigDecimal(String value) {
        return new BigDecimal(value)
            .setScale(2, RoundingMode.CEILING);
    }

    public static BigDecimal createBigDecimal(BigDecimal value) {
        return value
            .setScale(2, RoundingMode.CEILING);
    }

    public static int getRandomDistance(int min, int max) {
        Random random = new Random();
        return random.ints(min, max)
            .findFirst()
            .getAsInt();
    }

    public static void log(Object value) {
        if (DEBUG_LEVEL == 1) {
            System.out.println(value);
        } else if (DEBUG_LEVEL == 2) {
            try {
                PrintWriter out = new PrintWriter(
                    new FileWriter(OUTPUT_PATH, true)
                );
                out.println(value);
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static boolean printStackTrace() {
        return Utils.DEBUG_LEVEL == 1;
    }

    public static List<String> toList(Matcher commandParams) {
        List<String> params = new ArrayList<>();
        for (int i = 1; i <= commandParams.groupCount(); i++) {
            params.add(commandParams.group(i));
        }
        return params;
    }

    public static <T> List<T> disjunction(final List<? extends T> s1, final List<? extends T> s2) {
        List<T> symmetricDiff = new ArrayList<>(s1);
        symmetricDiff.addAll(s2);
        List<T> tmp = new ArrayList<T>(s1);
        tmp.retainAll(s2);
        symmetricDiff.removeAll(tmp);
        return symmetricDiff;
    }
}
