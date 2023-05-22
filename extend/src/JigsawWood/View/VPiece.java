package JigsawWood.View;

import GDX11.IObject.IActor.IGroup;
import GDX11.IObject.IObject;
import JigsawWood.Model.Shape;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;

public class VPiece extends VShape {

    public VPiece(Shape shape, IGroup iBoard, Group parent) {
        super(shape, iBoard, parent);
        iGroup.iComponents.GetIComponent("draw").active = !shape.texture.equals("");
    }

    @Override
    protected void InitData(IGroup iBoard) {
        iGroup.FindIImage("mask").texture = shape.texture;
        iGroup.FindIActor("mask").iSize.width = iBoard.FindIActor("mask").iSize.width;
        iGroup.FindIActor("mask").iSize.height = iBoard.FindIActor("mask").iSize.height;
    }

    @Override
    protected IGroup NewIGroup() {
        return IObject.Get("vPiece").Clone();
    }
    public void SetMaskPos(Vector2 maskPos)
    {
        iGroup.FindIActor("mask").SetPosition(maskPos);
    }
}
