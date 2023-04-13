package com.game;

import GDX11.Json;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.JsonValue;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

// Please note that on macOS your application needs to be started with the -XstartOnFirstThread JVM argument
public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = 720;
		config.height = 1280;

//		String path = Path.of("").toAbsolutePath().getParent().toString();
//		FileHandle rootDir = new FileHandle(path+"/assets");
//		FileHandle androidDir = new FileHandle(path+"/android/assets");
//		androidDir.child("first").deleteDirectory();
		//System.out.print(Path.of("").toAbsolutePath().getParent().toString());
		List<String> list = new ArrayList<>(Arrays.asList("0","1","2"));
		list.set(1,"x");
		System.out.println(list);

		//new LwjglApplication(new MyGame(), config);
	}
}
