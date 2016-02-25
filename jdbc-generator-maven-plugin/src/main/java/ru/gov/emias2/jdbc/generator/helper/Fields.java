package ru.gov.emias2.jdbc.generator.helper;

import java.util.Arrays;
import java.util.List;

public class Fields {
    private Fields() {
    }

    private final static String PREFIX = "$";

    private final static List<String> JAVA_KEYWORDS = Arrays.asList(
            "abstract", "continue", "for", "new", "switch", "assert", "default", "goto",
            "package", "synchronized", "boolean", "do", "if", "private", "this", "break",
            "double", "implements", "protected", "throw", "byte", "else", "import",
            "public", "throws", "case", "enum", "instanceof", "return", "transient",
            "catch", "extends", "int", "short", "try", "char", "final", "interface",
            "static", "void", "class", "finally", "long", "strictfp", "volatile", "const",
            "float", "native", "super", "while"
    );

    public static String name(String name) {
        return (JAVA_KEYWORDS.contains(name) ? PREFIX : "") + name;
    }
}
