package Blackjack;

import Blackjack.Controller.GBoard;
import Blackjack.Screen.GameScreen;
import Extend.XItem;
import GDX11.GDX;
import GDX11.IObject.IActor.IGroup;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

public class Blackjack {

    public Blackjack()
    {
        XItem.InitItem(new XItem("money",5000));

        GameScreen screen = new GameScreen();
        XItem.Get("money").AddChangeEvent("game",(o,n)->{
            IGroup bar = screen.FindIGroup("top").FindIGroup("bar");
            bar.FindILabel("lb").SetText(n);
        });
        screen.Show();


        GBoard gBoard = new GBoard(screen.iGroup);
        gBoard.Start();
    }
}
