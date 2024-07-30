package com.example.scrapmanagement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
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

public class AllScrapManProfile extends AppCompatActivity {


    RecyclerView recyclerView;

    FirebaseDatabase database= FirebaseDatabase.getInstance();
    DatabaseReference reference;
    static int PERMISSION_CODE;

    Button signOutBtn, sprofileBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_scrap_man_profile);


        recyclerView= findViewById(R.id.usrecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

    }


    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<ScrapManMember> options= new FirebaseRecyclerOptions.Builder<ScrapManMember>().setQuery(FirebaseDatabase.getInstance().getReference().child("ScrapMan"),ScrapManMember.class).build();

        FirebaseRecyclerAdapter<ScrapManMember,SProfileViewHolder> firebaseRecyclerAdapter= new FirebaseRecyclerAdapter<ScrapManMember, SProfileViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull SProfileViewHolder holder, int position, @NonNull ScrapManMember model) {
                holder.setPost(model.getName(), model.getPhone(), model.getAddress(), model.getUrl());

            }

            @NonNull
            @Override
            public SProfileViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.sprofile_layout,parent,false);

                return null;
            }
        };


        firebaseRecyclerAdapter.startListening();
        recyclerView.setAdapter(firebaseRecyclerAdapter);
    }
}