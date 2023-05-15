package Tool.Puzzle.Core;

import GDX11.Screen;
import JigsawWood.Controller.GJigsawBoard;
import JigsawWood.Model.Shape;
import JigsawWood.Screen.GameScreen;

public class TestGame {
    public static boolean test;
    public static void TestJigsaw(Shape shape)
    {
        test = true;
        Screen screen = new GameScreen("JigsawGame");
        screen.onHide = ()->test = false;
        screen.AddClick("btBack",screen::Hide);
        screen.Show();

        GJigsawBoard gBoard = new GJigsawBoard(screen.iGroup);
        gBoard.Start(shape);
    }
}
