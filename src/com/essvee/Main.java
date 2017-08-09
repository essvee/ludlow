package com.essvee;

import java.io.*;
import java.util.ArrayList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.lang3.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;

public class Main {
    private ArrayList<Record> recordList = new ArrayList<>();
    private static final String HEADER_STRING = "priref,Object Number,Object Name,Scientific Name,Description,Collector Name,Collection Place," +
        "OS Grid Ref,Stratigraphy Unit,Stratigraphy Type,Image URL,Latitude,Longitude\n";
    private static final String URL = "https://github.com/NaturalHistoryMuseum/LudlowMuseumImgs/raw/master/";

    public static void main(String[] args) {
        Main main = new Main();
        main.doParse();
        main.writeOut();
    }

    // Parses each <record> element in xml into an object of type Record
    private void doParse() {
        try {
            File inputFile = new File("input.xml");

            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(inputFile);
            doc.getDocumentElement().normalize();

            NodeList nList = doc.getElementsByTagName("record");

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
                    String osGridRef = getElementValue(eElement, "field_coll.gridref");
                    String stratigraphyUnit = getElementValue(eElement, "stratigraphy.unit");
                    String stratigraphyType = getElementValue(eElement, "stratigraphy.type");
                    String scientificName = getElementValue(eElement, "taxonomy.scientific_name");
                    String description = getElementValue(eElement, "description");
                    String collectorName = getElementValue(eElement, "field_coll.name");
                    String collectionPlace = getElementValue(eElement, "field_coll.place");

                    // Create new record object and store
                    Record tempRec = new Record(priref, objectNumber, objectName, scientificName,
                        description, collectorName, collectionPlace, osGridRef, stratigraphyUnit,
                        stratigraphyType, reproRef);
                    recordList.add(tempRec);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String escapeCommas(String strIn) {
        String strOut = strIn;
        if (StringUtils.contains(strIn, ",")) {
        strOut = "\"" + strIn + "\"";
      }
      return strOut;
    }

    // For when you only want the value from the first occurrence of a field
    private String getFirstElementValue(Element eElement, String tagName) {
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

        // For image field, extract filename from filepath
        if (tagName.equals("reproduction.reference")) {
            returnString = StringUtils.substringAfterLast(returnString, "\\");
        }
        return escapeCommas(returnString);
    }

    // For when you want all the values, concatenated where > 1 exists
    private String getElementValue(Element eElement, String tagName) {
        String returnString = "";
        NodeList nList = eElement.getElementsByTagName(tagName);
        if (nList.getLength() > 0) {
            for (int i = 0; i < nList.getLength(); i++) {
                if (i != (nList.getLength() - 1)) {
                    returnString += eElement
                            .getElementsByTagName(tagName)
                            .item(i)
                            .getTextContent()
                            .trim() + "; ";

                } else {
                    returnString += eElement
                            .getElementsByTagName(tagName)
                            .item(i)
                            .getTextContent()
                            .trim();
                }
            }
        } else {
            returnString = "unknown";
        }
        return escapeCommas(returnString);
    }

    // Writes each object to a new row in ludlowOut.csv
    private void writeOut() {
        String fileName = "ludlowOut.csv";

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(fileName))) {
            bw.write(HEADER_STRING);
            for (Record record : recordList) {
                bw.write(record.getBigString());
            }
        } catch (IOException ex) {
            System.out.println("Flush failed: couldn't write ludlow.csv");
            ex.printStackTrace();
        }
    }
}