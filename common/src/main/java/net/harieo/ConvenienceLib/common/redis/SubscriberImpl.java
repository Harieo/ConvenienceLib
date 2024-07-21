package net.harieo.ConvenienceLib.common.redis;

import com.google.gson.JsonParser;
import org.jetbrains.annotations.NotNull;
import redis.clients.jedis.JedisPubSub;

public class SubscriberImpl extends JedisPubSub {

	private final RedisSubscriber subscriber;

	/**
	 * An implementation of {@link JedisPubSub} which parses information for a {@link RedisSubscriber}
	 *
	 * @param subscriber to parse information to
	 */
	SubscriberImpl(@NotNull RedisSubscriber subscriber) {
		this.subscriber = subscriber;
	}

	@Override
	public void onMessage(@NotNull String channel, @NotNull String message) {
		// This impl should only be subscribed to the correct channel so checking the channel is not necessary
		try {
			RedisMessage redisMessage = RedisMessage.deserialize(JsonParser.parseString(message).getAsJsonObject());
			subscriber.onMessage(channel, redisMessage);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
