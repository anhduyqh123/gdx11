package com.game;

import Blackjack.Blackjack;
import GDX11.GDXGame;
import SDK.SDK;

import java.util.Arrays;
import java.util.Collection;

public class MyGame extends GDXGame {

    @Override
    protected void FirstLoad() {
        SDK.SetSDK(SDK.i);
        new Blackjack();
    }

    @Override
    protected Collection<String> GetFirstPacks() {
        return Arrays.asList("first","default","theme0");
    }
}
