package com.game;

import Blackjack.Blackjack;
import GDX11.AssetData.AssetData;
import GDX11.GDX;
import GDX11.GDXGame;
import GDX11.IObject.IActor.IActor;
import GDX11.Json;
import GDX11.Scene;
import com.badlogic.gdx.utils.JsonWriter;

public class MyGame extends GDXGame {
    public void LoadAssetData() //need to Override
    {
        asset.SetData(GetGameData(false));
        asset.LoadPackages(()->{
            //done loading
            new Blackjack();
        },"first","default","theme0");//load first package
    }
    @Override
    protected AssetData LoadPackages(String path) {
        AssetData data = new AssetData();
        data.LoadPackages();
        GDX.WriteToFile(path, Json.ToJson(data).toJson(JsonWriter.OutputType.minimal));
        return data;
    }
}
