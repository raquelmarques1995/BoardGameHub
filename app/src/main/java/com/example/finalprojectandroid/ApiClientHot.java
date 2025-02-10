package com.example.finalprojectandroid;

import retrofit2.Retrofit;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;

public class ApiClientHot {
    private static final String BASE_URL_HOT = "https://boardgamegeek.com/xmlapi2/"; // Replace with actual URL
    private static Retrofit retrofitHotApi;

    public static Retrofit getApiClientForHotApi() {
        if (retrofitHotApi == null) {
            retrofitHotApi = new Retrofit.Builder()
                    .baseUrl(BASE_URL_HOT)
                    .addConverterFactory(SimpleXmlConverterFactory.create()) // XML Converter
                    .build();
        }
        return retrofitHotApi;
    }
}
