package JigsawWood;

import GDX11.Screen;
import JigsawWood.Controller.GBoard;
import JigsawWood.Controller.GJigsawBoard;
import JigsawWood.Controller.GSudoBoard;
import JigsawWood.Model.Shape;
import JigsawWood.Screen.GameScreen;
import JigsawWood.View.VShape;

public class JigsawWood{
    public JigsawWood(){
        //Sudo();
        JigSaw();
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
        gBoard.Start(3);
    }
}
