package com.game;

import DrinkGame.Game.Crocodile.Crocodile;
import GDX11.Asset;
import GDX11.GDXGame;
import SDK.SDK;
import Wolvesville.Wolves;

import java.util.Arrays;
import java.util.Collection;

public class MyGame extends GDXGame {

    @Override
    protected void FirstLoad() {
        Asset.i.LoadPackages(()->new Crocodile(),"crocodile");
    }

    @Override
    protected Collection<String> GetFirstPacks() {
        return Arrays.asList("first","default");
    }
}
