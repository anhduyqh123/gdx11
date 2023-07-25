package GDX11.IObject.IActor;

import GDX11.Asset;
import GDX11.GDX;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class IImage extends IActor{
    private transient final String key = "texture";
    public String texture = "";
    public int left,right,top,bottom;

    public IImage()
    {
        iSize.width = "dw";//default of width
        iSize.height = "dh";//default of height
    }
    //IActor
    @Override
    protected Actor NewActor() {
        return new Image(){
            @Override
            public void act(float delta) {
                super.act(delta);
                OnUpdate(delta);
            }

            @Override
            public void draw(Batch batch, float parentAlpha) {
                OnDraw(batch,parentAlpha,()->super.draw(batch, parentAlpha));
            }

            @Override
            public boolean remove() {
                OnRemove();
                return super.remove();
            }
        };
    }

    @Override
    public void Connect() {
        super.Connect();
        iParam.Set("dw",(GDX.Func<Object>) () ->(float)GetTexture().getRegionWidth());
        iParam.Set("dh",(GDX.Func<Object>) () ->(float)GetTexture().getRegionHeight());
    }

    //IImage
    protected boolean IsNinePath()
    {
        return left+right+top+bottom>0;
    }
    public void RefreshContent()
    {
        iParam.SetChangeEvent(key,()-> SetDrawable(NewDrawable()));
        SetDrawable(NewDrawable());
    }
    protected Drawable NewDrawable()
    {
        TextureRegion tr = GetTexture();
        if (IsNinePath()) return new NinePatchDrawable(new NinePatch(tr,left,right,top,bottom));
        return NewDrawable(tr);
    }
    public String GetTextureName()
    {
        if (iParam.Has(key)) return iParam.Get(key);
        return texture;
    }
    public TextureRegion GetTexture()
    {
        return GDX.Try(()->Asset.i.GetTexture(GetTextureName()),
                ()->new TextureRegion(Asset.GetEmptyTexture()));
    }
    public void SetTexture(String name)
    {
        SetTexture(Asset.i.GetTexture(name));
    }
    public void SetTexture(Texture texture)
    {
        SetTexture(new TextureRegion(texture));
    }
    public void SetTexture(TextureRegion texture)
    {
        SetDrawable(NewDrawable(texture));
    }
    public void SetDrawable(Drawable drawable)
    {
        Image img = GetActor();
        img.setDrawable(drawable);
    }
    //Drawable
    public static Drawable NewDrawable(Texture texture)
    {
        return NewDrawable(new TextureRegion(texture));
    }
    public static Drawable NewDrawable(TextureRegion textureRegion)
    {
        return new TextureRegionDrawable(textureRegion);
    }
    public static Drawable NewDrawable(NinePatch ninePath)
    {
        return new NinePatchDrawable(ninePath);
    }
}
