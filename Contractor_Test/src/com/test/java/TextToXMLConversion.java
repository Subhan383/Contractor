package com.test.java;
	
	import org.w3c.dom.Document;
	import org.w3c.dom.Element;
	import org.w3c.dom.Node;

	import javax.xml.parsers.DocumentBuilder;
	import javax.xml.parsers.DocumentBuilderFactory;
	import javax.xml.transform.Transformer;
	import javax.xml.transform.TransformerFactory;
	import javax.xml.transform.dom.DOMSource;
	import javax.xml.transform.stream.StreamResult;
	import java.io.BufferedReader;
	import java.io.File;
	import java.io.FileReader;
	import java.text.Normalizer;
	import java.util.ArrayList;
	import java.util.Collections;
	import java.util.Comparator;
	import java.util.Scanner;

	public class TextToXMLConversion {
	    public static void main(String[] args) throws Exception {

	        class Person {
	            String name;
	            int age;

	            public Person(String name, int age) {
	                this.name = name;
	                this.age = age;
	            }
	        }

	        class ageCompare implements Comparator<Person> {
	            public int compare(Person p1, Person p2) {
	                return p2.age - p1.age;
	            }
	        }
            //First Git
	        Scanner scanner = new Scanner(System.in);
	        System.out.println("Enter the input text file path");
	        String inputFilePath = scanner.next();

	        System.out.println("Enter the output xml file path");
	        String outputFilePath = scanner.next();

	        File file = new File(inputFilePath);
	        BufferedReader br = new BufferedReader(new FileReader(file));
	        ArrayList<Person> records = new ArrayList<Person>();
	        String currentLine = br.readLine();

	        while (currentLine != null) {
	            //Method to remove the accented characters.
	            String input = Normalizer.normalize(currentLine, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "");

	            String[] personDetails = currentLine.split(" ");
	            String name = personDetails[0];
	            int age = Integer.valueOf(personDetails[1]);
	            records.add(new Person(name, age));
	            currentLine = br.readLine();
	        }
	        //Sort the people in descending order based on the age.
	        Collections.sort(records, new ageCompare());

	        //Building the XML file.
	        ClassLoader classLoader = TextToXMLConversion.class.getClass().getClassLoader();
	        ArrayList<Person> inputStream = records;
	        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
	        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
	        Document document = documentBuilder.newDocument();

	        Element root = document.createElement("Person");
	        document.appendChild(root);

	        for (int i = 0; i < records.size(); i++) {
	            Element name = document.createElement("Name");
	            name.appendChild(document.createTextNode(records.get(i).name));
	            root.appendChild(name);

	            Element age = document.createElement("Age");
	            age.appendChild(document.createTextNode(String.valueOf(records.get(i).age)));
	            root.appendChild(age);
	        }

	        TransformerFactory transformerFactory = TransformerFactory.newInstance();
	        Transformer transformer = transformerFactory.newTransformer();
	        DOMSource source = new DOMSource((Node) document);
	        //Sending the converted XML to the XML File. 
	        StreamResult xmlResult = new StreamResult(new File(outputFilePath));
	        transformer.transform(source, xmlResult);
	    }
	}

