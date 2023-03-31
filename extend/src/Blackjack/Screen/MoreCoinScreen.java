package Blackjack.Screen;

import Blackjack.Controller.GGame;
import Extend.XItem;
import GDX11.Screen;

public class MoreCoinScreen extends Screen {
    public MoreCoinScreen() {
        super("MoreCoin");
        AddClick("btClose",this::Hide);
        AddClick("btAdd",()->{
            GGame.ShowVideoReward(()->{
                XItem.Get("money").Add(2000);
                Hide();
            });
        });
        AddClick("btNo",this::Hide);
    }
}
