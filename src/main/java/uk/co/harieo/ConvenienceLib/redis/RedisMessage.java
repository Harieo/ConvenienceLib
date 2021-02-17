package uk.co.harieo.ConvenienceLib.redis;

import com.google.gson.JsonObject;

/**
 * A message in JSON format to be sent over Redis for the purposes of being received.
 *
 * Note: To edit the body of the message, call {@link #body()} and edit that instance. This is the intended way to
 * add information to the message body.
 */
public class RedisMessage {

	// These are constant to ensure integrity when receiving as well as sending messages
	public static final String messageTypeKey = "type";
	public static final String versionKey = "version";
	public static final String messageBodyKey = "message";

	private final String messageType;
	private final int messageVersion;

	private final JsonObject messageBody; // An encapsulated message to prevent overlapping primary data

	/**
	 * An instance of this class where the message body is a new, blank {@link JsonObject}
	 *
	 * @param messageType which can identify the message when received
	 * @param version which can identify the version of the system that the message was sent on
	 */
	public RedisMessage(String messageType, int version) {
		this(messageType, version, new JsonObject());
	}

	/**
	 * An instance of this class where the message body is provided
	 *
	 * @param messageType which can identify the message when received
	 * @param version which can identify the version of the system that the message was sent on
	 * @param messageBody which holds the body of the message
	 */
	public RedisMessage(String messageType, int version, JsonObject messageBody) {
		this.messageType = messageType;
		this.messageVersion = version;
		this.messageBody = messageBody;
	}

	/**
	 * This should be edited to include the exclusive message information when implemented
	 *
	 * @return the editable {@link JsonObject} for the message content
	 */
	public JsonObject body() {
		return messageBody;
	}

	/**
	 * @return the message type
	 */
	public String getMessageType() {
		return messageType;
	}

	/**
	 * @return the message version
	 */
	public int getMessageVersion() {
		return messageVersion;
	}

	/**
	 * Creates a {@link JsonObject} containing all necessary data to publish this message to Redis
	 *
	 * @return the full object containing the message body and all relevant metadata such as message type and version
	 */
	public JsonObject serialize() {
		JsonObject messageJson = new JsonObject();
		messageJson.addProperty(messageTypeKey, getMessageType());
		messageJson.addProperty(versionKey, getMessageVersion());
		messageJson.add(messageBodyKey, body());
		return messageJson;
	}

	/**
	 * Takes a serialized instance of {@link RedisMessage} then deserializes it into a full object of this class
	 *
	 * @param object the serialized JsonObject created by {@link #serialize()}
	 * @return the deserialized message object
	 */
	public static RedisMessage deserialize(JsonObject object) {
		String messageType = object.get(messageTypeKey).getAsString();
		int version = object.get(versionKey).getAsInt();
		JsonObject body = object.getAsJsonObject(messageBodyKey);
		return new RedisMessage(messageType, version, body);
	}

}
