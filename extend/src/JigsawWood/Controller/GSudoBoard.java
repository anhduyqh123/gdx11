package JigsawWood.Controller;

import GDX11.IObject.IActor.IGroup;
import GDX11.Json;
import JigsawWood.Model.Shape;
import JigsawWood.Model.ShapeData;
import JigsawWood.Model.SudoBoard;

public class GSudoBoard extends GBoard{
    public GSudoBoard(IGroup game) {
        super(game);
    }
    protected ShapeData LoadData()
    {
        return Json.ToObjectFomKey("sudoData",ShapeData.class);
    }

    @Override
    protected void InitModel() {
        this.model = new SudoBoard(9,9);
    }

    @Override
    protected void BackShape(Shape shape) {}

    @Override
    protected void RemoveShape(Shape shape) {
        newShapes.remove(shape);
        if (newShapes.size() == 0) NewShapes();
    }
}
