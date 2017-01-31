package massim.scenario.city.data;

import massim.Log;

/**
 * Represents a map location in the city scenario.
 */
public class Location {

    private double lat;
    private double lon;
    private static double proximity;

    public Location(double lon, double lat) {
        this.lat = lat;
        this.lon = lon;
    }

    /**
     * @return the location's latitude
     */
    public double getLat() {
        return lat;
    }

    /**
     * @return the location's longitude
     */
    public double getLon() {
        return lon;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        long temp = Math.round(lat / proximity);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        temp = Math.round(lon / proximity);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (obj.getClass() != Location.class)
            return false;
        Location other = (Location) obj;
        return Math.round(this.lat / proximity) == Math.round(other.lat / proximity) && Math.round(this.lon / proximity) == Math.round(other.lon / proximity);
    }

    /**
     * Sets the global proximity value
     * @param newProximity the new proximity value
     */
    static void setProximity(double newProximity){
        proximity = newProximity;
    }

    /**
     * Tries to create a new Location object from the given (double) strings
     * @param latString latitude double
     * @param lonString longitude double
     * @return the location object or null if the strings are invalid
     */
    public static Location parse(String latString, String lonString) {
        try{
            double lat = Double.parseDouble(latString);
            double lon = Double.parseDouble(lonString);
            return new Location(lon, lat);
        } catch(NullPointerException | NumberFormatException e){
            Log.log(Log.ERROR, "Invalid doubles: " + latString + " - " + lonString);
            return null;
        }
    }

    /**
     * Checks if two locations are "near" each other. Depends on {@link Location#proximity}.
     * @param other another location
     * @return whether both locations are considered equal/in range
     */
    public boolean inRange(Location other){
        if (other == null) return false;
        return this.equals(other);
    }
}
