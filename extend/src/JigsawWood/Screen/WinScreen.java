package JigsawWood.Screen;

import GDX11.Config;
import GDX11.Screen;

public class WinScreen extends Screen {
    public WinScreen(int level) {
        super("Win");
        FindILabel("lb").SetText("LEVEL "+level);
        AddClick("btAd",()->{
            Config.GetRun("videoCoin").run();
        });
    }
}
