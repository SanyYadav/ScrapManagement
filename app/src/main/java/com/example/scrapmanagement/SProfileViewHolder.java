package com.example.scrapmanagement;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class SProfileViewHolder extends RecyclerView.ViewHolder {

    TextView usutnameTextView, usutphoneNumberTextView, usutAddressTextView;

    ImageView usutimageView;

    Button uscallButton;

    FirebaseDatabase database= FirebaseDatabase.getInstance();

    public SProfileViewHolder(@NonNull View itemView) {
        super(itemView);
    }


    public void setPost(String name, String phone, String address, String url){
        usutnameTextView = itemView.findViewById(R.id.usutnameTextView);

        usutphoneNumberTextView = itemView.findViewById(R.id.usutphoneNumberTextView);
        usutAddressTextView = itemView.findViewById(R.id.sutAddressTextView);

        usutimageView = itemView.findViewById(R.id.sutimageView);

            usutnameTextView.setText(name);
            usutphoneNumberTextView.setText(phone);
            usutAddressTextView.setText(address);
            Picasso.get().load(url).into(usutimageView);


    }
}
