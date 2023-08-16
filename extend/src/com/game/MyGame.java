package com.game;

import DrinkGame.DrinkGame;
import DrinkGame.Game.PassOut;
import Extend.Spine.SpineAsset;
import GDX11.Asset;
import GDX11.GDXGame;
import GDX11.Scene;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;

import java.util.Arrays;
import java.util.Collection;

public class MyGame extends GDXGame {

    @Override
    protected void FirstLoad() {
        new DrinkGame();
    }

    @Override
    protected Collection<String> GetFirstPacks() {
        return Arrays.asList("first","default");
    }

    @Override
    protected Asset NewAssets() {
        return new SpineAsset();
    }

    @Override
    protected Scene NewScene() {
        return NewScene(new PolygonSpriteBatch());
    }
}
