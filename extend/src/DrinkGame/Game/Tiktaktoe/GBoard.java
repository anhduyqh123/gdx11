package DrinkGame.Game.Tiktaktoe;

import GDX11.Config;
import GDX11.GAudio;
import GDX11.GDX;
import GDX11.IObject.IActor.IActor;
import GDX11.IObject.IActor.IGroup;
import GDX11.Util;
import com.badlogic.gdx.scenes.scene2d.Touchable;

import java.util.Arrays;
import java.util.List;

public class GBoard {
    {
        Board.Clear();
        Board.SetMatch(NewMatch3x3());
    }
    private IGroup iGroup;
    private Board model = new Board();
    private GPlayer player1,player2,player;
    private int round;
    public GDX.Runnable1<Integer> endGame;
    public GBoard(IGroup iGroup){
        this.iGroup = iGroup;
        Config.i.SetRun1("touch",a-> player.Touch((IGroup) a));
        iGroup.iParam.Set("getCell",(GDX.Func1)name->GetCell((String) name));
    }
    public void Multi(){
        player1 = new GPlayer(1,iGroup,model);
        player2 = new GPlayer(2,iGroup,model);
    }
    public void Bot(){
        player1 = new GPlayer(1,iGroup,model);
        player2 = new GBot(2,iGroup,model);
    }
    public void Start(){
        model.Reset();
        iGroup.RunAction("reset");
        round++;
        player = round%2==0?player2:player1;
        Turn();
    }
    private void Turn()
    {
        player.Turn(this::NextTurn);
    }
    private void NextTurn()
    {
        List<String> match = model.GetMatch(player.id);
        if (match!=null){
            player.Win();
            EndGame(match);
            return;
        }
        if (model.GetEmptySlots().size()==0)
        {
            endGame.Run(0);
            return;
        }
        player = player.equals(player1)?player2:player1;
        Turn();
    }
    private void EndGame(List<String> match){
        GAudio.i.PlaySound("completed");
        Util.ForIndex(match,i->
                iGroup.Run(()->GetCell(match.get(i)).RunAction("up"),0.1f*i));
        iGroup.Run(()->endGame.Run(player.id),0.1f*match.size());
    }
    private IActor GetCell(String name){
        String[] arr = name.split("\\.");
        return iGroup.FindITable("table").Get(Integer.parseInt(arr[0]),Integer.parseInt(arr[1]));
    }

    //static
    private static List<String> NewMatch3x3(){
        return Arrays.asList("0.0,1.0,2.0","0.1,1.1,2.1","0.2,1.2,2.2",
                            "0.0,0.1,0.2","1.0,1.1,1.2","2.0,2.1,2.2",
                            "0.0,1.1,2.2","0.2,1.1,2.0");
    }
}
