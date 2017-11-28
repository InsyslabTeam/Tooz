package com.insyslab.tooz.ui.activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import com.insyslab.tooz.R;

public class DashboardActivity extends BaseActivity {

    private static final String TAG = "Dashboard ==> ";

    private Toolbar toolbar;
    private ImageView ivToolbarSettings;
    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        initView();
        setUpToolbar();
        setUpActions();
    }

    private void setUpToolbar() {
        setSupportActionBar(toolbar);
    }

    private void setUpActions() {
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        ivToolbarSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    private void initView() {
        toolbar = findViewById(R.id.toolbar);
        ivToolbarSettings = findViewById(R.id.dt_settings);
        fab = findViewById(R.id.fab);
    }
}
