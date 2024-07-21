package net.harieo.ConvenienceLib.common.database.api;

public interface SQLConfiguration {

    String getHost();

    int getPort();

    String getDatabase();

    String getUsername();

    String getPassword();

    String getAppend();

    /**
     * @return the address of the MySQL database consisting of host and port
     */
    default String getAddress() {
        return getHost() + ":" + getPort();
    }

}
