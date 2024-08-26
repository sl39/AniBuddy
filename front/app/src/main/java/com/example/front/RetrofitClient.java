package com.example.front;

import android.content.Context;

import com.example.front.data.AuthInterceptor;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class RetrofitClient {

    private static Retrofit retrofit = null;
    private static final String BASE_URL = BuildConfig.BASE_URL;
    private static final Gson gson = new GsonBuilder()
            .setLenient()
            .create();

    public static Retrofit getRetrofitInstance(Context context) {
        AuthInterceptor authInterceptor = new AuthInterceptor(context);
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(authInterceptor)
                .build();
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .client(okHttpClient)
                    .build();
        }
        return retrofit;
    }
}










/*





private static final String BASE_URL = "http://10.0.2.2:8080/";
    private static Retrofit retrofit = null;
    public static Retrofit getRetrofitInstance() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}


 */



