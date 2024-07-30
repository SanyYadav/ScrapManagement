package com.example.scrapmanagement;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.firestore.DocumentReference;
//import com.google.firebase.firestore.DocumentSnapshot;
//import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
//import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
//import java.util.HashMap;
//import java.util.Map;

public class CreateTask extends AppCompatActivity {
    ImageView imageView;
    VideoView videoView;
    ProgressBar progressBar;
    private Uri selectedUri;
    private static final int PICK_FILE = 1;
    UploadTask uploadTask;
    EditText editTextName, etPhone, editTextAddress, editTextDesc;
    Button btnChooseImage, btnUpload;
    String url, phone;
    StorageReference storageReference;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference db1, db2, db3;
    MediaController mediaController;



    String type;
    Postmember postmember;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_task);
        postmember = new Postmember();
        mediaController = new MediaController( this);
        progressBar = findViewById(R.id.progressBar);

        editTextName=findViewById(R.id.editTextName);
        editTextDesc = findViewById(R.id.editTextDesc);
        etPhone=findViewById(R.id.etPhone);
        editTextAddress=findViewById(R.id.editTextAddress);

        imageView = findViewById(R.id.imageView);
        videoView = findViewById(R.id.videoView);
        btnChooseImage =findViewById(R.id.btnChooseImage);
        btnUpload = findViewById(R.id.btnUpload);
        editTextDesc = findViewById(R.id.editTextDesc);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String currentuid = user.getUid();
        storageReference = FirebaseStorage .getInstance().getReference(currentuid);

        db1 = database.getReference( "All images").child(currentuid);
        db2 = database.getReference( "All videos").child(currentuid);
        db3 = database.getReference( "All posts");

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dopost();
            } });

        btnChooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }});}
    private void chooseImage() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/* video/*");



//intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,PICK_FILE);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==PICK_FILE || resultCode==RESULT_OK || data!=null || data.getData() != null) {
            selectedUri = data.getData();
            if (selectedUri.toString().contains("image")) {
                Picasso.get().load(selectedUri).into(imageView);
                imageView.setVisibility(View.VISIBLE);
                videoView.setVisibility(View.INVISIBLE);
                type = "iv";
            } else if (selectedUri.toString().contains("video")) {
                videoView.setMediaController(mediaController);
                videoView.setVisibility(View.VISIBLE);
                imageView.setVisibility(View.INVISIBLE);
                videoView.setVideoURI(selectedUri);
                videoView.start();
                type = "vv";
            } else {
                Toast.makeText(this, "No file selected", Toast.LENGTH_SHORT).show();
            }}}


    private String getFileExt(Uri uri){
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimetypeMap = MimeTypeMap.getSingleton();
        return mimetypeMap.getExtensionFromMimeType((contentResolver.getType(uri)));
    }

    void Dopost() {

        String name = editTextName.getText().toString();
        String address = editTextAddress.getText().toString();
        String phone = etPhone.getText().toString();

        //FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        //String currentuid = user.getUid();
        String desc = editTextDesc.getText().toString();
        Calendar cdate = Calendar.getInstance();
        SimpleDateFormat currentdate = new SimpleDateFormat("dd-MMMM-yyyy");
        final String savedate = currentdate.format(cdate.getTime());
        Calendar ctime = Calendar.getInstance();
        SimpleDateFormat currenttime = new SimpleDateFormat("HH:mm:ss");
        final String savetime = currentdate.format(ctime.getTime());
        String time = savedate + ":" + savetime;

        if (TextUtils.isEmpty(desc) || selectedUri != null) {
            progressBar.setVisibility(View.VISIBLE);
            final StorageReference reference = storageReference.child(System.currentTimeMillis() + "." + getFileExt(selectedUri));
            uploadTask = reference.putFile(selectedUri);
            Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }
                    return reference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Uri downloadUri = task.getResult();
                        if(type.equals("iv")){
                            postmember.setName(name);
                            postmember.setPhone(phone);
                            postmember.setAddress(address);
                            postmember.setDesc(desc);
                            postmember.setPostUri(downloadUri.toString());
                            postmember.setTime(time) ;
                            // postmember.setCurrentuid(currentuid);
                            postmember.setType("iv");
                            String id = db1.push().getKey();
                            db1.child(id).setValue(postmember);
                            String id1 = db3.push().getKey();
                            db3.child(id1).setValue(postmember);
                            progressBar.setVisibility(View.INVISIBLE);
                            Toast.makeText(CreateTask.this, "Uploaded", Toast.LENGTH_SHORT).show();
                        }else if(type.equals("vv")){
                            postmember.setName(name);
                            postmember.setPhone(phone);
                            postmember.setAddress(address);
                            postmember.setDesc(desc);
                            postmember.setPostUri(downloadUri.toString());
                            postmember.setTime(time) ;
                            //postmember.setCurrentuid(currentuid);
                            postmember.setType("iv");
                            String id3 = db2.push().getKey();
                            db2.child(id3).setValue(postmember);
                            String id4 = db3.push().getKey();
                            db3.child(id4).setValue(postmember);
                            progressBar.setVisibility(View.INVISIBLE);
                            Toast.makeText(CreateTask.this, "Uploaded", Toast.LENGTH_SHORT).show();

                        }else{

                            Toast.makeText(CreateTask.this, "error", Toast.LENGTH_SHORT).show();
                        }}}});
        } else {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
        }
    }
}