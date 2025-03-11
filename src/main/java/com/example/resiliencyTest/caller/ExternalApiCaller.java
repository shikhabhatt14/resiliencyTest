package com.example.resiliencyTest.caller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestTemplate;

@Component
public class ExternalApiCaller {

    DiscoveryClient discoveryClient;
    RestClient restClient;

    public ExternalApiCaller(DiscoveryClient discoveryClient, RestClient.Builder restClientBuilder) {
        this.discoveryClient = discoveryClient;
        restClient = restClientBuilder.build();
    }

   // private final RestTemplate restTemplate;

    /*@Autowired
    public ExternalApiCaller(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }*/

    public String callApi() {

        ServiceInstance serviceInstance = discoveryClient.getInstances("externalApi").get(0);
        return restClient.get()
                .uri(serviceInstance.getUri() + "/api/external")
                .retrieve()
                .body(String.class);
        //return restTemplate.getForObject("/api/external", String.class);
    }

    public String callApiWithDelay() {
        //String result = restTemplate.getForObject("/api/external", String.class);
        ServiceInstance serviceInstance = discoveryClient.getInstances("externalApi").get(0);
        String result = restClient.get()
                .uri(serviceInstance.getUri() + "/api/external")
                .retrieve()
                .body(String.class);
        try {
            Thread.sleep(5000);
        } catch (InterruptedException ignore) {
        }
        return result;
    }
}
