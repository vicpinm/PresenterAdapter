package com.vicpin.presenteradapter.sample.view.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.vicpin.presenteradapter.sample.R;
import com.vicpin.presenteradapter.sample.view.fragment.MainFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_main);
        getSupportFragmentManager().beginTransaction().replace(android.R.id.content, new MainFragment()).commit();
    }
}
