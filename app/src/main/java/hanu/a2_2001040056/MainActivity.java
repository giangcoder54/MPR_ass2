package hanu.a2_2001040056;



import androidx.appcompat.app.AppCompatActivity;
import androidx.core.os.HandlerCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;



import java.io.IOException;
import java.io.InputStream;

import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import hanu.a2_2001040056.Adapter.ProductAdapter;

import hanu.a2_2001040056.models.Product;



public class MainActivity extends AppCompatActivity  {
    private ProductAdapter productAdapter;
    private List<Product> products;
    private RecyclerView recyclerView;

    private ImageButton myCartImageButton;

    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        products = new ArrayList<>();



        String url = "https://hanu-congnv.github.io/mpr-cart-api/products.json";
        Handler handler = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.P) {
            handler = Handler.createAsync(Looper.getMainLooper());
        }

        Handler finalHandler = handler;
        Constants.executor.execute(() -> {
            //connect to api
            //read api
            String json = loadJSON(url);
            assert finalHandler != null;
            finalHandler.post(() -> {
                if (json == null) {
                    Toast.makeText(this , "Error load JSON", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        JSONArray root = new JSONArray(json);
                        for(int i=0; i<root.length();i++) {
                            JSONObject product = root.getJSONObject(i);
                            long id = product.getLong("id");
                            String thumbnail = product.getString("thumbnail");
                            String name = product.getString("name");
                            String category = product.getString("category");
                            long unitPrice = product.getLong("unitPrice");

                            products.add(new Product(id,thumbnail,name,category,unitPrice));
                        }
                        productAdapter = new ProductAdapter(products,this);
                        recyclerView = findViewById(R.id.RecycleView);
                        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
                        recyclerView.setAdapter(productAdapter);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        });


                        myCartImageButton = findViewById(R.id.my_cart);
                        myCartImageButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                // Step 5: Create an Intent object to start Screen 2
                                Intent intent = new Intent(MainActivity.this, MainActivity2.class);

                                // Step 6: Start Screen 2
                                startActivity(intent);
                            }
                        });

                        searchView = findViewById(R.id.searchView);
                        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                            @Override
                            public boolean onQueryTextSubmit(String query) {
                                // Not used, but required to implement SearchView.OnQueryTextListener
                                return false;
                            }

                            @Override
                            public boolean onQueryTextChange(String newText) {
                                filterProducts(newText);
                                return true;
                            }
                        });

                    }

                    private void filterProducts(String query) {
                        List<Product> filteredList = new ArrayList<>();

                        for (Product product : products) {
                            if (product.getName().toLowerCase().contains(query.toLowerCase())) {
                                filteredList.add(product);
                            }
                        }

                        productAdapter.filterList(filteredList);
                    }



    public String loadJSON(String link) {
        URL url;
        HttpURLConnection urlConnection;
        try {
            url = new URL(link);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.connect();
            InputStream is = urlConnection.getInputStream();
            Scanner sc = new Scanner(is);
            StringBuilder result = new StringBuilder();
            String line;
            while(sc.hasNextLine()) {
                line = sc.nextLine();
                result.append(line);
            }
            return result.toString();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}

