package DrinkGame.Base;

import GDX11.GDX;
import GDX11.Screen;

import java.util.HashMap;
import java.util.Map;

public class OptionScreen extends Screen {
    private final Map<String,Integer> map = new HashMap<>();
    public OptionScreen() {
        super("Option");
    }
    public VSlider NewSlider(String name, int from, int to, int value0){
        return NewSlider(name, from, to, value0, vl -> {});
    }
    public VSlider NewSlider(String name, int from, int to, int value0, GDX.Runnable1<Integer> cb){
        VSlider slider = new VSlider(FindIGroup(name));
        slider.onChange = vl->{
            map.put(name,vl);
            cb.Run(vl);
        };
        slider.SetLimit(from,to);
        slider.SetValue(value0);
        return slider;
    }
    public int GetValue(String name){
        return map.get(name);
    }
    public void Show(Runnable start){
        Click("btStart",()->{
            Hide();
            start.run();
        });
        Show();
    }
}
