package com.example.scrapmanagement;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class SPostViewHolder extends RecyclerView.ViewHolder{


    TextView sutnameTextView, suttimeTextView, sutphoneNumberTextView, sutAddressTextView, sutDescTextView;

    ImageView sutimageView;

    PlayerView sutvideoView;

    Button callButton;

    FirebaseDatabase database= FirebaseDatabase.getInstance();

    public SPostViewHolder(@NonNull View itemView) {
        super(itemView);
    }

    public void setPost(String name, String phone, String address, String desc, String postUri, String time, String type){
        sutnameTextView = itemView.findViewById(R.id.sutnameTextView);
        suttimeTextView = itemView.findViewById(R.id.suttimeTextView);
        sutphoneNumberTextView = itemView.findViewById(R.id.sutphoneNumberTextView);
        sutAddressTextView = itemView.findViewById(R.id.sutAddressTextView);
        sutDescTextView = itemView.findViewById(R.id.sutDescTextView);
        sutimageView = itemView.findViewById(R.id.sutimageView);
        sutvideoView= itemView.findViewById(R.id.sutvideoView);

        SimpleExoPlayer exoPlayer;


        if(type.equals("iv")){
            sutnameTextView.setText(name);
            sutphoneNumberTextView.setText(phone);
            sutAddressTextView.setText(address);
            sutDescTextView.setText(desc);
            Picasso.get().load(postUri).into(sutimageView);
            suttimeTextView.setText(time);
            sutvideoView.setVisibility(View.INVISIBLE);
        }else if (type.equals("vv")) {
            sutimageView.setVisibility(View.INVISIBLE);
            sutnameTextView.setText(name);
            suttimeTextView.setText(time);
            sutphoneNumberTextView.setText(phone);
            sutAddressTextView.setText(address);
            sutDescTextView.setText(desc);

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
