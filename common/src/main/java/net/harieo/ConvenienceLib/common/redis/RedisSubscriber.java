package net.harieo.ConvenienceLib.common.redis;

public interface RedisSubscriber {

	/**
	 * @return the channels which this subscriber is listening on
	 */
	String[] getChannels();

	/**
	 * Called when this subscriber, after being subscribed to a {@link RedisClient}, receives a message from one of
	 * the channels it is listening to
	 *
	 * @param channel which the message was received on
	 * @param receivedMessage the message which was received
	 */
	void onMessage(String channel, RedisMessage receivedMessage);

}
