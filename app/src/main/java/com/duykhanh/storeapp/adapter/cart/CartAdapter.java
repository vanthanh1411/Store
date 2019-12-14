package com.duykhanh.storeapp.adapter.cart;

import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.duykhanh.storeapp.R;
import com.duykhanh.storeapp.model.CartItem;
import com.duykhanh.storeapp.utils.Formater;
import com.duykhanh.storeapp.view.order.cart.CartFragment;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {
    final String TAG = this.getClass().toString();

    CartFragment context;
    List<CartItem> cartItems;
    int resource;

    public CartAdapter(CartFragment context, List<CartItem> cartItems, int resource) {
        this.context = context;
        this.cartItems = cartItems;
        this.resource = resource;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(resource, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CartItem cartItem = cartItems.get(position);

        holder.ivCartImage.setImageBitmap(BitmapFactory.decodeByteArray(cartItem.getImage(), 0, cartItem.getImage().length));
        holder.tvCartName.setText(cartItem.getName());
        holder.tvCartTotal.setText("Thanh toán: " + Formater.formatMoney((int)(cartItem.getQuantity() * cartItem.getPrice())) + " đ");
        holder.tvCartQuantity.setText(cartItem.getQuantity() + "");
        holder.tvCartStorage.setText("Tồn kho: " + cartItem.getStorage());

        if (Integer.parseInt(holder.tvCartQuantity.getText().toString()) >= cartItem.getStorage()) {
            holder.ivAddQuantity.setBackgroundColor(ContextCompat.getColor(context.getContext(), R.color.colorGreyAccentTransparency));
            holder.ivAddQuantity.setEnabled(false);
        } else {
            holder.ivAddQuantity.setBackgroundColor(ContextCompat.getColor(context.getContext(), R.color.colorOrange));
            holder.ivAddQuantity.setEnabled(true);
        }
        if (Integer.parseInt(holder.tvCartQuantity.getText().toString()) <= 1) {
            holder.ivSubQuantity.setBackgroundColor(ContextCompat.getColor(context.getContext(), R.color.colorGreyAccentTransparency));
            holder.ivSubQuantity.setEnabled(false);
        } else {
            holder.ivSubQuantity.setBackgroundColor(ContextCompat.getColor(context.getContext(), R.color.colorOrange));
            holder.ivSubQuantity.setEnabled(true);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.onCartItemClick(position);
            }
        });
        holder.ivAddQuantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.onIncreaseButtonClick(position);
            }
        });
        holder.ivSubQuantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.onDecreaseButtonClick(position);
            }
        });
        holder.ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.onDeleteButtonClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView ivCartImage;
        TextView tvCartName, tvCartTotal, tvCartStorage, tvCartQuantity;
        ImageView ivAddQuantity, ivSubQuantity, ivDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            ivCartImage = itemView.findViewById(R.id.ivCartImage);
            tvCartName = itemView.findViewById(R.id.tvCartName);
            tvCartTotal = itemView.findViewById(R.id.tvCartTotal);
            tvCartStorage = itemView.findViewById(R.id.tvCartStorage);
            tvCartQuantity = itemView.findViewById(R.id.tvCartQuanity);
            ivAddQuantity = itemView.findViewById(R.id.tvAddQuantity);
            ivSubQuantity = itemView.findViewById(R.id.tvSubQuantity);
            ivDelete = itemView.findViewById(R.id.ivDeleteCartItem);
        }
    }
}
