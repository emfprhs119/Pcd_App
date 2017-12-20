package com.min.pcd_app;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	EditText pcdT;
	EditText numT;
	Button button, pcdButton, angleButton;
	TextView tv, tv1;
	Switch roSwitch;
	DrawPcd calcView;
	float pcd;
	int num;
	RelativeLayout container;
	Button.OnClickListener click;
	Typeface typeface;
	boolean pcdAngle;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE); // remove titlebar
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		setView();
	}

	void setView() {
		pcdAngle = false;
		container = (RelativeLayout) findViewById(R.id.container);
		typeface = Typeface.createFromAsset(getAssets(), "Braxton.otf");
		click = new Click();
		DisplayMetrics displayMetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
		int deviceWidth = displayMetrics.widthPixels;
		int deviceHeight = displayMetrics.heightPixels;
		calcView = new DrawPcd(this, deviceWidth, deviceHeight);
		calcView.setZ(-5);
		calcView.pcdCalc(0, 0, false);
		container.addView(calcView);
		pcdT = (EditText) findViewById(R.id.pcdText);
		numT = (EditText) findViewById(R.id.numText);
		button = (Button) findViewById(R.id.calcButton);
		pcdButton = (Button) findViewById(R.id.pcdButton);
		angleButton = (Button) findViewById(R.id.angleButton);
		roSwitch = (Switch) findViewById(R.id.roSwitch);
		tv1 = (TextView) findViewById(R.id.textView1);
		tv = (TextView) findViewById(R.id.TextView01);
		tv.setTypeface(typeface);
		tv1.setTypeface(typeface);
		pcdT.setTypeface(typeface);
		numT.setTypeface(typeface);
		button.setTypeface(typeface);
		pcdButton.setTypeface(typeface);
		pcdButton.setWidth(deviceWidth / 2);
		angleButton.setTypeface(typeface);
		angleButton.setWidth(deviceWidth / 2);
		roSwitch.setTypeface(typeface);
		roSwitch.setOnClickListener(click);
		button.setOnClickListener(click);
		container.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(pcdT.getWindowToken(), 0);
				imm.hideSoftInputFromWindow(numT.getWindowToken(), 0);
			}
		});
		container.setOnTouchListener(new OnTouchListener() {
			float x,y;
            @Override
            public boolean onTouch(View v, MotionEvent event) {
            	InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(pcdT.getWindowToken(), 0);
				imm.hideSoftInputFromWindow(numT.getWindowToken(), 0);
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    // 버튼을 눌렀을 때
                	x=event.getX();
                	y=event.getY();
                } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
                    // 버튼을 누른 상태로 움직이고 있을 때
                	calcView.move((int)(event.getX()-x), (int)(event.getY()-y));
                	calcView.invalidate();
                	x=event.getX();
                	y=event.getY();
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    // 버튼에서 손을 떼었을 때
                }
                // 이벤트 처리를 여기서 완료했을 때 
                // 다른곳에 이벤트를 넘기지 않도록
                // 리턴값을 true 로 준다
                // 리턴값이 있음
                return true;
            }
        });
		pcdButton.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				tv.setText("Circle        ");
				pcdAngle = false;
			}

		});
		angleButton.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				tv.setText("Angle        ");
				pcdAngle = true;
			}

		});

	}

	class Click implements Button.OnClickListener {
		@Override
		public void onClick(View v) {
			InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(pcdT.getWindowToken(), 0);
			imm.hideSoftInputFromWindow(numT.getWindowToken(), 0);
			// get pcd & num
			try {
				pcd = Float.parseFloat(pcdT.getText().toString());
				num = Integer.parseInt(numT.getText().toString());
			} catch (NumberFormatException e) {
				Toast.makeText(getApplicationContext(), "숫자를 입력해주세요.", Toast.LENGTH_LONG).show();
				return;
			}
			// calc and refresh
			if (!pcdAngle)
				calcView.pcdCalc(pcd, num, roSwitch.isChecked());
			else
				calcView.angleCalc(pcd, num);
			calcView.invalidate();
			container.invalidate();
		}

	}
}
