package com.game;

import GDX11.Asset;
import GDX11.GDX;
import SDK.SDK;
import android.app.Activity;
import android.util.Log;

import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.files.FileHandle;

public class AndroidSDK extends SDK {
    private Activity app;
    private AssetDelivery assetDelivery;
    public AndroidSDK(Activity app){
        this.app = app;
        assetDelivery = new AssetDelivery(app);
    }

    @Override
    public void DownloadPack(String pack, Runnable done) {
        assetDelivery.Download(pack, path->{
            Asset.i.SetPath(pack,path);
            done.run();
        });
    }
}
