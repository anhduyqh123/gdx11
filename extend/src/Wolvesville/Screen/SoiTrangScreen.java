package Wolvesville.Screen;

import Wolvesville.Global;
import Wolvesville.Model.Card;
import Wolvesville.Model.HumanX;

import java.util.List;

public class SoiTrangScreen extends HumanXScreen implements Global {
    public SoiTrangScreen() {
        super(soitrang);
    }

    @Override
    public void Init(Card fc) {
        super.Init(fc);

        BtNext(this,()->{
            leftName.remove(fc.player);
            soitrang.Can();
        });
    }

    @Override
    protected List<String> GetTargetListView() {
        List<String> list = super.GetTargetListView();
        list.add(0,no);
        return list;
    }
}
