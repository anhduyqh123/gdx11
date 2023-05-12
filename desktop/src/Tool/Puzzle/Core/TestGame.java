package Tool.Puzzle.Core;

import GDX11.Screen;
import JigsawWood.Controller.GJigsawBoard;
import JigsawWood.Model.Shape;
import JigsawWood.Screen.GameScreen;

public class TestGame {
    public static void TestJigsaw(Shape shape)
    {
        Screen screen = new GameScreen("JigsawGame");
        screen.AddClick("btBack",screen::Hide);
        screen.Show();

        GJigsawBoard gBoard = new GJigsawBoard(screen.iGroup);
        gBoard.Start(shape);
    }
}
