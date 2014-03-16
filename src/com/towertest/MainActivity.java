package com.towertest;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import biz.abits.towertest.R;

public class MainActivity extends Activity {
	private ImageView treebtn;
	private ImageView grassbtn;
	private ImageView naturebtn;
	private ImageView humanbtn;
	private Button playbtn;
	private Button optionbtn;
	private ImageView titlebtn;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_layout);
		treebtn = (ImageView) findViewById(R.id.tree);
		grassbtn = (ImageView) findViewById(R.id.grass);
		naturebtn = (ImageView) findViewById(R.id.nature);
		humanbtn = (ImageView) findViewById(R.id.human);
		playbtn = (Button) findViewById(R.id.play);
		optionbtn = (Button) findViewById(R.id.option);
		titlebtn = (ImageView) findViewById(R.id.title);
		
		Animation fadeinAnim = AnimationUtils.loadAnimation(this, R.anim.fade_in);
		Animation slideinLeftAnim = AnimationUtils.loadAnimation(this, R.anim.slide_in_left);
		Animation slideinRightAnim = AnimationUtils.loadAnimation(this, R.anim.slide_in_right);
		Animation zoominAnim = AnimationUtils.loadAnimation(this, R.anim.zoom_in);
		
		grassbtn.startAnimation(fadeinAnim);

		titlebtn.setVisibility(View.INVISIBLE);
		titlebtn.startAnimation(zoominAnim);
		
		naturebtn.startAnimation(slideinLeftAnim);
		humanbtn.startAnimation(slideinRightAnim);
		
		playbtn.setAnimation(fadeinAnim);
		optionbtn.setAnimation(fadeinAnim);
		
		playbtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(MainActivity.this, GameActivity.class);
				startActivity(intent);
			}
		});
	}
}
