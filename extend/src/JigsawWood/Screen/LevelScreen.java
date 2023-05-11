package JigsawWood.Screen;

import GDX11.GDX;
import GDX11.IObject.IActor.IGroup;
import GDX11.Screen;
import JigsawWood.Model.Shape;

import java.util.List;

public class LevelScreen extends Screen {
    private int current;
    public GDX.Runnable1<Integer> onLoad;
    public LevelScreen(List<Shape> list,int current) {
        super("Level");
        this.current = current;
        FindITable("table").CloneChild(list,this::InitButton);
    }
    private void InitButton(int index, Shape shape, IGroup bt)
    {
        int level = index+1;
        bt.FindIImage("img").SetTexture(shape.texture.equals("")?"icon_4":"icon_3");
        if (level<=current){
            bt.FindIImage("img").SetTexture(shape.texture.equals("")?"icon_2":"icon_1");
            bt.RunAction("unlock");
            bt.AddClick(()->{
                Hide();
                onLoad.Run(level);
            });
        }
    }
}
