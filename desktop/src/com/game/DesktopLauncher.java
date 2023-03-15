package com.game;

import GDX11.*;
import GDX11.IObject.IActor.IActor;
import GDX11.IObject.IComponent.IComponent;
import GDX11.IObject.IObject;
import GDX11.IObject.IParam;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.reflect.Field;

import java.util.HashMap;
import java.util.Map;

// Please note that on macOS your application needs to be started with the -XstartOnFirstThread JVM argument
public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = 360;
		config.height = 720;

		new LwjglApplication(new MyGame(), config);

	}
	public static  <T> T GetValue(String stInit,String stFloat)
	{
		Object ob = GetInt(stInit)+GetFloat(stFloat);
		return (T)ob;
	}
	public static Integer GetInt(String st)
	{
		return Integer.parseInt(st);
	}
	public static Float GetFloat(String st)
	{
		return Float.parseFloat(st);
	}
}
