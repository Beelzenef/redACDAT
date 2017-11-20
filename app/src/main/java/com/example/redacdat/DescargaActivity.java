package com.example.redacdat;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.jakewharton.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

import okhttp3.OkHttpClient;

public class DescargaActivity extends AppCompatActivity {

    EditText edT_URLImagen;
    ImageView imgV_ImagenDescargada;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_descarga);

        edT_URLImagen = (EditText) findViewById(R.id.edT_URLImagen);
        imgV_ImagenDescargada = (ImageView) findViewById(R.id.imgV_ImagenDescargada);
    }

    public void onClick_descargarImgen(View v) {
        String url = edT_URLImagen.getText().toString();

        switch (v.getId()) {
            case R.id.btn_DescargarImagen:
                descargaImagen(url);
                break;
            case R.id.btn_DescargarFichero:
                descargarImagenOKHTTP(url);
                break;
        }
    }

    private void descargaImagen(String url) {
        Picasso.with(this)
                .load(url)
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.placeholder_error)
                .into(imgV_ImagenDescargada);
    }

    private void descargarImagenOKHTTP(String url) {
        OkHttpClient client = new OkHttpClient();
        Picasso picasso = new Picasso.Builder(this)
                .downloader(new OkHttp3Downloader(client))
                .build();

        picasso.with(this)
                .load(url)
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.placeholder_error)
                .into(imgV_ImagenDescargada);
    }
}
