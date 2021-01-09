package com.tika.mykrecek;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.tika.mykrecek.Model.NewOrders;

public class AdminNewOrderActivity extends AppCompatActivity {
    private RecyclerView orderList;
    private DatabaseReference ordersRef, cartListRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_new_order);

        ordersRef = FirebaseDatabase.getInstance().getReference().child("Orders");
        orderList = findViewById(R.id.orders_list);
        orderList.setLayoutManager(new LinearLayoutManager(this));
        cartListRef = FirebaseDatabase.getInstance().getReference().child("cart list").child("User View");

    }

    @Override
    protected void onStart() {
        super.onStart();

        final FirebaseRecyclerOptions<NewOrders> options =
                new FirebaseRecyclerOptions.Builder<NewOrders>()
                        .setQuery(ordersRef,NewOrders.class)
                        .build();

        FirebaseRecyclerAdapter<NewOrders,AdminOrderViewHolder> adapter =
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

                        adminOrderViewHolder.btnSelsesai.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                String uID = getRef(i).getKey();
                                ordersRef.child(uID).removeValue();


                            }
                        });

                        adminOrderViewHolder.showOrderButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                String uID = getRef(i).getKey();

                                Intent intent = new Intent(AdminNewOrderActivity.this, AdminUserOrdersActivity.class);
                                intent.putExtra("uid",uID);
                                startActivity(intent);
                            }
                        });

                        adminOrderViewHolder.btnKonfirmasi.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                String uID = getRef(i).getKey();

                                ChangState(uID);

                                Toast.makeText(AdminNewOrderActivity.this, "Pesanan Sudah dikonfirmasi", Toast.LENGTH_LONG).show();

                            }
                        });

//                        adminOrderViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View view) {
//                                CharSequence option[] = new CharSequence[]{
//
//                                        "Ya",
//                                        "Tidak"
//                                };
//                                AlertDialog.Builder builder = new AlertDialog.Builder(AdminNewOrderActivity.this);
//                                builder.setTitle("Konfirmasi pesanan?");
//                                builder.setItems(option, new DialogInterface.OnClickListener() {
//                                    @Override
//                                    public void onClick(DialogInterface dialogInterface, int i) {
//                                        if (i == 0){
//                                            String uID = getRef(i).getKey();
//
//                                            ChangState(uID);
//
//                                            Toast.makeText(AdminNewOrderActivity.this, "Pesanan Sudah dikonfirmasi", Toast.LENGTH_LONG).show();
//
//
//                                        }
//                                        else {
//                                            finish();
//                                        }
//
//                                    }
//                                });
//                                builder.show();
//                            }
//                        });
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

    private void ChangState(String uID) {
        ordersRef
                .child(uID)
                .child("status")
                .setValue("sudah dikonfirmasi");
    }


    public static class AdminOrderViewHolder extends RecyclerView.ViewHolder{
        public TextView userName, userPhone, userTotalHarga,userTanggal,userAlamat, status,btnSelsesai, pembayaranku,btnKonfirmasi;
        public Button showOrderButton;

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
            btnKonfirmasi = itemView.findViewById(R.id.konfirmasi);
        }
    }
}