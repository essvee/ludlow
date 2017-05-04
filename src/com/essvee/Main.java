package com.essvee;

import java.io.*;
import java.util.ArrayList;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;

import org.apache.commons.lang.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;

public class Main {
    private ArrayList<Record> recordList = new ArrayList<>();
    private String headerString = "priref \t" + "Object Number \t" + "Object Name \t" + "Scientific Name \t" + "Description \t" + "Collector Name \t" + "Collection Place \t"
            + "OS Grid Ref \t" + "Stratigraphy Unit \t" + "Stratigraphy Type \t";
    private int maxColumns = 0;

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
                // Prints on new line
                System.out.println("\nCurrent Element :" + nNode.getNodeName());

                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;

                    // Get the value of each element
                    String priref = eElement
                            .getElementsByTagName("priref")
                            .item(0)
                            .getTextContent() + "\t";
                    String objectNumber = getElementValue(eElement, "object_number");
                    String objectName = getElementValue(eElement, "object_name");
                    String scientificName = getElementValue(eElement, "taxonomy.scientific_name");
                    String description = getElementValue(eElement, "description");
                    String collectorName = getElementValue(eElement, "field_coll.name");
                    String collectionPlace = getElementValue(eElement, "field_coll.place");
                    String osGridRef = getElementValue(eElement, "field_coll.gridref");
                    String stratigraphyUnit = getElementValue(eElement, "stratigraphy.unit");
                    String stratigraphyType = getElementValue(eElement, "stratigraphy.type");
                    String reproRef = getElementValue(eElement, "reproduction.reference");

                    // Create new record object and store
                    Record tempRec = new Record(priref, objectNumber, objectName, scientificName,
                            description, collectorName, collectionPlace, osGridRef, stratigraphyUnit, stratigraphyType,
                            reproRef);
                    recordList.add(tempRec);
                    System.out.println("Size of recordList is: " + recordList.size());

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Checks if field needs to be concatenated and adds tab delimiter.
    public String getElementValue(Element eElement, String tagName) {
        String returnString = "";
        NodeList nList = eElement.getElementsByTagName(tagName);
        if (nList.getLength() > 0 && tagName != "reproduction.reference") {
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
                            .getTextContent() + "\t";
                }
            }

            // Put each image location into a separate column
        } else if ((nList.getLength() > 0 && tagName.equals("reproduction.reference"))) {
            returnString = ""
            int thisColumns = 0;
            for (int i = 0; i < nList.getLength(); i++) {
                returnString += StringUtils.substringAfterLast(eElement
                        .getElementsByTagName(tagName)
                        .item(i)
                        .getTextContent() + "\t ", "\\");
                thisColumns++;
                if (thisColumns > maxColumns) {
                    maxColumns = thisColumns;
                    headerString += "Image link " + (i + 1) + "\t";
                }
                }
        } else {
            returnString = "unknown" + "\t";
        }
        return returnString;
    }

    // Writes each object to a new row in ludlowOut.txt
    public void writeOut() {
        String fileName = "ludlowOut.txt";

        headerString += "\n";

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(fileName))) {
            bw.write(headerString);
            for (Record record : recordList) {
                bw.write(record.getBigString());
            }
        } catch (IOException ex) {
            System.out.println("Flush failed: couldn't write ludlow.txt");
            ex.printStackTrace();
        }
    }
}