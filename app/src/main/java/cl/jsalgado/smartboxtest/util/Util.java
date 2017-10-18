package cl.jsalgado.smartboxtest.util;

import android.content.Context;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 * Created by joels on 16-10-2017.
 *
 */

public class Util {

    public static String URL1 = "http://fxservicesstaging.nunchee.com/api/1.0/auth/users/login/anonymous";
    public static String URL2 = "http://fxservicesstaging.nunchee.com/api/1.0/sport/events";
    public static String CONTENT_TYPE = "application/json";
    public static String AUTHORIZATION = "Basic cHJ1ZWJhc2RldjpwcnVlYmFzZGV2U2VjcmV0";

    /**
     * Método para formatear la fecha
     * @param date fecha a formatear
     * @return devuelve fecha formateada
     */
    public static String getDate(String date){
        DateTime dateTime = new DateTime(date);
        DateTimeFormatter formatter = DateTimeFormat.forPattern("dd-MM-yyyy HH:mm");
        return formatter.print(dateTime);
    }

    /**
     * Método para llamar imagen según nombre
     * @param name nombre de la imagen
     * @param context contexto de la apicación
     * @return devuelve ID de la imagen
     */
    public static int getDrawable(String name, Context context){
        Resources resources = context.getResources();
        return resources.getIdentifier(name.toLowerCase(), "drawable", context.getPackageName());
    }

    /**
     * Metodo para validar conexión a internet
     * @param context contexto de la aplicación
     * @return boolean devuelve true si hay conexión a internet
     */
    public static boolean isConnected(Context context){
        boolean isConnected = false;
        ConnectivityManager cm =  (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if(activeNetwork != null){
            isConnected = activeNetwork.isConnectedOrConnecting();
        }
        return  isConnected;
    }

}