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
import com.zaryab.omovie.Model.Films;
import com.zaryab.omovie.ViewHolder.MovieViewHolder;

import java.util.ArrayList;
import java.util.List;

import static maes.tech.intentanim.CustomIntent.customType;


public class LollyWood extends Fragment implements SecActivity.onSearchSelectedInterface {

    private RecyclerView mLollyList;
    InterstitialAd mInterstitialAd;
    boolean isAddLoaded=true;



//    private ViewPager sliderPager;

    private FirebaseDatabase database;
    private DatabaseReference Movies;
    RecyclerView.LayoutManager layoutManager;

    FirebaseRecyclerAdapter<Films, MovieViewHolder> adapter;
    FirebaseRecyclerAdapter<Films, MovieViewHolder> searchAdapter;
    List<String> suggestList = new ArrayList<>();

    private View mMainView;
    MaterialSearchBar materialSearchBar;


    public LollyWood() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mMainView = inflater.inflate( R.layout.fragment_lolly_wood, container, false );


        database = FirebaseDatabase.getInstance();
        Movies = database.getReference( "CategoryLolly" );

        mLollyList = (RecyclerView) mMainView.findViewById( R.id.lollywood );
        mLollyList.setHasFixedSize( true );
        layoutManager = new LinearLayoutManager( getContext() );
        LinearLayoutManager mLayoutManager = new LinearLayoutManager( getActivity() );
        mLayoutManager.setReverseLayout( true );
        mLayoutManager.setStackFromEnd( true );
        mLollyList.setLayoutManager( mLayoutManager );


        loadIntertcicialAdd();


//        sliderPager = (ViewPager) mMainView.findViewById(R.id.slider_pager);
//
//        lstSlides = new ArrayList<>();
//        lstSlides.add(new Slide(R.drawable.slide1,"Slide Title \nmore text here"));
//        lstSlides.add(new Slide(R.drawable.slide2,"Slide Title \nmore text here"));
//        lstSlides.add(new Slide(R.drawable.slide1,"Slide Title \nmore text here"));
//        lstSlides.add(new Slide(R.drawable.slide2,"Slide Title \nmore text here"));
//
//        SliderPagerAdapter adapter = new SliderPagerAdapter(getContext(),lstSlides);
//
//        sliderPager.setAdapter(adapter);


        return mMainView;
    }


    private void startSearch(CharSequence text) {
        searchAdapter = new FirebaseRecyclerAdapter<Films, MovieViewHolder>(
                Films.class,
                R.layout.films_item,
                MovieViewHolder.class,
                Movies.orderByChild( "name" ).equalTo( text.toString() )


        ) {
            @Override
            protected void populateViewHolder(MovieViewHolder viewHolder, final Films model, int position) {

                Log.d( "searchtest", "populateViewHolder: " + position );
                viewHolder.txtPlaceName.setText( model.getName() );
                final int p = position;
                Picasso.with( getContext() ).load( "http://img.youtube.com/vi/" + model.getImage() + "/0.jpg" )
                        .into( viewHolder.imageView );
                final Films local = model;

                viewHolder.setItemClickListener( new ItemClickListener() {
                    @Override
                    public void OnClick(View view, int position, boolean isLongClick) {


                        Intent detailIntent = new Intent( view.getContext(), SelectedMovie.class );
                        detailIntent.putExtra( "FilmId", model.getMenuId() );
                        startActivityForResult(detailIntent,98);
                        customType( getContext(), "bottom-to-up" );

                    }
                } );
            }
        };
        mLollyList.setAdapter( searchAdapter );
    }


    @Override
    public void onStart() {
        super.onStart();

        adapter = new FirebaseRecyclerAdapter<Films, MovieViewHolder>(
                Films.class,
                R.layout.films_item,
                MovieViewHolder.class,
                Movies

        ) {
            @Override
            protected void populateViewHolder(MovieViewHolder viewHolder, Films model, int position) {
                viewHolder.txtPlaceName.setText( model.getName() );
                Picasso.with( getContext() ).load( "http://img.youtube.com/vi/" + model.getImage() + "/0.jpg" )
                        .into( viewHolder.imageView );
                final Films clickItem = model;
                viewHolder.setItemClickListener( new ItemClickListener() {
                    @Override
                    public void OnClick(View view, int position, boolean isLongClick) {
                        // Placeid

                        Intent detailIntent = new Intent( view.getContext(), SelectedMovie.class );
                        detailIntent.putExtra( "FilmId", adapter.getRef( position ).getKey() );
                        startActivityForResult(detailIntent,98);
                        customType( getContext(), "bottom-to-up" );

                        if (isAddLoaded){
                            mInterstitialAd.show();
                        }else {
                            loadIntertcicialAdd();
                        }
                    }
                } );
            }
        };
        mLollyList.setAdapter( adapter );


    }

    @Override
    public void onSearchSelected() {
        Log.d( "search is here" , String.valueOf(10 ) );

        materialSearchBar = (MaterialSearchBar) mMainView.findViewById( R.id.searchBar );
        materialSearchBar.setHint( "Enter your Movie" );
        materialSearchBar.setLastSuggestions( suggestList );
        materialSearchBar.setCardViewElevation( 10 );
        materialSearchBar.addTextChangeListener( new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                List<String> suggest = new ArrayList<String>();
                for (String search : suggestList) {
                    if (search.toLowerCase().contains( materialSearchBar.getText().toLowerCase() ))
                        suggest.add( search );
                }
                materialSearchBar.setLastSuggestions( suggest );
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        } );

        materialSearchBar.setOnSearchActionListener( new MaterialSearchBar.OnSearchActionListener() {
            @Override
            public void onSearchStateChanged(boolean enabled) {

                if (!enabled) {
                    mLollyList.setAdapter( adapter );
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
        if (materialSearchBar.getVisibility() == View.VISIBLE)
            materialSearchBar.setVisibility( View.GONE );
        else
            materialSearchBar.setVisibility( View.VISIBLE );


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
