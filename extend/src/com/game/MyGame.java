package com.game;

import GDX11.GDXGame;
import Wolvesville.Wolves1;

import java.util.Arrays;
import java.util.Collection;

public class MyGame extends GDXGame {

    @Override
    protected void FirstLoad() {
        new Wolves1();
    }

    @Override
    protected Collection<String> GetFirstPacks() {
        return Arrays.asList("default");
    }
}
