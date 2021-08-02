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
import com.zaryab.omovie.Model.MovieDataAni;

import static maes.tech.intentanim.CustomIntent.customType;

public class SelectedAnimated extends YouTubeBaseActivity {


    FirebaseDatabase database;
    DatabaseReference filmDataAni;

    TextView loc_name,txtani,film_Descriptioani;
    ImageView coverImageani ,  mapImage;
    private Toolbar mToolbar;
    String filmId = "";
    AdView adView;

    YouTubePlayerView youTubePlayerView;
    ImageButton btnPlayani;
    String vUrl;
    YouTubePlayer.OnInitializedListener onInitializedListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_selected_animated );



        film_Descriptioani =  (TextView) findViewById(R.id.decriptionani);
        btnPlayani = findViewById(R.id.fab);
        txtani = (TextView) findViewById(R.id.txtani);
        coverImageani = (ImageView) findViewById( R.id.coverImageAni);


        youTubePlayerView = (YouTubePlayerView)findViewById(R.id.youtube_playerani);

        adView=findViewById(R.id.home_activity_banner_adview);
        MyApplication.setAddView(this,adView);



        database = FirebaseDatabase.getInstance();
        filmDataAni = database.getReference("ItemsAnimated");
        filmDataAni.keepSynced(true);

        if (getIntent() !=null)
            filmId =  getIntent().getStringExtra("FilmId");
        if(!filmId.isEmpty() && filmId != null)
        {
            loadfilmdataani(filmId);

        }

        btnPlayani.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onClick(View view) {
                youTubePlayerView.initialize(PlayerConfig.API_KEY,onInitializedListener);
                btnPlayani.setVisibility(View.INVISIBLE);
                coverImageani.setVisibility( View.INVISIBLE );


            }
        });

    }

    private void loadfilmdataani(String filmId) {

        filmDataAni.child(filmId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                final MovieDataAni loc = dataSnapshot.getValue(MovieDataAni.class);

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

                txtani.setText(loc.getName());

                Picasso.with(getApplicationContext()).load("http://img.youtube.com/vi/"+loc.getLink()+"/0.jpg")
                        .into(coverImageani);



                film_Descriptioani.setText(loc.getDescription());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }
    @Override
    public void finish() {
        super.finish();
        customType(SelectedAnimated.this,"up-to-bottom");
    }
}
