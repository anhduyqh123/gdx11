package JigsawWood.Screen;

import GDX11.Screen;

public class WinScreen extends Screen {
    public WinScreen() {
        super("Win");
    }

    public void Show(int level,int reward) {
        FindILabel("lb").SetText("LEVEL "+level);
        FindIGroup("coin").FindILabel("lb").iParam.Set("value",reward);
        FindIGroup("btAd").
                FindIGroup("coin").FindILabel("lb").iParam.Set("value",reward*2);
        super.Show();
    }
}
