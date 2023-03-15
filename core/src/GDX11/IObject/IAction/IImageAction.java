package GDX11.IObject.IAction;

import GDX11.IObject.IActor.IImage;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

public class IImageAction extends IAction{
    public String texture = "";
    public IImageAction() {
        super("image");
    }

    @Override
    public void Run() {
        IImage iImage = GetIActor();
        iImage.SetTexture(texture);
    }

    @Override
    public Action Get() {
        return Actions.run(this::Run);
    }
}
