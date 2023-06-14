package JigsawWood.Screen;

import Extend.XItem;
import GDX11.GAudio;
import GDX11.IObject.IActor.IGroup;
import GDX11.Screen;
import JigsawWood.Controller.Global;

public class ShopScreen extends Screen {
    public ShopScreen() {
        super("Shop");
        Global.AddTopCoin(this);

        Click("btCoin",()->Global.ShowVideoReward(()->Global.AddCoin(100)));
        Global.itemCoin.outValue = this::NotEnoughCoin;

        InitItem(Global.itemKill,100,FindIGroup("btKill"));
        InitItem(Global.itemShuffle,100,FindIGroup("btShuffle"));
        InitItem(Global.itemHint,100,FindIGroup("btHint"));
    }
    private void InitItem(XItem item, int price, IGroup iGroup)
    {
        iGroup.FindILabel("lb").SetText("+"+item.addItem.value);
        iGroup.FindIActor("bt").Click(()->{
            Global.itemCoin.Use(price,()->{
                iGroup.RunAction("add");
                GAudio.i.PlaySound("btCoin");
                item.Add();
            });
        });
    }
    private void NotEnoughCoin()
    {
        Screen dialog = new Screen("Dialog");
        dialog.iGroup.RunAction("no_coin");
        dialog.Show();
    }
}
