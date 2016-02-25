package ru.gov.emias2.jdbc.generator;


import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;

import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.codehaus.plexus.util.FileUtils;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Mojo(name = "generate", defaultPhase = LifecyclePhase.GENERATE_SOURCES)
public class GenerateMojo extends AbstractMojo
{
    @Parameter(property = "output", defaultValue = "${project.build.directory}/generated-sources/jdbc/")
    private File output;

    @Parameter(property = "source", required = true)
    private File source;

    public void execute() throws MojoExecutionException {
        if (output.exists()) {
            try {
                FileUtils.cleanDirectory(output);
            } catch (IOException e) {
                e.printStackTrace();
                throw new MojoExecutionException("Error while clean output dir", e);
            }
        }
        List<File> files = getFiles(source);
        RequestGeneratorService service = new RequestGeneratorService();
        service.generate(files, output, getLog());
    }

    private static class MetaFileFilter implements FileFilter {
        @Override
        public boolean accept(File pathname) {
            return pathname.isFile() && pathname.getName().matches(".+\\.xml");
        }
    }

    private static List<File> getFiles(File file) {
        List<File> files = new ArrayList<>(0);
        if (file.exists()) {
            if (file.isFile()) {
                files = Collections.singletonList(file);
            } else if (file.isDirectory()) {
                files = Arrays.asList(file.listFiles(new MetaFileFilter()));
            }
        }
        return files;
    }
}
