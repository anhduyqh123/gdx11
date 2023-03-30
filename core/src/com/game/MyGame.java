package com.game;

import Blackjack.Blackjack;
import GDX11.AssetData.AssetData;
import GDX11.GDX;
import GDX11.GDXGame;
import GDX11.IObject.IActor.IActor;
import GDX11.Json;
import GDX11.Scene;
import com.badlogic.gdx.utils.JsonWriter;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class MyGame extends GDXGame {

    @Override
    protected void FirstLoad() {
        new Blackjack();
    }

    @Override
    protected Collection<String> GetFirstPacks() {
        return Arrays.asList("first","default","theme0");
    }
}
