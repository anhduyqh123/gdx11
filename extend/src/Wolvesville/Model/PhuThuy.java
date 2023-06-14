package Wolvesville.Model;

public class PhuThuy extends Card {
    public int save = 1,kill=1;
    public String savePlayer, killPlayer;
    public PhuThuy() {
        super("Phù Thủy");
    }

    public void Reset(){
        save = 1;
        kill = 1;
        ResetX();
    }
    public void ResetX(){
        savePlayer = null;
        killPlayer = null;
    }

    @Override
    public void Run() {
        if (savePlayer!=null) map.get(savePlayer).PhuThuySave();
        if (killPlayer!=null) map.get(killPlayer).PhuThuyKill();
    }

    public void Save(String pl){
        if (BiVoHieu() || VoHieuBoiGiaLangDie()) return;
        save--;
        savePlayer = pl;
    }
    public void Kill(String pl){
        if (BiVoHieu() || VoHieuBoiGiaLangDie()) return;
        kill--;
        killPlayer = pl;
    }
    @Override
    public boolean VoHieuBoiGiaLangDie() {
        return gialang.Valid() && gialang.Die();
    }
}
