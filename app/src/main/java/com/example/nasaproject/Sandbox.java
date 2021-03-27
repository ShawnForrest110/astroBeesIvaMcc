package com.example.nasaproject;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;

public class Sandbox extends AppCompatActivity {

    private ImageView moonMap;
    private TextView xCoord;
    private TextView yCoord;
    private TextView xTitle;
    private TextView yTitle;
    private Button setWaypointButton;
    private Button clearWaypointsButton;

    private int n = 0;
    private ArrayList<ArrayList<Integer>> coordsArray = new ArrayList<>(2);

    private int waypointRadius = 20;
    private ArrayList<Waypoint> waypoints = new ArrayList<>();

    private String tag = this.getClass().getSimpleName();

    // TODO: 3/26/21 Continue working on this (GV)
    // TODO: 3/27/21 Maybe overload the method and if the number is specified then draw a double waypoint circle to show the selected one
    private void drawWaypoints() {
        // Stopping the drawWaypoints if there is nothing to draw or draw on
        if (moonMap == null || waypoints == null || waypoints.size() == 0 )
            return;

        // Now that we know there is a map and there are waypoints, we can draw

        for ( Waypoint w : waypoints ) {
            Log.i(tag, "Waypoint: " + w.toString());
        }

        // TODO: 3/26/21 Continue from here... GV

        Log.i(tag, "Drawing waypoints...");

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.moonnavtest);

        Bitmap newBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(newBitmap);

        canvas.drawBitmap(bitmap, 0, 0, null);

        // Drawing the waypoints

        Paint paint = new Paint();

        Paint pathPaint = new Paint();
        pathPaint.setColor(getResources().getColor(R.color.waypointPath));
        pathPaint.setStrokeWidth(3);
        pathPaint.setStyle(Paint.Style.STROKE);

        // Draw the paths between consecutive waypoints
        for ( int i = 0; i < waypoints.size()-1; i++ ) {
            int xOri = (int)(waypoints.get(i).xCoord * (double)moonMap.getWidth());
            int yOri = (int)(waypoints.get(i).yCoord * (double)moonMap.getHeight());

            int xDest = (int)(waypoints.get(i+1).xCoord * (double)moonMap.getWidth());
            int yDest = (int)(waypoints.get(i+1).yCoord * (double)moonMap.getHeight());

            canvas.drawLine(xOri,yOri,xDest,yDest,pathPaint);
        }

        // Iterate through the array list and draw every point that is stored - MA
        for ( int i = 0; i < waypoints.size(); i++ ) {
            int x = (int)(waypoints.get(i).xCoord * (double)moonMap.getWidth());
            int y = (int)(waypoints.get(i).yCoord * (double)moonMap.getHeight());

            if ( i == 0 )
                paint.setColor(getResources().getColor(R.color.firstWaypoint));
            else if ( i == waypoints.size()-1 )
                paint.setColor(getResources().getColor(R.color.finalWaypoint));
            else
                paint.setColor(getResources().getColor(R.color.intermediateWaypoint));

            canvas.drawCircle(x,y,waypointRadius,paint);
        }
        
