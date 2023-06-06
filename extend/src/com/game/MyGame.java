package com.game;

import GDX11.GDXGame;
import JigsawWood.JigsawWood;
import SDK.SDK;
import Wolvesville.Screen.PlayerScreen;
import Wolvesville.Wolvesville;

import java.util.Arrays;
import java.util.Collection;

public class MyGame extends GDXGame {

    @Override
    protected void FirstLoad() {
        new Wolvesville();
    }

    @Override
    protected Collection<String> GetFirstPacks() {
        return Arrays.asList("default");
    }
}
