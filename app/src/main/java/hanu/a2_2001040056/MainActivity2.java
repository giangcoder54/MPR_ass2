package hanu.a2_2001040056;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.TextView;

import java.util.List;
import java.util.Locale;

import hanu.a2_2001040056.Adapter.CartAdapter;
import hanu.a2_2001040056.DB.DBHelper;
import hanu.a2_2001040056.models.Product;

public class MainActivity2 extends AppCompatActivity implements CartAdapter.OnCartItemQuantityChangeListener {

    private CartAdapter cartAdapter;
   private TextView totalPriceTextView ;
    private List<Product>  carts;

    private RecyclerView recyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        // Create an instance of the DBHelper class
        DBHelper dbHelper = new DBHelper(this);
        carts = dbHelper.getAllProductItems();


        cartAdapter = new CartAdapter(carts);
        recyclerView = findViewById(R.id.product_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        recyclerView.setAdapter(cartAdapter);
        totalPriceTextView= findViewById(R.id.total_price);
        updateTotalPrice();
       cartAdapter.setOnCartItemQuantityChangeListener(this);







    }
    public long getTotalPrice(){
        long totalPrice = 0;
        for(Product product : carts){
            totalPrice += product.getSumPrice();
        }
        return totalPrice;
    }
    private void updateTotalPrice() {
        long totalPrice = getTotalPrice();
        totalPriceTextView.setText(String.format(Locale.getDefault(), " Total Price : $%,d", totalPrice));
    }

    @Override
    public void onCartItemQuantityChanged() {

    }

    @Override
    public void onCartTotalPriceChanged(long newTotalPrice) {
        totalPriceTextView.setText(String.format(Locale.getDefault(), "Total Price :$%,d", newTotalPrice));
    }
}