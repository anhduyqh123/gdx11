package Tool.Puzzle.Core;

import Extend.XItem;
import GDX11.GDX;
import GDX11.Screen;
import JigsawWood.Controller.GBoard;
import JigsawWood.Controller.GJigsawBoard;
import JigsawWood.Controller.GPuzzBoard;
import JigsawWood.Model.Shape;
import JigsawWood.Screen.GameScreen;

public class TestGame {
    public static boolean test;
    static {
        XItem.InitItem(new XItem("coin",100));
    }
    public static void TestJigsaw(Shape shape)
    {
        test = true;
        GJigsawBoard gBoard = shape.IsJigsaw()?new GJigsawBoard():new GPuzzBoard();
        Screen screen = gBoard.game;
        screen.onHide = ()->test = false;
        screen.AddClick("btBack",screen::Hide);
        gBoard.Start(shape);
    }
}
