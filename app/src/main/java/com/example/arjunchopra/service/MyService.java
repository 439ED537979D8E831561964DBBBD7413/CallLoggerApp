package com.example.arjunchopra.service;

import java.util.ArrayList;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.ListView;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MyService extends Service {


    public static final String URL_SAVE_NAME = "http://shreeartspatiala.com/saveName.php";

    //database helper object
    private DatabaseHelper db;

    //View objects

    private ListView listViewNames;
    private String phNumber;


    //List to store all the names
    private List<Name> names;

    //1 means data is synced and 0 means data is not synced
    public static final int NAME_SYNCED_WITH_SERVER = 1;
    public static final int NAME_NOT_SYNCED_WITH_SERVER = 0;





    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {




        //initializing views and objects
        db = new DatabaseHelper(this);
        names = new ArrayList<>();






        if (intent.getStringExtra("Number") != null) {


                phNumber = intent.getStringExtra("Number");

                saveNameToServer();
                Toast.makeText(this, "registration " + phNumber,
                        Toast.LENGTH_LONG).show();


        } else {
        }
return START_STICKY;

    }










    private void saveNameToServer() {
       // final ProgressDialog progressDialog = new ProgressDialog(this);
       // progressDialog.setMessage("Saving Name...");
        //progressDialog.show();



        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_SAVE_NAME,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                       // progressDialog.dismiss();
                        try {
                            JSONObject obj = new JSONObject(response);
                            if (!obj.getBoolean("error")) {
                                //if there is a success
                                //storing the name to sqlite with status synced
                                saveNameToLocalStorage(phNumber, NAME_SYNCED_WITH_SERVER);
                            } else {
                                //if there is some error
                                //saving the name to sqlite with status unsynced
                                saveNameToLocalStorage(phNumber, NAME_NOT_SYNCED_WITH_SERVER);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //progressDialog.dismiss();
                        //on error storing the name to sqlite with status unsynced
                        saveNameToLocalStorage(phNumber, NAME_NOT_SYNCED_WITH_SERVER);
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("name", phNumber);
                return params;
            }
        };

        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);







    }

    //saving the name to local storage
    private void saveNameToLocalStorage(String phNumber, int status) {
        db.addName(phNumber, status);
        Name n = new Name(phNumber, status);
        names.add(n);



    }



}






