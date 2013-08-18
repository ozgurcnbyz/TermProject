package com.activesoft.altapp;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.widget.ImageView;

public class ImgCompass extends ImageView
{
	Paint mydPaint;
	float myddirection = 0;
	
	Paint new_paint;

	public ImgCompass(Context context)
	{
		super(context);

		mydPaint = new Paint();
		mydPaint.setColor(Color.WHITE);
		mydPaint.setStrokeWidth(2);
		mydPaint.setStyle(Style.STROKE);
		
		new_paint = new Paint();
		new_paint.setColor(Color.WHITE);
		new_paint.setStrokeWidth(1);
		new_paint.setStyle(Style.STROKE);

		this.setImageResource(R.drawable.btn_bg);
	}

	@Override
	public void onDraw(Canvas canvas)
	{
		int xPoint = getMeasuredWidth() / 2;
	    int yPoint = getMeasuredHeight() / 2;

	    float radius = (float) (Math.max(xPoint, yPoint) * 0.4);
	    float new_radius = (float) (Math.max(xPoint, yPoint) * 0.6);
	  //  canvas.drawCircle(xPoint, yPoint, radius, mydPaint);
	   
	    canvas.drawCircle(xPoint, yPoint,radius, mydPaint);

	    // 3.143 is a good approximation for the circle
	    canvas.drawLine(xPoint,
	        yPoint,
	        (float) (xPoint + radius
	            * Math.sin((double) (-myddirection) / 180 * 3.143)),
	        (float) (yPoint - radius
	            * Math.cos((double) (-myddirection) / 180 * 3.143)), mydPaint);
	    
	    canvas.drawText("N", (float) (xPoint + new_radius
	            * Math.sin((double) (-myddirection) / 180 * 3.143)), (float) (yPoint - new_radius
	    	            * Math.cos((double) (-myddirection) / 180 * 3.143))+5, new_paint);
	    
	    canvas.drawText("S", (float) (xPoint - new_radius
	            * Math.sin((double) (-myddirection) / 180 * 3.143)), (float) (yPoint + new_radius
	    	            * Math.cos((double) (-myddirection) / 180 * 3.143))+5, new_paint);
	    
	  
	    
	    /*if(myddirection>0 && myddirection <45)
	    {
	    	 canvas.drawText("N", xPoint, yPoint-20, mydPaint);
	 	    canvas.drawText("S", xPoint, yPoint+28, mydPaint);
	 	    canvas.drawText("W", xPoint-28, yPoint, mydPaint);
	 	    canvas.drawText("E", xPoint+20, yPoint, mydPaint);
	    }
	    else if(myddirection >=45 && myddirection <135)
	    {
	    	 canvas.drawText("E", xPoint, yPoint-20, mydPaint);
	 	    canvas.drawText("W", xPoint, yPoint+28, mydPaint);
	 	    canvas.drawText("N", xPoint-28, yPoint, mydPaint);
	 	    canvas.drawText("S", xPoint+20, yPoint, mydPaint);
	    }
	    else if(myddirection >=135 && myddirection < 225)
	    {
	    	 canvas.drawText("S", xPoint, yPoint-20, mydPaint);
	 	    canvas.drawText("N", xPoint, yPoint+28, mydPaint);
	 	    canvas.drawText("E", xPoint-28, yPoint, mydPaint);
	 	    canvas.drawText("W", xPoint+20, yPoint, mydPaint);
	    }
	    else if(myddirection >=225 && myddirection <315)
	    {
	    	 canvas.drawText("W", xPoint, yPoint-20, mydPaint);
	 	    canvas.drawText("E", xPoint, yPoint+28, mydPaint);
	 	    canvas.drawText("S", xPoint-28, yPoint, mydPaint);
	 	    canvas.drawText("N", xPoint+20, yPoint, mydPaint);
	    }
	    else
	    {
	    	 canvas.drawText("N", xPoint, yPoint-20, mydPaint);
		 	    canvas.drawText("S", xPoint, yPoint+28, mydPaint);
		 	    canvas.drawText("W", xPoint-28, yPoint, mydPaint);
		 	    canvas.drawText("E", xPoint+20, yPoint, mydPaint);
	    }*/

	   // canvas.drawText(String.valueOf(myddirection), xPoint, yPoint, mydPaint);
	}

	//Set Direction of compass
	public void setDirection(float direction)
	{
		this.myddirection = direction;
		this.invalidate();
	}

}
