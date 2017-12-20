package com.min.pcd_app;

import java.util.Iterator;
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
	double height;
	double width;

	public Point(double height, double width) {
		this.height = height;
		this.width = width;
	}
}

public class DrawPcd extends View {
	enum Arrow {
		up, down, rightUp, rightDown
	}
	
	int centerX = 1080 / 2 - 100;// 140
	int centerY = 1920 / 5 * 3 - 300;// 952
	int rad = 1000 / 3;// 266
	int circle = 15;
	int diff_turm = 60;
	
	int originX = 1080 / 2 - 100;
	int originY = 1920 / 5 * 3 - 300;
	double xRatio;
	double yRatio ;
	private Paint paint;
	Point pointArr[];
	double pcd_rad = 0;
	int num = 0;
	Set<Double> xSetUp, xSetDown, ySetUp, ySetDown;
	
	public DrawPcd(Context context, int deviceWidth, int deviceHeight) {
		super(context);
		xRatio = deviceWidth / (double) 1080;
		yRatio = deviceHeight / (double) 1920;
		rad = (int) (rad * xRatio);// 266
		circle = (int) (circle * xRatio);
		diff_turm = (int) (diff_turm * xRatio);

		paint = new Paint();
		// paint config
		paint.setStrokeWidth(5);
		paint.setColor(Color.BLACK);
		paint.setAntiAlias(true);
		paint.setTextSize(30);
		paint.setTextAlign(Align.CENTER);
		// set init
		xSetUp = new TreeSet<Double>();
		xSetDown = new TreeSet<Double>();
		ySetUp = new TreeSet<Double>();
		ySetDown = new TreeSet<Double>();
	}
	public void move(int x,int y){
		centerX+=x;
		centerY+=y;
	}
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		paint.setStyle(Style.STROKE);
		canvas.drawCircle(centerX, centerY, rad, paint);
		for (int i = 0; i < num; i++) {
			canvas.drawCircle(centerX + (float) pointArr[i].width * rad, centerY + (float) pointArr[i].height * rad,
					circle, paint);
		}
		paint.setStyle(Style.FILL);
		xSetUp.clear();
		xSetDown.clear();
		ySetUp.clear();
		ySetDown.clear();
		for (int i = 0; i < num; i++) {
			pushSet(pointArr[i]);
		}
		drawSet(canvas, xSetUp.iterator(), Arrow.up);
		drawSet(canvas, ySetUp.iterator(), Arrow.rightUp);
		if (num % 2 != 0) {
			drawSet(canvas, xSetDown.iterator(), Arrow.down);
			drawSet(canvas, ySetDown.iterator(), Arrow.rightDown);
		}
	}

	void drawSet(Canvas canvas, Iterator<Double> it, Arrow arrow) {
		double size;
		int diff = 0;
		int upDown = -1;
		boolean isRight = false;
		switch (arrow) {
		case rightUp:
			isRight = true;
		case up:
			upDown = -1;
			break;
		case rightDown:
			isRight = true;
		case down:
			upDown = 1;
			break;
		}
		diff = (rad + diff_turm) * upDown;
		while (it.hasNext()) {
			size = it.next();
			if (size == 0)
				continue;
			if (isRight) {
				diff = Math.abs(diff);
				
				canvas.drawText(String.format("%.2f", Math.abs(size) / rad * pcd_rad), (float)(centerX + diff), (float)(
						centerY + (Math.abs(size) / 2 * upDown)), paint);
				canvas.drawLine(centerX + diff, centerY, centerX + diff,
						(float)(centerY + ((size - (upDown < 0 ? diff_turm / 2 : diff_turm)) * upDown) / 2), paint);
				canvas.drawLine(centerX + diff,
						(float)(centerY + ((size + (upDown < 0 ? diff_turm : diff_turm / 2)) * upDown) / 2),centerX + diff,
						 (float)(centerY + size * upDown), paint);
				// assist line
				canvas.drawLine(centerX + diff, (float)(centerY + size * upDown), centerX, (float)(centerY + size * upDown), paint);
				canvas.drawLine(centerX + diff, centerY, centerX, centerY, paint);
				diff += diff_turm * 1.5;
			} else {
				canvas.drawText(String.format("%.2f", Math.abs(size) / rad * pcd_rad),(float)( centerX + Math.abs(size) / 2),
						centerY + (diff_turm / 2) * upDown + diff + (upDown < 0 ? diff_turm / 3 : 0), paint);
				canvas.drawLine(centerX, centerY + diff, (float)(centerX + size), centerY + diff, paint);
				// assist line
				canvas.drawLine((float)(centerX + size), centerY + diff, (float)(centerX + size), centerY, paint);
				canvas.drawLine(centerX, centerY + diff, centerX, centerY, paint);
				diff += diff_turm / 3 * 2 * upDown;
			}

		}
	}

	void pushSet(Point p) {
		double height = (p.height * rad);
		double width = (p.width * rad);
		if (height <= 0) {
			xSetUp.add(Math.abs(width));
			ySetUp.add(Math.abs(height));
		} else {
			xSetDown.add(Math.abs(width));
			ySetDown.add(Math.abs(height));
		}
	}

	// circle angle calc
	public void pcdCalc(double pcd, int num, boolean rotation) {
		centerX = (int) (originX * xRatio);// 140
		centerY = (int) (originY * yRatio);// 952
		this.num = num;
		this.pcd_rad = pcd / 2;
		pointArr = new Point[num];

		for (int i = 0; i < num; i++) {
			double heightf = Math.sin(2 * Math.PI / num * (i + (rotation ? 0.5f : 0)) + 3 * Math.PI / 2);
			double widthf = Math.cos(2 * Math.PI / num * (i + (rotation ? 0.5f : 0)) + 3 * Math.PI / 2);
			pointArr[i] = new Point(heightf, widthf);
		}

	}

	public void angleCalc(double pcd, int deg) {

		centerX = (int) (originX * xRatio);// 140
		centerY = (int) (originY * yRatio);// 952
		if (deg == 0)
			return;
		this.num = 1;
		this.pcd_rad = pcd / 2;
		pointArr = new Point[num];
		double heightd = Math.sin(2 * Math.PI / 360 * deg + 3 * Math.PI / 2);
		double widthd = Math.cos(2 * Math.PI / 360 * deg + 3 * Math.PI / 2);
		double heightf = Double.parseDouble(String.format("%.5f", heightd));
		double widthf = Double.parseDouble(String.format("%.5f", widthd));
		pointArr[0] = new Point(heightf, widthf);

	}

}