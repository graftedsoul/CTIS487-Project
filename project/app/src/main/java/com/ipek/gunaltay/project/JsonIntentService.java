package com.ipek.gunaltay.project;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class JsonIntentService extends IntentService {

    RequestQueue requestQueue; //VOLLEY STEP 1:

    ArrayList<String> mc;
    ArrayList<String> sh;
    ArrayList<String> ts2;

    public JsonIntentService() {
        super("MyIntentService");
        Log.d("Service","Service Started");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        requestQueue = Volley.newRequestQueue(this); //VOLLEY STEP 2

        Log.d("JSON", "Requesting");
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, Commons.URL_FOR_JSON, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("JSON", response.toString());

                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                try{
                    JSONArray minecraft = response.getJSONArray("Minecraft");
                    JSONArray silentHill = response.getJSONArray("Silent Hill");
                    JSONArray theSims2 = response.getJSONArray("Sims 2");

                    Log.d("JSON","MINECRAFT: " + minecraft.toString());
                    Log.d("JSON","SILENT HILL: " + silentHill.toString());
                    Log.d("JSON","SIMS 2: " + theSims2.toString());

                    mc = new ArrayList<String>();
                    sh = new ArrayList<String>();
                    ts2 = new ArrayList<String>();

                    for(int i = 0; i < minecraft.length(); i++){
                        JSONObject jsobj = minecraft.getJSONObject(i);
                        String song = jsobj.getString((i + 1) + "");

                        Log.d("JSON", song);

                        Commons.mc_songs.add(song);
                        //recipeItems.add(r);
                    }

                    for(int j = 0; j < silentHill.length(); j++){
                        JSONObject jsobj = silentHill.getJSONObject(j);
                        String song = jsobj.getString((j + 1) + "");

                        Log.d("JSON", song);

                        Commons.sh_songs.add(song);
                        //recipeItems.add(r);
                    }

                    for(int k = 0; k < theSims2.length(); k++){
                        JSONObject jsobj = theSims2.getJSONObject(k);
                        String song = jsobj.getString((k + 1) + "");

                        Log.d("JSON", song);

                        Commons.ts2_songs.add(song);
                        //recipeItems.add(r);
                    }
                    Log.d("JSON","JSON parsed: " + Commons.ts2_songs.size());

                    Intent broadcastIntent = new Intent();
                    broadcastIntent.setAction("JSON_PARSE_COMPLETED_ACTION");

                    if(minecraft.length() > 0 || silentHill.length() > 0 || theSims2.length() > 0) {
                        broadcastIntent.putExtra("result", "FOUND");
                        //broadcastIntent.putExtra("recipeItems", recipeItems);
                    } else
                        broadcastIntent.putExtra("result", "NOTFOUND");

                    sendBroadcast(broadcastIntent);
                }
                catch (Exception e) {
                    Log.d("JSON", "Error: " + e.getMessage());
                }
            }
        },
        new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //If file cannot be taken, ıf an error returned form server, onErrorResponse will be executed
                //calling this method handled by Volley's thread
                Log.d("JSON", error.toString());
            }
        }
        );
        //VOLLEY STEP 4
        requestQueue.add(jsonObjectRequest);
    }
}
