package com.zaryab.omovie;


import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mancj.materialsearchbar.MaterialSearchBar;
import com.squareup.picasso.Picasso;
import com.zaryab.omovie.Interface.ItemClickListener;
import com.zaryab.omovie.Model.Filmsbl;
import com.zaryab.omovie.ViewHolder.BMovieViewHolder;

import java.util.ArrayList;
import java.util.List;

import static maes.tech.intentanim.CustomIntent.customType;


/**
 * A simple {@link Fragment} subclass.
 */
public class BollyWood extends Fragment  implements SecActivity.onSearchSelectedInterface {

    private RecyclerView mBollyList;
    private FirebaseDatabase database;
    private DatabaseReference BWMovies;
    RecyclerView.LayoutManager layoutManager;

    FirebaseRecyclerAdapter<Filmsbl, BMovieViewHolder> adapter;
    FirebaseRecyclerAdapter<Filmsbl, BMovieViewHolder> searchAdapter;
    List<String> suggestList = new ArrayList<>();

    private View mMainView;
    MaterialSearchBar materialSearchBar1;

    InterstitialAd mInterstitialAd;
    boolean isAddLoaded=true;


    public BollyWood() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mMainView =  inflater.inflate(R.layout.fragment_bolly_wood, container, false);
        database = FirebaseDatabase.getInstance();
        BWMovies = database.getReference("CategoryBolly");
        mBollyList =(RecyclerView)mMainView.findViewById(R.id.bollywood);
        mBollyList.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext());
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);
        mBollyList.setLayoutManager(mLayoutManager);
        loadIntertcicialAdd();



        return mMainView;
    }
    private void startSearch(CharSequence text) {
        searchAdapter = new FirebaseRecyclerAdapter<Filmsbl, BMovieViewHolder>(
                Filmsbl.class,
                R.layout.films_itemb,
                BMovieViewHolder.class,
                BWMovies.orderByChild( "name" ).equalTo( text.toString() )


        ) {
            @Override
            protected void populateViewHolder(BMovieViewHolder bMovieViewHolder, final Filmsbl filmsbl, int position) {



                Log.d( "searchtest", "populateViewHolder: " + position );
                bMovieViewHolder.txtPlaceName.setText( filmsbl.getName() );
                final int p = position;
                Picasso.with( getContext() ).load( "http://img.youtube.com/vi/" + filmsbl.getImage() + "/0.jpg" )
                        .into( bMovieViewHolder.imageView );
                final Filmsbl local = filmsbl;

                bMovieViewHolder.setItemClickListener( new ItemClickListener() {
                    @Override
                    public void OnClick(View view, int position, boolean isLongClick) {


                        Intent detailIntent = new Intent( view.getContext(), SelectedBolly.class );
                        detailIntent.putExtra( "FilmId", filmsbl.getMenuId());
                        startActivityForResult(detailIntent,98);
                        customType( getContext(), "bottom-to-up" );

                    }
                } );
            }
        };
        mBollyList.setAdapter( searchAdapter );
    }

    @Override
    public void onStart() {
        super.onStart();

        adapter = new FirebaseRecyclerAdapter<Filmsbl, BMovieViewHolder>(
                Filmsbl.class,
                R.layout.films_itemb,
                BMovieViewHolder.class,
                BWMovies

        ) {
            @Override
            protected void populateViewHolder(BMovieViewHolder viewHolder, Filmsbl model, int position) {
                viewHolder.txtPlaceName.setText(model.getName());
                Picasso.with(getContext()).load("http://img.youtube.com/vi/"+model.getImage()+"/0.jpg")
                        .into(viewHolder.imageView);
                final Filmsbl clickItem =  model;
                viewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void OnClick(View view, int position, boolean isLongClick) {
                        // Placeid

                        Intent detailIntent = new Intent(view.getContext(), SelectedBolly.class);
                        detailIntent.putExtra("FilmId",adapter.getRef(position).getKey());
                        startActivityForResult(detailIntent,98);
                        customType(getContext(),"bottom-to-up");


                        if (isAddLoaded){
                            mInterstitialAd.show();
                        }else {
                            loadIntertcicialAdd();
                        }

                    }
                });
            }
        };
        mBollyList.setAdapter(adapter);


    }


    @Override
    public void onSearchSelected() {
        Log.d( "search is here", String.valueOf( 10 ) );

        materialSearchBar1 = (MaterialSearchBar) mMainView.findViewById( R.id.searchBar1 );
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
                    mBollyList.setAdapter( adapter );
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

}
