package org.wkfg.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import org.wkfg.R;

public class SearchActivity extends AppCompatActivity {
    public static String queryString;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Intent intent =getIntent();
        queryString = intent.getStringExtra("query_string");
    }
}
