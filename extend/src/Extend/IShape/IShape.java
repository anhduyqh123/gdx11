package Extend.IShape;

import GDX11.GDX;
import GDX11.IObject.IComponent.IComponent;
import GDX11.IObject.IPos;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

public abstract class IShape extends IComponent {//all position is local of Actor
    public ShapeRenderer.ShapeType shapeType = ShapeRenderer.ShapeType.Line;
    private GDX.Func<ShapeRenderer> getRenderer;

    public ShapeRenderer GetRenderer()
    {
        if (getRenderer==null) Install();
        return getRenderer.Run();
    }
    private void Install()
    {
        ShapeRenderer renderer = new ShapeRenderer();
        renderer.setAutoShapeType(true);
        getRenderer = ()->renderer;
    }

    @Override
    public void Draw(Batch batch, float parentAlpha) {
        GetRenderer().setProjectionMatrix(batch.getProjectionMatrix());
        GetRenderer().setColor(GetActor().getColor());
        batch.end();
        GetRenderer().begin();
        GetRenderer().set(shapeType);
        DrawShape();
        GetRenderer().end();
        batch.begin();
    }
    protected abstract void DrawShape();

    protected Vector2 GetStagePos(Vector2 pos)
    {
        return GetActor().localToStageCoordinates(pos);
    }
    protected Vector2 GetStagePos(IPos iPos)
    {
        return GetStagePos(iPos.GetPosition());
    }

}

