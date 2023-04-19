package Tool.JFrame;

import GDX11.GDX;
import GDX11.Reflect;
import GDX11.Util;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class JObList<T> extends GList{
    private List<T> list;
    public GDX.Runnable1<T> onSelectObject;
    public GDX.Func<T> newObject;

    public JObList(){}
    public JObList(JTree tree, GDX.Func<List<String>> getData) {
        super(tree, getData);
    }
    public JObList(JTree tree, List<T> list)
    {
        Init(tree,list);
    }
    public void Init(JTree tree,List<T> list)
    {
        this.list = list;
        Init(tree, this::GetData);
        onSelect = id->onSelectObject.Run(GetObject(id));
        newID = ()->{
            String name = "i"+list.size();
            list.add(newObject.Run());
            return name;
        };
        cloneID = ()->{
            String name = "i"+list.size();
            Object ob = GetSelectedObject();
            list.add(Reflect.Clone(ob));
            return name;
        };
        deleteID = n-> list.remove(GetSelectedObject());
        if (list.size()>0) SetSelection("i0");
    }

    private List<String> GetData()
    {
        List<String> data = new ArrayList<>();
        Util.For(0,list.size()-1, i->data.add("i"+i));
        return data;
    }
    private T GetObject(String id)
    {
        int index = Integer.parseInt(id.replace("i",""));
        return list.get(index);
    }
    public T GetSelectedObject()
    {
        return GetObject(GetSelectedID());
    }
}
