package com.game;

import GDX11.GDXGame;
import Wolvesville.Wolves;

import java.util.Arrays;
import java.util.Collection;

public class MyGame extends GDXGame {

    @Override
    protected void FirstLoad() {
        new Wolves();
    }

    @Override
    protected Collection<String> GetFirstPacks() {
        return Arrays.asList("default");
    }
}
