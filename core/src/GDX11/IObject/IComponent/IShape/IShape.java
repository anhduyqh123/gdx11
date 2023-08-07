package GDX11.IObject.IComponent.IShape;

import GDX11.IObject.IActor.IActor;
import GDX11.IObject.IComponent.IComponent;
import GDX11.IObject.IPos;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;

public abstract class IShape extends IComponent {//all position is local of Actor
    public ShapeRenderer.ShapeType shapeType = ShapeRenderer.ShapeType.Line;
    protected transient ShapeRenderer renderer;

    private void Install() {
        renderer = new ShapeRenderer();
        renderer.setAutoShapeType(true);
    }

    @Override
    public void SetIActor(IActor iActor) {
        super.SetIActor(iActor);
        Connect();
    }
    @Override
    public void Draw(Batch batch, float parentAlpha, Runnable superDraw) {
        Draw(batch, parentAlpha);
    }
    public void Draw(Batch batch, float parentAlpha)
    {
        if (renderer==null) Install();
        renderer.setProjectionMatrix(batch.getProjectionMatrix());
        renderer.setColor(GetActor().getColor());
        renderer.begin();
        renderer.set(shapeType);
        DrawShape(renderer);
        renderer.end();
    }
    public abstract void DrawShape(ShapeRenderer renderer);
    public abstract void Connect();
    protected Vector2 GetStagePos(Vector2 pos)
    {
        return GetActor().localToStageCoordinates(pos);
    }
    protected Vector2 GetStagePos(IPos iPos)
    {
        return GetStagePos(iPos.GetPosition());
    }
    protected Vector2 GetPos(IPos iPos, Actor coordActor)
    {
        return GetActor().localToActorCoordinates(coordActor,iPos.GetPosition());
    }

}

