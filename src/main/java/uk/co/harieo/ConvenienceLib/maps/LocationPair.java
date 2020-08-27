package uk.co.harieo.ConvenienceLib.maps;

import org.bukkit.Location;

public class LocationPair {

	private final Location location;

	private String key;
	private String value;

	/**
	 * A pair of strings attached to a {@link Location} for the purpose of identifying this location in other systems
	 * which use this API and allowing said systems to know what this location is for.
	 *
	 * @param location which the pair of strings describes
	 * @param key to identify the value
	 * @param value which describes the location
	 */
	public LocationPair(Location location, String key, String value) {
		this.location = location;
		this.key = key;
		this.value = value;
	}

	/**
	 * @return the location attached to this pair
	 */
	public Location getLocation() {
		return location;
	}

	/**
	 * @return the key
	 */
	public String getKey() {
		return key;
	}

	/**
	 * Set the key for this pair
	 *
	 * @param key to set the key to
	 */
	public void setKey(String key) {
		this.key = key;
	}

	/**
	 * @return the value
	 */
	public String getValue() {
		return value;
	}

	/**
	 * Sets the value for this pair
	 *
	 * @param value to set the value to
	 */
	public void setValue(String value) {
		this.value = value;
	}

	/**
	 * Compares the X, Y and Z block axis of a location to compare whether the two locations are within the same block
	 *
	 * @param toCompare to compare to this pair's location
	 * @return whether this pair's location and the given location are within the same block
	 */
	public boolean compareLocations(Location toCompare) {
		return location.getBlockX() == toCompare.getBlockX() && location.getBlockY() == toCompare.getBlockY()
				&& location.getBlockZ() == toCompare.getBlockZ();
	}

}
