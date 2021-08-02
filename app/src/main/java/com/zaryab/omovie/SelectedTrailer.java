package com.zaryab.omovie;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.ads.AdView;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.zaryab.omovie.Model.Trailers;

import static maes.tech.intentanim.CustomIntent.customType;

public class SelectedTrailer extends YouTubeBaseActivity {


    FirebaseDatabase database;
    DatabaseReference filmDataT;

    TextView loc_name,txtt,film_Descriptiot;
    ImageView coverImaget ,  mapImage;
    private Toolbar mToolbar;
    String filmId = "";

    YouTubePlayerView youTubePlayerView;
    ImageButton btnPlayt;
    String vUrl;
    YouTubePlayer.OnInitializedListener onInitializedListener;

    AdView adView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_selected_trailer );


        film_Descriptiot =  (TextView) findViewById(R.id.decriptiont);
        btnPlayt =  findViewById(R.id.fab);
        txtt = (TextView) findViewById(R.id.txtt);
        coverImaget = (ImageView) findViewById( R.id.coverImaget);

        youTubePlayerView = (YouTubePlayerView)findViewById(R.id.youtube_playert);

        database = FirebaseDatabase.getInstance();
        filmDataT = database.getReference("Trailers");
        filmDataT.keepSynced(true);

        adView=findViewById(R.id.home_activity_banner_adview);
        MyApplication.setAddView(this,adView);

        if (getIntent() !=null)
            filmId =  getIntent().getStringExtra("FilmId");
        if(!filmId.isEmpty() && filmId != null)
        {
            loadfilmdatat(filmId);

        }


        btnPlayt.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onClick(View view) {
                youTubePlayerView.initialize(PlayerConfig.API_KEY,onInitializedListener);
                btnPlayt.setVisibility(View.INVISIBLE);
                coverImaget.setVisibility( View.INVISIBLE );


            }
        });








    }

    private void loadfilmdatat(String filmId) {
        filmDataT.child(filmId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                final Trailers loc = dataSnapshot.getValue(Trailers.class);

                onInitializedListener = new YouTubePlayer.OnInitializedListener() {
                    @Override
                    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                        youTubePlayer.loadVideo(loc.getLink());
                    }

                    @Override
                    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

                    }
                };

                txtt.setText(loc.getName());

                Picasso.with(getApplicationContext()).load("http://img.youtube.com/vi/"+loc.getLink()+"/0.jpg")
                        .into(coverImaget);



                film_Descriptiot.setText(loc.getDescription());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    public void finish() {
        super.finish();
        customType(SelectedTrailer.this,"right-to-left");
    }
}
