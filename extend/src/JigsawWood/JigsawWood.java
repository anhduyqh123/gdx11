package JigsawWood;

import Extend.XItem;
import GDX11.Screen;
import JigsawWood.Controller.GBoard;
import JigsawWood.Controller.GSudoBoard;
import JigsawWood.Screen.GameScreen;
import JigsawWood.Screen.MenuScreen;

public class JigsawWood{
    public JigsawWood(){
        InitItem();

        new MenuScreen().Show();
    }
    private void InitItem()
    {
        XItem.InitItem(new XItem("coin",100));
    }
}
