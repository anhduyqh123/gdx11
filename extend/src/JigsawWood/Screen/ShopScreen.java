package JigsawWood.Screen;

import GDX11.Config;
import GDX11.Screen;

public class ShopScreen extends Screen {
    public ShopScreen() {
        super("Shop");
        AddClick("btCoin",()->{
            Hide();
            Config.GetRun("videoCoin").run();
        });
    }
}
