package com.game;

import GDX11.Asset;
import GDX11.GDX;
import SDK.SDK;

import android.os.Bundle;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;


public class AndroidLauncher extends AndroidApplication {
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		SDK.SetSDK(new AndroidSDK(this));

		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		initialize(new MyGame(), config);
	}
}
