package com.example.scrapmanagement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

public class ScrapManProfile extends AppCompatActivity {

    ImageView sUpimageView;

    TextView sUpName, sUpPhone, sUpAddress;

    Button sGoToCrButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrap_man_profile);




        sUpimageView= findViewById(R.id.sUpimageView);
        sUpName= findViewById(R.id.sUpName);
        sUpPhone= findViewById(R.id.sUpPhone);
        sUpAddress= findViewById(R.id.sUpAddress);
        sGoToCrButton= findViewById(R.id.sGoToCrButton);

        sGoToCrButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToUserCrProfile();
            }
        });

    }


    private void navigateToUserCrProfile() {
        Intent intent = new Intent(ScrapManProfile.this, ScrapManCrProfile.class);
        startActivity(intent);
    }

    public void onStart() {
        super.onStart();

        FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
        String currentid= user.getUid();
        DocumentReference reference;
        FirebaseFirestore firestore= FirebaseFirestore.getInstance();

        reference = firestore.collection("ScrapMan").document(currentid);
        reference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.getResult().exists()){
                    String nameResult= task.getResult().getString("name");
                    String phoneResult= task.getResult().getString("phone");
                    String addressResult= task.getResult().getString("address");
                    String url= task.getResult().getString("url");

                    Picasso.get().load(url).into(sUpimageView);
                    sUpName.setText(nameResult);
                    sUpPhone.setText(phoneResult);
                    sUpAddress.setText(addressResult);

                }
            }
        });
    }
}