package com.jeyam.grpc.api;

import java.util.List;

public class ApiMapping {
    private List<ApiMappingDetail> apiMappingDetails;

    public ApiMapping() {

    }

    public List<ApiMappingDetail> getApiMappingDetails() {
        return apiMappingDetails;
    }

    public void setApiMappingDetails(List<ApiMappingDetail> apiMappingDetails) {
        this.apiMappingDetails = apiMappingDetails;
    }
}
