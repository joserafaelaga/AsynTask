package com.example.eag.asyntask;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    private ProgressDialog dialog;
    TextView texto;
    private RequestQueue mQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        texto = (TextView) findViewById(R.id.texto);

        mQueue = Volley.newRequestQueue(this);

        dialog = new ProgressDialog(this);
        dialog.setMessage("Descargando...");
        dialog.setTitle("Progreso");
        dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        dialog.setCancelable(false);
    }

    public void lanzarHilo(View view){
        new MiTarea().execute("https://www.w3schools.com/angular/customers.php");
    }

    private class MiTarea extends AsyncTask<String, Float, Integer>{


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.setProgress(0);
            dialog.setMax(100);
            dialog.show();
        }

        @Override
        protected Integer doInBackground(String... urls) {

            for(int i = 0; i < 250; i++){
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                publishProgress(i/250f);
            }

            JsonObjectRequest jsonObjectRequest =new JsonObjectRequest(
                    Request.Method.GET, urls[0], null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        JSONArray jsonArray = response.getJSONArray("records");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject empleados = jsonArray.getJSONObject(i);

                            String nombre = empleados.getString("Name");
                            String ciudad = empleados.getString("City");
                            String pais = empleados.getString("Country");

                            texto.append(nombre + " , " + ciudad + " , " + pais + " \n\n");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                }
            }
            );
            mQueue.add(jsonObjectRequest);


            return 250;
        }

        @Override
        protected void onProgressUpdate(Float... values) {
            super.onProgressUpdate(values);
            int p = Math.round(100*values[0]);
            dialog.setProgress(p);
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            dialog.dismiss();
        }
    }
}
