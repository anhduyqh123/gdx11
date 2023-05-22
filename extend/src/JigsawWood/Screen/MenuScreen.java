package JigsawWood.Screen;

import GDX11.*;
import JigsawWood.Controller.*;

public class MenuScreen extends Screen {
    public MenuScreen() {
        super("Menu");
        Global.SetCoinEvent(FindIGroup("coin"));

        FindIGroup("btJigsaw").FindIActor("lb").iParam.Set("value", Config.GetPref("level_jigsaw",1));
        FindIGroup("btPuzz").FindIActor("lb").iParam.Set("value", Config.GetPref("level_puzz",1));
        FindIGroup("btSudo").FindIActor("lb").iParam.Set("value", Config.GetPref("best_sudo",0));

        AddClick("btJigsaw",this::JigSaw);
        AddClick("btPuzz",this::Puzz);
        AddClick("btSudo",this::Sudo);
    }
    private void Sudo()
    {
        Hide();
        GBoard gBoard = new GSudoBoard();
        gBoard.Start();
    }
    private void JigSaw()
    {
        Hide();
        GBoard gBoard = new GJigsawBoard();
        gBoard.Start(1);
    }
    private void Puzz()
    {
        Hide();
        GBoard gBoard = new GPuzzBoard();
        gBoard.Start(1);
    }
}
