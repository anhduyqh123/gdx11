package Tool.Puzzle.Core;

import GDX11.Config;
import GDX11.IObject.IActor.IActor;
import GDX11.IObject.IActor.IGroup;
import GDX11.IObject.IActor.ITable;
import GDX11.IObject.IObject;
import GDX11.Scene;
import JigsawWood.Model.Shape;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.utils.Align;

public class BoardEditor {
    public static int numID = 1;
    private final String[] arrHex = {"0048BA","B0BF1A","7CB9E8","B284BE","72A0C1","DB2D43","C46210","EFDECD","9F2B68","F19CBB"};

    public BoardEditor(Shape shape)
    {
        Scene.i.ui.clearChildren();
        IGroup iGroup = IObject.Get("ShapeEdit").Clone();
        iGroup.SetIRoot(Scene.i.ui);

        ITable table = iGroup.FindITable("table");
        table.column = shape.width;
        table.clone = shape.height*shape.width;
        iGroup.Refresh();

        table.GetActor().setSize(table.GetTable().getPrefWidth(),table.GetTable().getPrefHeight());
        table.GetActor().setPosition(Scene.i.width/2,Scene.i.height/2, Align.center);
        table.ForActor(a->{
            a.debug();
            IActor iActor = IActor.GetIActor(a);
            Vector2 cell = table.GetCell(a);
            int value = shape.Get(cell);
            Refresh(value,iActor);
            a.addListener(new InputListener(){
                @Override
                public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                    if (pointer!=0) return;
                    int button = Config.Get("button");
                    int vl = shape.Get(cell);
                    if (button==1) shape.Set(cell,vl==-1?0:-1);
                    else shape.Set(cell,vl==numID?0:numID);
                    Refresh(shape.Get(cell),iActor);
                }
            });
        });
    }
    private void Refresh(int value,IActor iActor)
    {
        if (value==-1) iActor.Run("disable");
        if (value==0) iActor.Run("empty");
        if (value>=1) iActor.GetActor().setColor(Color.valueOf(arrHex[value-1]));
    }
}
