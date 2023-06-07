package Wolvesville.Model;

public class PhuThuy extends Card {
    public int save = 1,kill=1;
    public String savePlayer, killPlayer;
    public PhuThuy() {
        super("Phù Thủy");
    }

    public void Reset(){
        savePlayer = null;
        killPlayer = null;
    }
    public void Save(String pl){
        save--;
        savePlayer = pl;
        getCard.Run(pl).PhuThuySave();
    }
    public void Kill(String pl){
        kill--;
        killPlayer = pl;
        getCard.Run(pl).PhuThuyKill();
    }
}
