package Wolvesville.Model;

public class ThoSan extends HumanX{
    public ThoSan() {
        super("Thợ săn","Chọn mục tiêu");
    }

    @Override
    public void Morning() {
        super.Morning();
        Check();
    }
    public void Check(){
        if (willDead.contains(player) && map.get(target).Alive()){
            willDead.add(target);
            events.add("Thợ sắn đã bắn "+target);
        }
    }
}
