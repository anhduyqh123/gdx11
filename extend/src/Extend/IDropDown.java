package Extend;

import GDX11.GDX;
import GDX11.IObject.IActor.IGroup;
import GDX11.IObject.IActor.ITable;
import GDX11.IObject.IComponent.IComponent;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import java.util.*;

public class IDropDown extends IComponent {
    public List<String> items = new ArrayList<>();
    public String selected = "";
    private transient ITable table;
    private transient IGroup iGroup;
    private transient Map<String,IGroup> map = new HashMap<>();
    private transient String temp;
    public GDX.Runnable1<String> onSelect = vl->{};

    @Override
    public void Refresh() {
        iGroup = GetIActor().GetIGroup();
        table = iGroup.FindITable("table");
        SetItems(items);
        iGroup.FindILabel("lb").AddClick(()-> iGroup.FindActor("scroll").setVisible(!iGroup.FindActor("scroll").isVisible()));
        SetSelected(selected);
    }
    public void SetItems(List<String> items){
        this.items = items;
        table.CloneChild(items,this::InitBox);
    }
    private void InitBox(String name,IGroup box){
        map.put(name,box);
        box.FindILabel("lb").SetText(name);
        box.GetActor().addListener(new ClickListener(){
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                SetOver(name);
            }

            @Override
            public void clicked(InputEvent event, float x, float y) {
                SetSelected(name);
            }
        });
    }
    private void SetOver(String name){
        if (temp!=null) map.get(temp).Run("off");
        temp = name;
        map.get(temp).Run("on");
    }
    public void SetSelected(String name){
        if (name.equals("")) return;
        selected = name;
        iGroup.FindILabel("lb").SetText(name);
        iGroup.FindActor("scroll").setVisible(false);
        SetOver(name);
        onSelect.Run(selected);
    }
    public int GetIndexSelected(){
        return items.indexOf(selected);
    }

}
