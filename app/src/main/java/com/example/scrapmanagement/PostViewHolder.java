package com.example.scrapmanagement;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class PostViewHolder  extends RecyclerView.ViewHolder{

    TextView utnameTextView, uttimeTextView, utphoneNumberTextView, utAddressTextView, utDescTextView;

    ImageView utimageView;

    PlayerView utvideoView;
    FirebaseDatabase database= FirebaseDatabase.getInstance();

    public PostViewHolder(@NonNull View itemView) {
        super(itemView);
    }

    public void setPost(String name, String phone, String address, String desc, String postUri, String time, String type){
        utnameTextView = itemView.findViewById(R.id.utnameTextView);
        uttimeTextView = itemView.findViewById(R.id.uttimeTextView);
        utphoneNumberTextView = itemView.findViewById(R.id.utphoneNumberTextView);
        utAddressTextView = itemView.findViewById(R.id.utAddressTextView);
        utDescTextView = itemView.findViewById(R.id.utDescTextView);
        utimageView = itemView.findViewById(R.id.utimageView);
        utvideoView= itemView.findViewById(R.id.utvideoView);

        SimpleExoPlayer exoPlayer;


        if(type.equals("iv")){
            utnameTextView.setText(name);
            utphoneNumberTextView.setText(phone);
            utAddressTextView.setText(address);
            utDescTextView.setText(desc);
            Picasso.get().load(postUri).into(utimageView);
            uttimeTextView.setText(time);
            utvideoView.setVisibility(View.INVISIBLE);
        }else if (type.equals("vv")) {
            utimageView.setVisibility(View.INVISIBLE);
            utnameTextView.setText(name);
            uttimeTextView.setText(time);
            utphoneNumberTextView.setText(phone);
            utAddressTextView.setText(address);
            utDescTextView.setText(desc);

            //try {
            //    BandwidthMeter BandWidthMeter= new DefaultBandwidhMeter.Builder(acivity).build();
            //    TracSelector tracSelector= new DefaultTrackSelector(new AdaptiveTrackSelection.Factory(bandwidthMeter));
            //    utvideoView= (SimpleExoPlayer) ExoPlayerFactory.newSimpleInstance(activity);
            //    Uri video= Uri.parse(postUri);
            //   DefaultHttpDataSourceFactory df= new DefaultHttpDataSourceFactory("video");
            //    ExtractorsFactory ef= new DefaultExtractorsFactory();
            //    MediaSource mediaSource= new ExtractorMdeiaSource(video,df,ef,null,null);
            //    utvideoView.setPlayer(exoPlayer);
            //    exoPlayer.prepare(mediaSource);
            //    exoPlayer.setPlayWhenReady(false);
            //} catch (Exception e) {
            //    Toast.makeText(activity, "Error", Toast.LENGTH_SHORT),show();
            //}
        }
    }

}
