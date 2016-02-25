package ru.gov.emias2.jdbc.generator.helper;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;

public final class JavaBuilder {
    private final StringBuilder sb;
    private int offset = 0;
    private static final int STEP = 4;
    private final String className;
    private String packageName;
    private String outputDirectory;

    private JavaBuilder(String className, String packageName, String outputDirectory) {
        this.className = className;
        this.packageName = packageName;
        this.outputDirectory = outputDirectory;
        sb = new StringBuilder();
    }

    public JavaBuilder put() {
        return put("");
    }

    public JavaBuilder put(String format, Object... args) {
        String value = String.format(format, args);
        if (value.matches(".*}.*")) {
            offset -= STEP;
        }

        sb.append(getOffset());
        sb.append(value);
        sb.append(System.lineSeparator());

        if (value.matches(".*\\{$")) {
            offset += STEP;
        }

        return this;
    }

    public JavaBuilder doc(String format, Object... args) {
        return this.put("/**").put(" *  %s", String.format(format, args)).put(" */");
    }

    public JavaBuilder field(String type, String name, String defaultValue) {
        return put("private %s %s%s;", type, Fields.name(name), defaultValue == null ? "" : " = " + defaultValue);
    }

    public JavaBuilder getter(String type, String name, String defaultValue) {
        put("public %s get%s() {", type, Strings.capitalize(name));
        if (defaultValue != null) {
            put("if (this.%s == null) {", Fields.name(name));
            put("set%sDefault();", Strings.capitalize(name));
            put("}");
        }
        put("return %s;", Fields.name(name));
        put("}");
        return this;
    }

    public JavaBuilder setter(String type, String name, String defaultValue) {
        put("public void set%s(%s %s) {", Strings.capitalize(name), type, Fields.name(name));
        put("this.%s = %s;", Fields.name(name), Fields.name(name));
        if (defaultValue != null) {
            put("if (this.%s == null) {", Fields.name(name));
            put("set%sDefault();", Strings.capitalize(name), defaultValue);
            put("}");
        }

        put("}");
        return this;
    }

    @Override
    public String toString() {
        return sb.toString();
    }

    private String getOffset() {
        String str = "";
        for (int i = 0; i < offset; i++)
            str += " ";
        return str;
    }

    public static JavaBuilder get(String className, String packageName, String outputDirectory) {
        JavaBuilder sb = new JavaBuilder(className, packageName, outputDirectory);
        sb.put("////////////////////////////////////////////////////////////////");
        sb.put("//  Auto generated file");
        sb.put("//  Класс был сгенерирован автоматически");
        sb.put("////////////////////////////////////////////////////////////////");
        sb.put();
        sb.put("package %s;", packageName);
        sb.put();
        return sb;
    }

    public JavaBuilder fold(String format, Object... args) {
        return this.put("// <editor-fold desc=\"%s\" defaultstate=\"collapsed\">", String.format(format, args));
    }

    public JavaBuilder unfold() {
        return this.put("// </editor-fold>").put();
    }

    public void save() {
        try {
            Files.write(Paths.get(outputDirectory, className + ".java"), Collections.singletonList(this.toString()), Charset.forName("UTF-8"));
        } catch (IOException e) {
            throw new RuntimeException("Ошибка при генерации", e);
        }
    }
}
