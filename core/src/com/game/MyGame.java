package com.game;

import GDX11.GDXGame;
import GDX11.IObject.IActor.IActor;
import GDX11.Scene;

public class MyGame extends GDXGame {
    public void LoadAssetData() //need to Override
    {
        asset.SetData(GetGameData(true));
        asset.LoadPackages(()->{
            //done loading
            IActor iActor = new IActor();
            iActor.iSize.width = "200";
            iActor.iSize.height = "200";
            iActor.SetIRoot(Scene.i.ui);
            iActor.Refresh();
            iActor.GetActor().debug();
            scene.GetStage().addActor(iActor.GetActor());

        },"first");//load first package
    }
}
