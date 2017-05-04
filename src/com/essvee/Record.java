package com.essvee;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.Serializable;
import java.net.URL;

/**
 * Created by sarav on 02/05/2017.
 */
public class Record implements Serializable {
    private String priref;
    private String objectNumber;
    private String objectName;
    private String scientificName;
    private String description;
    private String collectorName;
    private String collectionPlace; // May be > 1 occurrence
    private String osGridRef;
    private String stratigraphyUnit; // May be > 1 occurrence
    private String stratigraphyType; // May be > 1 occurrence
    private String reproRef;
    private String bigString;
    private double latitude;
    private double longitude;

    public Record(String priref, String objectNumber, String objectName, String scientificName, String description, String collectorName,
                  String collectionPlace, String osGridRef, String stratigraphyUnit, String stratigraphyType, String reproRef) {
        this.priref = priref;
        this.objectNumber = objectNumber;
        this.objectName = objectName;
        this.scientificName = scientificName;
        this.description = description;
        this.collectorName = collectorName;
        this.collectionPlace = collectionPlace;
        this.stratigraphyUnit = stratigraphyUnit;
        this.stratigraphyType = stratigraphyType;
        this.reproRef = reproRef;
        this.osGridRef = osGridRef;
        setBigString();
    }

//    public void getLatLong() {
//        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
//        DocumentBuilder db = null;
//        try {
//            db = dbf.newDocumentBuilder();
//        } catch (ParserConfigurationException e) {
//            e.printStackTrace();
//        }
//        Document doc = db.parse(new URL("http://www.batlab.ucd.ie/gridref/?reftype=NATGRID&refs=O007727").openStream());
//
//
//    }


    public void setBigString() {
        bigString = priref + objectNumber + objectName + scientificName + description + collectorName + collectionPlace + osGridRef +
                stratigraphyUnit + stratigraphyType + reproRef + "\n";
    }

    public String getPriref() {
        return priref;
    }

    public String getDescription() {
        return description;
    }

    public String getCollectionPlace() {
        return collectionPlace;
    }

    public void setCollectionPlace(String str) {
        collectionPlace = str;
    }

    public String getStratigraphyUnit() {
        return stratigraphyUnit;
    }

    public void setStratigraphyUnit(String str) {
        stratigraphyUnit = str;
    }

    public String getStratigraphyType() {
        return stratigraphyType;
    }

    public void setStratigraphyType(String str) {
        stratigraphyType = str;
    }

    public String getOsGridRef() {
        return osGridRef;
    }

    public String getObjectNumber() {
        return objectNumber;
    }

    public String getObjectName() {
        return objectName;
    }

    public String getScientificName() {
        return scientificName;
    }

    public String getBigString() {
        return bigString;
    }

    public String getCollectorName() {
        return collectorName;
    }
}
