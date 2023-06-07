package Wolvesville.Model;

import java.util.List;

public class HumanX extends Card {
    public String target;
    public HumanX(String name,String wantTo) {
        super(name);
        this.wantTo = wantTo;
    }
    public void SetTarget(String dan){
        target = dan;
    }
    public List<String> GetValidList(List<String> list){
        return list;
    }
}
