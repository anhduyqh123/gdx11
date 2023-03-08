package com.game;

import GDX11.GDX;
import GDX11.IObject.IActor.IActor;
import GDX11.IObject.IMap;
import GDX11.IObject.IParam;
import GDX11.Json;
import GDX11.Reflect;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.reflect.Field;

import java.util.Map;

// Please note that on macOS your application needs to be started with the -XstartOnFirstThread JVM argument
public class DesktopLauncher {
	public static void main (String[] arg) {
//		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
//		config.setForegroundFPS(60);
//		config.setTitle("gdx11");
//		new Lwjgl3Application(new MyGame(), config);

		//System.out.println(Math.pow(10,2));
		String data = "{list:[{class:GDX11.IObject.IActor.IImage,texture:badlogic,name:img}]}";
		IMap<IActor> iMap = Json.FromJson(IMap.class,data);
		//Map<String, Field> map = Reflect.GetFields(IMap.class);
		System.out.println(iMap.GetMap().keySet());

	}
}
