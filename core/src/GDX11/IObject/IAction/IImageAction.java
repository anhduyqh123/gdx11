package GDX11.IObject.IAction;

import GDX11.Asset;
import GDX11.Config;
import GDX11.IObject.IActor.IImage;
import com.badlogic.gdx.graphics.Texture;
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
        TextureRegion tr = GetTexture();
        iImage.SetTexture(tr);
        if (sizeByTexture)
        {
            Vector2 mid = iImage.GetPosition(Align.center);
            iImage.GetActor().setSize(tr.getRegionWidth(),tr.getRegionHeight());
            iImage.SetPosition(mid,Align.center);
        }
    }
    private TextureRegion GetTexture()
    {
        if (Config.Has(texture)) return Config.Get(texture);
        if (GetIActor().iParam.Has(texture)) return GetIActor().iParam.Get(texture);
        return Asset.i.GetTexture(texture);
    }

    @Override
    public Action Get() {
        return Actions.run(this::Run);
    }
}
