package hanu.a2_2001040056.Adapter;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;


import hanu.a2_2001040056.DB.DBHelper;
import hanu.a2_2001040056.MainActivity;
import hanu.a2_2001040056.R;
import hanu.a2_2001040056.models.Product;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private List<Product> productsList;
    private DBHelper dbHelper ;

    public void setProductsList(List<Product> productsList) {
        this.productsList = productsList;
    }
    public ProductAdapter(){
        productsList = new ArrayList<>();
    }


    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_item, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, @SuppressLint("RecyclerView") int position) {
        dbHelper = new DBHelper(holder.itemView.getContext());

        Product product = productsList.get(position);
        holder.productNameTextView.setText(product.getName());
        holder.productPriceTextView.setText(String.format("$%,d", product.getUnitPrice()));


        // Load image for the product using Glide and an Executor
        Executor executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(() -> {
            try {
                Bitmap bitmap = Glide.with(holder.itemView.getContext())
                        .asBitmap()
                        .load(product.getThumbnail())
                        .submit()
                        .get();

                handler.post(() -> holder.productImageView.setImageBitmap(bitmap));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

       //  handle add to cart listener
        holder.addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Handle add to cart button click
                Product currentProduct = productsList.get(position);

               addToCart(currentProduct);
            }
        });
    }

    @Override
    public int getItemCount() {
        return productsList.size();
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {

        public ImageView productImageView;
        public TextView productNameTextView;
        public TextView productPriceTextView;
        public ImageButton addButton;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            productImageView = itemView.findViewById(R.id.product_image);
            productNameTextView = itemView.findViewById(R.id.product_name);
            productPriceTextView = itemView.findViewById(R.id.product_price);
            addButton = itemView.findViewById(R.id.btn_add_to_cart);
        }
    }


    public void addToCart(Product product) {
        long id = product.getId();
        String name = product.getName();
        long unitPrice = product.getUnitPrice();
        String imageUrl = product.getThumbnail();
        int quantity = 1;

        // Store/update the product information in the SQLite database

        // Check if the product already exists in the cart
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String query = "select *from cart where id = "+id+";";

        Cursor cursor = db.rawQuery(query, null);

        String[] columnNames = cursor.getColumnNames();
        Log.d("TAG", "Column Names: " + Arrays.toString(columnNames));

        if (cursor.moveToFirst()) {
            // The product already exists in the cart, so update its quantity
             @SuppressLint("Range") int currentQuantity =  cursor.getInt(cursor.getColumnIndex("quantity"));
            quantity += currentQuantity;
            ContentValues values = new ContentValues();
            values.put("quantity", quantity);
            db.update("cart", values, "name=?", new String[]{name});

            String show  = name + quantity;
            System.out.println(show);
        } else {

            String insert = "insert into cart(id,thumbnail,name,unitPrice,quantity) values(" + id + ",'" + imageUrl + "','" + name + "'," + unitPrice + "," + quantity + ");";
            db.execSQL(insert);

            System.out.println(name +" " + quantity);
        }
    }

}
