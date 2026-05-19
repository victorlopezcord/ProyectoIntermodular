package com.grupo5.gettoday;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {

    private static final String BASE_URL = "http://143.47.60.81:8080/";
    private static Retrofit retrofit = null;
    private static OkHttpClient okHttpClient = null;

    private static final Map<String, List<Cookie>> cookieStore = new HashMap<>();

    private static final CookieJar cookieJar = new CookieJar() {
        @Override
        public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
            cookieStore.put(url.host(), cookies);
        }

        @Override
        public List<Cookie> loadForRequest(HttpUrl url) {
            List<Cookie> cookies = cookieStore.get(url.host());
            if (cookies != null) {
                return cookies;
            }
            return new ArrayList<>();
        }
    };

    public static OkHttpClient getOkHttpClient() {
        if (okHttpClient == null) {
            okHttpClient = new OkHttpClient.Builder()
                    .cookieJar(cookieJar)
                    .addInterceptor(chain -> {
                        okhttp3.Request original = chain.request();
                        if (original.method().equals("DELETE") && original.body() == null) {
                            RequestBody emptyBody = RequestBody.create(
                                    MediaType.parse("application/json"), "");
                            original = original.newBuilder()
                                    .method("DELETE", emptyBody)
                                    .build();
                        }
                        return chain.proceed(original);
                    })
                    .build();
        }
        return okHttpClient;
    }

    public static Retrofit getClient() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(getOkHttpClient())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}