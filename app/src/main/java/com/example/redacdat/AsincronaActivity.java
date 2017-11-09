package com.example.redacdat;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

public class AsincronaActivity extends AppCompatActivity {

    EditText edT_URL;
    WebView webV_HTTP;
    TextView txtV_TiempoRespuesta;
    RadioButton rdB_Java;
    RadioButton rdB_Apache;

    ConectarAsincrona conectarAsincrona;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_http);

        edT_URL = (EditText) findViewById(R.id.edT_URL);
        webV_HTTP = (WebView) findViewById(R.id.webV_HTTP);
        txtV_TiempoRespuesta = (TextView) findViewById(R.id.txtV_TiempoRespuesta);
        rdB_Java = (RadioButton) findViewById(R.id.rdB_Java);
        rdB_Apache = (RadioButton) findViewById(R.id.rdB_Apache);

        //StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitNetwork().build());
    }

    public void onClick_HTTP(View v) {
        switch (v.getId()) {
            case R.id.btn_ConectarHTTP:
                establecerConexion();
                break;
        }
    }

    private void establecerConexion() {
        String url = edT_URL.getText().toString();
        String modoConexion = "";

        if (rdB_Apache.isChecked()) {
            modoConexion = "apache";
        }
        else {
            modoConexion = "java";
        }

        new ConectarAsincrona().execute(url, modoConexion);

    }

    class ConectarAsincrona extends AsyncTask<String, Integer, Resultado> {

        private ProgressDialog progreso;
        private Resultado resultado;

        public ConectarAsincrona()
        {
            resultado = new Resultado();
        }

        @Override
        protected void onPreExecute() {
            progreso = new ProgressDialog(AsincronaActivity.this);
            progreso.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progreso.setMessage("Conectando...");
            progreso.setCancelable(true
            );
            progreso.setOnCancelListener(new DialogInterface.OnCancelListener() {
                public void onCancel(DialogInterface dialog) {
                    ConectarAsincrona.this.cancel(true);
                }
            });
            progreso.show();
        }

        @Override
        protected Resultado doInBackground(String... cadena) {

            try {
                if (cadena[1].equals("java"))
                    resultado = Conectar.conectarJava(cadena[0]);
                if (cadena[1].equals("apache"))
                    resultado = Conectar.conectarApache(cadena[0]);

            } catch (Exception e) {
                resultado = null;
                cancel(true);
            }
            return resultado;
        }

        @Override
        protected void onPostExecute (Resultado result) {
            progreso.setMessage("Conexion exitosa");
            if (resultado.getCodigo())
                webV_HTTP.loadDataWithBaseURL(null, resultado.getContenido(), "text/html", "UTF-8", null);
            else
                webV_HTTP.loadDataWithBaseURL(null, resultado.getMensaje(), "text/html", "UTF-8", null);
            progreso.dismiss();
        }

        @Override
        protected void onCancelled () {
            progreso.setMessage("Conexion cancelada");
            progreso.dismiss();
        }
    }
}
