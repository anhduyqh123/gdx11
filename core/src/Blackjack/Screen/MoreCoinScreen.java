package Blackjack.Screen;

import Extend.XItem;
import GDX11.Screen;
import SDK.SDK;

public class MoreCoinScreen extends Screen {
    public MoreCoinScreen() {
        super("MoreCoin");
        AddClick("btClose",this::Hide);
        AddClick("btAdd",()->{
            SDK.i.ShowVideoReward(cb->{
                if (cb) XItem.Get("money").Add(2000);
            });
            Hide();
        });
        AddClick("btNo",this::Hide);
    }
}
