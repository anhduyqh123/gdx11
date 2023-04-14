package Extend.Spine;

import GDX11.Asset;
import GDX11.AssetData.AssetNode;
import com.esotericsoftware.spine.SkeletonData;

public class SpineAsset extends Asset {

    public SpineAsset()
    {
        super();
        manager.setLoader(SkeletonData.class,new SkeletonDataLoader(manager.getFileHandleResolver()));
    }
    @Override
    protected void DefaultLoad(AssetNode node) {
        if (node.kind== AssetNode.Kind.Spine)
        {
            String atlas = node.url.replace(node.extension, "atlas");
            SkeletonDataLoader.SkeletonDataLoaderParameter pep =
                    new SkeletonDataLoader.SkeletonDataLoaderParameter(atlas);
            manager.load(node.url,SkeletonData.class,pep);
        }
    }
}
