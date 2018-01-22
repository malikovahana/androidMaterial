package com.example.toshiba.myapplication;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.app.Activity;
import android.widget.Toast;

import com.jjoe64.graphview.GraphView;//knihovny ke grafu
import com.jjoe64.graphview.ValueDependentColor;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Date;


public class grafActivity extends AppCompatActivity {

    TextView zavodniciJmena;
    Button ulozitGraf;
    Button nacistGraf;
    Activity tatoaktivita;
    Context thiscontext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {//pri vytvoreni to vezme cisla, a jmena bezcudn
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nas_graf);

        zavodniciJmena = (TextView) findViewById(R.id.zavodniciJmena);
        ulozitGraf = (Button) findViewById(R.id.buttonUlozitGraf);
        nacistGraf = (Button) findViewById(R.id.buttonNacistGraf);
        int maxZavodnik = 0;
        thiscontext = getWindow().getDecorView().findViewById(android.R.id.content).getContext();//kontext okna grafu
        tatoaktivita = this;//ukazatel na aktualni objekt
        final GraphView graph = (GraphView) findViewById(R.id.graph);//final rika, ze ta promenna se da nastavit jenom jednou
        Intent intent = getIntent();//pres instanci intentu si ziskam z hlavni aktivity zavodniky , casy a jmena
        final String zavodnici = intent.getStringExtra("zavodnici");
        final String zavodniciJmena1 = intent.getStringExtra("zavodniciJmena");

        Toast.makeText(getApplicationContext(), "Grafický přehled:\r\n"+ zavodniciJmena1, Toast.LENGTH_LONG).show();//neni potreba
        zavodniciJmena.setText(zavodniciJmena1);
        //Log.d("myTag", zavodnici);
        graph.getViewport().setYAxisBoundsManual(true);//osy maji byt vykreslovany od 0

        graph.getViewport().setMinY(0);

        //MessageBox(tatoaktivita.getApplicationContext().getFilesDir().getAbsolutePath());

        nacistGraf.setOnClickListener(new View.OnClickListener() {
                                          @Override
                                          public void onClick(View v) {


                                              Toast.makeText(getApplicationContext(), "Vyberte, který otevřít", Toast.LENGTH_LONG).show();

                                              new FileChooser(tatoaktivita).setFileListener(new FileChooser.FileSelectedListener() {
                                                  @Override
                                                  public void fileSelected(final File file) {
                                                      // do something with the file

                                                       int pocitadelko = 0;
                                                      try {
                                                          InputStream inputStream = thiscontext.openFileInput(file.getName());

                                                          Boolean poprve = true;

                                                          if ( inputStream != null ) {
                                                              InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                                                              BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                                                              String receiveString = "";
                                                              int maxZavodnik = 0;
                                                              graph.removeAllSeries();//odejme stary graf

                                                              while ( (receiveString = bufferedReader.readLine()) != null ) {
                                                                  if(poprve){

                                                                      String[] separated = receiveString.split(",");
                                                                      BarGraphSeries<DataPoint> series = new BarGraphSeries<>();
                                                                      for (String hodnota : separated) {
                                                                          DataPoint dataPoint = new DataPoint(pocitadelko, Integer.parseInt(hodnota));
                                                                         // DataPoint dataPoint = new DataPoint(2, 7);
                                                                          if(poprve){
                                                                              maxZavodnik =  Integer.parseInt(hodnota);
                                                                              poprve= false;
                                                                          }

                                                                          if (maxZavodnik <  Integer.parseInt(hodnota))//hledam maximum pro navoleni osy grafu y
                                                                              maxZavodnik =  Integer.parseInt(hodnota);

                                                                          pocitadelko++;
                                                                          series.appendData(dataPoint, true, 30, false);
                                                                      }

                                                                      graph.getViewport().setYAxisBoundsManual(true);
                                                                      graph.clearAnimation();
                                                                      graph.getViewport().setMaxY(maxZavodnik+1);//max zavodnik, aby na nim bylo bile misto

                                                                      graph.addSeries(series);

                                                                      graph.getViewport().setMinY(0);
                                                                      graph.getViewport().setXAxisBoundsManual(true);
                                                                      graph.getViewport().setMaxX(10);//max zavodniku 10
                                                                      MessageBox("Časy:" + receiveString);


                                                                  }else
                                                                  {
                                                                      MessageBox("Závodnici jmena:" + receiveString);
                                                                      zavodniciJmena.setText(receiveString);
                                                                  }
                                                              }

                                                              inputStream.close();//uzavre sream

                                                          }
                                                      }
                                                      catch (FileNotFoundException e) {
                                                          MessageBox("login activity File not found: " + e.toString());
                                                      } catch (IOException e) {
                                                          MessageBox("login activity Can not read file: " + e.toString());
                                                      }

                                                  }
                                              }).showDialog();//zobrazi seznam se soubory

                                          }
                                      });

        ulozitGraf.setOnClickListener(new View.OnClickListener() {
                                          @Override
                                          public void onClick(View v) {
                                              Toast.makeText(getApplicationContext(), "Ukládám", Toast.LENGTH_LONG).show();
                                              final String ulozitText = zavodnici + "\r\n" + zavodniciJmena1;
                                              writeNameToFile(ulozitText, thiscontext);
                                              MessageBox(ulozitText);
                                          }
                                      });





        String[] separated = zavodnici.split(",");
        BarGraphSeries<DataPoint> series = new BarGraphSeries<>();
        int pocitadelko = 0;
        Boolean poprve = true;

        for (String hodnota : separated) {
            DataPoint dataPoint = new DataPoint(pocitadelko, Integer.parseInt(hodnota));
            if(poprve){
                maxZavodnik =  Integer.parseInt(hodnota);
                poprve= false;
            }

            if (maxZavodnik <  Integer.parseInt(hodnota))
                maxZavodnik =  Integer.parseInt(hodnota);


            pocitadelko++;
            series.appendData(dataPoint, true, 30, false);
        }
        graph.getViewport().setMinY(0);
        graph.getViewport().setMaxY(maxZavodnik+1);
        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setMaxX(10);

        series.setValueDependentColor(new ValueDependentColor<DataPoint>() {
            @Override
            public int get(DataPoint data) {
                return Color.rgb((int) data.getX() * 255 / 4, (int) Math.abs(data.getY() * 255 / 6), 100);
            }
        });



        series.setSpacing(15);//mezera mezi grafama

// draw values on top
        series.setDrawValuesOnTop(true);
        series.setValuesOnTopColor(Color.GREEN);
        graph.clearAnimation();
        graph.addSeries(series);
        graph.getViewport().setMinY(0);


    }

    private void writeNameToFile(String data,Context context) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
            String currentDateandTime = sdf.format(new Date());

            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput("graf_"+currentDateandTime+".txt", Context.MODE_PRIVATE));
            outputStreamWriter.write(data);
            outputStreamWriter.close();
        }
        catch (IOException e) {
            MessageBox("Exception File write failed: " + e.toString());
        }
    }


    public void MessageBox(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }


}

