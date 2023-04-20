package hanu.a2_2001040056.Adapter;

import android.annotation.SuppressLint;
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

import hanu.a2_2001040056.R;
import hanu.a2_2001040056.models.Product;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {
    private List<Product> cart;

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

        // handle decrease button
        holder.decreaseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cartItem.setQuantity(cartItem.getQuantity() -1);
                if(cartItem.getQuantity() == 0){
                    cart.remove(position);
                }

                holder.sumPriceTextView.setText(String.format(Locale.getDefault(), "$%,d", cartItem.getSumPrice()));
                // Notify the adapter that the cart has changed
                notifyDataSetChanged();
            }
        });
        // handle increase button
        holder.increaseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cartItem.setQuantity(cartItem.getQuantity() +1);

                holder.sumPriceTextView.setText(String.format(Locale.getDefault(), "$%,d", cartItem.getSumPrice()));
                // Notify the adapter that the cart has changed
                notifyDataSetChanged();
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
        TextView totalPriceTextView;
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
            totalPriceTextView = itemView.findViewById(R.id.total_price);
            increaseBtn = itemView.findViewById(R.id.increase_button);
            decreaseBtn = itemView.findViewById(R.id.decrease_button);
        }

    }
}