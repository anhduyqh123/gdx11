package DrinkGame.Game;

import DrinkGame.Base.BaseGame;
import DrinkGame.Base.TutScreen;
import GDX11.*;
import GDX11.IObject.IActor.IGroup;
import GDX11.IObject.IComponent.IComponent;

public class BottleSpin extends BaseGame {
    public BottleSpin() {
        super("bottlespin");
        IGroup board = game.FindIGroup("board");
        IGroup wheel = board.FindIActor("wheel");
//        Util.For(0,7,i->{
//            wheel.FindIGroup("i"+i).FindILabel("lb").SetText("text"+i);
//        });

        GDX.Ref<Integer> rotate0 = new GDX.Ref<>(0);
        Config.i.SetRun1("spinStart",a->{
            wheel.RunAction("reset");
            rotate0.Set((int)wheel.GetActor().getRotation());
        });
        Config.i.SetRun1("spinDone",a->{
            GAudio.i.PlaySound("completed");
            board.FindIActor("bottle").RunAction("reset");
            int id = wheel.iParam.Get("id");
            wheel.FindIActor("i"+id).RunAction("on");
        });
        wheel.iComponents.Add(new IComponent("update"){
            @Override
            public void Update(float delta) {
                if (board.FindActor("bottle").isTouchable()) return;
                int rotate = (int)wheel.GetActor().getRotation();
                if (rotate-rotate0.Get()>=45){
                    GAudio.i.PlaySingleSound("wheel_pap",0.02f);
                    rotate0.Set(rotate);
                }
            }
        });
    }
}
