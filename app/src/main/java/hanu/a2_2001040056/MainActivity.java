package hanu.a2_2001040056;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SearchView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import hanu.a2_2001040056.Adapter.ProductAdapter;
import hanu.a2_2001040056.Api.ApiService;
import hanu.a2_2001040056.models.Product;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private ProductAdapter productAdapter;
    private List<Product> products;
    private RecyclerView recyclerView;

    private ImageButton myCartImageButton;

    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        productAdapter = new ProductAdapter();

        ApiService.apiService.getProducts().enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                if (response.isSuccessful()) {
                    // Step 3.1: Populate the productsList in the ProductAdapter
                    products = response.body();
                    productAdapter.setProductsList(products);

                    // Step 4: Notify the ProductAdapter that the data set has changed
                    productAdapter.notifyDataSetChanged();
                }else {
                    System.out.println("Error");
                }


            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Call Api Error", Toast.LENGTH_SHORT).show();
            }
        });



        recyclerView = findViewById(R.id.RecycleView);
        recyclerView.setLayoutManager(new GridLayoutManager(this,2));

        recyclerView.setAdapter(productAdapter);

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


}