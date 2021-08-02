package com.zaryab.omovie;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;


public class PrivacyPolicyActivity extends AppCompatActivity {

    PreferencesManager prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy_policy);

        prefs = new PreferencesManager(this);

        if (prefs.isPrivacyAccepted()) {
            findViewById(R.id.layut_accept_decline).setVisibility( View.GONE);
        }

        findViewById(R.id.privacy_accept).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prefs.setIsPrivacyAccepted(true);
                startActivity(new Intent(PrivacyPolicyActivity.this, SecActivity.class));
                finish();
            }
        });
        findViewById(R.id.privacy_decline).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}
