package com.example.redacdat;

import android.content.Context;
import android.os.Environment;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


/**
 * Escribiendo en memoria interna y en memoria externa
 * @author Elena G (Beelzenef)
 */

public class EscribirMemorias {

    private Context contexto;

    public EscribirMemorias(Context c) {
        this.contexto = c;
    }

    // Metodos

    public String mostrarPropiedades(File fichero) {
        SimpleDateFormat formato = null;
        StringBuffer txt = new StringBuffer();
        try {
            if (fichero.exists()) {
                txt.append("Nombre: " + fichero.getName() + '\n');
                txt.append("Ruta: " + fichero.getAbsolutePath() + '\n');
                txt.append("Tamaño (bytes): " + Long.toString(fichero.length()) + '\n');
                formato = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss", Locale.getDefault());
                txt.append("Fecha: " + formato.format(new Date(fichero.lastModified())) + '\n');
            }
            else
                txt.append("No existe el fichero " + fichero.getName() + '\n');
        } catch (Exception e) {
            //Log.e("Error", e.getMessage());
            txt.append(e.getMessage());
        }
        return txt.toString();
    }

    // Para operar con memoria interna:

    public String mostrarPropiedadesInterna(String fichero) {
        File miFichero;
        miFichero = new File(contexto.getFilesDir(), fichero);
        return mostrarPropiedades(miFichero);
    }

    public boolean escribirInterna(String fichero, String cadena, Boolean anadir, String codigo) {
        File miFichero;
        miFichero = new File(contexto.getFilesDir(), fichero);
        return escribir(miFichero, cadena, anadir, codigo);
    }

    private boolean escribir(File fichero, String cadena, Boolean anadir, String codigo) {
        FileOutputStream fos = null;
        OutputStreamWriter osw = null;
        BufferedWriter out = null;
        boolean correcto = false;
        try {
            fos = new FileOutputStream(fichero, anadir);
            osw = new OutputStreamWriter(fos, codigo);
            out = new BufferedWriter(osw);
            out.write(cadena);
        } catch (IOException e) {
            //Log.e("Error de E/S", e.getMessage());
        } finally {
            try {
                if (out != null) {
                    out.close();
                    correcto = true;
                }
            } catch (IOException e) {
                //Log.e("Error al cerrar", e.getMessage());
            }
        }
        return correcto;
    }

    // Para operar con memoria externa:

    public String mostrarPropiedadesExterna(String fichero) {

        File miFichero;
        File tarjeta;

        tarjeta = Environment.getExternalStorageDirectory();
        miFichero = new File(tarjeta.getAbsolutePath(), fichero);

        return mostrarPropiedades(miFichero);
    }

    public boolean disponibleEscritura() {

        boolean escritura = false;

        //Comprobamos el estado de la memoria externa (tarjeta SD)

        String estado = Environment.getExternalStorageState();
        if (estado.equals(Environment.MEDIA_MOUNTED))
            escritura = true;
        return escritura;
    }

    public boolean disponibleLectura() {

        boolean lectura = false;

        //Comprobamos el estado de la memoria externa (tarjeta SD)

        String estado = Environment.getExternalStorageState();
        if (estado.equals(Environment.MEDIA_MOUNTED_READ_ONLY)
                || estado.equals(Environment.MEDIA_MOUNTED))
            lectura = true;
        return lectura;
    }

    public boolean escribirExterna(String fichero, String cadena, Boolean anadir, String codigo) {
        File miFichero, tarjeta;
        tarjeta = Environment.getExternalStorageDirectory();

        //tarjeta = Environment.getExternalStoragePublicDirectory("datos/programas/");
        //tarjeta.mkdirs();

        miFichero = new File(tarjeta.getAbsolutePath(), fichero);
        return escribir(miFichero, cadena, anadir, codigo);
    }
}
