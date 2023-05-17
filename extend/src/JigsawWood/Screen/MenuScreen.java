package JigsawWood.Screen;

import Extend.XItem;
import GDX11.*;
import GDX11.IObject.IActor.IActor;
import GDX11.IObject.IActor.IGroup;
import JigsawWood.Controller.GBoard;
import JigsawWood.Controller.GJigsawBoard;
import JigsawWood.Controller.GSudoBoard;
import JigsawWood.Model.Shape;
import JigsawWood.Model.ShapeData;
import com.badlogic.gdx.graphics.Color;

public class MenuScreen extends Screen {
    public MenuScreen() {
        super("Menu");
        GameScreen.SetCoinEvent(FindIGroup("coin"));

        FindIGroup("btJigsaw").FindIActor("lb").iParam.Set("value", Config.GetPref("level_jigsaw",1));
        FindIGroup("btPuzz").FindIActor("lb").iParam.Set("value", Config.GetPref("level_puzz",1));
        FindIGroup("btSudo").FindIActor("lb").iParam.Set("value", Config.GetPref("best_sudo",0));

        AddClick("btJigsaw",this::JigSaw);
        AddClick("btPuzz",this::Puzz);
        AddClick("btSudo",this::Sudo);

        AddClick("coin",()-> new ShopScreen().Show());
    }
    private void Sudo()
    {
        Hide();
        Screen screen = new GameScreen();
        screen.Show();
        GBoard gBoard = new GSudoBoard(screen.iGroup);
        gBoard.Start();
    }
    private void JigSaw()
    {
        Hide();
        Screen screen = new GameScreen("JigsawGame");
        screen.Show();
        GBoard gBoard = new GJigsawBoard(screen.iGroup);
        gBoard.Start(1);
    }
    private void Puzz()
    {
        Hide();
        Screen screen = new GameScreen("JigsawGame");
        screen.Show();

        GBoard gBoard = new GJigsawBoard(screen.iGroup){
            @Override
            protected ShapeData LoadData() {
                return Json.ToObjectFomKey("puzzData",ShapeData.class);
            }

            @Override
            protected void NewView(Shape shape, IGroup slot) {
                super.NewView(shape, slot);
                Color color = new Color(colors.get(newShapes.size()-1));
                color.lerp(Color.WHITE,0.5f);
                color.a = 1;
                GetView(shape).SetColor(color);
            }
        };
        gBoard.Start(1);
    }
}
