package com.garden.gardenorder;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.garden.gardenorder.Common.Common;
import com.garden.gardenorder.Interface.ItemClickListener;
import com.garden.gardenorder.Model.Request;
import com.garden.gardenorder.ViewHolder.OrderViewHolder;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class OrderStatus extends AppCompatActivity {

    public RecyclerView recyclerView;
    public RecyclerView.LayoutManager layoutManager;
    String phone;

    FirebaseRecyclerAdapter<Request, OrderViewHolder> adapter;


    FirebaseDatabase database;
    DatabaseReference requests;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_status);
        Intent i = getIntent();

        //bazza

        database = FirebaseDatabase.getInstance();
        requests = database.getReference("Requests");
        phone = Common.currentUser.getPhone();

        recyclerView = (RecyclerView)findViewById(R.id.listOrders);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        if(i.getStringExtra("brojStola") != null) {
            loadOrders(getIntent().getStringExtra("brojStola"));
        }
        else {
            loadOrders(phone);
        }
    }

    private void loadOrders(String phone) {
        adapter = new FirebaseRecyclerAdapter<Request, OrderViewHolder>(
                Request.class,
                R.layout.order_layout,
                OrderViewHolder.class,
                requests.orderByChild("phone")
                        .equalTo(phone)
        ) {
            @Override
            protected void populateViewHolder(OrderViewHolder viewHolder, Request model, int position) {
                viewHolder.txtOrderId.setText(adapter.getRef(position).getKey());
                viewHolder.txtOrderStatus.setText(Common.convertCodeToStatus(model.getStatus()));
                viewHolder.txtOrderPhone.setText(model.getPhone());

                viewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        //da se ne crasha app kad kliknemo na narudjbu
                        Log.d("info", "Clicked");

                    }
                });
            }
        };
        recyclerView.setAdapter(adapter);
    }


}
