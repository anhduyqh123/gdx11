package JigsawWood;

import GDX11.Json;
import GDX11.Screen;
import JigsawWood.Controller.GBoard;
import JigsawWood.Controller.GJigsawBoard;
import JigsawWood.Controller.GSudoBoard;
import JigsawWood.Model.ShapeData;
import JigsawWood.Screen.GameScreen;

public class JigsawWood{
    public JigsawWood(){
        Sudo();
        //JigSaw();
        //Puzz();
    }
    private void Sudo()
    {
        Screen screen = new GameScreen();
        screen.Show();
        GBoard gBoard = new GSudoBoard(screen.iGroup);
        gBoard.Start();
    }
    private void JigSaw()
    {
        Screen screen = new GameScreen("JigsawGame");
        screen.Show();
        GBoard gBoard = new GJigsawBoard(screen.iGroup);
        gBoard.Start(1);
    }
    private void Puzz()
    {
        Screen screen = new GameScreen("JigsawGame");
        screen.Show();

        GBoard gBoard = new GJigsawBoard(screen.iGroup){
            @Override
            protected ShapeData LoadData() {
                return Json.ToObjectFomKey("puzzData",ShapeData.class);
            }
        };
        gBoard.Start(1);
    }
}
