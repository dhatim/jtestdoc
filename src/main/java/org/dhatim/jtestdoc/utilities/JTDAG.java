package org.dhatim.jtestdoc.utilities;

import java.io.FileWriter;

import java.io.IOException;

import org.apache.tools.ant.BuildException;
import org.dhatim.jtestdoc.beans.File;

import com.google.gson.GsonBuilder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

/**
 * Java Test Documentation Automatically Generated is in charge of the
 * exportation of the documentation
 *
 * @author Nathanaël Langlois
 * @version 1.0
 */
public class JTDAG {

    // All files that need to be processed
    private final List<File> files;

    // Path of the destination folder
    private final Path destination;

    /**
     * This constructor initializes the templates
     *
     * @param files the files that were processed
     * @param destination the folder where the user wants to put his
     * documentation
     */
    public JTDAG(List<File> files, Path destination) {
        this.files = files;
        this.destination = destination;
    }

    /**
     * This method exports a documentation file from what was processed
     *
     * @throws BuildException if the exportation doesn't work
     */
    public void export() throws BuildException {
        try {
            Files.createDirectories(destination);
            Files.copy(
                    this.getClass().getClassLoader().getResourceAsStream("index.html"),
                    destination.resolve("index.html"), StandardCopyOption.REPLACE_EXISTING);
            Files.copy(
                    this.getClass().getClassLoader().getResourceAsStream("marked.js"),
                    destination.resolve("marked.js"), StandardCopyOption.REPLACE_EXISTING);

            try (FileWriter writer = new FileWriter(destination.resolve("doc.json").toFile())) {
                new GsonBuilder()
                        .disableHtmlEscaping()
                        .create()
                        .toJson(files, writer);
            }

        } catch (IOException e) {
            throw new BuildException(e);
        }

    }

}
