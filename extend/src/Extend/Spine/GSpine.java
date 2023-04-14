package Extend.Spine;

import GDX11.GDX;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.esotericsoftware.spine.*;
import com.esotericsoftware.spine.utils.SkeletonActor;
import com.esotericsoftware.spine.utils.SkeletonActorPool;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GSpine extends SkeletonActor {
    private static Map<SkeletonData, SkeletonActorPool> poolMap = new HashMap<>();
    private static SkeletonRenderer renderer = new SkeletonRenderer();
    {
        renderer.setPremultipliedAlpha(true);
    }
    private static void NewPool(SkeletonData data)
    {
        if (poolMap.containsKey(data)) return;
        poolMap.put(data,new SkeletonActorPool(renderer,data,new AnimationStateData(data)){
            @Override
            protected SkeletonActor newObject() {
                SkeletonActor actor = new GSpine();
                actor.setRenderer(renderer);
                return actor;
            }
        });
    }
    private static void Free(SkeletonActor actor)
    {
        poolMap.get(actor.getSkeleton().getData()).free(actor);
    }
    public static <T extends SkeletonActor> T NewSpine(SkeletonData data)
    {
        if (!poolMap.containsKey(data)) NewPool(data);
        return (T)poolMap.get(data).obtain();
    }

    public GDX.Runnable1<Float> onUpdate;
    public GDX.Runnable3<Batch,Float,Runnable> onDraw;
    public Runnable onRemove;
    @Override
    public void act(float delta) {
        if (getSkeleton()==null) return;
        super.act(delta);
        onUpdate.Run(delta);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        if (getSkeleton()==null) return;
        onDraw.Run(batch,parentAlpha,()->super.draw(batch, parentAlpha));
    }

    @Override
    public boolean remove() {
        onRemove.run();
        Free(this);
        return super.remove();
    }
    public void SetSkin(String name)
    {
        SkeletonData data = getSkeleton().getData();
        Skin skin = data.findSkin(name);
        if (skin==null) skin = data.getSkins().get(0);
        getSkeleton().setSkin(skin);
        //getSkeleton().setDrawOrder(); //check???
    }
    public AnimationState.TrackEntry SetAnimation(String name, boolean loop)
    {
        return getAnimationState().setAnimation(1,name,loop);
    }
    public AnimationState.TrackEntry SetAnimation(String name, Runnable done)
    {
        AnimationState.TrackEntry track = SetAnimation(name,false);
        track.setListener(new AnimationState.AnimationStateAdapter() {
            @Override
            public void complete(AnimationState.TrackEntry entry) {
                done.run();
            }
        });
        return track;
    }
    public AnimationState.TrackEntry SetAnimation(String name, String idle)
    {
        return SetAnimation(name,()->SetAnimation(idle,true));
    }

    public String[] GetAnimationNames()
    {
        SkeletonData data = getSkeleton().getData();
        if (data==null) return new String[]{""};
        List<String> list = new ArrayList<>();
        for(Animation a : data.getAnimations())
            list.add(a.getName());
        return list.toArray(new String[0]);
    }
    public String[] GetSkinNames()
    {
        SkeletonData data = getSkeleton().getData();
        if (data==null) return new String[]{""};
        List<String> list = new ArrayList<>();
        for (Skin s : data.getSkins())
            list.add(s.getName());
        return list.toArray(new String[0]);
    }
}
