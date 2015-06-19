package org.dhatim.jtestdoc.utilities;

import java.io.FileWriter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import org.apache.tools.ant.BuildException;
import org.dhatim.jtestdoc.beans.File;

import com.google.gson.Gson;
import java.util.List;

/**
 * Java Test Documentation Automatically Generated is in charge of the exportation of the documentation
 * @author Nathanaël Langlois
 * @version 1.0
 */
public class JTDAG
{

	// All files that need to be processed
	List<File> files;

	// Path of the final file
	String destination;

	// Template files
	private String TEMPLATE = "";
	private String JSPARSER = "";
	private String MK = "";

	/**
	 * This constructor initializes the templates
	 * @param files the files that were processed
	 * @param destination where the user wants to put his documentation
	 */
	public JTDAG(ArrayList<File> files, String destination) {
		this.files = files;
		this.destination = destination;

		try (Scanner templateScanner = new Scanner(JTDAG.class.getClassLoader()
				.getResourceAsStream("template.html"), "UTF-8");
				Scanner parserScanner = new Scanner(JTDAG.class
						.getClassLoader().getResourceAsStream(
								"jsparser.js.template"), "UTF-8");
				Scanner markedScanner = new Scanner(JTDAG.class
						.getClassLoader().getResourceAsStream("marked.js"),
						"UTF-8");) {
			while(templateScanner.hasNextLine())
				TEMPLATE += templateScanner.nextLine()+"\n";
			while(parserScanner.hasNextLine())
				JSPARSER += parserScanner.nextLine()+"\n";
			while(markedScanner.hasNextLine())
				MK += markedScanner.nextLine()+"\n";
		}

	}
	
	/**
	 * This method exports a documentation file from what was processed
	 * @throws BuildException if the exportation doesn't work
	 */
	public void export() throws BuildException {
		Gson gson = new Gson();
		String json = "";

		json += gson.toJson(files);

		try {
			try (FileWriter writer = new FileWriter("doc.json")) {
				writer.write(json);
			}
			try (FileWriter writer0 = new FileWriter("doc.js")) {
				writer0.write("var doc = JSON.parse('" + json + "'" + JSPARSER);
			}
			try (FileWriter writer2 = new FileWriter("documentation.html")) {
				writer2.write(TEMPLATE);
			}
			try (FileWriter writer3 = new FileWriter("marked.js")) {
				writer3.write(MK);
			}
		} catch (IOException e) {
			throw new BuildException(e);
		}

	}

}
