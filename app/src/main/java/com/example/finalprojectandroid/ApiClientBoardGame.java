package com.example.finalprojectandroid;

import retrofit2.Retrofit;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;

public class ApiClientBoardGame {
    private static final String BASE_URL_XMLAPI = "https://boardgamegeek.com/xmlapi/"; // Board Game Details API
    private static Retrofit retrofitXmlApi;

    public static Retrofit getApiClientForXmlApi() {
        if (retrofitXmlApi == null) {
            retrofitXmlApi = new Retrofit.Builder()
                    .baseUrl(BASE_URL_XMLAPI)
                    .addConverterFactory(SimpleXmlConverterFactory.create()) // XML Converter
                    .build();
        }
        return retrofitXmlApi;
    }
}
