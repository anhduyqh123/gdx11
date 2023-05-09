package JigsawWood.View;

import GDX11.IObject.IActor.IGroup;
import GDX11.IObject.IActor.ITable;
import GDX11.IObject.IObject;
import JigsawWood.Model.Shape;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.utils.Align;

public class VPiece extends VShape {
    public VPiece(Shape shape, ITable table0, Group parent) {
        super(shape, table0, parent);
    }

    @Override
    protected void InitData() {
        iGroup.FindIImage("mask").texture = shape.texture;
    }

    @Override
    protected IGroup NewIGroup() {
        return IObject.Get("vPiece").Clone();
    }
    public void SetMaskPos(Vector2 maskPos)
    {
        iGroup.FindIActor("mask").SetPosition(maskPos, Align.bottomLeft);
    }
}
