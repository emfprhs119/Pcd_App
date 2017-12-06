package com.min.pcd_app;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class MainActivity extends Activity {
	EditText pcdT;
	EditText numT;
	Button button;
	DrawPcd calcView;
	float pcd;
	int num;
	RelativeLayout container;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);	// remove titlebar
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		setView();
	}
	
	void setView(){
		container=(RelativeLayout) findViewById(R.id.container);
		calcView=new DrawPcd(this);
		calcView.angleCalc(0,1);
		container.addView(calcView);
		pcdT = (EditText)findViewById(R.id.pcdText);
		numT = (EditText)findViewById(R.id.numText);
		button = (Button)findViewById(R.id.calcButton);
		button.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				// remove inputpad
				InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(pcdT.getWindowToken(),0);
				imm.hideSoftInputFromWindow(numT.getWindowToken(),0);
				// get pcd & num
				try{
				pcd=Float.parseFloat(pcdT.getText().toString());
				num=Integer.parseInt(numT.getText().toString());
				}catch(NumberFormatException e){
					Toast.makeText(getApplicationContext(), "숫자를 입력해주세요.", Toast.LENGTH_LONG).show();
					return;
				}
				// calc and refresh
				calcView.angleCalc(pcd,num);
				calcView.invalidate();
				container.invalidate();
			}
		});
	}
}
