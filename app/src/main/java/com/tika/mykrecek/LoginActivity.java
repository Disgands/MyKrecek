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
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rey.material.widget.CheckBox;
import com.tika.mykrecek.Model.Users;
import com.tika.mykrecek.Prevelent.Prevelent;

import io.paperdb.Paper;

public class LoginActivity extends AppCompatActivity {
    private EditText InputNumber, InputPassword;
    private Button LoginButton;
    private ProgressDialog loading;

    private String parentDbName = "Users";
    private CheckBox chkBoxRemember ;
    private TextView AdminLink, NotAdmin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        LoginButton = (Button) findViewById(R.id.login);
        InputNumber = (EditText) findViewById(R.id.login_phone);
        InputPassword = (EditText) findViewById(R.id.login_password);
        loading = new ProgressDialog(this);

        AdminLink = (TextView) findViewById(R.id.admin);
        NotAdmin = (TextView) findViewById(R.id.not_admin);

        chkBoxRemember = (CheckBox) findViewById(R.id.remember_me);
        Paper.init(this);

        LoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginUser();
            }
        });
        AdminLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginButton.setText("Login Admin");
                AdminLink.setVisibility(View.INVISIBLE);
                NotAdmin.setVisibility(View.VISIBLE);
                parentDbName = "Admins";
            }
        });

        NotAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                LoginButton.setText("Login User");
                AdminLink.setVisibility(View.VISIBLE);
                NotAdmin.setVisibility(View.INVISIBLE);
                parentDbName = "Users";

            }
        });

    }

    private void LoginUser() {
        String phone = InputNumber.getText().toString();
        String password = InputPassword.getText().toString();

        if (TextUtils.isEmpty(phone))
        {
            Toast.makeText(this,"telepon harus diisi",Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(password))
        {
            Toast.makeText(this,"password harus diisi",Toast.LENGTH_SHORT).show();
        }
        else {
            loading.setTitle("Login Akun");
            loading.setMessage("Mohon menunggu sebentar");
            loading.setCanceledOnTouchOutside(false);
            loading.show();

            AllowAccessToAccount(phone,password);

        }
    }

    private void AllowAccessToAccount(final String phone, final String password) {
        if (chkBoxRemember.isChecked()){

            Paper.book().write(Prevelent.UserPhoneKey, phone);
            Paper.book().write(Prevelent.UserPasswordKey, password);
        }

        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();

        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(parentDbName).child(phone).exists()){
                    Users userData = dataSnapshot.child(parentDbName).child(phone).getValue(Users.class);

                    if (userData.getTelepon().equals(phone)){
                        if (userData.getPassword().equals(password)){
                            if (parentDbName.equals("Admins"))
                            {
                                Toast.makeText(LoginActivity.this, "Login Berhasil", Toast.LENGTH_SHORT).show();
                                loading.dismiss();

                                Intent intent = new Intent(LoginActivity.this, AdminHomeActivity.class);
                                startActivity(intent);
                            }
                            else if (parentDbName.equals("Users"))
                            {
                                Toast.makeText(LoginActivity.this, "Login Berhasil", Toast.LENGTH_SHORT).show();
                                loading.dismiss();

                                Intent intent = new Intent(LoginActivity.this, UserHomeActivity.class);
                                Prevelent.currentOnlineUser = userData;
                                startActivity(intent);
                            }

                        }
                        else {
                            loading.dismiss();
                            Toast.makeText(LoginActivity.this, "password salah", Toast.LENGTH_SHORT).show();
                        }
                    }

                }
                else {
                    Toast.makeText(LoginActivity.this, "Nomor " + phone + "tidak terdaftar", Toast.LENGTH_SHORT).show();
                    loading.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}