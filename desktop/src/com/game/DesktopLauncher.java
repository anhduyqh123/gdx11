package com.game;

import GDX11.Json;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.JsonValue;

import java.io.File;
import java.nio.file.Path;

// Please note that on macOS your application needs to be started with the -XstartOnFirstThread JVM argument
public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = 720;
		config.height = 1280;

		File file = Path.of("").toAbsolutePath().getParent().toFile();
		File android = new File(file.getPath()+"/android/assets");
		File cf = new File(android.getPath()+"/config.json");
		FileHandle cfx = new FileHandle(cf);
		//new LwjglApplication(new MyGame(), config);

	}
}
