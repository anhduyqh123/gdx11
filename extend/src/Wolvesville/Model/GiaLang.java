package Wolvesville.Model;

public class GiaLang extends Card{
    public int heal = 1;
    public GiaLang() {
        super("Già Làng");
    }

    @Override
    public void Reset() {
        super.Reset();
        heal = 1;
    }

    public void Morning(){
        if (!biSoiCan) return;
        if (heal==1){
            events.add("Già làng "+player+":bị cắn lần 1");
            heal--;
            return;
        }
        if (heal==0){
            events.add("Già làng "+player+":bị cắn lần 2");
            willDead.add(player);
        }
    }
}
