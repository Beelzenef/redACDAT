package com.example.redacdat;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.jakewharton.picasso.OkHttp3Downloader;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.FileAsyncHttpResponseHandler;
import com.squareup.picasso.Picasso;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import cz.msebera.android.httpclient.Header;
import okhttp3.OkHttpClient;

public class DescargaActivity extends AppCompatActivity {

    EditText edT_URLImagen;
    ImageView imgV_ImagenDescargada;

    ProgressDialog progreso;

    EscribirMemorias mem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_descarga);

        mem = new EscribirMemorias(this);

        edT_URLImagen = (EditText) findViewById(R.id.edT_URLImagen);
        imgV_ImagenDescargada = (ImageView) findViewById(R.id.imgV_ImagenDescargada);
    }

    public void onClick_descargarImgen(View v) {
        String url = edT_URLImagen.getText().toString();

        switch (v.getId()) {
            case R.id.btn_DescargarImagen:
                descargaImagen(url);
                break;
            case R.id.btn_DescargarPicasso:
                descargarImagenOKHTTP(url);
                break;
            case R.id.btn_DescargarFichero:
                descargarFichero(url);
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

    private void descargarFichero(String url) {

        // FileAsyncHttpResponseHandler

        final File ficheroRenombrado = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "ficheroDescargado.png");

        final AsyncHttpClient client = new AsyncHttpClient();
        client.get(url, new FileAsyncHttpResponseHandler(this) {
            @Override
            public void onStart() {
                progreso = new ProgressDialog(DescargaActivity.this);
                progreso.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progreso.setMessage("Buscando imagen...");
                progreso.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialogInterface) {
                        client.cancelRequests(DescargaActivity.this, true);
                    }
                });
                progreso.show();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, File file) {
                progreso.dismiss();
                Toast.makeText(DescargaActivity.this, "Fallo al buscar imagen :(", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, File response) {
                progreso.dismiss();
                Toast.makeText(DescargaActivity.this, "Imagen descargada", Toast.LENGTH_SHORT).show();

                // WIP guardar fichero response
            }
        });

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
