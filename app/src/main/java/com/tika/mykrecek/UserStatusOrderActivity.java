package com.tika.mykrecek;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tika.mykrecek.Model.NewOrders;
import com.tika.mykrecek.Prevelent.Prevelent;

public class UserStatusOrderActivity extends AppCompatActivity {
    private RecyclerView orderList;
    private DatabaseReference ordersRef, cartListRef;
    private TextView status;
    String stringDB = "sudah dikonfirmasi";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_status_order);

        ordersRef = FirebaseDatabase.getInstance().getReference().child("Orders");
        orderList = findViewById(R.id.orders_list2);
        orderList.setLayoutManager(new LinearLayoutManager(this));
        cartListRef = FirebaseDatabase.getInstance().getReference().child("cart list").child("User View");
        status = findViewById(R.id.statusku);

    }

    @Override
    protected void onStart() {
        super.onStart();
//         String string = Prevelent.currentOnlineUser.getPhone();
//        Query query = ordersRef.orderByKey().startAt(string);
//        DatabaseReference ordersRef,tes ;
//                        ordersRef = FirebaseDatabase.getInstance().getReference().child("Users").child(Prevelent.currentOnlineUser.getPhone());
//        tes = FirebaseDatabase.getInstance().getReference().child("Orders").getRef();
//
//
//        if (tes != null && ordersRef.equalTo(tes)){

        final FirebaseRecyclerOptions<NewOrders> options =
                new FirebaseRecyclerOptions.Builder<NewOrders>()
                        .setQuery(ordersRef,NewOrders.class)
                        .build();


        FirebaseRecyclerAdapter<NewOrders, AdminOrderViewHolder> adapter =
                new FirebaseRecyclerAdapter<NewOrders, AdminOrderViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull AdminOrderViewHolder adminOrderViewHolder, final int i, @NonNull NewOrders newOrders) {
                        adminOrderViewHolder.userName.setText("Nama :" + newOrders.getNama() );
                        adminOrderViewHolder.userPhone.setText("Telepon :" + newOrders.getPhone() );
                        adminOrderViewHolder.userAlamat.setText("Alamat :" + newOrders.getAlamat() );
                        adminOrderViewHolder.userTanggal.setText("Tanggal :" + newOrders.getTanggal() );
                        adminOrderViewHolder.userTotalHarga.setText("Total Harga :" + newOrders.getTotalHarga() );
                        adminOrderViewHolder.status.setText("Status :" + newOrders.getStatus());
                        adminOrderViewHolder.pembayaranku.setText("Pembayaran :"+ newOrders.getPembayaran());
                        ordersRef = FirebaseDatabase.getInstance().getReference().child("Orders").child(Prevelent.currentOnlineUser.getTelepon()).child("status");
                        ordersRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                TextView pengiriman;
                                pengiriman = findViewById(R.id.proses_pengiriman);
                                String status = dataSnapshot.getValue(String.class);
                                if (dataSnapshot.exists() && status.equals(stringDB)){
                                    pengiriman.setVisibility(View.VISIBLE);

                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                        adminOrderViewHolder.konfirmasiku.setVisibility(View.GONE);
                        adminOrderViewHolder.btnSelsesai.setVisibility(View.GONE);
                        adminOrderViewHolder.showOrderButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                String uID = getRef(i).getKey();

                                Intent intent = new Intent(UserStatusOrderActivity.this, AdminUserOrdersActivity.class);
                                intent.putExtra("uid",uID);
                                startActivity(intent);
                            }
                        });
                    }

                    @NonNull
                    @Override
                    public AdminOrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_order,parent,false);
                        return  new AdminOrderViewHolder(view);
                    }
                };
        orderList.setAdapter(adapter);
        adapter.startListening();
    }

//    private void ChangState(String uID) {
//        ordersRef
//                .child(uID)
//                .child("status")
//                .setValue("sudah dikonfirmasi");
//    }


    public static class AdminOrderViewHolder extends RecyclerView.ViewHolder{
        public TextView userName, userPhone, userTotalHarga,userTanggal,userAlamat, status, pembayaranku,pengiriman;
        public Button showOrderButton, btnSelsesai,konfirmasiku;

        public AdminOrderViewHolder(@NonNull View itemView) {
            super(itemView);
            userName = itemView.findViewById(R.id.order_user_name);
            userPhone = itemView.findViewById(R.id.order_phone_number);
            userTotalHarga = itemView.findViewById(R.id.order_total_price);
            userTanggal = itemView.findViewById(R.id.order_date_time);
            userAlamat = itemView.findViewById(R.id.order_address_city);
            status = itemView.findViewById(R.id.status);
            pembayaranku = itemView.findViewById(R.id.pembayaran);
            showOrderButton = itemView.findViewById(R.id.show_all_products_btn);
            btnSelsesai = itemView.findViewById(R.id.selesai);
            pengiriman = itemView.findViewById(R.id.proses_pengiriman);
            konfirmasiku = itemView.findViewById(R.id.konfirmasi);
        }
    }
}