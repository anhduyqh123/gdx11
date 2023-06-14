package JigsawWood.Screen;

import GDX11.*;
import JigsawWood.Controller.*;

public class MenuScreen extends Screen {
    public MenuScreen() {
        super("Menu");
        Global.SetCoinEvent(FindIGroup("coin"));

        FindIGroup("btJigsaw").FindIActor("lb").iParam.Set("value", Config.GetPref("JigsawGame",1));
        FindIGroup("btPuzz").FindIActor("lb").iParam.Set("value", Config.GetPref("PuzzGame",1));
        FindIGroup("btSudo").FindIActor("lb").iParam.Set("value", Config.GetPref("sudo_best",0));

        Click("btJigsaw",this::JigSaw);
        Click("btPuzz",this::Puzz);
        Click("btSudo",this::Sudo);
    }
    private void Sudo() {
        Hide();
        new GSudoBoard().Start();
    }
    private void JigSaw() {
        Hide();
        new GJigsawBoard().Start();
    }
    private void Puzz() {
        Hide();
        new GPuzzBoard().Start();
    }
}
