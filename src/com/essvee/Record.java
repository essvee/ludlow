package com.essvee;

import org.apache.commons.lang.StringEscapeUtils;

import java.io.Serializable;

/**
 * Created by sarav on 02/05/2017.
 */
public class Record implements Serializable {
    private String priref;
    private String objectNumber;
    private String objectName;
    private String scientificName;
    private String description;
    private String collectionPlace; // May be > 1 occurrence
    private String osGridRef;
    private String stratigraphyUnit; // May be > 1 occurrence
    private String stratigraphyType; // May be > 1 occurrence
    private String bigString;

    public Record(String priref, String objectNumber, String objectName, String scientificName, String description,
                  String collectionPlace, String osGridRef, String stratigraphyUnit, String stratigraphyType) {
        this.priref = StringEscapeUtils.escapeCsv(priref);
        this.objectNumber = StringEscapeUtils.escapeCsv(objectNumber);
        this.objectName = StringEscapeUtils.escapeCsv(objectName);
        this.scientificName = StringEscapeUtils.escapeCsv(scientificName);
        this.description = StringEscapeUtils.escapeCsv(description);
        this.collectionPlace = StringEscapeUtils.escapeCsv(collectionPlace);
        this.stratigraphyUnit = StringEscapeUtils.escapeCsv(stratigraphyUnit);
        this.stratigraphyType = StringEscapeUtils.escapeCsv(stratigraphyType);
        this.osGridRef = StringEscapeUtils.escapeCsv(osGridRef);;
        setBigString();
    }

    public void setBigString() {
        bigString = priref + objectNumber + objectName + scientificName + description + collectionPlace + osGridRef +
                stratigraphyUnit + stratigraphyType + "\n";
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
        collectionPlace = StringEscapeUtils.escapeCsv(str);
    }

    public String getStratigraphyUnit() {
        return stratigraphyUnit;
    }

    public void setStratigraphyUnit(String str) {
        stratigraphyUnit = StringEscapeUtils.escapeCsv(str);
    }

    public String getStratigraphyType() {
        return stratigraphyType;
    }

    public void setStratigraphyType(String str) {
        stratigraphyType = StringEscapeUtils.escapeCsv(str);
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
}
