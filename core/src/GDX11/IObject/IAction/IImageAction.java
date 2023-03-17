package GDX11.IObject.IAction;

import GDX11.Asset;
import GDX11.IObject.IActor.IImage;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.Align;

public class IImageAction extends IAction{
    public boolean sizeByTexture;
    public String texture = "";
    public IImageAction() {
        super("image");
    }

    @Override
    public void Run() {
        IImage iImage = GetIActor();
        iImage.SetTexture(texture);
        if (sizeByTexture)
        {
            TextureRegion tr = Asset.i.GetTexture(texture);
            Vector2 mid = iImage.GetPosition(Align.center);
            iImage.GetActor().setSize(tr.getRegionWidth(),tr.getRegionHeight());
            iImage.SetPosition(mid,Align.center);
        }
    }

    @Override
    public Action Get() {
        return Actions.run(this::Run);
    }
}
