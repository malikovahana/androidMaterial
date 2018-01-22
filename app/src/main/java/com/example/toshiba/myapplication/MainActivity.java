package com.example.toshiba.myapplication;

import android.content.Context;//pro operace na úrovni aplikace : spousteni aktivit, vysilani prijimani intent
import android.content.Intent;//
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.app.Activity;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    TextView stopky;
    TextView jmenoBezce;
    TextView nazevJmenoBezce;
    Button NastartujStopky;
    Button StopniStopky;
    Button UkazatGraf;
    Button ukazSeznam;
    ImageView imageView;

    int counter = 0;
    Timer timer;
    int zavodnik = 0;
    String zavodnici = "";
    String zavodniciJmena = "";
    boolean first = true;
    final Context context = this;
    Activity tatoaktivita = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        stopky = (TextView) findViewById(R.id.stopky_display);
        jmenoBezce = (TextView) findViewById(R.id.jmenoBezce);
        NastartujStopky = (Button) findViewById(R.id.buttonStart);
        StopniStopky = (Button) findViewById(R.id.button2Stop);
        UkazatGraf = (Button) findViewById(R.id.buttonUkazatGraf);
        UkazatGraf.setVisibility(View.INVISIBLE);
        nazevJmenoBezce = (TextView) findViewById(R.id.nazevJmenoBezce);
        ukazSeznam = (Button) findViewById(R.id.ukazSeznam);
        imageView =  (ImageView) findViewById(R.id.imageView);
        imageView.setScaleType(ImageView.ScaleType.CENTER);
        imageView.setAdjustViewBounds(true);


        UkazatGraf.setOnClickListener(new View.OnClickListener() {//kliknu na ukazat graf
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, grafActivity.class);//vytvori obsah toho grafu

                intent.putExtra("zavodnici", zavodnici); //Optional parameters//predam intentu
                intent.putExtra("zavodniciJmena", zavodniciJmena);//zavodnici
                startActivity(intent);//aktivuje grafActivity class
            }
        });

        ukazSeznam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, seznam_bezcu.class);
                intent.putExtra("zavodnici", zavodnici); //Optional parameters
                intent.putExtra("zavodniciJmena", zavodniciJmena);
                startActivity(intent);
            }
        });

        StopniStopky.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "STOP", Toast.LENGTH_LONG).show();
                NastartujStopky.setEnabled(true);
                StopniStopky.setEnabled(false);
                if (first) {//poprve, parzer bez carky, pokud podruhe, tak to oddeluje carkama
                    zavodnici += String.valueOf(counter);
                    zavodniciJmena +=  jmenoBezce.getText();
                    UkazatGraf.setVisibility(View.VISIBLE);
                }
                    else {
                    zavodnici += "," + String.valueOf(counter);
                    zavodniciJmena +=   "," + jmenoBezce.getText();
                }
                    first = false;
                if (v == StopniStopky) {
                    stopky.setText("Stopky zastaveny! " + String.valueOf(counter));
                    jmenoBezce.setText("");//vynulujeme jmeno bezce
                    if (timer != null) {
                        timer.cancel();
                        timer = null;
                    }
                    counter = 0;
                }
            }
        });

        NastartujStopky.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v == NastartujStopky ) {

                    if(jmenoBezce.getText().toString().equals("") )
                    {
                        MessageBox("Nejprve vyplň jméno běžce");
                        return;
                    }
                    isKeyBoardShow(tatoaktivita);//zmizeni klavesnice, v emulatoru nefunguje

                    timer = new Timer();
                    Toast.makeText(getApplicationContext(), "START - \r\n Stopky spuštěny!", Toast.LENGTH_LONG).show();
                    StopniStopky.setEnabled(true);
                    NastartujStopky.setEnabled(false);
                    timer.scheduleAtFixedRate(new TimerTask() {
                        @Override
                        public void run() {
                            counter++;//counter se pocita po 1s = 1000 milisekund
                            mHandler.obtainMessage(1).sendToTarget();//na frotendu mi zobrazuje pocitani
                        }
                    }, 1000, 1000);
                }
            }
        });
    }

    public Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            stopky.setText(String.valueOf(counter)); //this is the textview
        }
    };

    public void MessageBox(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    public void isKeyBoardShow(Activity activity) {             //skrytí klavesnice
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        if (imm.isActive()) {
            imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0); // hide
        } else {
            imm.toggleSoftInput(0, InputMethodManager.HIDE_IMPLICIT_ONLY); // show
        }
    }

}
