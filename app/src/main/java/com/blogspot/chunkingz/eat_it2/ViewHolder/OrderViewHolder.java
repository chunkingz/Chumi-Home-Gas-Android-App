package com.blogspot.chunkingz.eat_it2.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.blogspot.chunkingz.eat_it2.Interface.ItemClickListener;
import com.blogspot.chunkingz.eat_it2.R;

public class OrderViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public final TextView txtOrderId;
    public final TextView txtOrderStatus;
    public final TextView txtOrderSize;
    public final TextView txtOrderPrice;
    public final TextView txtOrderPhone;
    public final TextView txtOrderName;
    public final TextView txtOrderDate;
    public final TextView txtOrderTime;
    public final TextView txtOrderAddress;
    public final TextView txtOrderDeliveryType;
    public final TextView txtOrderExtraComments;

    private ItemClickListener itemClickListener;

    public OrderViewHolder(View itemView) {
        super(itemView);

        txtOrderId = itemView.findViewById(R.id.order_id);
        txtOrderStatus = itemView.findViewById(R.id.order_status);
        txtOrderSize = itemView.findViewById(R.id.order_size);
        txtOrderPrice = itemView.findViewById(R.id.order_price);
        txtOrderPhone = itemView.findViewById(R.id.order_phone);
        txtOrderName = itemView.findViewById(R.id.order_name);
        txtOrderDate = itemView.findViewById(R.id.order_date);
        txtOrderTime = itemView.findViewById(R.id.order_time);
        txtOrderAddress = itemView.findViewById(R.id.order_address);
        txtOrderDeliveryType = itemView.findViewById(R.id.order_delivery_type);
        txtOrderExtraComments = itemView.findViewById(R.id.order_extra_comments);

        itemView.setOnClickListener(this);

    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View v) {
        itemClickListener.onClick(v, getAdapterPosition(), false);
    }
}
