package com.example.toshiba.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.widget.EditText;


/**
 * Created by toshiba on 13.1.2018.
 */

public class seznam_bezcu extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.seznam_bezcu);

        EditText seznam_bezcu;

        seznam_bezcu = (EditText) findViewById(R.id.seznamBezcu);
        seznam_bezcu.setTextSize(TypedValue.COMPLEX_UNIT_DIP,30);

        Intent intent = getIntent();
        final String zavodnici = intent.getStringExtra("zavodnici");
        final String zavodniciJmena1 = intent.getStringExtra("zavodniciJmena");


        String[] separated = zavodnici.split(",");
        String[] zavodniciJmenaSeparated = zavodniciJmena1.split(",");

        int counter = 0;
        for (String hodnota : separated) {
            seznam_bezcu.append(zavodniciJmenaSeparated[counter] + " : " + hodnota + "\r\n");
            counter++;
        }
    }
}
