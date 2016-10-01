package com.example.setup.cs450project3;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity {

    public static final String RESTORE_PREF = "restore_pref";
    private MainFragment mainFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainFragment = (MainFragment) getFragmentManager().findFragmentById(R.id.fragment_main);
    }

    public void loading() {
        System.out.println("this happened");
        View loading = findViewById(R.id.loading);
        loading.setVisibility(View.VISIBLE);
    }

    public void stopLoading() {
        View loading = findViewById(R.id.loading);
        loading.setVisibility(View.GONE);
    }

}
