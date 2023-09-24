package uk.co.harieo.ConvenienceLib.redis;

import com.google.gson.JsonParser;
import redis.clients.jedis.JedisPubSub;

@Deprecated
public class SubscriberImpl extends JedisPubSub {

	private static final JsonParser parser = new JsonParser();

	private final RedisSubscriber subscriber;

	/**
	 * An implementation of {@link JedisPubSub} which parses information for a {@link RedisSubscriber}
	 *
	 * @param subscriber to parse information to
	 */
	SubscriberImpl(RedisSubscriber subscriber) {
		this.subscriber = subscriber;
	}

	@Override
	public void onMessage(String channel, String message) {
		// This impl should only be subscribed to the correct channel so checking the channel is not necessary
		try {
			RedisMessage redisMessage = RedisMessage.deserialize(parser.parse(message).getAsJsonObject());
			subscriber.onMessage(channel, redisMessage);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
