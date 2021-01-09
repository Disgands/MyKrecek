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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.tika.mykrecek.Model.Cart;
import com.tika.mykrecek.Prevelent.Prevelent;
import com.tika.mykrecek.ViewHolder.CartViewHolder;

public class UserCartActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private Button nextBtn;
    private TextView totalHarga,txtMsg1;
    private int overTotalHarga ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_cart);

        recyclerView = (RecyclerView) findViewById(R.id.cart_list);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        nextBtn = (Button) findViewById(R.id.next_btn);
        totalHarga = (TextView) findViewById(R.id.total_harga);
        txtMsg1 = (TextView) findViewById(R.id.msg1);

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                totalHarga.setText("Total = Rp." + String.valueOf(overTotalHarga));

                Intent intent = new Intent(UserCartActivity.this, UserFinalOrderActivity.class);
                intent.putExtra("Total Harga", String.valueOf(overTotalHarga));
                startActivity(intent);
                finish();
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
//        CheckOrderState();

        final DatabaseReference cartlistRef = FirebaseDatabase.getInstance().getReference().child("cart list");
        FirebaseRecyclerOptions<Cart> options =
                new FirebaseRecyclerOptions.Builder<Cart>()
                        .setQuery(cartlistRef.child("User View")
                                .child(Prevelent.currentOnlineUser.getTelepon()).child("Products"), Cart.class)
                        .build();

        FirebaseRecyclerAdapter<Cart, CartViewHolder> adapter
                = new FirebaseRecyclerAdapter<Cart, CartViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final CartViewHolder cartViewHolder, int i, @NonNull final Cart cart) {

                cartViewHolder.txtProductName.setText(cart.getNama());
                cartViewHolder.txtProductPrice.setText("Rp. "+ cart.getHarga());
                cartViewHolder.txtProductQuantity.setText("Jumlah = x" +cart.getJumlah());

                int onetypeProductPrice = ((Integer.valueOf(cart.getHarga()))) * Integer.valueOf(cart.getJumlah());
                overTotalHarga = overTotalHarga + onetypeProductPrice;
                totalHarga.setText("Total = Rp." + String.valueOf(overTotalHarga));

                cartViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        CharSequence options[] = new CharSequence[]
                                {
                                        "Edit","Hapus"

                                };
                        AlertDialog.Builder builder = new AlertDialog.Builder(UserCartActivity.this);
                        builder.setTitle("Pilihan");
                        builder.setItems(options, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                if (i == 0 ){
                                    Intent intent = new Intent(UserCartActivity.this, UserProdukDetailActivity.class);
                                    intent.putExtra("pid",cart.getPid());
                                    startActivity(intent);
                                }
                                if (i == 1){

                                    cartlistRef.child("User View")
                                            .child(Prevelent.currentOnlineUser.getTelepon())
                                            .child("Products")
                                            .child(cart.getPid())
                                            .removeValue()
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful())
                                                    {
                                                        if (task.isSuccessful())
                                                        {
                                                            Toast.makeText(UserCartActivity.this, "Produk berhasil dihapus", Toast.LENGTH_SHORT).show();
                                                            Intent intent = new Intent(UserCartActivity.this, UserHomeActivity.class);
                                                            startActivity(intent);
                                                        }

                                                    }
                                                }
                                            });
                                }
                            }
                        });
                        builder.show();
                    }
                });

            }

            @NonNull
            @Override
            public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_cart,parent, false);
                CartViewHolder holder = new CartViewHolder(view);
                return holder;
            }
        };
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }

//    private  void CheckOrderState(){
//            DatabaseReference ordersRef ;
//            ordersRef = FirebaseDatabase.getInstance().getReference().child("Orders").child(Prevelent.currentOnlineUser.getPhone());
//
//            ordersRef.addValueEventListener(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                    if (dataSnapshot.exists()){
//                        String shipingState = dataSnapshot.child("status").getValue().toString();
//                        String UserName = dataSnapshot.child("nama").getValue().toString();
//
//                        if (shipingState.equals("sudah dikonfirmasi")){
//
//                            totalHarga.setText(UserName + ", order telah dikonfirmasi. harap segera mengambil pesanan anda");
//                            recyclerView.setVisibility(View.GONE);
//
//                            txtMsg1.setVisibility(View.VISIBLE);
//                            nextBtn.setVisibility(View.GONE);
//
//
//                            Toast.makeText(CartActivity.this, "", Toast.LENGTH_SHORT).show();
//                        }
//                        else if (shipingState.equals("belum dikonfirmasi")){
//                            totalHarga.setText("Pesanan Belum dikonfirmasi");
//                            recyclerView.setVisibility(View.GONE);
//
//                            txtMsg1.setVisibility(View.VISIBLE);
//                            nextBtn.setVisibility(View.GONE);
//
//
//                            Toast.makeText(CartActivity.this, "", Toast.LENGTH_SHORT).show();
//
//                        }
//
//
//                    }
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                }
//            });

    //   }
}