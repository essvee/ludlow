package com.essvee;

import java.io.*;
import java.util.ArrayList;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;

import org.apache.commons.lang3.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;

public class Main {
    private ArrayList<Record> recordList = new ArrayList<>();
    private static final String HEADER_STRING = "priref\tObject Number\tObject Name\tScientific Name\tDescription\tCollector Name\tCollection Place\t" +
        "OS Grid Ref\tStratigraphy Unit\tStratigraphy Type\tImage URL\n";
    private static final String URL = "https://github.com/NaturalHistoryMuseum/LudlowMuseumImgs/raw/master/";

    public static void main(String[] args) {
        Main main = new Main();
        main.doParse();
        main.writeOut();
    }

    // Parses each <record> element in xml into an object of type Record
    public void doParse() {
        try {
            File inputFile = new File("input.xml");

            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(inputFile);
            doc.getDocumentElement().normalize();

            NodeList nList = doc.getElementsByTagName("record");

            // Todo: Escape any commas in original string and output as csv
            // Todo: Convert OS to lat long (Use Jcoord?)
            // Iterate over each node
            for (int temp = 0; temp < nList.getLength(); temp++) {
                // Returns the item in the list at index point 'temp'
                Node nNode = nList.item(temp);

                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;

                    // Get the value of the first field if we only want one
                    String priref = getFirstElementValue(eElement, "priref");
                    String reproRef = URL + getFirstElementValue(eElement, "reproduction.reference");

                    // Concatenate values of duplicate fields together if we want them all
                    String objectNumber = getElementValue(eElement, "object_number");
                    String objectName = getElementValue(eElement, "object_name");
                    String scientificName = getElementValue(eElement, "taxonomy.scientific_name");
                    String description = getElementValue(eElement, "description");
                    String collectorName = getElementValue(eElement, "field_coll.name");
                    String collectionPlace = getElementValue(eElement, "field_coll.place");
                    String osGridRef = getElementValue(eElement, "field_coll.gridref");
                    String stratigraphyUnit = getElementValue(eElement, "stratigraphy.unit");
                    String stratigraphyType = getElementValue(eElement, "stratigraphy.type");


                    // Create new record object and store
                    Record tempRec = new Record(priref, objectNumber, objectName, scientificName,
                            description, collectorName, collectionPlace, osGridRef, stratigraphyUnit, stratigraphyType,
                            reproRef);
                    recordList.add(tempRec);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // For when you only want the value from the first occurrence of a field
    public String getFirstElementValue(Element eElement, String tagName) {
        String returnString = "";
        int i = 0;

        // Iterates over fields until it finds the first non-empty one
        while (returnString.equals("")) {
            returnString = eElement
                .getElementsByTagName(tagName)
                .item(i)
                .getTextContent();
            i++;
        }

        // For image url field, trim the local filepath so we just have the filename
        if (tagName.equals("reproduction.reference")) {
            returnString = StringUtils.substringAfterLast(returnString, "\\");
        }
        return returnString + "\t";
    }

    // For when you want all the values, concatenated where > 1 exists
    public String getElementValue(Element eElement, String tagName) {
        String returnString = "";
        NodeList nList = eElement.getElementsByTagName(tagName);
        if (nList.getLength() > 0) {
            for (int i = 0; i < nList.getLength(); i++) {
                if (i != (nList.getLength() - 1)) {
                    returnString += eElement
                            .getElementsByTagName(tagName)
                            .item(i)
                            .getTextContent() + "; ";
                } else {
                    returnString += eElement
                            .getElementsByTagName(tagName)
                            .item(i)
                            .getTextContent();
                }
            }

        } else {
            returnString = "unknown";
        }
        return returnString + "\t";
    }

    // Writes each object to a new row in ludlowOut.txt
    public void writeOut() {
        String fileName = "ludlowOut.txt";

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(fileName))) {
            bw.write(HEADER_STRING);
            for (Record record : recordList) {
                bw.write(record.getBigString());
            }
        } catch (IOException ex) {
            System.out.println("Flush failed: couldn't write ludlow.txt");
            ex.printStackTrace();
        }
    }
}