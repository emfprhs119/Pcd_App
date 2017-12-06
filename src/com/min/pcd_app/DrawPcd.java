package com.min.pcd_app;

import java.util.Set;
import java.util.TreeSet;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.view.View;

class Point {
	float height;
	float width;

	public Point(float height, float width) {
		this.height = height;
		this.width = width;
	}
}

public class DrawPcd extends View {
	
	final int centerX = 1080 / 2;
	final int centerY = 1920 / 5*3;
	final int rad = 1080 / 3;
	private Paint paint;
	Point pointArr[];
	float pcd_rad=0;
	int num=0;
	Set<Float> xSet, ySet;

	public DrawPcd(Context context) {
		super(context);
		paint = new Paint();
		// paint config
		paint.setAntiAlias(true);
		paint.setColor(Color.BLACK);
		paint.setStyle(Style.STROKE);
		paint.setTextSize(30);
		paint.setTextAlign(Align.CENTER);
		// set init
		xSet = new TreeSet<Float>();
		ySet = new TreeSet<Float>();
	}

	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		canvas.drawCircle(centerX, centerY, rad, paint);
		for (int i = 0; i < num; i++) {
			canvas.drawCircle(centerX + (float) pointArr[i].width * rad, centerY + (float) pointArr[i].height * rad, 30,
					paint);
		}

		xSet.clear();
		ySet.clear();
		for (int i = 0; i < num; i++) {
			drawSizeText(canvas, i);
		}

	}

	void drawSizeText(Canvas canvas, int i) {
		float height =(pointArr[i].height * rad);
		float width = (pointArr[i].width * rad);

		// xSize
		if (xSet.add(Math.abs(width)) && width != 0.0) {
			canvas.drawLine(centerX, centerY + height, centerX + width, centerY + height, paint);
			canvas.drawText("" + String.format("%.3f",Math.abs(width)/360*pcd_rad), centerX + width / 2, centerY + height + 30, paint);
		}
		// ySize
		if (ySet.add(Math.abs(height)) && height != 0.0) {
			canvas.drawLine(centerX + width, centerY, centerX + width, centerY + height, paint);
			canvas.drawText("" + String.format("%.3f", Math.abs(height)/360*pcd_rad), centerX + width + 30, centerY + height / 2, paint);
		}
	}

	// circle angle calc
	public void angleCalc(float pcd, int num) {
		this.num = num;
		this.pcd_rad=pcd/2;
		pointArr = new Point[num];

		for (int i = 0; i < num; i++) {
			double heightd = Math.sin(2 * Math.PI / num * i + 3 * Math.PI / 2);
			double widthd = Math.cos(2 * Math.PI / num * i + 3 * Math.PI / 2);
			float heightf = Float.parseFloat(String.format("%.3f", heightd));
			float widthf = Float.parseFloat(String.format("%.3f", widthd));
			pointArr[i] = new Point(heightf, widthf);
		}

	}

}