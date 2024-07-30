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

public class UserCrProfile extends AppCompatActivity {

    EditText uUploadName, uUploadPhone, uUploadAddress;

    Button uUploadButton;

    ImageView uUploadImage;

    ProgressBar uUploadprogressBar;

    Uri imageUri;

    UploadTask uploadTask;

    StorageReference storageReference;
    FirebaseDatabase database=  FirebaseDatabase.getInstance();
    DatabaseReference databaseReference;
    FirebaseFirestore db= FirebaseFirestore.getInstance();
    DocumentReference documentReference;

    NormalUserMember umember;

    String currentUserId;
    private static final int PICK_IMAGE=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_cr_profile);

        uUploadName= findViewById(R.id.uUploadName);
        uUploadPhone= findViewById(R.id.uUploadPhone);
        uUploadAddress= findViewById(R.id.uUploadAddress);
        uUploadButton= findViewById(R.id.uUploadButton);
        uUploadImage= findViewById(R.id.uUploadImage);
        uUploadprogressBar= findViewById(R.id.uUploadprogressBar);
        umember= new NormalUserMember();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        currentUserId = user.getUid();

        documentReference = db.collection("Normal User").document(currentUserId);
        storageReference = FirebaseStorage.getInstance().getReference("Profile images");
        databaseReference = database.getReference("Normal Users");
        uUploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadData();
            }
        });

        uUploadImage.setOnClickListener(new View.OnClickListener() {
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

                Picasso.get().load(imageUri).into(uUploadImage);
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

        String name = uUploadName.getText().toString();
        String phoneno = uUploadPhone.getText().toString();
        String address = uUploadAddress.getText().toString();

        validationinfo(name,phoneno);

        if(!TextUtils.isEmpty(name) || !TextUtils.isEmpty(phoneno) || !TextUtils.isEmpty(address) || imageUri != null){

            uUploadprogressBar.setVisibility(View.VISIBLE);
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


                        umember.setName(name);
                        umember.setPhone(phoneno);
                        umember.setAddress(address);
                        umember.setUid(currentUserId);
                        umember.setUrl(downloadUri.toString());
                        databaseReference.child(currentUserId).setValue(umember);
                        documentReference.set(profile)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        uUploadprogressBar.setVisibility(View.INVISIBLE);
                                        Toast.makeText(UserCrProfile.this, "Profile Created", Toast.LENGTH_SHORT).show();
                                        Handler handler = new Handler();

                                        handler.postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                Intent intent = new Intent (UserCrProfile.this,UserProfile.class);
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
            uUploadName.requestFocus();
            uUploadName.setError("Enter valid name");
            return false;
        }
        else if(phoneno.length()!=10){
            uUploadPhone.requestFocus();
            uUploadPhone.setError("Only 10 charcters allowed");
            return false;
        }
        else{
            return true;
        }
    }


}