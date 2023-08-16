package DrinkGame;

import DrinkGame.Base.MenuScreen;
import DrinkGame.Game.*;
import GDX11.Config;
import GDX11.Scene;

import java.util.Arrays;
import java.util.List;

public class DrinkGame {
    private final List<String> availableGames = Arrays.asList("crocodile","pirate","bottlespin","drinkcup",
            "drinko","drinkopoly","drunkdice","drunkroll","hammer","passout","roulette","snakeladder","spinner","tabletop","tiktaktoe");
    public DrinkGame(){
        float delH = Scene.i.height-Scene.i.height0;
        Config.i.Set("long_device",delH>300);
        Config.i.Set("medium_device",delH>=240);
        MenuScreen menu = new MenuScreen();
        menu.NewGame("crocodile", ()->new Crocodile());
        menu.NewGame("pirate", ()->new Pirate());
        menu.NewGame("bottlespin", ()->new BottleSpin());
        menu.NewGame("drinkcup", ()->new DrinkCup());
        menu.NewGame("drinko", ()->new Drinko());
        menu.NewGame("drinkopoly", ()->new Drinkopoly());
        menu.NewGame("drunkdice", ()->new DrunkDice());
        menu.NewGame("drunkroll", ()->new DrunkRoll());
        menu.NewGame("hammer", ()->new Hammer());
        menu.NewGame("passout", ()->new PassOut());
        //menu.NewGame("roulette", Crocodile::new);
        menu.NewGame("snakeladder", ()->new SnakeLadder());
        menu.NewGame("spinner", ()->new Spinner());
        menu.NewGame("tabletop", ()->new TableTop());
        menu.NewGame("tiktaktoe", ()->new Tiktaktoe());
        menu.Show(availableGames);
    }
}
