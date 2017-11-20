package com.example.redacdat;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.redacdat.red.MySingleton;

import java.io.UnsupportedEncodingException;

public class VolleyActivity extends AppCompatActivity {

    private WebView webV_Contenido;
    private EditText edT_URL;
    private TextView txtV_Tiempo;

    RequestQueue mRequestQueue;

    public static final String TAG = "MyTag";

    private long tiempoInicio;
    private long tiempoFin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_volley);

        iniciar();
    }

    private void iniciar() {
        edT_URL = (EditText) findViewById(R.id.edT_URLVolley);
        webV_Contenido = (WebView) findViewById(R.id.webV_ContenidoVolley);
        txtV_Tiempo = (TextView) findViewById(R.id.txtV_TiempoVolley);
    }

    public void onClick_Volley(View v) {
        switch (v.getId()) {
            case R.id.btn_ConexionVolley:
                tiempoInicio = System.currentTimeMillis();
                makeRequest(edT_URL.getText().toString());
                break;
        }
    }

    public void makeRequest(String url) {
        final String enlace = url;

        // Instantiate the RequestQueue
        //mRequestQueue = Volley.newRequestQueue(this);
        mRequestQueue = MySingleton.getInstance(this.getApplicationContext()).getRequestQueue();

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Conectando...");
        progressDialog.setCancelable(true);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                mRequestQueue.cancelAll(TAG);
            }
        });

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                tiempoFin = System.currentTimeMillis();
                progressDialog.dismiss();
                webV_Contenido.loadDataWithBaseURL(enlace, response, "text/html", "UTF-8", null);
                txtV_Tiempo.setText("Duración: " + String.valueOf(tiempoFin - tiempoInicio) + " milisegundos");

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                String mensaje = "Error";
                if (error instanceof TimeoutError || error instanceof NoConnectionError)
                    mensaje = "Timeout Error: " + error.getMessage();
                else {
                    NetworkResponse errorResponse = error.networkResponse;
                    if (errorResponse != null && errorResponse.data != null) {
                        try {
                            mensaje = "Error: " + errorResponse.statusCode + " " + "\n " +
                                    new String(errorResponse.data, "UTF-8");

                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                    }
                }
                tiempoFin = System.currentTimeMillis();
                progressDialog.dismiss();
                webV_Contenido.loadDataWithBaseURL(null, mensaje, "text/html", "UTF-8", null);
                txtV_Tiempo.setText("Duración: " + String.valueOf(tiempoFin - tiempoInicio) + " milisegundos, sin exito");

            }
        });
        // Set the tag on the request.
        stringRequest.setTag(TAG);
        // Set retry policy
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(3000, 1, 1));
        // Add the request to the RequestQueue.
        mRequestQueue.add(stringRequest);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(TAG);
        }
    }
}