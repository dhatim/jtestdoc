package org.dhatim.jtestdoc.utilities;

import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import org.apache.tools.ant.BuildException;
import org.dhatim.jtestdoc.beans.File;

import com.google.common.io.CharStreams;
import com.google.gson.Gson;

public class JTDAG // Java Test Documentation Automatically Generated
{
	//All files that need to be processed
	ArrayList<File> files;
	
	//Path of the final file
	String destination;
	
	
	//Template files
	public String TEMPLATE="";
	public String JSPARSER="";
	public String MK="";

	public JTDAG(ArrayList<File> files,String destination)
	{
		this.files = files;
		this.destination = destination;

		try {
			TEMPLATE = CharStreams.toString(new InputStreamReader(JTDAG.class.getClassLoader().getResourceAsStream("template.html"),"UTF-8"));
			JSPARSER = CharStreams.toString(new InputStreamReader(JTDAG.class.getClassLoader().getResourceAsStream("jsparser.js"),"UTF-8"));
			MK = CharStreams.toString(new InputStreamReader(JTDAG.class.getClassLoader().getResourceAsStream("marked.js"),"UTF-8"));

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {

			e.printStackTrace();
		}
	}
	
	public void export() throws BuildException
	{	
		Gson gson = new Gson();
		String json = "";
		
		json += gson.toJson(files);
		


		try {
			FileWriter writer = new FileWriter("doc.json");
			writer.write(json);
			writer.close();
			FileWriter writer0 = new FileWriter("doc.js");
			writer0.write("var doc = JSON.parse('"+json+"'"+JSPARSER);
			writer0.close();
			FileWriter writer2 = new FileWriter("documentation.html");
			writer2.write(TEMPLATE);
			writer2.close();
			FileWriter writer3 = new FileWriter("marked.js");
			writer3.write(MK);
			writer3.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
}
