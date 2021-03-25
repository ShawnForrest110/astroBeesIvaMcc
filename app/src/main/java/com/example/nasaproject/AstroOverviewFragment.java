package com.example.nasaproject;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;


/**
 A simple android fragment that allows for monitoring of multiple telemetry streams via api calls on different ports
 FIXME: Fix timer restarting on app close/restart
 TODO: add setting to input expected mission time by user
 TODO: find a way to get the data to refresh faster runnable seems to have 20,000 millisecond delay limit as fastest
 */
public class AstroOverviewFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    private final String URL = "http://192.168.1.10:8002/telemetry";
    private final String URL2 = "http://192.168.1.10:8001/telemetry";

    private static TextView Oxygen;
    private static TextView Battery;
    private static TextView OxygenRate;
    private static TextView Oxygen2;
    private static TextView Battery2;
    private static TextView OxygenRate2;
    public static TextView Time;
    public static TextView Time2;
    public long millis;
    public long millis2;

    private Button detailsButton;

    public long timeRemaining;

    CountDownTimer countDownTimer;
    CountDownTimer countDownTimer2;



    protected Handler handler = new Handler();

    public void useAPI(String URL){
        System.gc();
        new GetFromAPI().execute(URL);
    }



    public AstroOverviewFragment() {
        // Required empty public constructor
    }


    public static AstroOverviewFragment newInstance(String param1, String param2) {
        AstroOverviewFragment fragment = new AstroOverviewFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onPause(){
        System.gc();
        countDownTimer.cancel();
        countDownTimer2.cancel();
        super.onPause();
    }

    @Override
    public void onResume(){

        if(millis > 0){
            countDownTimer = new CountDownTimer(millis, 1000){
                public void onTick(long millisUntilFinished) {
                    Time.setText(String.format("%02d:%02d:%02d",
                            TimeUnit.MILLISECONDS.toHours(millisUntilFinished) % 60,
                            TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)% 60,
                            TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) %60
                    ));
                    millis = millisUntilFinished;
                   // Log.i("TIMER",Long.toString(millisUntilFinished));
                }


                public void onFinish() {
                    Time.setText("done!");
                }

            }.start();
        }

        /*else {
            countDownTimer = new CountDownTimer(millis, 1000){
                public void onTick(long millisUntilFinished) {
                    Time.setText(String.format("%02d:%02d:%02d",
                            TimeUnit.MILLISECONDS.toHours(millisUntilFinished) % 60,
                            TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)% 60,
                            TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) %60
                    ));
                    millis = millisUntilFinished;
                }


                public void onFinish() {
                    Time.setText("done!");
                }

            }.start();
        } */

        super.onResume();

    }

    @Override
    public void onStart(){

        super.onStart();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_astro_overview, container, false);

        //gives us mission duration in from hours to milliseconds
        millis = TimeUnit.HOURS.toMillis(8);
        millis2 = TimeUnit.HOURS.toMillis(5);

        new GetFromAPI().execute(URL);
        new GetFromAPI2().execute(URL2);

        Oxygen = rootView.findViewById(R.id.Oxygen);
        Battery = rootView.findViewById(R.id.battery);
        OxygenRate = rootView.findViewById(R.id.Pressure);
        Time = rootView.findViewById(R.id.beat);
        detailsButton = rootView.findViewById(R.id.btnMore);
        Oxygen2 = rootView.findViewById(R.id.Oxygen2);
        Battery2 = rootView.findViewById(R.id.battery2);
        OxygenRate2 = rootView.findViewById(R.id.Pressure2);
        Time2 = rootView.findViewById(R.id.beat2);


        handler.postDelayed(runnable, 10000);

        detailsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment myFrag = new singleAstro();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, myFrag);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        countDownTimer2 = new CountDownTimer(millis2, 1000){
            public void onTick(long millisUntilFinished) {
                Time2.setText(String.format("%02d:%02d:%02d",
                        TimeUnit.MILLISECONDS.toHours(millisUntilFinished) % 60,
                        TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)% 60,
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) %60
                ));
                millis2 = millisUntilFinished;
                // Log.i("TIMER",Long.toString(millisUntilFinished));
            }


            public void onFinish() {
                Time2.setText("done!");
            }

        }.start();

        // GV: I added this call to test the different UI elements with the new server
        sandbox(rootView); // Comment this out and the LinearLayout in AstroOverviewFragment when done

        // Inflate the layout for this fragment
        return rootView;


    }
    // A method for refreshing the telemetry stream data

    /*
     * GV: I added the method sandbox(rootView) to test the different UI elements. Each element will
     * handle one action and output anything that is returned using log.i.
     *
     * I also added volley 1.1.1 to build.gradle (Module:NasaProject.app)
     *
     * Start EVA: An example to how to get the EVA started, otherwise the system
     * won't be able to pull the telemetry stream.
     *
     * Get Telemetry: An example of how to pull telemetry once the stream was started.
     *
     * Toggle: To turn some element of the DCU on or off, depending on the state.
     */

    /*
     * GV: This will have to change if the IP/port change
     * We cannot have the loopback, because when this runs on a device (even virtual), the loopback
     * address will try to open a connection on the device itself and not the host.
     */
    private final String host = "192.168.0.104:3000";

    // Examples: https://www.geeksforgeeks.org/volley-library-in-android/

    private void sandbox(View rootView) {
        final String logTag = "Sandbox";
        // To start the EVA
        Button startEVA = rootView.findViewById(R.id.sandbox_start_eva);
        startEVA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(logTag, "Clicked on the Start EVA Button");
                // Sample call: http://localhost:3000/api/simulation/start (POST)
                // Returns a String, so we can just utilize the simple StringRequest instead of the JsonObjectRequest
                String url = "http://" + host + "/api/simulation/start";
                Log.i(logTag, "Call: " + url);

                StringRequest stringRequest = new StringRequest(
                        Request.Method.POST,
                        url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Log.i(logTag, "Successful request! Now do something...");
                                Log.i(logTag, "Response: " + response);
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.i(logTag, "An error occurred...");
                                Log.i(logTag, error.toString());
                            }
                        }
                );
                Volley.newRequestQueue(getContext()).add(stringRequest);
            }
        });

        // To get the telemetry information
        Button getTelemetryInfo = rootView.findViewById(R.id.sandbox_get_telemetry);
        getTelemetryInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(logTag, "Clicked on the Get Telemetry Button");
                // Sample call: http://localhost:3000/api/simulation/state (GET)
                // Returns telemetry in JSON format
                String url = "http://" + host + "/api/simulation/state";
                Log.i(logTag, "Call: " + url);

                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                        Request.Method.GET,
                        url,
                        null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Log.i(logTag, "Successful connection");
                                Log.i(logTag, response.toString());
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.i(logTag, "Connection error...");
                                Log.i(logTag, "Error: " + error.toString());
                            }
                        }
                );

                Volley.newRequestQueue(getContext()).add(jsonObjectRequest);
            }
        });

        // To toggle the fan switch on the DCU on and off
        ToggleButton toggleButton = rootView.findViewById(R.id.sandbox_toggle);
        toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // Sample call: http://localhost:3000/api/simulation/newcontrols?fan_switch=false (PATCH)
                // Returns current state of all the switches in JSON format
                String url = "http://" + host + "/api/simulation/newcontrols?fan_switch=";
                if ( isChecked ) { // Turn on
                    Log.i(logTag, "Attempting to turn something off...");
                    url += "true";
                } else { // Turn off
                    Log.i(logTag, "Attempting to turn something on...");
                    url += "false";
                }
                //TODO
                Log.i(logTag, "Call: " + url);
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                        Request.Method.PATCH,
                        url,
                        null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Log.i(logTag, "Successful connection...");
                                Log.i(logTag, "Response: " + response.toString());
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.i(logTag, "Something did not work...");
                                Log.i(logTag, "Error: " + error.toString());
                            }
                        }
                );

                Volley.newRequestQueue(getContext()).add(jsonObjectRequest);
            }
        });
    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {

            try {
                new GetFromAPI().execute(URL);

                handler.postDelayed(this, 20000);
            } catch (Exception e) {
                Log.e("API Call", e.toString());
            }
        }
    };






    //The class we use to get the telemetry data from our local telemetry server
    private class GetFromAPI extends AsyncTask<String, Void, String> {
        private String jsonResponse = "";
        private int oxygen;
        private int battery;
        private String oxygenrate;
        @Override
        protected String doInBackground(String... strings){
            String request = strings[0];


            Log.i("Api Call","Request: " + request);

            try {
                Log.i("API Call", "Initiating Call");

                java.net.URL url = new URL(request);
                url.openConnection().setConnectTimeout(6000);

                InputStream in = url.openStream();
                Scanner streamInput = new Scanner(in, StandardCharsets.UTF_8.name());

                jsonResponse = streamInput.nextLine();

                streamInput.close();
                in.close();


                JSONObject jsonObject = new JSONObject(jsonResponse);

                //the json data does not come as a json array so we use jsonObjects to get each value in each object
                //not the jsonobject out of an array which was the original approach
                JSONObject jsonOxygen = jsonObject.getJSONObject("2");
                oxygen = jsonOxygen.getInt("value");

                JSONObject jsonBattery = jsonObject.getJSONObject("4");
                battery = jsonBattery.getInt("value");

                JSONObject jsonOxygenRate = jsonObject.getJSONObject("3");
                oxygenrate = jsonOxygenRate.getString("value");





            }catch(Exception e){
                Log.e("API Call", e.toString());
            }

            return "API Call Complete";
        }
        protected void onPostExecute(String result) {
            Log.i("API Call", "Result: " + result);

            Oxygen.setText("Oxygen Pressure: " + (oxygen) + " psia");
            Battery.setText("Battery Usage: " + (battery) + " amp-hr");
            OxygenRate.setText("Oxygen Flow Rate: " + (oxygenrate) + " psi/min");
        }
    }
    //we have to use multithreading for additional astronauts as we can't update different values from a single call
    //so we use two async tasks to get the job done
    private class GetFromAPI2 extends AsyncTask<String, Void, String> {
        private String jsonResponse = "";
        private int oxygen;
        private int battery;
        private String oxygenrate;
        @Override
        protected String doInBackground(String... strings){
            String request = strings[0];


            Log.i("Api Call2","Request: " + request);

            try {
                Log.i("API Call2", "Initiating Call");

                java.net.URL url = new URL(request);
                url.openConnection().setConnectTimeout(6000);

                InputStream in = url.openStream();
                Scanner streamInput = new Scanner(in, StandardCharsets.UTF_8.name());

                jsonResponse = streamInput.nextLine();

                streamInput.close();
                in.close();

                //JSONArray arrayData = new JSONArray(jsonResponse);

                JSONObject jsonObject = new JSONObject(jsonResponse);

                JSONObject jsonOxygen = jsonObject.getJSONObject("2");
                oxygen = jsonOxygen.getInt("value");

                JSONObject jsonBattery = jsonObject.getJSONObject("4");
                battery = jsonBattery.getInt("value");

                JSONObject jsonOxygenRate = jsonObject.getJSONObject("3");
                oxygenrate = jsonOxygenRate.getString("value");





            }catch(Exception e){
                Log.e("API Call2", e.toString());
            }

            return "API Call Complete";
        }
        protected void onPostExecute(String result) {
            Log.i("API Call2", "Result: " + result);

            Oxygen2.setText("Oxygen Pressure: " + (oxygen) + " psia");
            Battery2.setText("Battery Usage: " + (battery) + " amp-hr");
            OxygenRate2.setText("Oxygen Flow Rate: " + (oxygenrate) + " psi/min");
        }
    }
}
