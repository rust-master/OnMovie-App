package com.zaryab.omovie;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;

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
import com.zaryab.omovie.Model.MovieData;

import static maes.tech.intentanim.CustomIntent.customType;

public class SelectedMovie extends  YouTubeBaseActivity  {

    FirebaseDatabase database;
    DatabaseReference filmData;

    TextView loc_name,txt,film_Descriptio;

    ImageView coverImage ,  mapImage;
    private Toolbar mToolbar;
    String filmId = "";
    AdView adView;


   YouTubePlayerView youTubePlayerView;
    ImageButton btnPlay;
    String vUrl;
    YouTubePlayer.OnInitializedListener onInitializedListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected_movie);




        film_Descriptio =  (TextView) findViewById(R.id.decription);
        btnPlay =  findViewById(R.id.fab);
        txt = (TextView) findViewById(R.id.txtt);
        coverImage = (ImageView) findViewById( R.id.coverImage );

        youTubePlayerView = (YouTubePlayerView)findViewById(R.id.youtube_player);

        adView=findViewById(R.id.home_activity_banner_adview);
        MyApplication.setAddView(this,adView);


        database = FirebaseDatabase.getInstance();
        filmData = database.getReference("ItemsLolly");
        filmData.keepSynced(true);

        if (getIntent() !=null)
            filmId =  getIntent().getStringExtra("FilmId");
        if( filmId != null &&!filmId.isEmpty() )
        {
            loadfilmdata(filmId);

        }




        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                youTubePlayerView.initialize(PlayerConfig.API_KEY,onInitializedListener);
                btnPlay.setVisibility(View.INVISIBLE);
                coverImage.setVisibility( View.INVISIBLE );


            }
        });
    }

    private void loadfilmdata(String filmId) {

        filmData.child(filmId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                final MovieData loc = dataSnapshot.getValue(MovieData.class);

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

                txt.setText(loc.getName());

                Picasso.with(getApplicationContext()).load("http://img.youtube.com/vi/"+loc.getLink()+"/0.jpg")
                        .into(coverImage);



                film_Descriptio.setText(loc.getDescription());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    public void finish() {
        super.finish();
        customType(SelectedMovie.this,"up-to-bottom");
    }
}