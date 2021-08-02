package com.zaryab.omovie;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RelativeLayout;


import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;
import com.google.android.material.tabs.TabLayout;

import static maes.tech.intentanim.CustomIntent.customType;

public class SecActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    RelativeLayout relativeLayout;

    private ViewPager mViewPager;
    private SectionPagerAdapter mSectionPagerAdapter;
    private TabLayout mTabLayout;

    //ADD
    InterstitialAd mInterstitialAd;
    private RewardedVideoAd mRewardedVideoAd;
    boolean isAddLoaded=true;
    AdView adView; //banner add


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_sec );

        //ADD
        adView=findViewById(R.id.home_activity_banner_adview);
        MyApplication.setAddView(this,adView);
        loadIntertcicialAdd();
        mRewardedVideoAd = MobileAds.getRewardedVideoAdInstance(this);
        mRewardedVideoAd.setRewardedVideoAdListener(rewardedVideoAdListener);
        loadRewardedVideoAd();


        relativeLayout = (RelativeLayout) findViewById(R.id.rele);






        mToolbar = (Toolbar) findViewById( R.id.main_page_toolbar );
        setSupportActionBar( mToolbar );
        getSupportActionBar().setTitle( "OMovies" );


        mViewPager = (ViewPager) findViewById( R.id.main_taber );

        mSectionPagerAdapter = new SectionPagerAdapter( getSupportFragmentManager() );
        mViewPager.setAdapter( mSectionPagerAdapter );

        mTabLayout = (TabLayout) findViewById( R.id.main_tabs );
        mTabLayout.setupWithViewPager( mViewPager );




    }

//
//    public boolean isOnline() {
//        ConnectivityManager conMgr = (ConnectivityManager) getApplicationContext().getSystemService( Context.CONNECTIVITY_SERVICE );
//        NetworkInfo netInfo = conMgr.getActiveNetworkInfo();
//
//        if (netInfo == null || !netInfo.isConnected() || !netInfo.isAvailable()) {
//
////            Snackbar snackbar = Snackbar
////                    .make(relativeLayout, "No Internet connection!", Snackbar.LENGTH_LONG);
////            snackbar.show();
//            // Toast.makeText(getApplicationContext(), "No Internet connection!", Toast.LENGTH_LONG).show();
//            return false;
//        }
//        return true;
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu( menu );

        getMenuInflater().inflate( R.menu.main_menu, menu );

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected( item );

        if (item.getItemId() == R.id.aboutid) {



            Intent intent = new Intent( SecActivity.this, PrivacyPolicyActivity.class );
            startActivity( intent );
            customType( SecActivity.this, "bottom-to-up" );
            return true;
        }
        if (item.getItemId() == R.id.search) {

            mSectionPagerAdapter.showSearchBar( mViewPager.getCurrentItem() );
            return true;
        }
        else if (item.getItemId() == R.id.rateus) {
            Uri uri = Uri.parse("market://details?id=com.nextlogic.omovie");
            Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
            // To count with Play market backstack, After pressing back button,
            // to taken back to our application, we need to add following flags to intent.
            goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                    Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                    Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
            try {
                startActivity(goToMarket);
            } catch (ActivityNotFoundException e) {
                startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse("https://play.google.com/store/apps/details?id=com.nextlogic.omovie")));
            }
            return true;
        }
        else if (item.getItemId()== R.id.moreapps) {

            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://play.google.com/store/apps/developer?id=JOJO+Kids")));
            return true;
        }
        else if (item.getItemId()== R.id.moregames) {

            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://play.google.com/store/apps/developer?id=Draxters")));
            return true;
        }


        return false;
    }

    public interface onSearchSelectedInterface {
        public void onSearchSelected();
    }


    //add
    void loadIntertcicialAdd(){
        mInterstitialAd = new InterstitialAd(this);

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

    private RewardedVideoAdListener rewardedVideoAdListener=new RewardedVideoAdListener() {
        @Override
        public void onRewarded(RewardItem reward) {
            //Toast.makeText(QuizActivity.this, "onRewarded! currency: " + reward.getType() + "  amount: " + reward.getAmount(), Toast.LENGTH_SHORT).show();

            // Reward the user.
        }

        @Override
        public void onRewardedVideoAdLeftApplication() {
            //Toast.makeText(QuizActivity.this, "onRewardedVideoAdLeftApplication",Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onRewardedVideoAdClosed() {
            loadRewardedVideoAd();
            //Toast.makeText(QuizActivity.this, "onRewardedVideoAdClosed", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onRewardedVideoAdFailedToLoad(int errorCode) {
            //Toast.makeText(QuizActivity.this, "onRewardedVideoAdFailedToLoad", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onRewardedVideoAdLoaded() {
            //Toast.makeText(QuizActivity.this, "onRewardedVideoAdLoaded", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onRewardedVideoAdOpened() {
            //Toast.makeText(QuizActivity.this, "onRewardedVideoAdOpened", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onRewardedVideoStarted() {
            //Toast.makeText(QuizActivity.this, "onRewardedVideoStarted", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onRewardedVideoCompleted() {
            //Toast.makeText(QuizActivity.this, "onRewardedVideoCompleted", Toast.LENGTH_SHORT).show();
        }
    };

    private void loadRewardedVideoAd() {
        mRewardedVideoAd.loadAd(getString(R.string.reward_video_ad_id),
                new AdRequest.Builder().build());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        Log.d( "addtest", "onActivityResult: "+resultCode );
        super.onActivityResult( requestCode, resultCode, data );
            if (mRewardedVideoAd.isLoaded()){
                mRewardedVideoAd.show();
            }else if (isAddLoaded){
                mInterstitialAd.show();
                loadRewardedVideoAd();
            }else {
                loadRewardedVideoAd();
                loadIntertcicialAdd();
            }

    }
}
