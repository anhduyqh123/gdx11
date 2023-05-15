package JigsawWood.Screen;

import GDX11.IObject.IAction.ICountAction;
import GDX11.IObject.IActor.ILabel;
import GDX11.Screen;

public class LoseScreen extends Screen {
    public LoseScreen(int score,int best) {
        super("Lose");
        ILabel lbBest = FindIActor("lbBest");
        lbBest.SetText(best);
        ILabel lbScore = FindIActor("lbScore");
        lbScore.iAction.Find("count", ICountAction.class).Set(0,score);
        Run(()->lbScore.RunAction("count"),0.4f);
    }
}
