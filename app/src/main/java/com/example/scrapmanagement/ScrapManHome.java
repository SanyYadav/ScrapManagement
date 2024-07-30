package com.example.scrapmanagement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ScrapManHome extends AppCompatActivity {

    RecyclerView recyclerView;

    FirebaseDatabase database= FirebaseDatabase.getInstance();
    DatabaseReference reference;
    static int PERMISSION_CODE;

    Button signOutBtn, sprofileBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrap_man_home);

        signOutBtn= findViewById(R.id.ssignOutBtn);

        signOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(ScrapManHome.this, RegisterPage.class));
                finish();
            }
        });

        sprofileBtn= findViewById(R.id.sprofileBtn);

        sprofileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(ScrapManHome.this, ScrapManProfile.class));

            }
        });


        recyclerView= findViewById(R.id.srecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

    }


    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<Postmember> options= new FirebaseRecyclerOptions.Builder<Postmember>().setQuery(FirebaseDatabase.getInstance().getReference().child("All posts"),Postmember.class).build();

        FirebaseRecyclerAdapter<Postmember,SPostViewHolder> firebaseRecyclerAdapter= new FirebaseRecyclerAdapter<Postmember, SPostViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull SPostViewHolder holder, int position, @NonNull Postmember model) {

                holder.setPost(model.getName(), model.getPhone(), model.getAddress(), model.getDesc(), model.getPostUri(), model.getTime(), model.getType());

            }

            @NonNull
            @Override
            public SPostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.spost_layout,parent,false);
                return null;
            }
        };

        firebaseRecyclerAdapter.startListening();
        recyclerView.setAdapter(firebaseRecyclerAdapter);
    }
}