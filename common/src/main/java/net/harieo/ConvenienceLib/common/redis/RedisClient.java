package net.harieo.ConvenienceLib.common.redis;

import lombok.Getter;
import net.harieo.ConvenienceLib.common.database.DatabaseManager;
import net.harieo.ConvenienceLib.common.database.api.RedisConfiguration;
import org.jetbrains.annotations.NotNull;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

public class RedisClient {

	private static final ExecutorService executorService = Executors.newCachedThreadPool();

	@Getter
	private final JedisPool publishPool;
	@Getter
	private final JedisPool subscribePool;

	@Getter
	private final RedisConfiguration configuration;

	/**
	 * A Redis API which handles publishing and subscribing to Redis messages, as well as custom functions
	 *
	 * @param redisConfiguration the configuration for the Redis server
	 */
	public RedisClient(@NotNull RedisConfiguration redisConfiguration) {
		this.configuration = redisConfiguration;
		this.publishPool = createJedisPool();
		this.subscribePool = createJedisPool();
	}

	/**
	 * Publishes a message to Redis
	 *
	 * @param channel to publish the message on
	 * @param message to be published
	 * @return the async publishing task
	 */
	public CompletableFuture<Void> publishMessage(@NotNull String channel, @NotNull RedisMessage message) {
		return consumeJedisAsync(jedis -> jedis.publish(channel, message.serialize().toString()),
				getPublishPool());
	}

	/**
	 * Subscribes a {@link RedisSubscriber} to the subscriber Jedis pool
	 *
	 * @param subscriber to be subscribed
	 */
	public void subscribe(@NotNull RedisSubscriber subscriber) {
		consumeJedisAsync(jedis -> jedis.subscribe(new SubscriberImpl(subscriber), subscriber.getChannels()),
				getSubscribePool());
	}

	/**
	 * Accesses a {@link Jedis} instance from a provided {@link JedisPool} asynchronously through a consumer
	 *
	 * @param consumer to consume the accessed {@link Jedis} instance in the async thread
	 * @param pool to retrieve the {@link Jedis} instance from
	 * @return the async task
	 */
	public CompletableFuture<Void> consumeJedisAsync(@NotNull Consumer<Jedis> consumer,
													  @NotNull JedisPool pool) {
		return CompletableFuture.runAsync(() -> {
			try (Jedis jedis = pool.getResource()) {
				consumer.accept(jedis);
			}
		}, executorService);
	}

	/**
	 * Creates a {@link JedisPool} with the provided {@link RedisConfiguration} details.
	 *
	 * @return the created pool
	 */
	public JedisPool createJedisPool() {
		return new JedisPool(new JedisPoolConfig(), configuration.getHost(), configuration.getPort(),
				configuration.getTimeout(), configuration.getPassword(), configuration.getDatabase());
	}

}
