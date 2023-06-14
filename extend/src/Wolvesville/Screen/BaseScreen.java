package Wolvesville.Screen;

import GDX11.Screen;
import Wolvesville.Global;
import Wolvesville.Model.Card;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseScreen extends Screen {
    public Card fc,preFc;
    protected List<String> left = new ArrayList<>(Global.leftName);
    public BaseScreen(String name, Card fc) {
        super(name);
        this.fc = fc;
        preFc = fc.Clone();
        Init(fc);
    }
    public abstract void Init(Card fc);
    public void OnBackScreen(){
        fc.Set(preFc);
        Global.leftName.clear();
        Global.leftName.addAll(left);
    }
}
