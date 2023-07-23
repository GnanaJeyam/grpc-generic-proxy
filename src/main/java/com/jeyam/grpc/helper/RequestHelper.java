package com.jeyam.grpc.helper;

import com.jeyam.grpc.api.ApiMappingDetail;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class RequestHelper {
    private final ApiMappingDetail apiMappingDetail;

    public RequestHelper(ApiMappingDetail apiMappingDetail) {
        this.apiMappingDetail = apiMappingDetail;
    }

    public String getResponse() {
        var url = constructUrl();
        var response = triggerRequest(url);

        return response;
    }

    private String constructUrl() {
      var url =  apiMappingDetail.getHost() + ":" + apiMappingDetail.getPort() + apiMappingDetail.getReqUrl();

      return url;
    }

    private String triggerRequest(String url) {
        var uri = URI.create(url);
        var request = HttpRequest.newBuilder()
            .uri(uri)
            .GET()
            .build();
        var httpClient = HttpClient.newHttpClient();
        try {
            var res = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            return res.body();
        } catch (Exception e){
            return null;
        }
    }
}
