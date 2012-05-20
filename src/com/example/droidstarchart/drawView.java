package com.example.droidstarchart;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

public class drawView extends View {
    Paint paint = new Paint();
    
    private float screenWidth;
    private float screenHeight;

    public drawView(Context context) {
        super(context);
        paint.setColor(Color.BLACK);
    }

    @Override
    public void onDraw(Canvas canvas) {
            canvas.drawLine(0, 0, 20, 20, paint);
            canvas.drawLine(20, 0, 0, 20, paint);
            canvas.drawCircle(20, 50, 25, paint);
    }

}