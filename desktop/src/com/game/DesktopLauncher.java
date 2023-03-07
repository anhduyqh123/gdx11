package com.game;

import GDX11.GDX;
import GDX11.IObject.IParam;
import GDX11.Json;
import com.badlogic.gdx.utils.JsonValue;

// Please note that on macOS your application needs to be started with the -XstartOnFirstThread JVM argument
public class DesktopLauncher {
	public static void main (String[] arg) {
//		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
//		config.setForegroundFPS(60);
//		config.setTitle("gdx11");
//		new Lwjgl3Application(new MyGame(), config);

		//System.out.println(Math.pow(10,2));
		Test a = new Test();
		a.num = 2;
		//a.iRun.SetRun("123",()->{});
		JsonValue js = Json.ToJson(a);
		System.out.println(js);

	}
}
