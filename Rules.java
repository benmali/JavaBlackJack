package com.example.blackjack;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

public class Rules extends AppCompatActivity {
    ImageButton btnClose;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //activity made as pop up using styles.xml
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rules);
        btnClose = findViewById(R.id.btnClose);
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm .heightPixels;
        getWindow().setLayout((int) (width * 0.8), (int) (height*0.7));
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
