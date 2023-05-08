package JigsawWood;

import GDX11.Screen;
import JigsawWood.Controller.GBoard;
import JigsawWood.Controller.GJigsawBoard;
import JigsawWood.Model.Shape;
import JigsawWood.Screen.GameScreen;
import JigsawWood.View.VShape;

public class JigsawWood{
    public JigsawWood(){
        Screen screen = new GameScreen("JigsawGame");
        screen.Show();

        //GBoard gBoard = new GBoard(screen.iGroup);
        GJigsawBoard gBoard = new GJigsawBoard(screen.iGroup);
    }
}
