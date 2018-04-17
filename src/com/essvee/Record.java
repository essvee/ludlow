package com.essvee;

import java.io.Serializable;
import uk.me.jstott.jcoord.LatLng;
import uk.me.jstott.jcoord.OSRef;

/**
 * Created by sarav on 02/05/2017.
 */
class Record implements Serializable {
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
    private Double latitude = null;
    private Double longitude = null;

    Record(String priref, String objectNumber, String objectName, String scientificName, String description, String collectorName,
                  String collectionPlace, String osGridRef, String stratigraphyUnit, String stratigraphyType, String reproRef) {
        this.priref = priref;
        this.objectNumber = decodeSHYMS(objectNumber);
        this.objectName = objectName;
        this.scientificName = scientificName;
        this.description = description;
        this.collectorName = collectorName;
        this.collectionPlace = collectionPlace;
        this.stratigraphyUnit = stratigraphyUnit;
        this.stratigraphyType = stratigraphyType;
        this.reproRef = reproRef;
        this.osGridRef = bufferOSRef(osGridRef.replace(" ", ""));
        setLatLong();
        setBigString();
    }

    private String decodeSHYMS(String shyms) {
        if (shyms.startsWith("S")) {
            shyms = shyms.replace("/", "_");
            shyms = shyms.replace(": ", "_");
            shyms = shyms.toLowerCase();
            System.out.println(shyms);
        }
        return shyms;
    }


    /**
     * @param osGridRef Grid reference (4/6 chars)
     * @return String 'unknown' if not a grid reference. Returns gridRef buffered to eight characters if valid.
     */
    private String bufferOSRef(String osGridRef) {
        String str = osGridRef;
        if (osGridRef.equals("unknownlocation") || osGridRef.equals("Unknown")) {
            return "unknown";
        } else if (osGridRef.length() == 4) {
            str = osGridRef.substring(0, 3) + "00" + osGridRef.substring(3, 4) + "00";
            return str;
        } else if (!osGridRef.equals("unknown")) {
            str = osGridRef.substring(0, 4) + "0" + osGridRef.substring(4, 6) + "0";
        }
        return str;
    }

    private void setLatLong() {
        if (!osGridRef.equals("unknown")) {
            OSRef os = new OSRef(osGridRef);
            LatLng latLng = os.toLatLng();
            this.latitude = latLng.getLat();
            this.longitude = latLng.getLng();
        }
    }

    private String getLatitude() {
        return String.valueOf(latitude);
    }

    private String getLongitude() {
        return String.valueOf(longitude);
    }

    private void setBigString() {
        bigString = priref + "," + objectNumber + "," + objectName + "," + scientificName + "," + description + ","
                + collectorName + "," + collectionPlace + "," + osGridRef + ","
                + stratigraphyUnit + "," + stratigraphyType + "," + reproRef + ","
                + getLatitude() + "," + getLongitude() + "\n";
    }

    String getBigString() {
        return bigString;
    }

    String getPriref() {
        return priref;
    }

}
