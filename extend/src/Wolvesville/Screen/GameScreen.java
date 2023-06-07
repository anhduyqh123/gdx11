package Wolvesville.Screen;

import GDX11.IObject.IActor.IGroup;
import GDX11.IObject.IActor.ILabel;
import GDX11.IObject.IActor.ITable;
import GDX11.Screen;
import Wolvesville.Global1;
import Wolvesville.Model.Card;

public class GameScreen extends Screen implements Global1 {
    public GameScreen() {
        super("Game");
        ILabel lb = FindILabel("lb");
        lb.iParam.Set("count",Card.willDead.size());
        lb.iParam.Set("xname",Card.willDead.toString());

        ITable table = FindIGroup("group").FindITable("table");
        table.CloneChild(Card.events,this::InitText);
    }
    private void InitText(String text, IGroup iGroup){
        iGroup.FindILabel("lb").SetText(text);
    }
}
