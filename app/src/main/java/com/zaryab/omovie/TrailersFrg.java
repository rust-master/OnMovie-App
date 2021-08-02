package com.zaryab.omovie;


import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.firebase.ui.database.FirebaseRecyclerAdapter;


import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.mancj.materialsearchbar.MaterialSearchBar;
import com.squareup.picasso.Picasso;
import com.zaryab.omovie.Interface.ItemClickListener;

import com.zaryab.omovie.Model.TrailersItem;

import com.zaryab.omovie.ViewHolder.TrailersItemViewHolder;


import java.util.ArrayList;
import java.util.List;

import static maes.tech.intentanim.CustomIntent.customType;


/**
 * A simple {@link Fragment} subclass.
 */
public class TrailersFrg extends Fragment implements SecActivity.onSearchSelectedInterface {


    private FirebaseDatabase database;
    private DatabaseReference Trailerstem;
    List<String> suggestList = new ArrayList<>();
    MaterialSearchBar materialSearchBar1;
    InterstitialAd mInterstitialAd;
    ProgressDialog progressDialog;
    boolean isAddLoaded=true;


//    private DatabaseReference Trailers;
    FirebaseRecyclerAdapter<TrailersItem, TrailersItemViewHolder> adapterT;
    FirebaseRecyclerAdapter<TrailersItem, TrailersItemViewHolder> searchAdapter;
    CardView cardTry;
    TextView TryTEXT;
    RecyclerView itemRecycler;
//    FirebaseRecyclerAdapter<Trailers, TrailersViewHolder> adapter;
    private View mMainView;

//    String trailerId = "01";
//    String trailerId1 = "02";
//    ImageButton btnD;
//    ImageView titemCover;
//
//    TextView Title,description;
//    ImageView trailerCover;
//    YouTubePlayerView youTubePlayerView;
//    FloatingActionButton btnPlayT;
//    YouTubePlayer.OnInitializedListener onInitializedListener;
//
//    private FragmentActivity myContext;
//    private YouTubePlayer YPlayer;
//    YouTubePlayerSupportFragment youTubePlayerFragment;



    public TrailersFrg() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mMainView =  inflater.inflate(R.layout.fragment_trailers, container, false);


        loadIntertcicialAdd();

        itemRecycler =(RecyclerView)mMainView.findViewById(R.id.trailerRecyler);
        itemRecycler.addItemDecoration(new LinePagerIndicatorDecoration());
        database = FirebaseDatabase.getInstance();
//        Trailers = database.getReference("Trailers");
        Trailerstem = database.getReference("TrailersItem");
        cardTry = mMainView.findViewById( R.id.cardTry );
        TryTEXT = mMainView.findViewById( R.id.textView );

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle("Refreshing");
        progressDialog.setMessage("Please wait");



        if (isOnline() == false) {
            TryTEXT.setVisibility( View.VISIBLE );
            cardTry.setVisibility( View.VISIBLE );

        }

        cardTry.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.show();

                TryTEXT.setVisibility( View.INVISIBLE );
                cardTry.setVisibility( View.INVISIBLE );


                if (isOnline()) {

                    TryTEXT.setVisibility( View.INVISIBLE );
                    cardTry.setVisibility( View.INVISIBLE );

                }

                if (isOnline() == false) {
                    TryTEXT.setVisibility( View.VISIBLE );
                    cardTry.setVisibility( View.VISIBLE );

                }

            }
        } );



