package com.game;

import Blackjack.Blackjack;
import GDX11.GDXGame;

import java.util.Arrays;
import java.util.Collection;

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
