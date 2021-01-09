package com.tika.mykrecek;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.tika.mykrecek.Model.Product;
import com.tika.mykrecek.Prevelent.Prevelent;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class UserProdukDetailActivity extends AppCompatActivity {

    //private FloatingActionButton addToCartBtn;
    private ImageView productImage;
    private ElegantNumberButton numberButton;
    private TextView productName,productPrice,productAddress;
    private String productID = "", status ="Normal";
    private Button addToCart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_produk_detail);

        productID = getIntent().getStringExtra("pid");

        // addToCartBtn = (FloatingActionButton) findViewById(R.id.btn_cart);
        productImage = (ImageView) findViewById(R.id.product_detail);
        numberButton = (ElegantNumberButton) findViewById(R.id.numberBtn);
        productName = (TextView) findViewById(R.id.namaProduk);
        productPrice = (TextView) findViewById(R.id.hargaProduk);
        productAddress = (TextView) findViewById(R.id.alamatProduk);
        addToCart = (Button) findViewById(R.id.btn_add_to_cart);

        getProductDetails(productID);

        addToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


//                if (status.equals("pesanan sudah dikonfirmasi")){
//
//                    Toast.makeText(ProductDetailActivity.this, "kamu dapat menambahkan produk jika produk sudah selesai dikonfirmasi", Toast.LENGTH_LONG).show();
//                }
//                else {
//                    addingToCartList();
//                }
                addingToCartList();
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        CheckOrderState();
    }

    private void addingToCartList() {
        String saveCurrentTime, saveCurrentDate;

        Calendar calForDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate = currentDate.format(calForDate.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentDate.format(calForDate.getTime());

        final DatabaseReference cartListRef = FirebaseDatabase.getInstance().getReference().child("cart list");

        final HashMap<String, Object> cartMap = new HashMap<>();
        cartMap.put("pid",productID);
        cartMap.put("nama",productName.getText().toString());
        cartMap.put("harga",productPrice.getText().toString());
        cartMap.put("tanggal",saveCurrentDate);
        cartMap.put("waktu",saveCurrentTime);
        cartMap.put("jumlah",numberButton.getNumber());

        cartListRef.child("User View").child(Prevelent.currentOnlineUser.getTelepon())
                .child("Products").child(productID)
                .updateChildren(cartMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if (task.isSuccessful()){
                            cartListRef.child("Admin View").child(Prevelent.currentOnlineUser.getTelepon())
                                    .child("Products").child(productID)
                                    .updateChildren(cartMap)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {

                                            if (task.isSuccessful())
                                            {
                                                Toast.makeText(UserProdukDetailActivity.this, "Berhasil menambahkan ke keranjang", Toast.LENGTH_SHORT).show();
                                                Intent intent = new Intent(UserProdukDetailActivity.this, UserHomeActivity.class);
                                                startActivity(intent);

                                            }
                                        }
                                    });
                        }
                    }
                });
    }

    private void getProductDetails(String productID) {

        DatabaseReference productRef = FirebaseDatabase.getInstance().getReference().child("Product");
        productRef.child(productID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()){

                    Product product = dataSnapshot.getValue(Product.class);

                    productName.setText(product.getNama());
                    productPrice.setText(product.getHarga());
                    productAddress.setText(product.getAlamat());
                    Picasso.get().load(product.getImage()).into(productImage);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
    private  void CheckOrderState(){
        DatabaseReference ordersRef ;
        ordersRef = FirebaseDatabase.getInstance().getReference().child("Orders").child(Prevelent.currentOnlineUser.getTelepon());

        ordersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    String shipingState = dataSnapshot.child("status").getValue().toString();


                    if (shipingState.equals("sudah dikonfirmasi")){

                        status ="pesanan sudah dikonfirmasi";
                    }
                    else if (shipingState.equals("belum dikonfirmasi")){

                        status ="pesanan belum dikonfirmasi";
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}