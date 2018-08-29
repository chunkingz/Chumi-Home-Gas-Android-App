package com.blogspot.chunkingz.eat_it2;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.blogspot.chunkingz.eat_it2.Common.Common;
import com.blogspot.chunkingz.eat_it2.Model.Request;
import com.blogspot.chunkingz.eat_it2.ViewHolder.OrderViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class OrderStatus extends AppCompatActivity {

    private RecyclerView recyclerView;

    private FirebaseRecyclerAdapter<Request, OrderViewHolder> adapter;

    private DatabaseReference requests;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_status);

        // Firebase
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        requests = database.getReference("Requests");

        recyclerView = findViewById(R.id.list_orders);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        loadOrders(Common.currentUser.getPhoneNumber());
    }

    private void loadOrders(String phoneNumber) {
        adapter = new FirebaseRecyclerAdapter<Request, OrderViewHolder>(
                Request.class,
                R.layout.order_layout,
                OrderViewHolder.class,
                requests.orderByChild("phone").equalTo(phoneNumber)
        ) {
            @Override
            protected void populateViewHolder(OrderViewHolder viewHolder, Request model, int position) {
                viewHolder.txtOrderId.setText("Order Id: "+adapter.getRef(position).getKey());
                viewHolder.txtOrderStatus.setText("Order status: "+convertCodeToStatus(model.getStatus()));
                viewHolder.txtOrderSize.setText("Size of Cylinder: "+model.getSizeOfCylinder());
                viewHolder.txtOrderPrice.setText("Amount payable: "+model.getAmountPayable());
                viewHolder.txtOrderPhone.setText("Phone number: "+model.getPhone());
                viewHolder.txtOrderName.setText("Name: "+model.getName());
                viewHolder.txtOrderDate.setText("Date of delivery: "+model.getDate());
                viewHolder.txtOrderTime.setText("Time of Delivery: "+model.getTime());
                viewHolder.txtOrderAddress.setText("Address: "+model.getAddress());
                viewHolder.txtOrderDeliveryType.setText("Delivery method: "+model.getDeliveryMethod());
                viewHolder.txtOrderExtraComments.setText("Extra Comments: "+model.getExtraComments());
            }
        };
        recyclerView.setAdapter(adapter);
    }

    private String convertCodeToStatus(String status) {
        switch (status) {
            case "0":
                return "Order Placed";
            case "1":
                return "Order Shipping";
            default:
                return "Order Shipped";
        }
    }
}
