package com.tika.mykrecek;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {
    private EditText namaLengkap,nomorHp,password;
    private Button btnRegister;
    private ProgressDialog loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        namaLengkap = (EditText) findViewById(R.id.nama_lengkap);
        nomorHp = (EditText) findViewById(R.id.nomor_hp);
        password = (EditText) findViewById(R.id.password);
        loading = new ProgressDialog(this);

        btnRegister = (Button) findViewById(R.id.register_button);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BuatAkun();
            }
        });
    }

    private void BuatAkun() {
        String nama = namaLengkap.getText().toString();
        String phone = nomorHp.getText().toString();
        String passwordku = password.getText().toString();

        if (TextUtils.isEmpty(nama))
        {
            Toast.makeText(this,"nama harus diisi",Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(phone))
        {
            Toast.makeText(this,"telepon harus diisi",Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(passwordku))
        {
            Toast.makeText(this,"password harus diisi",Toast.LENGTH_SHORT).show();
        }
        else {
            loading.setTitle("Membuat Akun Baru");
            loading.setMessage("Mohon menungggu sebentar");
            loading.setCanceledOnTouchOutside(false);
            loading.show();

            VallidateIdentitas(nama,phone,passwordku);

        }

    }

    private void VallidateIdentitas(final String nama, final String phone,final String passwordku) {
        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();
        RootRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!(dataSnapshot.child("Users").child(phone).exists()))
                {
                    HashMap<String, Object> userdataMap = new HashMap<>();
                    userdataMap.put("telepon",phone);
                    userdataMap.put("password",passwordku);
                    userdataMap.put("nama",nama);

                    RootRef.child("Users").child(phone).updateChildren(userdataMap)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful())
                                    {
                                        Toast.makeText(RegisterActivity.this, "Selamat, akun anda berhasil dibuat", Toast.LENGTH_SHORT).show();
                                        loading.dismiss();

                                        Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                                        startActivity(intent);
                                    }
                                    else
                                    {
                                        loading.dismiss();
                                        Toast.makeText(RegisterActivity.this, "Akun anda gagal dibuat, silahkan coba lagi", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}