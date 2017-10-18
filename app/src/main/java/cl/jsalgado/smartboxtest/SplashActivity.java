package cl.jsalgado.smartboxtest;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import cl.jsalgado.smartboxtest.auth.Data;
import cl.jsalgado.smartboxtest.device.Description;
import cl.jsalgado.smartboxtest.util.Util;

public class SplashActivity extends AppCompatActivity {

    private String accessToken;
    private String expires;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        if(getSupportActionBar()!= null ){
            getSupportActionBar().hide();
        }
        SharedPreferences prefs = getSharedPreferences("preferences", MODE_PRIVATE);
        accessToken = prefs.getString("token", null);
        expires = prefs.getString("expires", null);
        validate();
    }

    public void loadMain(){
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("token", accessToken);
        intent.putExtra("expires", expires);
        startActivity(intent);
        finish();
    }
    /**
     * Método para validar que el dispositivo cuente con conexión a internet
     */
    public void validate(){
        if(Util.isConnected(this)){
            if(accessToken != null && expires != null){
                loadMain();
            }else {
                initAuth();
            }
        }else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(getResources().getString(R.string.internet_msg))
                    .setTitle(getResources().getString(R.string.title_msg))
                    .setPositiveButton(getResources().getString(R.string.retry_msg)
                            , new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            validate();
                            dialog.cancel();
                        }
                    });
            builder.setCancelable(false);
            builder.create();
            builder.show();
        }
    }

    /**
     * Se inicia la autenticación para obtener un token
     */
    private void initAuth(){
        String url = Util.URL1;
        RequestQueue queue = Volley.newRequestQueue(this);

        try {
            JSONObject jsonObject = new JSONObject(getDeviceInfo());
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject jsonObject) {
                    Data data = new Gson().fromJson(jsonObject.toString(), Data.class);
                    SharedPreferences preferences = getSharedPreferences("preferences", MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("token", data.getData().getAccessToken());
                    editor.putString("expires", data.getData().getExpiresIn());
                    editor.apply();
                    accessToken = preferences.getString("token", null);
                    expires = preferences.getString("expires", null);
                    loadMain();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    String message = "";
                    if (error instanceof NetworkError) {
                        message = getString(R.string.network_error_msg);
                    } else if (error instanceof ServerError) {
                        message = getString(R.string.server_error_msg);
                    }  else if (error instanceof ParseError) {
                        message = getString(R.string.parse_error_msg);
                    } else if (error instanceof TimeoutError) {
                        message = getString(R.string.time_out_error_msg);
                    }
                    Log.e("Error: ", message);
                }
            }){
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> headers = new HashMap<>();
                    headers.put("Content-Type", Util.CONTENT_TYPE);
                    headers.put("Authorization", Util.AUTHORIZATION);
                    return headers;
                }
            };
            queue.add(jsonObjectRequest);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    /**
     * Se obtienen los datos del dispositivo
     */
    private String getDeviceInfo(){
        Description description = new Description();
        description.getDeviceInfo(this);
        Gson gson = new Gson();
        return gson.toJson(description);
    }

}