        moonMap.setImageDrawable(new BitmapDrawable(getResources(), newBitmap));
    }

    /**
     * Clears the waypoints and restores the original image.
     */
    private void clearWaypoints() {
        // Remove all waypoints from the server and locally
        Log.i(tag, "Clearing waypoints");
        waypoints.clear();

        // Removing the values within the TextViews
        Log.i(tag, "Clearing the UI");
        xCoord.setText("");
        yCoord.setText("");

        // TODO: 3/27/21 Clear the waypoints in ARGOS

        // Resetting the original bitmap
        moonMap.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.moonnavtest));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sandbox); //old ContentView used for getting coordinates in last version
        //setContentView(new CustomView(this)); //use this contentView or one above?

        this.moonMap = (ImageView)this.findViewById(R.id.moonMap);
        //this.moonMap.setImageResource(R.drawable.moonnavtest);
        this.xCoord = (TextView)this.findViewById(R.id.xPos);
        this.yCoord = (TextView)this.findViewById(R.id.yPos);
        this.xTitle = (TextView)this.findViewById(R.id.xView);
        this.yTitle = (TextView)this.findViewById(R.id.yView);

        this.setWaypointButton = (Button)this.findViewById(R.id.setWaypointBtn);
        this.clearWaypointsButton = (Button)this.findViewById(R.id.clearWaypointsBtn);

        //CustomView customView = new CustomView(this); //is this CustomView needed? -CG I don't think so. -GV
        setWaypointButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("submitBtn", "I just clicked the button...");

                String xLoc = xCoord.getText().toString();
                String yLoc = yCoord.getText().toString();

                Waypoint waypoint = new Waypoint();
                try {
                    waypoint.xCoord = Double.parseDouble(xCoord.getText().toString());
                    waypoint.yCoord = Double.parseDouble(yCoord.getText().toString());
                    waypoint.description = "Waypoint " + waypoints.size();
                    waypoints.add(waypoint);
                    Log.i(tag, "New waypoint: " + waypoint);
                    xCoord.setText("");
                    yCoord.setText("");
                    drawWaypoints();
                } catch (Exception e) {
                    Log.i(tag, "An exception occurred trying to save the coordinates.");
                }

                String toSubmit = "http://192.168.0.107:8123/locations/add?loc_id=test102&coords=[" + xLoc + ", " + yLoc + "]&description=Some new location&title=First saved location&images=[path1, path2]&owner=xemu2&static=true";

                StringRequest stringRequest = new StringRequest(
                        Request.Method.PUT,
                        toSubmit,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Log.i("Location Test", "Successful request! Now do something...");
                                Log.i("Location Test", "Response: " + response);
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i("Location Test", "An error occurred...");
                        Log.i("Location Test", error.toString());
                    }
                }
                );
                Volley.newRequestQueue(getApplicationContext()).add(stringRequest);
            }
        });

        clearWaypointsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearWaypoints();
            }
        });

        //create request queue outside listener - CG
        final RequestQueue requestQueue = Volley.newRequestQueue(this); //for submit button

        // onHoverListener? Maybe later...
        this.moonMap.setOnHoverListener(new View.OnHoverListener() {
            @Override
            public boolean onHover(View v, MotionEvent motionEvent) {
                int x = (int)motionEvent.getX();
                int y = (int)motionEvent.getY();

                Log.i(tag, "Hovering over [" + x + "," + y + "]");
                return false;
            }
        });

        //gets x & y coordinates to display in textviews & send to octavia - CG
        this.moonMap.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View customView, MotionEvent motionEvent) { //should CustomView replace myView here? - CG
                int x = (int)motionEvent.getX();
                int y = (int)motionEvent.getY();

                // initialize an ArrayList of ArrayLists - MA
                coordsArray.add(new ArrayList<Integer>());
                // add x & y coordinates to Array List - MA
                coordsArray.get(n).add(x);
                coordsArray.get(n).add(y);

                double imageWidth = moonMap.getWidth();
                double imageHeight = moonMap.getHeight();

                double normalizedX = ((double)(x)/imageWidth);
                double normalizedY = ((double)(y)/imageHeight);

                // Rounding to two decimal places
                normalizedX = (double)(Math.round(normalizedX*100d)/100d);
                normalizedY = (double)(Math.round(normalizedY*100d)/100d);

                Log.i("Image", "Clicked on coords x and y " + x + ", " + y + " - " + moonMap.getWidth() + "x" + moonMap.getHeight());

                Log.i("Testing","List " + n + ":" + coordsArray.get(n).get(0) + ' ' + coordsArray.get(n).get(1));

                //display coordinates in textviews - CG
                //xCoord.setText(String.valueOf(x) + " | Norm: " + normalizedX);
                //yCoord.setText(String.valueOf(y) + " | Norm: " + normalizedY);
                xCoord.setText(Double.toString(normalizedX));
                yCoord.setText(Double.toString(normalizedY));

                // GV Testing - Start
                Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.moonnavtest);

                Bitmap newBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(newBitmap);

                canvas.drawBitmap(bitmap, 0, 0, null);

                Paint paint = new Paint();
                paint.setColor(Color.parseColor("#ff0000")); // Red

                //created a new Paint for the line between points - MA
                Paint paint2 = new Paint();

                paint2.setColor(Color.GREEN);

                paint2.setStrokeWidth(3);

                paint2.setStyle(Paint.Style.STROKE);

                //iterate through the array list and draw every point that is stored - MA
                for(int i =0; i<coordsArray.size(); i++){
                    canvas.drawCircle(coordsArray.get(i).get(0),coordsArray.get(i).get(1),25, paint);
                }

                //only try and draw a line between points if there is more than 1 point
                if(coordsArray.size() >=2){
                    canvas.drawLine(coordsArray.get(n-1).get(0),coordsArray.get(n-1).get(1),coordsArray.get(n).get(0),coordsArray.get(n).get(1),paint2);
                }

                //canvas.drawCircle(x, y, 25, paint);
                //moonMap.setImageBitmap(bitmap); // TODO: Continue from here
                moonMap.setImageDrawable(new BitmapDrawable(getResources(), newBitmap));
                // GV Testing - End

                //submit button will send coordinates in a json object to octavia; not finished - CG
                /*submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //create url for octavia
                        //String url = "https://localhost:3000/api/...rest of url

                        Log.i("SubmitBtn", "Testing...");

                        //string vars for each property in the array
                        String id;
                        String loc_id;
                        String coords;
                        String description;
                        String title;
                        String images;
                        String owner;
                        String staticx;
                    }
                });*/

                //increase the first coordArray index - MA
                n = n+1;

                return false;
            }



            //onTouchEvent to create circle as waypoint on map; not finished - CG
            /*@Override //how do I get CustomView into activity? As line 44 or inner class in onTouchEvent? -CG
            public boolean onTouchEvent(MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN) {
                    if (surfaceHolder.getSurface().isValid()) {
                        Canvas canvas = surfaceHolder.lockCanvas();
                        canvas.drawColor(Color.YELLOW);
                        canvas.drawCircle(event.getX(), event.getY(), 50, paint);
                        surfaceHolder.unlockCanvasAndPost(canvas);
                    }
                    Log.i("Image2", "Waypoint created");
                }
                return false;
            }*/

            //onTouchEvent to handle drawing a path; not finished
            /*@Override
            public boolean onTouchEvent(MotionEvent event)
            {
                int x = (int)event.getX();
                int y = (int)event.getY();
                Log.i("Image2", "coords x and y " + x + " , " + y);

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                    case MotionEvent.ACTION_MOVE:
                    case MotionEvent.ACTION_UP:
                }

                return false;
            }*/

        });
    }
}