//        Trailers.child(trailerId).addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                final Trailers trailers = dataSnapshot.getValue(Trailers.class);
//
//
//
//                youTubePlayerFragment = YouTubePlayerSupportFragment.newInstance();
//                FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
//                int commit = transaction.add( R.id.youtube_fragment, youTubePlayerFragment ).commit();
//
//                youTubePlayerFragment.initialize(PlayerConfig.API_KEY, new YouTubePlayer.OnInitializedListener() {
//
//                    @Override
//                    public void onInitializationSuccess(YouTubePlayer.Provider arg0, YouTubePlayer youTubePlayer, boolean b) {
//
//                        YPlayer = youTubePlayer;
//                        YPlayer.setFullscreen( false );
//                        YPlayer.loadVideo( trailers.getLink() );
//                        YPlayer.play();
//
//                    }
//
//                    @Override
//                    public void onInitializationFailure(YouTubePlayer.Provider arg0, YouTubeInitializationResult arg1) {
//                        // TODO Auto-generated method stub
//
//                    }
//                });
//
//                Title.setText(trailers.getName());
//
//
//
////                Picasso.with(getContext()).load(trailers.getImage())
////                        .into(trailerCover);
//            }
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });




        return  mMainView;
    }


    public boolean isOnline() {
        ConnectivityManager conMgr = (ConnectivityManager) getContext().getSystemService( Context.CONNECTIVITY_SERVICE );
        NetworkInfo netInfo = conMgr.getActiveNetworkInfo();

        if (netInfo == null || !netInfo.isConnected() || !netInfo.isAvailable()) {

//            Snackbar snackbar = Snackbar
//                    .make(relativeLayout, "No Internet connection!", Snackbar.LENGTH_LONG);
//            snackbar.show();
            // Toast.makeText(getApplicationContext(), "No Internet connection!", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    private void startSearch(CharSequence text) {
        searchAdapter = new FirebaseRecyclerAdapter<TrailersItem, TrailersItemViewHolder>(
                TrailersItem.class,
                R.layout.trailer_item,
                TrailersItemViewHolder.class,
                Trailerstem.orderByChild( "name" ).equalTo( text.toString() )


        ) {
            @Override
            protected void populateViewHolder(TrailersItemViewHolder trailersItemViewHolder, final TrailersItem trailersItem, int position) {



                Log.d( "searchtest", "populateViewHolder: " + position );
                trailersItemViewHolder.txtPlaceName.setText( trailersItem.getName() );
                final int p = position;
                Picasso.with( getContext() ).load( "http://img.youtube.com/vi/" + trailersItem.getImage() + "/0.jpg" )
                        .into( trailersItemViewHolder.imageView );
                final TrailersItem local = trailersItem;

                trailersItemViewHolder.setItemClickListener( new ItemClickListener() {
                    @Override
                    public void OnClick(View view, int position, boolean isLongClick) {


                        Intent detailIntent = new Intent( view.getContext(), SelectedTrailer.class );
                        detailIntent.putExtra( "FilmId", trailersItem.getMenuId());
                        startActivityForResult( detailIntent,98 );
                        customType( getContext(), "bottom-to-up" );

                    }
                } );
            }
        };
        itemRecycler.setAdapter( searchAdapter );
    }


    @Override
    public void onStart() {
        super.onStart();

        adapterT = new FirebaseRecyclerAdapter<TrailersItem, TrailersItemViewHolder>(
                TrailersItem.class,
                R.layout.trailer_item,
                TrailersItemViewHolder.class,
                Trailerstem

        ) {
            @Override
            protected void populateViewHolder(TrailersItemViewHolder viewHolder, TrailersItem model, int position) {


                if (isOnline()) {
                    progressDialog.dismiss();
                    TryTEXT.setVisibility( View.INVISIBLE );
                    cardTry.setVisibility( View.INVISIBLE );

                }

                viewHolder.txtPlaceName.setText(model.getName());
                Picasso.with(getContext()).load("http://img.youtube.com/vi/"+model.getImage()+"/0.jpg")
                        .into(viewHolder.imageView);
                final TrailersItem clickItem =  model;
                viewHolder.setItemClickListener(new ItemClickListener() {
                    @SuppressLint("RestrictedApi")
                    @Override
                    public void OnClick(View view, int position, boolean isLongClick) {



                        Intent detailIntent = new Intent(view.getContext(), SelectedTrailer.class);
                        detailIntent.putExtra("FilmId",adapterT.getRef(position).getKey());
                        startActivityForResult( detailIntent,98 );
                        customType(getContext(),"left-to-right");


                        if (isAddLoaded){
                            mInterstitialAd.show();
                        }else {
                            loadIntertcicialAdd();
                        }

                    }
                });
            }
        };
        itemRecycler.setAdapter(adapterT);
      //  layoutManager.findFirstVisibleItemPosition()
        itemRecycler.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false));




    }
    void loadIntertcicialAdd(){
        mInterstitialAd = new InterstitialAd(getContext());

        // set the ad unit ID
        mInterstitialAd.setAdUnitId(getString(R.string.interstitial_full_screen));
        AdRequest adRequest = new AdRequest.Builder().build();

        // Load ads into Interstitial Ads
        mInterstitialAd.loadAd(adRequest);
        mInterstitialAd.setAdListener(new AdListener() {
            public void onAdLoaded() {
                if (mInterstitialAd.isLoaded()) {
                    isAddLoaded=true;
                }
            }

            @Override
            public void onAdClosed() {
                new Handler().post( new Runnable() {
                    @Override
                    public void run() {
                        isAddLoaded=false;
                        loadIntertcicialAdd();
                    }
                });
            }
        });
    }

