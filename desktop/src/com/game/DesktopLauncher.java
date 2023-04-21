package com.game;

import GDX11.Json;
import SDK.SDK;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.JsonValue;

import java.io.File;
import java.nio.file.Path;
import java.util.*;

// Please note that on macOS your application needs to be started with the -XstartOnFirstThread JVM argument
public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = 720;
		config.height = 1280;

		SDK.SetDesktopSDK();
		//new LwjglApplication(new MyGame(), config);
		JsonValue js = new JsonValue(JsonValue.ValueType.object);
		js.addChild("aaa",new JsonValue("xxx"));
		js.addChild("bbb",new JsonValue("yyy"));
		System.out.println(js);
		JsonValue i = js.get("bbb");
		System.out.println(i);
		i.set("zzz");
		System.out.println(js);

	}
}
