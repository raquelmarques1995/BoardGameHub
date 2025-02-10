package com.example.finalprojectandroid;

import retrofit2.Retrofit;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;

public class ApiClient {
    private static final String BASE_URL_XMLAPI2 = "https://boardgamegeek.com/xmlapi2/"; // Search API
    private static Retrofit retrofitXmlApi2;

    public static Retrofit getApiClientForXmlApi2() {
        if (retrofitXmlApi2 == null) {
            retrofitXmlApi2 = new Retrofit.Builder()
                    .baseUrl(BASE_URL_XMLAPI2)
                    .addConverterFactory(SimpleXmlConverterFactory.create()) // XML Converter
                    .build();
        }
        return retrofitXmlApi2;
    }
}
