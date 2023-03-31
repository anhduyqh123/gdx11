package Blackjack.Controller;

import GDX11.Screen;
import SDK.SDK;

public class GGame {

    public static void ShowVideoReward(Runnable cb)
    {
        if (!SDK.i.isVideoRewardReady()){
            new Screen("VideoNotReady").Show();
            return;
        }
        SDK.i.ShowVideoReward(vl->{
            if (vl) cb.run();
        });
    }
}
