package com.example.androidsportswearserver;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.TextView;

import com.example.androidsportswearserver.Common.Common;
import com.example.androidsportswearserver.ViewHolder.OrderDetailAdapter;

public class OrderDetail extends AppCompatActivity {
    TextView order_id,order_phone,order_address,order_total,order_comment;
    String order_id_value="";
    RecyclerView lstSportswear;
    RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);
        order_id=(TextView) findViewById(R.id.order_id);
        order_phone=(TextView) findViewById(R.id.order_phone);
        order_address=(TextView) findViewById(R.id.order_address);
        order_total=(TextView) findViewById(R.id.order_total);
        order_comment=(TextView) findViewById(R.id.order_comment);

        lstSportswear=(RecyclerView) findViewById(R.id.lstSportswear);
        lstSportswear.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(this);
        lstSportswear.setLayoutManager(layoutManager);

        if(getIntent()!=null)
            order_id_value=getIntent().getStringExtra("OrderId");

        order_id.setText(order_id_value);
        order_phone.setText(Common.currentRequest.getPhone());
        order_total.setText(Common.currentRequest.getTotal());
        order_address.setText(Common.currentRequest.getAddress());
        order_comment.setText(Common.currentRequest.getComment());

        OrderDetailAdapter adapter =new OrderDetailAdapter(Common.currentRequest.getSportswear());
       adapter.notifyDataSetChanged();
        lstSportswear.setAdapter(adapter);



    }
}