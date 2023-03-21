package Blackjack.Screen;

import GDX11.Screen;

public class MoreCoinScreen extends Screen {
    public MoreCoinScreen() {
        super("MoreCoin");
        AddClick("btClose",this::Hide);
        AddClick("btAdd",()->{

        });
        AddClick("btNo",this::Hide);
    }
}
