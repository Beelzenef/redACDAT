package com.example.redacdat;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.TextView;

import com.example.redacdat.red.RestClient;
import com.loopj.android.http.TextHttpResponseHandler;

import cz.msebera.android.httpclient.Header;

public class AAHCActivity extends AppCompatActivity {

    private WebView webV_Contenido;
    private EditText edT_URL;
    private TextView txtV_Tiempo;

    private long tiempoInicio;
    private long tiempoFin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aahc);

        iniciar();
    }

    private void iniciar() {
        edT_URL = (EditText) findViewById(R.id.edT_URLAAHC);
        webV_Contenido = (WebView) findViewById(R.id.webV_ContenidoAAHC);
        txtV_Tiempo = (TextView) findViewById(R.id.txtV_TiempoAAHC);
    }

    public void onClick_AAHC(View v) {
        switch (v.getId()) {
            case R.id.btn_ConexionAAHC:
                AAHC();
                break;
        }
    }

    private void AAHC() {
        final String texto = edT_URL.getText().toString();
        final ProgressDialog progreso = new ProgressDialog(AAHCActivity.this);
        tiempoInicio = System.currentTimeMillis();
        RestClient.get(texto, new TextHttpResponseHandler() {
            @Override
            public void onStart() {
                //called before request is started
                progreso.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progreso.setMessage("Conectando . . .");
                //progreso.setCancelable(false);
                progreso.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    public void onCancel(DialogInterface dialog) {
                        RestClient.cancelRequests(getApplicationContext(), true);
                    }
                });
                progreso.show();


            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String response) {
                // called when response HTTP status is "200 OK"
                tiempoFin = System.currentTimeMillis();
                progreso.dismiss();
                webV_Contenido.loadDataWithBaseURL(null, response, "text/html", "UTF-8", null);
                txtV_Tiempo.setText("Duración: " + String.valueOf(tiempoFin - tiempoInicio) + " milisegundos");
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String response, Throwable t) {
                // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                tiempoFin = System.currentTimeMillis();
                progreso.dismiss();
                webV_Contenido.loadDataWithBaseURL(null, "Error: " + response + " ", "text/html", "UTF-8", null);
                txtV_Tiempo.setText("Duración: " + String.valueOf(tiempoFin - tiempoInicio) + " milisegundos");
            }
        });
    }
}