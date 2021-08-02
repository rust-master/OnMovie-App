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
import com.zaryab.omovie.Model.MovieDataB;

import static maes.tech.intentanim.CustomIntent.customType;

public class SelectedBolly extends YouTubeBaseActivity {


    FirebaseDatabase database;
    DatabaseReference filmDataBl;

    TextView loc_name,txtb,film_Descriptiob;
    ImageView coverImageb ,  mapImage;
    private Toolbar mToolbar;
    String filmId = "";

    YouTubePlayerView youTubePlayerView;
    ImageButton btnPlayb;
    String vUrl;
    YouTubePlayer.OnInitializedListener onInitializedListener;
    AdView adView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_selected_bolly);


        film_Descriptiob =  (TextView) findViewById(R.id.decriptionb);
        btnPlayb = findViewById(R.id.fab);
        txtb = (TextView) findViewById(R.id.txtb);
        coverImageb = (ImageView) findViewById( R.id.coverImageB);

        youTubePlayerView = (YouTubePlayerView)findViewById(R.id.youtube_playerb);

        adView=findViewById(R.id.home_activity_banner_adview);
        MyApplication.setAddView(this,adView);


        database = FirebaseDatabase.getInstance();
        filmDataBl = database.getReference("ItemsBolly");
        filmDataBl.keepSynced(true);

        if (getIntent() !=null)
            filmId =  getIntent().getStringExtra("FilmId");
        if(!filmId.isEmpty() && filmId != null)
        {
            loadfilmdatabl(filmId);

        }

        btnPlayb.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onClick(View view) {
                youTubePlayerView.initialize(PlayerConfig.API_KEY,onInitializedListener);
                btnPlayb.setVisibility(View.INVISIBLE);
                coverImageb.setVisibility( View.INVISIBLE );


            }
        });


    }

    private void loadfilmdatabl(String filmId) {

        filmDataBl.child(filmId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                final MovieDataB loc = dataSnapshot.getValue(MovieDataB.class);

                vUrl = loc.getLink();

                onInitializedListener = new YouTubePlayer.OnInitializedListener() {
                    @Override
                    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                        youTubePlayer.loadVideo(loc.getLink());
                    }

                    @Override
                    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

                    }
                };

                txtb.setText(loc.getName());

                Picasso.with(getApplicationContext()).load("http://img.youtube.com/vi/"+loc.getLink()+"/0.jpg")
                        .into(coverImageb);



                film_Descriptiob.setText(loc.getDescription());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    public void finish() {
        super.finish();
        customType(SelectedBolly.this,"up-to-bottom");
    }
}
