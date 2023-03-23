package GDX11.IObject.IActor;

import GDX11.Actors.ScrollImage;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class IScrollImage extends IImage{
    public float speed,scroll;
    public boolean isScrollX,isScrollY,keepSize;
    @Override
    protected Actor NewActor() {
        return new ScrollImage();
    }

    @Override
    public void RefreshContent() {
        SetTexture(GetTexture());
    }

    @Override
    public void SetTexture(TextureRegion texture) {
        ScrollImage img = GetActor();
        img.speed = speed;
        img.isScrollX = isScrollX;
        img.isScrollY = isScrollY;
        img.keepSize = keepSize;
        img.SetTexture(texture);
        img.SetValue(scroll);
    }
}
