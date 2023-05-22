package JigsawWood;

import Extend.XItem;
import GDX11.Screen;
import JigsawWood.Controller.GBoard;
import JigsawWood.Controller.GSudoBoard;
import JigsawWood.Controller.Global;
import JigsawWood.Screen.GameScreen;
import JigsawWood.Screen.MenuScreen;

public class JigsawWood{
    public JigsawWood(){
        Global.InitItem();

        new MenuScreen().Show();
    }
}
