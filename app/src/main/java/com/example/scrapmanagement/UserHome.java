package com.example.scrapmanagement;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.auth.FirebaseUser;

public class UserHome extends AppCompatActivity {

    Button signOutBtn, uprofileBtn,goToScrapManList;
    RecyclerView recyclerView;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference reference;
    Button fButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_home);

        //reference= database.getReference("All posts");
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        uprofileBtn = findViewById(R.id.uprofileBtn);

        uprofileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(UserHome.this, UserProfile.class));

            }
        });

        goToScrapManList = findViewById(R.id.goToScrapManList);

        goToScrapManList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(UserHome.this, AllScrapManProfile.class));

            }
        });

        signOutBtn = findViewById(R.id.usignOutBtn);
        signOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(UserHome.this, RegisterPage.class));
                finish();
            }
        });

        fButton = findViewById(R.id.fab);
        fButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Start CreateTaskActivity when the button is clicked
                Intent intent = new Intent(UserHome.this, CreateTask.class);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<Postmember> options = new FirebaseRecyclerOptions.Builder<Postmember>().setQuery(FirebaseDatabase.getInstance().getReference().child("All posts"), Postmember.class).build();


        FirebaseRecyclerAdapter<Postmember, PostViewHolder> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<Postmember, PostViewHolder>(options) {

                    @Override
                    protected void onBindViewHolder(@NonNull PostViewHolder holder, int position, @NonNull Postmember model) {

                        //FireabaseUser user= FirebaseAuth.getInstance().getCurrentUser();
                        //final String currentUserid= user.getId();

                        final String postkey = getRef(position).getKey();

                        holder.setPost(model.getName(), model.getPhone(), model.getAddress(), model.getDesc(), model.getPostUri(), model.getTime(), model.getType());

                    }

                    @NonNull
                    @Override
                    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_layout, parent, false);
                        return new PostViewHolder(view);
                    }

                };
        firebaseRecyclerAdapter.startListening();
        recyclerView.setAdapter(firebaseRecyclerAdapter);

    }
}