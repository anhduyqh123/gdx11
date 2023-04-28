package Tool.ObjectTool.Core;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ObjectData {
    private Map<String,ObjectPack> map = new HashMap<>();
    public void Load(List<String> packs)
    {
        for(String p : packs)
        {
            ObjectPack op = new ObjectPack(p);
            map.put(p,op);
        }
    }
    public ObjectPack Get(String pack)
    {
        return map.get(pack);
    }
}
