package com.tika.mykrecek;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class AdminInputNewProductActivity extends AppCompatActivity {

    private EditText namaBarang,harga,alamatToko;
    private Button addProduk;
    private ImageView addPhoto;
    private static final int GalleryPict = 1;
    private Uri imageUri;
    private String nBarang, hHarga, aToko, saveCurrentDate, saveCurrentTime, productRandomKey;
    private String downloadImageUrl;
    private StorageReference productImageRef;
    private DatabaseReference productRef;
    private ProgressDialog loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_input_new_product);

        loading = new ProgressDialog(this);
        productImageRef = FirebaseStorage.getInstance().getReference().child("Product Image");
        productRef = FirebaseDatabase.getInstance().getReference().child("Product");

        addPhoto = (ImageView) findViewById(R.id.image);
        namaBarang = (EditText) findViewById(R.id.produk_name);
        harga = (EditText) findViewById(R.id.harga);
        alamatToko = (EditText) findViewById(R.id.alamat);
        addProduk = (Button) findViewById(R.id.tambahkan);

        addPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                OpenGallery();

            }
        });

        addProduk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ValidateProductData();
            }
        });

    }

    private void OpenGallery() {

        Intent gallery = new Intent();
        gallery.setAction(Intent.ACTION_GET_CONTENT);
        gallery.setType("image/*");
        startActivityForResult(gallery,GalleryPict);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==GalleryPict && resultCode==RESULT_OK && data!=null)
        {
            imageUri = data.getData();
            addPhoto.setImageURI(imageUri);
        }
    }

    private void ValidateProductData (){

        nBarang = namaBarang.getText().toString();
        hHarga = harga.getText().toString();
        aToko = alamatToko.getText().toString();

        if (imageUri == null)
        {
            Toast.makeText(this, "Product image is mandatory", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(nBarang))
        {
            Toast.makeText(this, "silahkan memasukkan nama barang", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(hHarga))
        {
            Toast.makeText(this, "silahkan memasukkan berat barang", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(aToko))
        {
            Toast.makeText(this, "silahkan memasukkan harga masuk viar", Toast.LENGTH_SHORT).show();
        }
        else {
            StoreProductInformation();

        }
    }

    private void StoreProductInformation() {
        loading.setTitle("Menambahkan Produk Baru");
        loading.setMessage("Mohon menungggu sebentar");
        loading.setCanceledOnTouchOutside(false);
        loading.show();

        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate = currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(calendar.getTime());

        productRandomKey = saveCurrentDate + saveCurrentTime;

        final StorageReference filePath = productImageRef.child(imageUri.getLastPathSegment() + productRandomKey + ".jpg");

        final UploadTask uploadTask = filePath.putFile(imageUri);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                String message = e.toString();
                Toast.makeText(AdminInputNewProductActivity.this, "Error" + message, Toast.LENGTH_SHORT).show();
                loading.dismiss();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                Toast.makeText(AdminInputNewProductActivity.this, "Foto Berhasil diUpload", Toast.LENGTH_SHORT).show();
                Task<Uri> uriTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful())
                        {
                            throw task.getException();
                        }
                        downloadImageUrl = filePath.getDownloadUrl().toString();
                        return filePath.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()){

                            downloadImageUrl = task.getResult().toString();
                            Toast.makeText(AdminInputNewProductActivity.this, "Foto berhasil diupload ke database", Toast.LENGTH_SHORT).show();

                            saveProductInToDatabase();
                        }
                    }
                });

            }

            private void saveProductInToDatabase() {
                HashMap<String, Object> productMap = new HashMap<>();
                productMap.put("pid",productRandomKey);
                productMap.put("date",saveCurrentDate);
                productMap.put("time",saveCurrentTime);
                productMap.put("image",downloadImageUrl);
                productMap.put("nama",nBarang);
                productMap.put("harga",hHarga);
                productMap.put("alamat",aToko);

                productRef.child(productRandomKey).updateChildren(productMap)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                if (task.isSuccessful()){

                                    Intent intent = new Intent(AdminInputNewProductActivity.this, AdminHomeActivity.class);
                                    startActivity(intent);

                                    loading.dismiss();
                                    Toast.makeText(AdminInputNewProductActivity.this, "Produk berhasil diupload", Toast.LENGTH_SHORT).show();
                                }
                                else {
                                    loading.dismiss();
                                    String message = task.getException().toString();
                                    Toast.makeText(AdminInputNewProductActivity.this, "Error" + message, Toast.LENGTH_SHORT).show();

                                }
                            }
                        });
            }
        });
    }

}