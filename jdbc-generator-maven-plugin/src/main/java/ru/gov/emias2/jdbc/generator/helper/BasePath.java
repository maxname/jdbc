package ru.gov.emias2.jdbc.generator.helper;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

public final class BasePath {

    private BasePath() {
    }

    public static String get(File outputDirectory, String packageName) {
        final String[] parts = packageName.split("\\.");
        Path basePath = Paths.get(outputDirectory.getPath());
        for (String part : parts) {
            basePath = basePath.resolve(part);
        }
        File f = basePath.toFile();
        f.mkdirs();
        return f.toString();
    }
}
