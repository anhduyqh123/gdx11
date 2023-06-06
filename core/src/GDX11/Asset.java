package GDX11;

import GDX11.AssetData.AssetData;
import GDX11.AssetData.AssetNode;
import GDX11.AssetData.AssetPackage;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.BitmapFontLoader;
import com.badlogic.gdx.assets.loaders.ParticleEffectLoader;
import com.badlogic.gdx.assets.loaders.TextureLoader;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;

import java.util.*;

public class Asset extends Actor {
    public static Asset i;

    private final HashSet<String> packLoaded = new HashSet<>();// loaded package
    private final Map<String, AssetNode> mapAssets = new HashMap<>(); //loaded node
    public AssetData data;
    public AssetManager manager = new AssetManager();

    private Runnable doneLoading;
    private GDX.Runnable1<Float> cbProgress;

    public Asset()
    {
        i = this;
        InitLoader();
    }
    public void SetData(AssetData data)
    {
        this.data = data;
        data.Install();
    }
    protected void InitLoader()
    {
        manager.setLoader(Texture.class,new GTextureLoader(manager.getFileHandleResolver()));
    }

    @Override
    public void act(float delta) {
        if (doneLoading==null) return;
        if (manager.update()) {
            Runnable done = doneLoading;
            doneLoading = null;
            done.run();
        }
        if (cbProgress !=null) cbProgress.Run(manager.getProgress());
    }
    public void Dispose()
    {
        manager.dispose();
    }

    //LoadAsset
    private void LoadAssets(List<AssetNode> list)
    {
        for (AssetNode as : list)
            Load(as);
    }
    public void Load(AssetNode as)
    {
        if (manager.isLoaded(as.url)) return;
        switch (as.kind)
        {
            case TextureAtlas:
                manager.load(as.url, TextureAtlas.class);
                break;
            case Texture:
                TextureLoader.TextureParameter pepTexture = new TextureLoader.TextureParameter();
                pepTexture.minFilter = Texture.TextureFilter.Linear;
                pepTexture.magFilter = Texture.TextureFilter.Linear;
                manager.load(as.url, Texture.class,pepTexture);
                break;
            case Sound:
                manager.load(as.url, Sound.class);
                break;
            case Music:
                manager.load(as.url, Music.class);
                break;
            case BitmapFont:
                BitmapFontLoader.BitmapFontParameter pepBitmap = new BitmapFontLoader.BitmapFontParameter();
                pepBitmap.minFilter = Texture.TextureFilter.Linear;
                pepBitmap.magFilter = Texture.TextureFilter.Linear;
                pepBitmap.loadedCallback = (assetManager, fileName, type) -> {
                    manager.get(as.url, BitmapFont.class).getData().markupEnabled = true;
                };
                manager.load(as.url, BitmapFont.class,pepBitmap);
                break;
            case Particle:
                ParticleEffectLoader.ParticleEffectParameter pepParticle=  new ParticleEffectLoader.ParticleEffectParameter();
                AssetPackage pack = GetAssetPackage(as.pack);
                if (pack.Contain("particle"))
                    pepParticle.atlasFile = pack.Get("particle").url;
                manager.load(as.url, ParticleEffect.class,pepParticle);
                break;
            default:
                DefaultLoad(as);
        }
    }
    protected void DefaultLoad(AssetNode node)
    {

    }
    public List<AssetNode> GetLoaded(AssetNode.Kind kind)
    {
        List<AssetNode> list = new ArrayList<>();
        for(AssetNode n : mapAssets.values())
            if (n.kind==kind) list.add(n);
        return list;
    }

    public void PushMapAssetNode(String pack)
    {
        for(AssetNode n : GetAssetPackage(pack).assetNodes)
            mapAssets.put(n.name,n);
    }

    private void ForceLoadPackage(String pack)
    {
        packLoaded.add(pack);
        PushMapAssetNode(pack);
        Util.For(GetAssetPackage(pack).loadableNode,this::Load);
    }
    private void LoadPackage(String pack)
    {
        if (!data.Contains(pack)) return;
        if (packLoaded.contains(pack)) return;
        ForceLoadPackage(pack);
    }
    public void ForceLoadPackages(Runnable done, String... packs){
        for(String pack : packs) ForceLoadPackage(pack);
        manager.finishLoading();
        if(done!=null) done.run();
    }
    public void LoadPackages(Runnable done, String... packs){
        for(String pack : packs) LoadPackage(pack);
        manager.finishLoading();
        if(done!=null) done.run();
    }
    public void LoadPackagesSync(GDX.Runnable1<Float> cbProgress, Runnable onLoaded, String... packs)
    {
        for(String pack : packs) LoadPackage(pack);
        doneLoading = onLoaded;
        this.cbProgress = cbProgress;
    }

    public void UnloadPackage(String pack)
    {
        if (!data.Contains(pack)) return;
        RemovePackage(pack);
        for (AssetNode n : GetAssetPackage(pack).loadableNode)
        {
            if (manager.contains(n.url))
                manager.unload(n.url);
        }
    }
    public void UnloadPackages(String... packs)
    {
        for (String pack : packs) UnloadPackage(pack);
    }
    public void RemovePackage(String pack)
    {
        packLoaded.remove(pack);
    }

    //get value
    public TextureRegion GetTexture(String name)
    {
        if (name.equals("")) return new TextureRegion(emptyTexture);
        AssetNode node = GetNode(name);
        AssetPackage pack = GetAssetPackage(node.pack);
        if (pack.Contain(node.atlas))
        {
            AssetNode al = pack.Get(node.atlas);
            return manager.get(al.url, TextureAtlas.class).findRegion(name);
        }
        return new TextureRegion(Get(name, Texture.class));
    }

    public BitmapFont GetFont(String name)
    {
        AssetNode node = GetNode(name);
        return node==null?new BitmapFont():Get(name, BitmapFont.class);
    }
    public Sound GetSound(String name)
    {
        return Get(name, Sound.class);
    }
    public Music GetMusic(String name)
    {
        return Get(name, Music.class);
    }
    public ParticleEffect GetParticleEffect(String name)
    {
        return Get(name, ParticleEffect.class);
    }
    public <T> T Get(String name,Class<T> type){
        return manager.get(GetNode(name).url,type);
    }
    public AssetNode GetNode(String name)
    {
        return mapAssets.get(name);
    }
    public AssetPackage GetAssetPackage(String name)
    {
        return data.Get(name);
    }
    public List<AssetNode> Get(AssetNode.Kind kind)
    {
        List<AssetNode> list = new ArrayList<>();
        data.For(p->{
            for(AssetNode n : p.loadableNode)
                if (n.kind==kind) list.add(n);
        });
        return list;
    }

    //static
    public static Texture emptyTexture = NewTexture(Color.WHITE,100,100);
    private static Texture NewTexture(Color color, float width, float height)
    {
        Pixmap pixmap = new Pixmap((int)width, (int)height, Pixmap.Format.RGBA8888);
        pixmap.setColor(color);
        pixmap.fillRectangle(0, 0, (int)width, (int)height);
        Texture texture = new Texture(pixmap);
        pixmap.dispose();
        return texture;
    }
}
