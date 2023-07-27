package Tool.ObjectToolV2.Form;

import GDX11.Asset;
import GDX11.AssetData.AssetData;
import GDX11.AssetData.AssetNode;
import GDX11.AssetData.AssetPackage;
import GDX11.Config;
import GDX11.GDX;
import GDX11.IObject.IMap;
import GDX11.IObject.IObject;
import GDX11.Util;
import Tool.Swing.GTree2;
import Tool.Swing.UI;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AssetForm {
    public JPanel panel1;
    private JTree tree1;
    private JComboBox cbPack;
    private JComboBox cbKind;
    private JPanel pnDraw;
    private GTree2 gTree = new GTree2(tree1);

    public AssetForm()
    {
        gTree.onSelect = this::OnSelect;
        Config.i.SetRun("reloadAsset",this::RefreshData);
        RefreshData();
    }
    private void RefreshData(){
        List<String> packs = new ArrayList<>(Asset.i.data.GetKeys());
        packs.add(0,"all");
        UI.ComboBox(cbPack,packs.toArray(),packs.get(1),vl-> GDX.Try(()->gTree.SetRoot(GetData())));

        List kinds = new ArrayList<>();
        kinds.add("all");
        kinds.addAll(Arrays.asList(AssetNode.Kind.values()));
        UI.ComboBox(cbKind,kinds.toArray(),AssetNode.Kind.Texture,vl-> gTree.SetRoot(GetData()));
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
    private void OnSelect(Object object) {
        if (!(object instanceof INode)) return;
        INode iNode = (INode) object;
        if (iNode.node.kind== AssetNode.Kind.Texture)
            DrawTexture(iNode.node);
    }
    private void DrawTexture(AssetNode node) {
        try {
            final BufferedImage image = ImageIO.read(new File(node.url));
            float scaleX = pnDraw.getWidth()*1f/image.getWidth();
            float scaleY = pnDraw.getHeight()*1f/image.getHeight();
            float scale = Math.min(scaleX,scaleY);
            int w = (int)(image.getWidth()*scale);int h = (int)(image.getHeight()*scale);
            int x = (pnDraw.getWidth()-w)/2;int y = (pnDraw.getHeight()-h)/2;
            JPanel jPanel = new JPanel(){
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    g.drawImage(image, x, y,(int)(image.getWidth()*scale),(int)(image.getHeight()*scale), null);
                }
            };
            pnDraw.removeAll();
            pnDraw.add(jPanel);
            UI.Repaint(pnDraw);
        }catch (Exception e){}
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
        private AssetData data = Asset.i.data;
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
