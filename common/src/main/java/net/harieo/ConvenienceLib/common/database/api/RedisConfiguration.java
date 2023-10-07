package net.harieo.ConvenienceLib.common.database.api;

public interface RedisConfiguration {

    String getHost();

    int getPort();

    int getTimeout();

    String getPassword();

    int getDatabase();

}
