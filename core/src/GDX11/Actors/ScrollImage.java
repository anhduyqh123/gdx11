package GDX11.Actors;

import GDX11.GDX;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class ScrollImage extends GSprite {
    public float scroll,speed;
    public boolean isScrollX,isScrollY,keepSize;
    protected float trWidth,trHeight;

    public ScrollImage(){}
    public void SetTexture(TextureRegion tr) //not from atlas
    {
        InitTexture(tr);
        GDX.PostRunnable(()->tr.getTexture().
                setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat));
    }
    protected void InitTexture(TextureRegion tr)
    {
        trWidth = tr.getRegionWidth();
        trHeight = tr.getRegionHeight();
        tRegion.setRegion(tr);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        if (speed!=0)
        {
            scroll += delta*speed;
            SetValue(scroll);
        }
    }
    public void SetValue(float percent) //percent 0->1
    {
        this.scroll = percent;
        if (scroll > 1.0f)  scroll = 0.0f;
        float percentX = 1;
        float percentY = 1;
        if (keepSize){
            percentX = getWidth()/trWidth;
            percentY = getHeight()/trHeight;
        }
        if (IsScroll()) Scroll(percentX,percentY,scroll);
    }
    protected boolean IsScroll()
    {
        return isScrollX || isScrollY;
    }
    protected void Scroll(float percentX,float percentY,float scroll)
    {
        if (isScrollX)
        {
            tRegion.setU(-scroll);
            tRegion.setU2(percentX-scroll);
        }
        if (isScrollY)
        {
            tRegion.setV(-scroll);
            tRegion.setV2(percentY-scroll);
        }
    }
}
