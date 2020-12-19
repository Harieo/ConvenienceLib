package uk.co.harieo.ConvenienceLib.redis;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import uk.co.harieo.ConvenienceLib.database.DatabaseManager;

public class RedisClient {

	private final DatabaseManager databaseManager;

	/**
	 * A Redis API which handles publishing and subscribing to Redis messages, as well as custom functions
	 *
	 * @param databaseManager which holds the Redis database details
	 */
	public RedisClient(DatabaseManager databaseManager) {
		this.databaseManager = databaseManager;
	}

	/**
	 * Publishes a message to Redis
	 *
	 * @param channel to publish the message on
	 * @param message to be published
	 * @return the async publishing task
	 */
	public CompletableFuture<Void> publishMessage(String channel, RedisMessage message) {
		return consumeJedisAsync(jedis -> jedis.publish(channel, message.serialize().toString()),
				databaseManager.getPublishPool());
	}

	/**
	 * Subscribes a {@link RedisSubscriber} to the subscriber Jedis pool
	 *
	 * @param subscriber to be subscribed
	 */
	public void subscribe(RedisSubscriber subscriber) {
		consumeJedisAsync(jedis -> jedis.subscribe(new SubscriberImpl(subscriber), subscriber.getChannels()),
				databaseManager.getSubscribePool());
	}

	/**
	 * Accesses a {@link Jedis} instance from a provided {@link JedisPool} asynchronously through a consumer
	 *
	 * @param consumer to consume the accessed {@link Jedis} instance in the async thread
	 * @param pool to retrieve the {@link Jedis} instance from
	 * @return the async task
	 */
	private CompletableFuture<Void> consumeJedisAsync(Consumer<Jedis> consumer, JedisPool pool) {
		return CompletableFuture.runAsync(() -> {
			try (Jedis jedis = pool.getResource()) {
				consumer.accept(jedis);
			}
		});
	}

}
