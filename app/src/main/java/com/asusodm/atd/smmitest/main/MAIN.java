package com.asusodm.atd.smmitest.main;

import com.wingtech.diagnostic.activity.MainActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class MAIN extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		Log.d("asusMainActivity","enter...");
		Intent intent = new Intent(MAIN.this,MainActivity.class);
		startActivity(intent);
		finish();
	}

}