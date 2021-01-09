package com.tika.mykrecek;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.tika.mykrecek.Prevelent.Prevelent;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class UserFinalOrderActivity extends AppCompatActivity {
    private EditText nameEdtText, phoneEdtText, alamatEdtText, kabupatenEdtText;
    private Button confirmBtn;
    private RadioGroup rgPembayaran;

    private String totalHarga;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_final_order);
        totalHarga = getIntent().getStringExtra("Total Harga");

        confirmBtn = (Button) findViewById(R.id.confirm_final_order_btn);
        nameEdtText = (EditText) findViewById(R.id.shippment_name);
        phoneEdtText = (EditText) findViewById(R.id.shippment_phone_number);
        alamatEdtText = (EditText) findViewById(R.id.shippment_address);
        kabupatenEdtText = (EditText) findViewById(R.id.shippment_city);
        rgPembayaran = (RadioGroup) findViewById(R.id.rg_pembayaran);


        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Check();
            }
        });
    }

    private void Check() {
        if (TextUtils.isEmpty(nameEdtText.getText().toString())){
            Toast.makeText(this, "silahkan memasukkan nama lengkap", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(phoneEdtText.getText().toString())){
            Toast.makeText(this, "silahkan memasukkan nomor telepon", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(alamatEdtText.getText().toString())){
            Toast.makeText(this, "silahkan memasukkan alamat lengkap", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(kabupatenEdtText.getText().toString())){
            Toast.makeText(this, "silahkan memasukkan kabupaten/kota", Toast.LENGTH_SHORT).show();
        }

        else {
            ConfimOrder();
        }
    }

    private void ConfimOrder() {
        final String saveCurrentDate, saveCurrentTime;
        Calendar calForDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate = currentDate.format(calForDate.getTime());
        int selectedId = rgPembayaran.getCheckedRadioButtonId();

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(calForDate.getTime());


        final DatabaseReference ordersRef = FirebaseDatabase.getInstance().getReference()
                .child("Orders")
                .child(Prevelent.currentOnlineUser.getTelepon());

        HashMap<String, Object> orderMap = new HashMap<>();
        orderMap.put("TotalHarga", totalHarga);
        orderMap.put("nama", nameEdtText.getText().toString());
        orderMap.put("phone", phoneEdtText.getText().toString());
        orderMap.put("alamat", alamatEdtText.getText().toString());
        orderMap.put("kabupaten", kabupatenEdtText.getText().toString());
        orderMap.put("tanggal", saveCurrentDate);
        orderMap.put("waktu", saveCurrentTime);
        orderMap.put("status","belum dikonfirmasi");
        switch (selectedId){
            case R.id.rb_transfer :
                orderMap.put("Pembayaran","Transfer");
                break;
            case R.id.rb_cod :
                orderMap.put("Pembayaran","COD");
                break;
        }

        ordersRef.updateChildren(orderMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    FirebaseDatabase.getInstance().getReference().child("cart list")
                            .child("User View")
                            .child(Prevelent.currentOnlineUser.getTelepon())
                            .removeValue()
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful())
                                        Toast.makeText(UserFinalOrderActivity.this, "Order berhasil silahkan menunggu konfirmasi", Toast.LENGTH_SHORT).show();
                                    int selectedId = rgPembayaran.getCheckedRadioButtonId();
                                    switch (selectedId){
                                        case R.id.rb_transfer :
                                            Intent intent = new Intent(UserFinalOrderActivity.this, UserTransferActivity.class);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                            startActivity(intent);
                                            break;
                                        case R.id.rb_cod :
                                            Intent intent2 = new Intent(UserFinalOrderActivity.this, UserCodActivity.class);
                                            intent2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                            startActivity(intent2);
                                            break;}

                                    finish();
                                }
                            });

                }
            }
        });
    }


    }
