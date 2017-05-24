package com.example.neo.connect_four;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button startButton = (Button) findViewById(R.id.start);
        startButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent myIntent = new Intent(view.getContext(), Game.class);
                startActivityForResult(myIntent, 0);
            }
        });
//        Button endButton = (Button)findViewById(R.id.end);
//        endButton.setOnClickListener(new View.OnClickListener(){
//            public void onClick(View view)
//            {
//                finish();
//            }
//        });
    }


}