package uk.co.harieo.ConvenienceLib.maps;

import org.bukkit.Location;
import org.bukkit.World;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.*;
import java.nio.file.FileAlreadyExistsException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class stores plots (single locations) from a world then commits them to a JSON formatted file inside the world
 * for easy portability and accessibility. This class can also de-serialize these JSON files as it would be a bit silly
 * not doing that.
 *
 * Important note: For the purposes of integrity, you must check the {@link #isValid()} function before calling the
 * {@link #commitToFile()} method (the serializer) to make sure there are no null or invalid values in the JSON which
 * would cause errors for {@link #parseWorld(World)} (the de-serializer).
 *
 * @author Harieo
 */
public class MapImpl {

	private static Map<World, MapImpl> CACHE = new HashMap<>();

	private World world;
	private Map<Location, String> plottedLocations = new HashMap<>();

	// Map settings //
	private String fullName; // Different from the world name, set by the user
	private List<String> authors = new ArrayList<>();

	private MapImpl(World world) {
		this.world = world;
	}

	public World getWorld() {
		return world;
	}

	// Plotted Locations handling //

	/**
	 * Adds a location with a String id to be identified as an important location for this map. Plotted locations will
	 * be written to external file then can be read by another system.
	 *
	 * @param locationId name of this location
	 * @param location the location
	 */
	public void addLocation(String locationId, Location location) {
		if (!location.getWorld().equals(world)) {
			throw new IllegalArgumentException("Attempt to plot location from different world");
		} else if (!plottedLocations.containsKey(location)) { // Prevent duplicate entries
			plottedLocations.put(location, locationId);
		}
	}

	/**
	 * Removes a location from the plotted locations by finding its locationId and removing
	 *
	 * @param location to be removed
	 */
	public void removeLocation(Location location) {
		Location locToRemove = null; // This is to prevent concurrent modification on remove
		for (Location loc : plottedLocations.keySet()) {
			if (location.equals(loc)) {
				locToRemove = loc;
			}
		}

		if (locToRemove != null) {
			plottedLocations.remove(locToRemove);
		}
	}

	/**
	 * Checks if a location is saved as a plotted location
	 *
	 * @param location to be checked for
	 * @return whether the location was found
	 */
	public boolean isLocationPlotted(Location location) {
		for (Location loc : plottedLocations.keySet()) {
			if (loc.getBlockX() == location.getBlockX() && loc.getBlockY() == location.getBlockY()
					&& loc.getBlockZ() == location.getBlockZ()) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Checks whether a locationId is being used
	 *
	 * @param locationId to check the map for
	 * @return whether the locationId was found
	 */
	public boolean isLocationPlotted(String locationId) {
		return plottedLocations.containsValue(locationId);
	}

	/**
	 * Retrieves a list of {@link Location} by their locationId. As the map allows for duplicates, there will be no
	 * singular value unless it was specifically input by the user as such.
	 *
	 * @param locationId of the locations
	 * @return a list of locations with matching id
	 */
	public List<Location> getLocations(String locationId) {
		List<Location> list = new ArrayList<>();
		for (Location location : plottedLocations.keySet()) {
			String id = plottedLocations.get(location);
			if (locationId.equals(id)) {
				list.add(location);
			}
		}
		return list;
	}

	/**
	 * @return all stored plotted locations with their ids
	 */
	public Map<Location, String> getAllLocations() {
		return plottedLocations;
	}

	/**
	 * @return a list of locations with a locationId of "spawn"
	 */
	public List<Location> getSpawns() {
		return getLocations("spawn");
	}

	/**
	 * Clears all stored plotted locations
	 */
	public void clearLocations() {
		plottedLocations.clear();
	}

	// Settings //

	/**
	 * Sets the full name of the map which will be displayed to other players when the map is being used
	 *
	 * @param fullName of the map
	 */
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	/**
	 * @return the full name of this map
	 */
	public String getFullName() {
		return fullName;
	}

	/**
	 * Adds a map author which will be displayed to other players when the map is being used
	 *
	 * @param author of the map
	 */
	public void addAuthor(String author) {
		authors.add(author);
	}

	/**
	 * Removes an author from the list of map authors
	 *
	 * @param author to be removed
	 */
	public void removeAuthor(String author) {
		authors.remove(author);
	}

	/**
	 * @return a list of this map's authors
	 */
	public List<String> getAuthors() {
		return authors;
	}

	// Finalisation //

	/**
	 * @return whether the plotted locations contains "spawn", has a full name and has at least 1 author
	 */
	public boolean isValid() {
		return plottedLocations.containsValue("spawn") && fullName != null && !authors.isEmpty();
	}

	/**
	 * Creates a file with the name map.json in the world directory attached to this map which contains a serialised
	 * version of all data contained in this instance. Before using this, reference {@link #isValid()} beforehand or
	 * else this will throw an exception to prevent erroneous use of external files.
	 *
	 * @return whether the file was successfully created
	 * @throws FileAlreadyExistsException if the file already exists and cannot be deleted/overwritten
	 */
	public boolean commitToFile() throws FileAlreadyExistsException {
		if (!isValid()) {
			throw new IllegalStateException("Map data is not yet valid");
		}

		JsonObject json = new JsonObject();
		json.addProperty("name", fullName);

		JsonArray authorArray = new JsonArray();
		for (String author : authors) {
			authorArray.add(author);
		}
		json.add("authors", authorArray);

		JsonArray locationArray = new JsonArray();
		for (Location location : plottedLocations.keySet()) {
			JsonObject locationObject = new JsonObject();
			String id = plottedLocations.get(location);
			locationObject.addProperty("id", id);
			locationObject.addProperty("x", location.getBlockX());
			locationObject.addProperty("y", location.getBlockY());
			locationObject.addProperty("z", location.getBlockZ());
			locationArray.add(locationObject);
		}
		json.add("locations", locationArray);

		System.out.println("Committing map data to JSON: " + json.toString());
		File worldFolder = world.getWorldFolder();

		File jsonFile = new File(worldFolder.getPath() + "/map.json");
		if (jsonFile.exists()) {
			if (!jsonFile.delete()) {
				throw new FileAlreadyExistsException("JSON file already exists and could not be deleted");
			}
		}

		try (FileWriter writer = new FileWriter(jsonFile)) {
			writer.write(json.toString());
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * Retrieves the cached version of {@link MapImpl} based on the specified world or creates one if none are cached
	 *
	 * @param world to be checked
	 * @return the matching instance of {@link MapImpl}
	 */
	public static MapImpl get(World world) {
		if (CACHE.containsKey(world)) {
			return CACHE.get(world);
		} else {
			MapImpl impl = new MapImpl(world);
			CACHE.put(world, impl);
			return impl;
		}
	}

	/**
	 * Retrieves the map.json file that {@link #commitToFile()} created and deserialises it, retrieving all values that
	 * were saved
	 *
	 * @param world to find the json file inside of
	 * @return the newly formed {@link MapImpl} containing the parsed values or null if an error occurred
	 * @throws FileNotFoundException if the world directory does not contain a map.json file
	 */
	public static MapImpl parseWorld(World world) throws FileNotFoundException {
		File mapFile = new File(world.getWorldFolder().getPath() + "/map.json");
		if (!mapFile.exists()) {
			throw new FileNotFoundException("Cannot find map.json");
		}

		try (FileReader reader = new FileReader(mapFile)) {
			JsonParser parser = new JsonParser();
			JsonObject object = parser.parse(reader).getAsJsonObject();
			MapImpl map = new MapImpl(world);

			map.setFullName(object.get("name").getAsString());
			JsonArray authorArray = object.getAsJsonArray("authors");
			for (JsonElement element : authorArray) {
				map.addAuthor(element.getAsString());
			}

			JsonArray locationArray = object.getAsJsonArray("locations");
			for (JsonElement element : locationArray) {
				JsonObject locationObject = element.getAsJsonObject();
				Location location = new Location(world, locationObject.get("x").getAsInt(),
						locationObject.get("y").getAsInt(), locationObject.get("z").getAsInt());
				map.addLocation(locationObject.get("id").getAsString(), location);
			}

			if (!map.isValid()) {
				throw new RuntimeException("Parsed map that doesn't conform to latest validity standards");
			}

			CACHE.put(world, map);
			return map;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

}
