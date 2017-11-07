package com.example.redacdat;

import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

public class HTTPActivity extends AppCompatActivity {

    EditText edT_URL;
    WebView webV_HTTP;
    TextView txtV_TiempoRespuesta;
    RadioButton rdB_Java;
    RadioButton rdB_Apache;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_http);

        edT_URL = (EditText) findViewById(R.id.edT_URL);
        webV_HTTP = (WebView) findViewById(R.id.webV_HTTP);
        txtV_TiempoRespuesta = (TextView) findViewById(R.id.txtV_TiempoRespuesta);
        rdB_Java = (RadioButton) findViewById(R.id.rdB_Java);
        rdB_Apache = (RadioButton) findViewById(R.id.rdB_Apache);

        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitNetwork().build());
    }

    public void onClick_HTTP(View v)
    {
        switch (v.getId())
        {
            case R.id.btn_ConectarHTTP:
                establecerConexion();
                break;
        }
    }

    private void establecerConexion()
    {
        String texto = edT_URL.getText().toString();
        long inicio;
        long fin;
        Resultado resultado;

        inicio = System.currentTimeMillis();
        if (rdB_Java.isChecked())
            resultado = Conectar.conectarJava(texto);
        else
            resultado = Conectar.conectarApache(texto);
        fin = System.currentTimeMillis();
        if (resultado.getCodigo())
            webV_HTTP.loadDataWithBaseURL(null, resultado.getContenido(),"text/html", "UTF-8", null);
        else
            webV_HTTP.loadDataWithBaseURL(null, resultado.getMensaje(),"text/html", "UTF-8", null);
        txtV_TiempoRespuesta.setText("Duración: " + String.valueOf(fin - inicio) + " milisegundos");
    }
}
