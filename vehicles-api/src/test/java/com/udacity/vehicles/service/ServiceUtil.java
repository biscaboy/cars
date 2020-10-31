package com.udacity.vehicles.service;

import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;


import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class ServiceUtil {

    public static boolean pingURL(DiscoveryClient client, String service, String localUrl, int timeout) {
        List<ServiceInstance> list = client.getInstances(service);
        if (list != null && list.size() > 0) {
            return true;
        } else {
            return pingURL(localUrl, timeout);
        }
    }

    /**
     * @Author BalusC - https://stackoverflow.com/users/157882/balusc
     * From Stack Overflow - https://stackoverflow.com/questions/3584210/preferred-java-way-to-ping-an-http-url-for-availability
     *
     * Pings a HTTP URL. This effectively sends a HEAD request and returns <code>true</code> if the response code is in
     * the 200-399 range.
     * @param url The HTTP URL to be pinged.
     * @param timeout The timeout in millis for both the connection timeout and the response read timeout. Note that
     * the total timeout is effectively two times the given timeout.
     * @return <code>true</code> if the given HTTP URL has returned response code 200-399 on a HEAD request within the
     * given timeout, otherwise <code>false</code>.
     */
    public static boolean pingURL(String url, int timeout) {
        url = url.replaceFirst("^https", "http"); // Otherwise an exception may be thrown on invalid SSL certificates.

        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setConnectTimeout(timeout);
            connection.setReadTimeout(timeout);
            connection.setRequestMethod("HEAD");
            int responseCode = connection.getResponseCode();
            return (200 <= responseCode && responseCode <= 399);
        } catch (IOException exception) {
            return false;
        }
    }


}
