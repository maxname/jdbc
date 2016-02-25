package ru.gov.emias2.jdbc.generator.helper;

public final class Strings {
    private Strings() {
    }

    /**
     * someString -> SOME_STRING
     * @param string someString
     * @return SOME_STRING
     */
    public static String underscore(String string) {
        if (string != null) {
            return string.replaceAll("([A-Z])", "_$1").toUpperCase();
        }
        return null;
    }

    /**
     * SomeString -> some-string
     * @param string SomeString
     * @return some-string
     */
    public static String dash(String string) {
        if (string != null) {
            string = string.replaceAll("([A-Z])", "-$1").toLowerCase();
            if (string.indexOf("-") == 0) {
                string = string.substring(1);
            }
            return string;
        }
        return null;
    }

    /**
     * someString -> SomeString
     * @param input someString
     * @return SomeString
     */
    public static String capitalize(String input) {
        return input.substring(0, 1).toUpperCase() + input.substring(1);
    }

}
