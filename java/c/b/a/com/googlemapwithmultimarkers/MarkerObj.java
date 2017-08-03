package c.b.a.com.googlemapwithmultimarkers;

/**
 * Created by Mirza on 8/4/2017.
 */

public class MarkerObj {

    private String LocationName;
    private String MarkerID;


    public MarkerObj(String locationName, String markerID) {
        LocationName = locationName;
        MarkerID = markerID;
    }

    public String getLocationName() {
        return LocationName;
    }

    public String getMarkerID() {
        return MarkerID;
    }

}
