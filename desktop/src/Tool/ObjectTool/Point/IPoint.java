package Tool.ObjectTool.Point;

import GDX11.Asset;
import GDX11.GDX;
import GDX11.IObject.IPos;
import GDX11.Util;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;

public class IPoint extends Image {
    public static int size = 20;
    public static Color selectColor = Color.BLUE;
    public static Color normalColor = Color.WHITE;
    public static IPoint selected;

    private IPos iPos;
    public IPoint(IPos iPos)
    {
        this.iPos = iPos;
        setDrawable(new TextureRegionDrawable(new TextureRegion(Asset.emptyTexture)));
        setSize(size,size);
        Vector2 pos = iPos.GetPosition();
        setPosition(pos.x, pos.y,Align.center);
        addListener(new ClickListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                setColor(selectColor);
                selected = IPoint.this;
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                setColor(normalColor);
                selected = null;
            }
        });
    }
    public void SetPos(Vector2 stagePos)
    {

    }
}
