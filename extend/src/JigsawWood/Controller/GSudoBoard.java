package JigsawWood.Controller;

import GDX11.*;
import GDX11.IObject.IActor.IGroup;
import GDX11.IObject.IActor.IParticle;
import JigsawWood.Model.Shape;
import JigsawWood.Model.ShapeData;
import JigsawWood.Model.SudoBoard;
import JigsawWood.Screen.LoseScreen;
import JigsawWood.View.VShape;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.utils.Align;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GSudoBoard extends GBoard{
    private int score = 0,best = Config.GetPref("sudo_best",0);
    public GSudoBoard() {
        InitItem(Global.itemKill,game.FindIGroup("btKill"),this::Kill);
        InitItem(Global.itemShuffle,game.FindIGroup("btShuffle"),()->{
            NewShapes();
            return true;
        });

        game.FindIActor("btKill").Click(Global.itemKill::Use);
        game.FindIActor("btShuffle").Click(Global.itemShuffle::Use);
        game.FindIActor("btReset").Click(this::Restart);
    }

    protected ShapeData LoadData() {
        return Json.ToObjectFomKey("sudoData",ShapeData.class);
    }

    @Override
    protected void InitModel() {
        this.model = new SudoBoard(9,9);
    }

    @Override
    public void OnStart() {
        super.OnStart();
        score = 0;
        game.SetBest(best);
        game.SetScore(score);
    }

    @Override
    public void Start() {
        OnStart();
    }

    private SudoBoard GetModel()
    {
        return (SudoBoard)model;
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

        for (Vector2 p : model.GetPosList())
            if (model.IsFit(p,shape)) return true;
        return false;
    }

    @Override
    protected void Destroy(List<List<Vector2>> lists) {
        int len = 0;
        int score0 = score;
        for (List l : lists) len+=l.size();
        score+=len*5;
        game.SetScore(score0,score);

        super.Destroy(lists);
        CheckValidShapes();
    }
    private void CheckValidShapes()
    {
        GDX.Ref<Boolean> lose = new GDX.Ref<>(true);
        Util.For(newShapes,shape->{
            boolean valid = ValidShape(shape);
            if (valid) lose.Set(false);
            GetView(shape).SetColor(valid? VShape.normal :VShape.unFit);
        });
        if (lose.Get()) Lose();
    }
    private void Lose() {
        game.setTouchable(Touchable.disabled);
        if (best<score)
        {
            best = score;
            Config.i.SetPref("sudo_best",best);
            game.SetBest(best);
        }
        Screen loseScreen = new LoseScreen(score,best);
        loseScreen.Click("btAd",()->{
            loseScreen.Hide();
            Continue();
        });
        loseScreen.Click("btRestart",()->{
            loseScreen.Hide();
            Restart();
        });

        loseScreen.onShowDone = ()->game.setTouchable(Touchable.enabled);

        game.FindIGroup("noti").RunAction("noti");
        game.FindIGroup("noti").iParam.SetRun("notiDone",()->{
            game.Run(loseScreen::Show,0.4f);
            GAudio.i.PlaySound("gameover");
        });
    }
    private void Continue() {
        NewShapes();
        game.setTouchable(Touchable.disabled);
        List<Integer> list = new ArrayList<>();
        Util.For(0,8, list::add);
        Collections.shuffle(list);
        Util.For(0,2,i-> game.Run(()->DestroyMini(list.get(i)),i*0.4f));
        game.Run(()->game.setTouchable(Touchable.enabled),1.2f);
    }
    private void DestroyMini(int mini)//delay 0.4f
    {
        IGroup iBoard = game.FindIGroup("board");
        Vector2 mid = iBoard.FindITable("table").Get(GetModel().GetMidPos(mini)).GetLocalToActor(iBoard.GetActor(), Align.center);
        IParticle eff = game.FindIGroup("board").Clone("thunder");
        eff.Refresh();
        eff.SetPosition(mid);
        eff.RunAction("play");
        game.Run(()->DestroyMini(GetModel().GetMini(mini)),0.4f);
    }
    private boolean Kill()
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
                    return true;
                }
        }
        return false;
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
        if (destroy.Get()){
            GAudio.i.PlaySingleSound("glass2");
            CheckValidShapes();
        }
    }

    private final List<Actor> hlCell = new ArrayList<>();

    @Override
    protected void HighLightOff() {
        super.HighLightOff();
        Util.For(hlCell,a->a.setColor(Color.valueOf("#FFFFCC")));
        hlCell.clear();
    }

    @Override
    protected void HighLight(Vector2 pos, Shape shape) {
        super.HighLight(pos, shape);
        HighLightDestroy(shape);
    }

    private void HighLightDestroy(Shape shape) {
        model.Set(shape);
        Util.For(GetModel().GetDestroyList(),list->
                Util.For(list,p->{
                    Actor a = blockMap.get(p);
                    if (a==null) return;
                    a.setColor(VShape.highLight);
                    hlCell.add(a);
                }));
        model.Remove(shape);
    }
}
