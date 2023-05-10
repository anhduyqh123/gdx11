package Tool.Puzzle.Core;

import GDX11.Asset;
import GDX11.Config;
import GDX11.GDX;
import GDX11.IObject.IActor.IActor;
import GDX11.IObject.IActor.IGroup;
import GDX11.IObject.IActor.IImage;
import GDX11.IObject.IActor.ITable;
import GDX11.IObject.IObject;
import GDX11.Scene;
import JigsawWood.Model.Shape;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.utils.Align;

import java.util.ArrayList;
import java.util.List;

public class BoardEditor extends Shape {
    public static char numID = 'a';
    private static final List<Color> colors = new ArrayList<>();
    {
        FileHandle file = new FileHandle("colors.txt");
        for (String s : file.readString().split("\n"))
            colors.add(Color.valueOf(s));
    }

    public BoardEditor(Shape shape)
    {
        Scene.i.ui.clearChildren();
        IGroup iGroup = IObject.Get("ShapeEdit").Clone();
        iGroup.SetIRoot(Scene.i.ui);

        IGroup iShape = iGroup.FindIGroup("shape");
        ITable table = iGroup.FindITable("table");
        table.column = shape.width;
        table.clone = shape.height*shape.width;
        if (!shape.texture.equals("")) FitSize(table.GetIActor("empty"),shape.texture,shape);

        IImage img = iGroup.FindIImage("img");
        img.texture = shape.texture;

        iGroup.Refresh();
        iShape.GetActor().setSize(table.GetTable().getPrefWidth(),table.GetTable().getPrefHeight());
        iShape.GetActor().setPosition(Scene.i.width/2,Scene.i.height/2, Align.center);

        if (shape.texture.equals("")) img.GetActor().setVisible(false);

        table.ForActor(a->{
            a.debug();
            IImage iActor = IActor.GetIActor(a);
            Vector2 cell = table.GetCell(a);
            char id = shape.Get(cell);
            Refresh(id,iActor);
            a.addListener(new InputListener(){
                @Override
                public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                    if (pointer!=0) return;
                    int button = Config.Get("button");
                    char vl = shape.Get(cell);
                    if (button==1) shape.Set(cell,vl==nullChar?emptyChar:nullChar);
                    else shape.Set(cell,vl==numID?emptyChar:numID);
                    Refresh(shape.Get(cell),iActor);
                }
            });
        });
    }
    private void Refresh(char id,IImage iActor)
    {
        iActor.SetTexture("");
        iActor.GetActor().clearActions();
        if (id==nullChar) iActor.Run("disable");
        if (id==emptyChar) iActor.Run("empty");
        if (id>=valueChar){
            int id0 = id<='a'?id+32:id;
            iActor.GetActor().setColor(colors.get(id0-'a'));
            if (id>='A' && (id<='Z')) iActor.RunAction("wall");
        }
    }
    private void FitSize(IActor iActor,String texture,Shape shape)
    {
        TextureRegion tr = Asset.i.GetTexture(texture);
        float aWidth = Float.parseFloat(iActor.iSize.width);
        float aHeight = Float.parseFloat(iActor.iSize.height);
        float width = shape.width*aWidth;
        float height = shape.height*aHeight;
        float scaleX = tr.getRegionWidth()/width;
        float scaleY = tr.getRegionHeight()/height;
        float scale = Math.max(scaleX,scaleY);
        iActor.iSize.width = scale*aWidth+"";
        iActor.iSize.height = scale*aHeight+"";
    }
}
