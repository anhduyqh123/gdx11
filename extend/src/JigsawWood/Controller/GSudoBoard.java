package JigsawWood.Controller;

import GDX11.*;
import GDX11.Actors.Particle;
import GDX11.IObject.IAction.ICountAction;
import GDX11.IObject.IActor.IActor;
import GDX11.IObject.IActor.IGroup;
import GDX11.IObject.IActor.IParticle;
import JigsawWood.Model.Shape;
import JigsawWood.Model.ShapeData;
import JigsawWood.Model.SudoBoard;
import JigsawWood.Screen.LoseScreen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.utils.Align;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GSudoBoard extends GBoard{
    private int score = 0;
    public GSudoBoard(IGroup game) {
        super(game);
        game.FindIActor("btKill").AddClick(()->Kill());
        game.FindIActor("btShuffle").AddClick(()->NewShapes());
        game.FindIActor("btReset").AddClick(this::Restart);
    }

    @Override
    protected void InitItem() {

    }

    protected ShapeData LoadData() {
        return Json.ToObjectFomKey("sudoData",ShapeData.class);
    }

    @Override
    protected void InitModel() {
        this.model = new SudoBoard(9,9);
    }

    @Override
    protected void BackShape(Shape shape) {
        GetView(shape).Back();
    }

    @Override
    protected void RemoveShape(Shape shape) {
        newShapes.remove(shape);
        if (newShapes.size() == 0) NewShapes();
    }
    private boolean ValidShape(Shape shape) {
        for (Vector2 p : model.GetEmptyList())
            if (model.IsFit(p,shape)) return true;
        return false;
    }

    @Override
    protected void Destroy(List<List<Vector2>> lists) {
        int len = 0;
        int score0 = score;
        for (List l : lists) len+=l.size();
        score+=len*5;
        IActor lbScore = game.FindIGroup("top").FindIGroup("score").FindIActor("lb");
        lbScore.iAction.Find("count",ICountAction.class).Set(score0,score);
        lbScore.RunAction("count");

        super.Destroy(lists);
        GDX.Ref<Boolean> lose = new GDX.Ref<>(true);
        Util.For(newShapes,shape->{
            boolean valid = ValidShape(shape);
            if (valid) lose.Set(false);
            GetView(shape).SetColor(valid?Color.valueOf("#FFFFCC"):Color.LIGHT_GRAY);
        });
        if (lose.Get()) Lose();
    }
    private void Lose() {
        game.GetGroup().setTouchable(Touchable.disabled);
        Screen loseScreen = new LoseScreen(score,560);
        loseScreen.AddClick("btAd",()->{
            loseScreen.Hide();
            Continue();
        });
        loseScreen.AddClick("btRestart",()->{
            loseScreen.Hide();
            Restart();
        });

        loseScreen.onShowDone = ()->game.GetGroup().setTouchable(Touchable.enabled);

        game.FindIGroup("noti").RunAction("noti");
        game.FindIGroup("noti").iParam.SetRun("notiDone",()->{
            game.Run(loseScreen::Show,0.4f);
            GAudio.i.PlaySound("gameover");
        });
    }
    private void Continue() {
        NewShapes();
        game.GetGroup().setTouchable(Touchable.disabled);
        List<Integer> list = new ArrayList<>();
        Util.For(0,8, list::add);
        Collections.shuffle(list);
        Util.For(0,2,i-> game.Run(()->DestroyMini(list.get(i)),i*0.4f));
        game.Run(()->game.GetGroup().setTouchable(Touchable.enabled),1.2f);
    }
    private void DestroyMini(int mini)//delay 0.4f
    {
        SudoBoard sudoBoard = (SudoBoard)model;
        IGroup iBoard = game.FindIGroup("board");
        Vector2 mid = iBoard.FindITable("table").Get(sudoBoard.GetMidPos(mini)).GetLocalToActor(iBoard.GetActor(), Align.center);
        IParticle eff = game.FindIGroup("board").Clone("thunder");
        eff.Refresh();
        eff.SetPosition(mid);
        eff.RunAction("play");
        game.Run(()->DestroyMini(sudoBoard.GetMini(mini)),0.4f);
    }
    private void Kill()
    {
        SudoBoard sudoBoard = (SudoBoard) model;
        List<Integer> list = new ArrayList<>();
        Util.For(0,8,list::add);
        Collections.shuffle(list);
        for (int i : list)
        {
            for (Vector2 p : sudoBoard.GetMini(i))
                if (sudoBoard.HasValue(p))
                {
                    DestroyMini(i);
                    return;
                }
        }
    }
    private void DestroyMini(List<Vector2> list)
    {
        GDX.Ref<Boolean> destroy = new GDX.Ref<>(false);
        for (Vector2 p : list){
            if (model.Empty(p)) continue;
            model.Set(p,model.emptyChar);
            if (blockMap.get(p)!=null){
                destroy.Set(true);
                DestroyEffect(blockMap.get(p));
            }
        }
        if (destroy.Get()) GAudio.i.PlaySingleSound("glass2");
    }
}
