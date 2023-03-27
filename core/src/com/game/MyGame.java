package com.game;

import Blackjack.Blackjack;
import GDX11.AssetData.AssetData;
import GDX11.GDXGame;
import GDX11.IObject.IActor.IActor;
import GDX11.Scene;

public class MyGame extends GDXGame {
    public void LoadAssetData() //need to Override
    {
        asset.SetData(GetGameData(true));
        asset.LoadPackages(()->{
            //done loading
            new Blackjack();
        },"first","default","theme0");//load first package
    }
    @Override
    protected AssetData LoadPackages(String path) {
        AssetData data = new AssetData();
        data.LoadPackages();
        return data;
    }
}
