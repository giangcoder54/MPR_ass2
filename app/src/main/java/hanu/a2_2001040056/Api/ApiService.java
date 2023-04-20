package hanu.a2_2001040056.Api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import hanu.a2_2001040056.models.Product;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;

public interface ApiService  {
    //https://hanu-congnv.github.io/mpr-cart-api/products.json
    Gson gSon = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
    ApiService apiService = new Retrofit.Builder()
            .baseUrl("https://hanu-congnv.github.io/mpr-cart-api/")
            .addConverterFactory(GsonConverterFactory.create(gSon))
            .build()
            .create(ApiService.class);
    @GET("products.json")
    Call<List<Product>> getProducts();
}
