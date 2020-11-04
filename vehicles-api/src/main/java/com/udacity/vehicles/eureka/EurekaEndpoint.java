package com.udacity.vehicles.eureka;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Provides access to the Eureka Service endpoint if connected.
 */
public class EurekaEndpoint {

    DiscoveryClient discoveryClient;
    private boolean connectToEureka;
    private String serviceName;
    private String localEndpoint;

    public EurekaEndpoint(DiscoveryClient discoveryClient, boolean connectToEureka, String serviceName, String localEndpoint) {
        this.discoveryClient = discoveryClient;
        this.connectToEureka = connectToEureka;
        this.serviceName = serviceName;
        this.localEndpoint = localEndpoint;
    }

    public String lookup() {
        String endpoint = localEndpoint;
        if (connectToEureka) {
            List<ServiceInstance> list = discoveryClient.getInstances(serviceName);
            if (list != null && list.size() > 0) {
                endpoint = list.get(0).getUri().toString();
            }
        }
        return endpoint;
    }

}
