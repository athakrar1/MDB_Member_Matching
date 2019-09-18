package com.example.project1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.project1.R;

public class MainActivity extends AppCompatActivity {

    Button start_button;
    public static final int UPDATE_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        start_button = findViewById(R.id.start_button);
        start_button.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v){
                switch(v.getId()){
                    case R.id.start_button:
                        Intent intent = new Intent(MainActivity.this, gameScreen.class);
                        startActivityForResult(intent, UPDATE_CODE);
                }

            }
        });
    }

}
