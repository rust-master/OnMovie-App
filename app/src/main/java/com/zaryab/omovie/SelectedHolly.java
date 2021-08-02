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
import com.zaryab.omovie.Model.MovieDataH;

import static maes.tech.intentanim.CustomIntent.customType;

public class SelectedHolly  extends YouTubeBaseActivity {


    FirebaseDatabase database;
    DatabaseReference filmDataHl;

    TextView loc_name,txth,film_Descriptioh;
    ImageView coverImageh ,  mapImage;
    private Toolbar mToolbar;
    String filmId = "";

    YouTubePlayerView youTubePlayerView;
    ImageButton btnPlayh;
    String vUrl;
    YouTubePlayer.OnInitializedListener onInitializedListener;
    AdView adView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_selected_holly );


        film_Descriptioh =  (TextView) findViewById(R.id.decriptionh);
        btnPlayh =  findViewById(R.id.fab);
        txth= (TextView) findViewById(R.id.txth);
        coverImageh = (ImageView) findViewById( R.id.coverImageH);

        youTubePlayerView = (YouTubePlayerView)findViewById(R.id.youtube_playerh);

        adView=findViewById(R.id.home_activity_banner_adview);
        MyApplication.setAddView(this,adView);




        database = FirebaseDatabase.getInstance();
        filmDataHl = database.getReference("ItemsHolly");
        filmDataHl.keepSynced(true);

        if (getIntent() !=null)
            filmId =  getIntent().getStringExtra("FilmId");
        if(!filmId.isEmpty() && filmId != null)
        {
            loadfilmdatahl(filmId);

        }

        btnPlayh.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onClick(View view) {
                youTubePlayerView.initialize(PlayerConfig.API_KEY,onInitializedListener);
                btnPlayh.setVisibility(View.INVISIBLE);
                coverImageh.setVisibility( View.INVISIBLE );


            }
        });


    }

    private void loadfilmdatahl(String filmId)
    {

        filmDataHl.child(filmId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                final MovieDataH loc = dataSnapshot.getValue(MovieDataH.class);



                onInitializedListener = new YouTubePlayer.OnInitializedListener() {
                    @Override
                    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                        youTubePlayer.loadVideo(loc.getLink());
                    }

                    @Override
                    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

                    }
                };

                txth.setText(loc.getName());

                Picasso.with(getApplicationContext()).load("http://img.youtube.com/vi/"+loc.getLink()+"/0.jpg")
                        .into(coverImageh);



                film_Descriptioh.setText(loc.getDescription());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    public void finish() {
        super.finish();
        customType(SelectedHolly.this,"up-to-bottom");
    }




}
