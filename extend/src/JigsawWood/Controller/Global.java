package JigsawWood.Controller;

import GDX11.Config;

public class Global {

    public static void AddCoin(int coin)
    {
        Config.Set("coinNum",coin/10);
        Config.GetRun("videoCoin").run();
    }
}
