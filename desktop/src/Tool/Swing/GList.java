package Tool.Swing;

import GDX11.GDX;
import GDX11.Util;

import javax.swing.*;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GList {
    private JTree tree;
    private Map<TreeNode, String> map = new HashMap<>();
    private Map<String, TreeNode> map0 = new HashMap<>();
    //event
    protected GDX.Func<List<String>> getData;
    public GDX.Runnable1<String> onSelect, deleteID;
    public GDX.Func<String> newID, cloneID;

    public GList(){}
    public GList(JTree tree,GDX.Func<List<String>> getData)
    {
        Init(tree,getData);
    }
    public void Init(JTree tree,GDX.Func<List<String>> getData)
    {
        this.tree = tree;
        this.getData = getData;

        tree.addTreeSelectionListener(e->{
            DefaultMutableTreeNode node = GetSelectedNode();
            if (node==null || node.isRoot()) return;
            onSelect.Run(GetID(node));
        });

        Refresh();
        List<String> list = getData.Run();
        if (list.size()>0) SetSelection(list.get(0));
    }
    private DefaultMutableTreeNode NewNode(String id)
    {
        DefaultMutableTreeNode node = new DefaultMutableTreeNode(id);
        map.put(node, id);
        map0.put(id,node);
        return node;
    }
    private DefaultMutableTreeNode NewNodeModel()
    {
        DefaultMutableTreeNode node = NewNode("root");
        Util.For(getData.Run(),i->node.add(NewNode(i)));
        return node;
    }
    public void Refresh()
    {
        tree.setModel(new DefaultTreeModel(NewNodeModel()));
    }
    public void SetSelection(String id)
    {
        DefaultTreeModel model = (DefaultTreeModel) tree.getModel();
        TreePath path = new TreePath(model.getPathToRoot(map0.get(id)));
        tree.setSelectionPath(path);
        tree.scrollPathToVisible(path);
    }
    private DefaultMutableTreeNode GetSelectedNode()
    {
        return (DefaultMutableTreeNode)tree.getLastSelectedPathComponent();
    }
    private TreeNode GetNode(String id)
    {
        return map0.get(id);
    }
    private String GetID(TreeNode node)
    {
        return map.get(node);
    }
    public String GetSelectedID()
    {
        return GetID(GetSelectedNode());
    }
    private List<TreeNode> GetSelectedList()
    {
        List<TreeNode> list = new ArrayList<>();
        for(TreePath path : tree.getSelectionPaths())
        {
            TreeNode node = (TreeNode) path.getLastPathComponent();
            list.add(node);
        }
        return list;
    }

    public void Delete()
    {
        Util.For(GetSelectedList(),n-> deleteID.Run(GetID(n)));
        Refresh();
        List<String> data = getData.Run();
        if (data.size()>0) SetSelection(data.get(0));
    }
    public void Clone()
    {
        String name = cloneID.Run();
        Refresh();
        SetSelection(name);
    }
    public void New()
    {
        String name = newID.Run();
        Refresh();
        SetSelection(name);
    }
}
