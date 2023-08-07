package com.game;

import DrinkGame.Game.BottleSpin.BottleSpin;
import DrinkGame.Game.Crocodile.Crocodile;
import DrinkGame.Game.Drinko.Drinko;
import DrinkGame.Game.Pirate.Pirate;
import DrinkGame.Game.SnakeLadder.SnakeLadder;
import DrinkGame.Game.Tiktaktoe.Tiktaktoe;
import GDX11.Asset;
import GDX11.GDXGame;
import SDK.SDK;
import Wolvesville.Wolves;

import java.util.Arrays;
import java.util.Collection;

public class MyGame extends GDXGame {

    @Override
    protected void FirstLoad() {
        Asset.i.LoadPackages(()->new BottleSpin(),"bottlespin");
    }

    @Override
    protected Collection<String> GetFirstPacks() {
        return Arrays.asList("first","default");
    }
}
