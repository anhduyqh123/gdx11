package Tool.ObjectToolV2.Form;

import GDX11.Asset;
import GDX11.AssetData.AssetData;
import GDX11.AssetData.AssetNode;
import GDX11.AssetData.AssetPackage;
import GDX11.GDX;
import GDX11.IObject.IMap;
import GDX11.IObject.IObject;
import GDX11.Util;
import Tool.Swing.GTree2;
import Tool.Swing.UI;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AssetForm {
    public JPanel panel1;
    private JTree tree1;
    private JComboBox cbPack;
    private JComboBox cbKind;
    private AssetData data = Asset.i.data;
    private GTree2 gTree = new GTree2(tree1);

    public AssetForm()
    {
        List<String> packs = new ArrayList<>(data.GetKeys());
        packs.add(0,"all");
        UI.ComboBox(cbPack,packs.toArray(),packs.get(0),vl-> GDX.Try(()->gTree.SetRoot(GetData())));

        List kinds = new ArrayList<>();
        kinds.add("all");
        kinds.addAll(Arrays.asList(AssetNode.Kind.values()));
        UI.ComboBox(cbKind,kinds.toArray(),kinds.get(0),vl-> gTree.SetRoot(GetData()));
    }
    private IObject GetData()
    {
        String pack = cbPack.getSelectedItem()+"";
        Object kind = cbKind.getSelectedItem()+"";
        if (pack.equals("all") && kind.equals("all")) return new IGroupNode();
        if (pack.equals("all")) return new IGroupNode(AssetNode.Kind.valueOf(kind+""));
        if (kind.equals("all")) return new IGroupNode(pack);
        return new IGroupNode(pack,AssetNode.Kind.valueOf(kind+""));
    }

    public class INode extends IObject
    {
        public AssetNode node;
        public INode(AssetNode node)
        {
            this.node = node;
            name = node.name;
        }
    }
    public class IGroupNode extends IObject
    {
        public IMap<IObject> iMap = new IMap<>();
        public IGroupNode(String pack,AssetNode.Kind kind)
        {
            name = kind.name();
            AssetPackage aPack = data.Get(pack);
            Util.For(aPack.GetNodes(kind),n->Add(new INode(n)));
        }
        public IGroupNode(String pack)
        {
            name = pack;
            Add(new IGroupNode(pack, AssetNode.Kind.Texture));
            Add(new IGroupNode(pack, AssetNode.Kind.Sound));
            Add(new IGroupNode(pack, AssetNode.Kind.Spine));
            Add(new IGroupNode(pack, AssetNode.Kind.Particle));
        }
        public IGroupNode()
        {
            name = "all";
            Util.For(data.GetKeys(),key->Add(new IGroupNode(key)));
        }
        public IGroupNode(AssetNode.Kind kind)
        {
            name = "all";
            Util.For(data.GetKeys(),key->Add(new IGroupNode(key,kind)));
        }
        public void Add(IObject node)
        {
            iMap.Add(node);
        }
    }
}