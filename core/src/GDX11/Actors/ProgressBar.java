package GDX11.Actors;

import GDX11.Actions.CountAction;
import GDX11.GDX;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Action;

public class ProgressBar extends ScrollImage {
    public GDX.Runnable1<Vector2> onChange;
    public float percent = 1f;

    @Override
    public void SetTexture(TextureRegion tr) {
        if (IsScroll()) super.SetTexture(tr);
        else InitTexture(tr);
    }
    public void SetValue(float percent)
    {
        this.percent = ValidPercent(percent);
        if (onChange!=null) onChange.Run(GetPos());
    }
    public void CountValue(float percent,float duration)
    {
        float start = this.percent;
        float end = ValidPercent(percent);
        Action ac = CountAction.Get(this::SetValue,start,end,duration);
        addAction(ac);
    }
    private Vector2 GetPos()//parent position
    {
        return localToParentCoordinates(new Vector2(percent*getWidth(),getHeight()/2));
    }


    private float ValidPercent(float percent)
    {
        if (percent>1) return 1;
        if (percent<0) return 0;
        return percent;
    }

    @Override
    protected void Scroll(float percentX, float percentY, float scroll) {
        super.Scroll(percent, 1f, scroll);
    }

    @Override
    protected float GetDrawWith() {
        return super.GetDrawWith()*percent;
    }

    @Override
    protected void Draw(Batch batch, float alpha) {
        float width = percent*trWidth;
        tRegion.setRegionWidth((int)width);
        super.Draw(batch, alpha);
    }
}
