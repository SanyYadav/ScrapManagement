package com.example.scrapmanagement;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class ScrapManCrProfile extends AppCompatActivity {


    EditText sUploadName, sUploadPhone, sUploadAddress;

    Button sUploadButton;

    ImageView sUploadImage;

    ProgressBar sUploadprogressBar;

    Uri imageUri;

    UploadTask uploadTask;

    StorageReference storageReference;
    FirebaseDatabase database=  FirebaseDatabase.getInstance();
    DatabaseReference databaseReference;
    FirebaseFirestore db= FirebaseFirestore.getInstance();
    DocumentReference documentReference;

    ScrapManMember smember;

    String currentUserId;
    private static final int PICK_IMAGE=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrap_man_cr_profile);


        sUploadName= findViewById(R.id.sUploadName);
        sUploadPhone= findViewById(R.id.sUploadPhone);
        sUploadAddress= findViewById(R.id.sUploadAddress);
        sUploadButton= findViewById(R.id.sUploadButton);
        sUploadImage= findViewById(R.id.sUploadImage);
        sUploadprogressBar= findViewById(R.id.sUploadprogressBar);
        smember= new ScrapManMember();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        currentUserId = user.getUid();

        documentReference = db.collection("ScrapMan").document(currentUserId);
        storageReference = FirebaseStorage.getInstance().getReference("ScrapMan Profile images");
        databaseReference = database.getReference("ScrapMan");
        sUploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadData();
            }
        });

        sUploadImage.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent,PICK_IMAGE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if(requestCode==PICK_IMAGE || resultCode == RESULT_OK || data!= null || data.getData() != null){
                imageUri = data.getData();

                Picasso.get().load(imageUri).into(sUploadImage);
            } }catch(Exception e){
            Toast.makeText(this,"Error"+e, Toast.LENGTH_SHORT).show();
        }
    }
    private String getFileExt(Uri uri){
        ContentResolver contentResolver= getContentResolver();
        MimeTypeMap mineTypeMap= MimeTypeMap.getSingleton();
        return mineTypeMap.getExtensionFromMimeType((contentResolver.getType(uri)));
    }

    private void uploadData() {

        String name = sUploadName.getText().toString();
        String phoneno = sUploadPhone.getText().toString();
        String address = sUploadAddress.getText().toString();

        validationinfo(name,phoneno);

        if(!TextUtils.isEmpty(name) || !TextUtils.isEmpty(phoneno) || !TextUtils.isEmpty(address) || imageUri != null){

            sUploadprogressBar.setVisibility(View.VISIBLE);
            final StorageReference reference = storageReference.child(System.currentTimeMillis()+"."+getFileExt(imageUri));
            uploadTask = reference.putFile(imageUri);

            Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if(!task.isSuccessful()){
                        throw task.getException();
                    }
                    return reference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if(task.isSuccessful()){
                        Uri downloadUri = task.getResult();

                        HashMap<String,String > profile = new HashMap<>();
                        profile.put("name",name);
                        profile.put("phone",phoneno);
                        profile.put("address",address);
                        profile.put("url",downloadUri.toString());
                        profile.put("uid",currentUserId);


                        smember.setName(name);
                        smember.setPhone(phoneno);
                        smember.setAddress(address);
                        smember.setUid(currentUserId);
                        smember.setUrl(downloadUri.toString());
                        databaseReference.child(currentUserId).setValue(smember);
                        documentReference.set(profile)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        sUploadprogressBar.setVisibility(View.INVISIBLE);
                                        Toast.makeText(ScrapManCrProfile.this, "Profile Created", Toast.LENGTH_SHORT).show();
                                        Handler handler = new Handler();

                                        handler.postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                Intent intent = new Intent (ScrapManCrProfile.this,UserProfile.class);
                                                startActivity(intent);
                                            }
                                        },2000);
                                    }
                                });
                    }
                }
            });
        }else{
            Toast.makeText(this,"Please fill all fields", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean validationinfo(String name, String phoneno) {
        if(!name.matches("[a-zA-Z]+")){
            sUploadName.requestFocus();
            sUploadName.setError("Enter valid name");
            return false;
        }
        else if(phoneno.length()!=10){
            sUploadPhone.requestFocus();
            sUploadPhone.setError("Only 10 charcters allowed");
            return false;
        }
        else{
            return true;
        }
    }
}