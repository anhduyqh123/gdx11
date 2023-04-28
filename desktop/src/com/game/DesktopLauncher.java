package com.game;

import GDX11.GDXGame;
import GDX11.Json;
import GDX11.Scene;
import GDX11.Screen;
import JigsawWood.Screen.GameScreen;
import SDK.SDK;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
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
		new LwjglApplication(new GDXGame(){
			@Override
			protected void FirstLoad() {
				Screen screen = new GameScreen();
				screen.Show();
				screen.FindActor("bg").debug();
				Scene.i.GetStage().addListener(new InputListener(){
					@Override
					public boolean keyTyped(InputEvent event, char character) {
						if (character=='a')
						{
							Scene.i.ui.clearChildren();
							new GameScreen().Show();
						}
						return true;
					}
				});
			}
		}, config);

	}
}
