package com.game;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import com.google.android.play.core.assetpacks.AssetPackLocation;
import com.google.android.play.core.assetpacks.AssetPackManager;
import com.google.android.play.core.assetpacks.AssetPackManagerFactory;
import com.google.android.play.core.assetpacks.AssetPackState;
import com.google.android.play.core.assetpacks.AssetPackStateUpdateListener;
import com.google.android.play.core.assetpacks.AssetPackStates;
import com.google.android.play.core.assetpacks.model.AssetPackStatus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import GDX11.GDX;

public class AssetDelivery {
    private AssetManager assetManager;
    private long totalSizeToDownloadInBytes = 0;
    private AssetPackManager assetPackManager;
    private final String TAG = "AssetDelivery";
    private AssetPackState assetPackState;
    private Activity app;
    private Map<String, GDX.Runnable1<String>> doneMap = new HashMap<>();

    public AssetDelivery(Activity app){
        this.app = app;

        //initInstallTime();
    }
    public void Download(String pack, GDX.Runnable1<String> done){
        doneMap.put(pack, done);
        initAssetPackManager(pack);
    }
    /**
     * lifecycle method to unregister the listener
     */
    public void OnDestroy() {
        //ssetPackManager.unregisterListener(assetPackStateUpdateListener);
    }

    /**
     * start install-time delivery mode
     */
    private void initInstallTime() {
        try {
            Context context = app.createPackageContext("com.gdx11", 0);
            assetManager = context.getAssets();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * This method will get Instance of AssetPackManager For fast-follow & on-demand deliver mode
     */
    private void initAssetPackManager(String pack) {//check internet
        if (assetPackManager == null) {
            assetPackManager = AssetPackManagerFactory.getInstance(app.getApplicationContext());
        }
        registerListener(pack);
    }

    /**
     * This method will check Asset Pack is available on device or not,
     * if not available, it will register listener for it & start downloading by calling
     * fetch method.
     */
    private void registerListener(String pack) {
        String onDemandAssetPackPath = getAbsoluteAssetPath(pack);
        if (onDemandAssetPackPath == null) {
            assetPackManager.registerListener(StateUpdateListener(()->initOnDemand(pack)));
            List<String> assetPackList = new ArrayList<>();
            assetPackList.add(pack);
            assetPackManager.fetch(assetPackList);
        } else {
            initOnDemand(pack);
        }
    }

    /**
     * start on-demand delivery mode
     */
    private void initOnDemand(String pack) {
        String assetsPath = getAbsoluteAssetPath(pack);
        if (assetsPath == null) getPackStates(pack);
        else{
            Log.d("OnDemand_Path", assetsPath);
            doneMap.get(pack).Run(assetsPath);
        }
    }

    /**
     * AssetPackStateUpdateListener that listens to multiple events while downlading
     */
    private AssetPackStateUpdateListener StateUpdateListener(Runnable onCompleted){
        return state -> {
            switch (state.status()) {
                case AssetPackStatus.PENDING:
                    Log.i(TAG, "Pending");
                    break;

                case AssetPackStatus.DOWNLOADING:
                    long downloaded = state.bytesDownloaded();
                    long totalSize = state.totalBytesToDownload();
                    double percent = 100.0 * downloaded / totalSize;

                    Log.i(TAG, "PercentDone=" + String.format("%.2f", percent));
                    break;

                case AssetPackStatus.TRANSFERRING:
                    // 100% downloaded and assets are being transferred.
                    // Notify user to wait until transfer is complete.
                    break;

                case AssetPackStatus.COMPLETED:
                    // Asset pack is ready to use. Start the Game/App.
                    onCompleted.run();
                    break;

                case AssetPackStatus.FAILED:
                    // Request failed. Notify user.
                    Log.e(TAG, String.valueOf(state.errorCode()));
                    break;

                case AssetPackStatus.CANCELED:
                    // Request canceled. Notify user.
                    break;

                case AssetPackStatus.WAITING_FOR_WIFI:
                    //showWifiConfirmationDialog();
                    break;

                case AssetPackStatus.NOT_INSTALLED:
                    // Asset pack is not downloaded yet.
                    break;
                case AssetPackStatus.UNKNOWN:
                    // The Asset pack state is unknown
                    break;
            }

        };
    }

    /**
     * This method is used to Get download information about asset packs
     */
    private void getPackStates(String assetPackName) {
        assetPackManager.getPackStates(Collections.singletonList(assetPackName))
                .addOnCompleteListener(task -> {
                    AssetPackStates assetPackStates;
                    try {
                        assetPackStates = task.getResult();
                        assetPackState =
                                assetPackStates.packStates().get(assetPackName);

                        if (assetPackState != null) {
                            if (assetPackState.status() == AssetPackStatus.WAITING_FOR_WIFI) {
                                totalSizeToDownloadInBytes = assetPackState.totalBytesToDownload();
                                if (totalSizeToDownloadInBytes > 0) {
                                    long sizeInMb = totalSizeToDownloadInBytes / (1024 * 1024);
                                    if (sizeInMb >= 150) {
                                        //showWifiConfirmationDialog();
                                    } else {
                                        registerListener(assetPackName);
                                    }
                                }
                            }

                            Log.d(TAG, "status: " + assetPackState.status() +
                                    ", name: " + assetPackState.name() +
                                    ", errorCode: " + assetPackState.errorCode() +
                                    ", bytesDownloaded: " + assetPackState.bytesDownloaded() +
                                    ", totalBytesToDownload: " + assetPackState.totalBytesToDownload() +
                                    ", transferProgressPercentage: " + assetPackState.transferProgressPercentage());
                        }
                    } catch (Exception e) {
                        Log.d("MainActivity", e.getMessage());
                    }
                });
    }

    /**
     * @param assetPack         : Name of assetPack i.e : fast-follow asser pack or on-demand asset pack
     * @return absolute asset path as String
     */
    private String getAbsoluteAssetPath(String assetPack) {
        AssetPackLocation assetPackPath = assetPackManager.getPackLocation(assetPack);
        if (assetPackPath == null) {
            // asset pack is not ready
            return null;
        }
        return assetPackPath.assetsPath();
    }
}