//
//    private void trailerdata(String key) {
//
//       // String id =
//
//
//        Trailers.child(key).addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                final Trailers trailers = dataSnapshot.getValue(Trailers.class);
//
//
//
//                youTubePlayerFragment = YouTubePlayerSupportFragment.newInstance();
//                FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
//                int commit = transaction.add( R.id.youtube_fragment, youTubePlayerFragment ).commit();
//
//                youTubePlayerFragment.initialize(PlayerConfig.API_KEY, new YouTubePlayer.OnInitializedListener() {
//
//                    @Override
//                    public void onInitializationSuccess(YouTubePlayer.Provider arg0, YouTubePlayer youTubePlayer, boolean b) {
//
//                        YPlayer = youTubePlayer;
//                        YPlayer.setFullscreen( false );
//                        YPlayer.loadVideo( trailers.getLink() );
//                        YPlayer.play();
//
//                    }
//
//                    @Override
//                    public void onInitializationFailure(YouTubePlayer.Provider arg0, YouTubeInitializationResult arg1) {
//                        // TODO Auto-generated method stub
//
//                    }
//                });
//
//                Title.setText(trailers.getName());
//
//
//
//
//
//            }
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
//
//
//
//    }




    @Override
    public void onSearchSelected() {
        Log.d( "search is here", String.valueOf( 10 ) );

        materialSearchBar1 = (MaterialSearchBar) mMainView.findViewById( R.id.searchBar4 );
        materialSearchBar1.setHint( "Enter your Movie" );
        materialSearchBar1.setLastSuggestions( suggestList );
        materialSearchBar1.setCardViewElevation( 10 );
        materialSearchBar1.addTextChangeListener( new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                List<String> suggest = new ArrayList<String>();
                for (String search : suggestList) {
                    if (search.toLowerCase().contains( materialSearchBar1.getText().toLowerCase() ))
                        suggest.add( search );
                }
                materialSearchBar1.setLastSuggestions( suggest );
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        } );

        materialSearchBar1.setOnSearchActionListener( new MaterialSearchBar.OnSearchActionListener() {
            @Override
            public void onSearchStateChanged(boolean enabled) {

                if (!enabled) {
                    itemRecycler.setAdapter( adapterT );
                    Log.d( "search is here", String.valueOf( 13 ) );
                }
            }

            @Override
            public void onSearchConfirmed(CharSequence text) {
                startSearch( text );
                Log.d( "search :", text.toString() );
            }

            @Override
            public void onButtonClicked(int buttonCode) {

            }
        } );
        if (materialSearchBar1.getVisibility() == View.VISIBLE) {
            Log.d( "search is here", String.valueOf( 11 ) );
            materialSearchBar1.setVisibility( View.GONE );
        }
        else {
            Log.d( "search is here", String.valueOf( 12 ) );
            materialSearchBar1.setVisibility( View.VISIBLE );
        }

    }


}
