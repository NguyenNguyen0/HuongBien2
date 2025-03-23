package com.huongbien.service;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.huongbien.utils.Json;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.HashMap;

public class ProvinceAPI {
    public static final String GET_ALL_PROVINCES_URI = "https://vn-public-apis.fpo.vn/provinces/getAll?limit=-1";
    public static final String GET_DISTRICTS_BY_PROVINCES_URI = "https://vn-public-apis.fpo.vn/districts/getByProvince?provinceCode=";
    public static final String GET_WARDS_BY_DISTRICTS_URI = "https://vn-public-apis.fpo.vn/wards/getByDistrict?districtCode=";
    public static final JsonObject provincesResponse = getAPI(GET_ALL_PROVINCES_URI);

    private ProvinceAPI() {}

    public static JsonObject getAPI(String uri) {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = null;
        try {
            request = HttpRequest.newBuilder()
                    .uri(new URI(uri))
                    .header("Content-Type", "application/json")
                    .GET()
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            return Json.jsonToJsonObject(response.body());
        } catch (URISyntaxException | IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public static HashMap<String, String> getProvincesMap() {
        JsonArray provinces = provincesResponse.getAsJsonObject("data").getAsJsonArray("data");
        HashMap<String, String> provincesMap = new HashMap<>();

        for (JsonElement province : provinces) {
            provincesMap.put(
                province.getAsJsonObject().get("name").getAsString(),
                province.getAsJsonObject().get("code").getAsString()
            );
        }

        return provincesMap;
    }

    public static HashMap<String, String> getDistrictMapByName(String provinceName) {
        String provinceCode = getProvincesMap().get(provinceName);
        if (provinceCode == null) {
            throw new IllegalArgumentException("Province name is invalid");
        }

        JsonObject response = getAPI(GET_DISTRICTS_BY_PROVINCES_URI + provinceCode);

        JsonArray districts = response.getAsJsonObject("data").getAsJsonArray("data");
        HashMap<String, String> districtMap = new HashMap<>();

        for (JsonElement district : districts) {
            districtMap.put(
                    district.getAsJsonObject().get("name").getAsString(),
                    district.getAsJsonObject().get("code").getAsString()
            );
        }
        return districtMap;
    }

    public static String[] getProvincesName() {
        return getProvincesMap().keySet().toArray(new String[0]);
    }

    public static String[] getDistrictsName(String provinceName) {
        return getDistrictMapByName(provinceName).keySet().toArray(new String[0]);
    }

    public static String[] getWardsName(String provinceName, String districtName) {
        HashMap<String, String> districts = getDistrictMapByName(provinceName);
        JsonArray wards = getAPI(GET_WARDS_BY_DISTRICTS_URI + districts.get(districtName))
                .getAsJsonObject("data")
                .getAsJsonArray("data");

        ArrayList<String> wardsName = new ArrayList<>();
        for (JsonElement ward : wards) {
            wardsName.add(ward.getAsJsonObject().get("name").getAsString());
        }

        return wardsName.toArray(new String[0]);
    }
}
