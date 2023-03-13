package com.game;

import GDX11.Config;
import GDX11.GDX;
import GDX11.IObject.IActor.IActor;
import GDX11.IObject.IComponent.IComponent;
import GDX11.IObject.IObject;
import GDX11.Json;
import GDX11.Reflect;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.reflect.Field;

// Please note that on macOS your application needs to be started with the -XstartOnFirstThread JVM argument
public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = 360;
		config.height = 720;

		IComponent iComponent  = new IComponent();
		Field field = Reflect.GetField(IComponent.class,"getIActor");
		GDX.Func<IActor> getIActor = Reflect.GetValue(field,iComponent);
		System.out.println(field.getName());

		//new LwjglApplication(new MyGame(), config);

	}
}
