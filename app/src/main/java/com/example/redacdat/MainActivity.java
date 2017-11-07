package com.example.redacdat;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onClick_lanzarExs(View v)
    {
        Intent unIntent = null;

        switch (v.getId())
        {
            case R.id.btn_ConexionHTTP:
                unIntent = new Intent(MainActivity.this, HTTPActivity.class);
                break;
            case R.id.btn_ConexionAsincrona:
                unIntent = new Intent(MainActivity.this, AsincronaActivity.class);
                break;
            case R.id.btn_ConexionAAHC:
                unIntent = new Intent(MainActivity.this, AAHCActivity.class);
                break;
            case R.id.btn_ConexionVolley:
                unIntent = new Intent(MainActivity.this, VolleyActivity.class);
                break;
            case R.id.btn_Descarga:
                unIntent = new Intent(MainActivity.this, DescargaActivity.class);
                break;
            case R.id.btn_SubirFichero:
                unIntent = new Intent(MainActivity.this, SubirFicheroActivity.class);
                break;
        }

        startActivity(unIntent);
    }
}
