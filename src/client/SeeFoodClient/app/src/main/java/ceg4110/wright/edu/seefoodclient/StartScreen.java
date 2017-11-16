package ceg4110.wright.edu.seefoodclient;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class StartScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_screen);

//        try {
//            wait(2000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        // Send an initialization message to the server


    }

    public void goToMain(View v){
        Intent i = new Intent(getApplicationContext(),MainActivity.class);
        startActivity(i);
    }
}
