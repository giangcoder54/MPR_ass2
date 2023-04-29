package hanu.a2_2001040056.Adapter;

import android.annotation.SuppressLint;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;
import java.util.Locale;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import hanu.a2_2001040056.DB.DBHelper;
import hanu.a2_2001040056.R;
import hanu.a2_2001040056.models.Product;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {
    private List<Product> cart;
    private DBHelper dbHelper;

    private OnCartItemQuantityChangeListener quantityChangeListener;

    // ...

    public void setOnCartItemQuantityChangeListener(OnCartItemQuantityChangeListener listener) {
        this.quantityChangeListener = listener;
    }

    public CartAdapter(List<Product> carts) {
      this.cart = carts;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cart_item, parent, false);
        return new CartViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Product cartItem = cart.get(position);
        holder.productNameTextView.setText(cartItem.getName());
        holder.quantityTextView.setText(String.valueOf(cartItem.getQuantity()));
        holder.unitPriceTextView.setText(String.format(Locale.getDefault(), "$%,d", cartItem.getUnitPrice()));

        holder.sumPriceTextView.setText(String.format(Locale.getDefault(), "$%,d", cartItem.getSumPrice()));

        dbHelper = new DBHelper(holder.itemView.getContext());
        SQLiteDatabase db = dbHelper.getReadableDatabase();


        // handle decrease button
        holder.decreaseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int currentQuantity = cartItem.getQuantity();
                int newQuantity = currentQuantity -1;
                cartItem.setQuantity(newQuantity);
                if(cartItem.getQuantity() == 0){
                    cart.remove(position);
                    db.execSQL("delete from cart where id ="+cartItem.getId());
                }
                String update ="update cart set quantity = "+newQuantity +" where id = "+ cartItem.getId();
                db.execSQL(update);

                // Gọi callback để thông báo khi số lượng sản phẩm được thay đổi
//                if (quantityChangeListener != null) {
//                    quantityChangeListener.onCartItemQuantityChanged();
//                }

                holder.sumPriceTextView.setText(String.format(Locale.getDefault(), "$%,d", cartItem.getSumPrice()));
                // Notify the adapter that the cart has changed
                notifyDataSetChanged();
                // Gọi callback để thông báo khi số lượng sản phẩm được thay đổi
                updateCartTotalPrice();
            }
        });
        // handle increase button
        holder.increaseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int currentQuality = cartItem.getQuantity();
                int newQuantity = currentQuality +1;
                cartItem.setQuantity(newQuantity);

                String update ="update cart set quantity = "+newQuantity +" where id = "+ cartItem.getId();
                db.execSQL(update);

                holder.sumPriceTextView.setText(String.format(Locale.getDefault(), "$%,d", cartItem.getSumPrice()));
                // Notify the adapter that the cart has changed
                notifyDataSetChanged();
                // Gọi callback để thông báo khi số lượng sản phẩm được thay đổi
                updateCartTotalPrice();
            }
        });



        // Load image for the product using Glide and an Executor
        Executor executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(() -> {
            try {
                Bitmap bitmap = Glide.with(holder.itemView.getContext())
                        .asBitmap()
                        .load(cartItem.getThumbnail())
                        .submit()
                        .get();

                handler.post(() -> holder.productImageView.setImageBitmap(bitmap));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public int getItemCount() {
        return cart.size();
    }



    static class CartViewHolder extends RecyclerView.ViewHolder {
        TextView productNameTextView;
        TextView quantityTextView;
        TextView unitPriceTextView;
        TextView sumPriceTextView;

        ImageView productImageView;

        Button increaseBtn;
        Button decreaseBtn;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            productNameTextView = itemView.findViewById(R.id.product_name);
            productImageView = itemView.findViewById(R.id.product_image);
            quantityTextView = itemView.findViewById(R.id.quantity_text);
            unitPriceTextView = itemView.findViewById(R.id.product_price);
            sumPriceTextView = itemView.findViewById(R.id.tv_SumPrice);
            increaseBtn = itemView.findViewById(R.id.increase_button);
            decreaseBtn = itemView.findViewById(R.id.decrease_button);


        }



    }
    private void updateCartTotalPrice() {
        long totalPrice = 0;

        for (Product cartItem : cart) {
            totalPrice += cartItem.getSumPrice();
        }

        if (quantityChangeListener != null) {
            quantityChangeListener.onCartTotalPriceChanged(totalPrice);
        }
    }
    public interface OnCartItemQuantityChangeListener {
        void onCartItemQuantityChanged();

        void onCartTotalPriceChanged(long newTotalPrice);
    }
}