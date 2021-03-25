package com.example.nasaproject;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

//customized view to handle drawing waypoints & lines on moon map in Sandbox activity - CG
//CustomView should be for map only, not entire screen - CG
public class CustomView extends SurfaceView
{
    private final SurfaceHolder surfaceHolder;
    private final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);

    public CustomView(Context context)
    {
        //not sure this constructor is set up correctly; found several ways to do it - CG
        super(context);
        //Canvas grid = new Canvas(Bitmap.createBitmap(Bitmap.Config.ARGB_8888));
        //grid.drawColor(Color.YELLOW);
        //Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        //grid.drawCircle(w/2, h/2 , w/2, paint);
        surfaceHolder = getHolder();
    }

    //override onDraw method is necessary to create customized Paint objects on map - CG
    @Override
            protected void onDraw(Canvas canvas)
            {
                super.onDraw(canvas);
                int x = getWidth();
                int y = getHeight();
                int radius;
                radius = 5;
                paint.setStyle(Paint.Style.FILL);
                paint.setColor(Color.YELLOW);
                canvas.drawPaint(paint);
                // Use Color.parseColor to define HTML colors
                paint.setColor(Color.parseColor("#FFFF00"));
                canvas.drawCircle(x / 2, y / 2, radius, paint);
            }

}
