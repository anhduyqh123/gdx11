package Tool.JFrame;

import GDX11.GDX;
import GDX11.IObject.IMap;
import GDX11.IObject.IObject;
import GDX11.Reflect;
import GDX11.Util;
import Tool.ObjectTool.Data.ClipBoard;

import javax.swing.*;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.*;

public class GTree<T extends IObject> {
    private JTextField tfName;
    private JTree tree;
    private IObject root;
    private Map<TreeNode, IObject> map = new HashMap<>();
    private Map<IObject, TreeNode> map0 = new HashMap<>();

    //event
    public GDX.Runnable1<T> refreshObject = ob->{};
    public GDX.Runnable1<T> onSelect;
    public GDX.Func<T> newObject;

    public GTree(JTree tree,JTextField tfName)
    {
        this.tfName = tfName;
        this.tree = tree;
        tree.addTreeSelectionListener(e->{
            DefaultMutableTreeNode node = GetSelectedNode();
            if (node==null || node.isRoot()) return;
            onSelect.Run(GetObject(node));
        });
        tree.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                if (e.getKeyChar()=='n') NewObject();//n
                if (e.getKeyChar()=='s') Select();//s
                if (e.getKeyChar()=='p') Paste();//p
                if (e.getKeyChar()=='m') MoveTo();//m
                if (e.getKeyChar()=='1') Move(-1);
                if (e.getKeyChar()=='2') Move(1);
                if (e.getKeyChar()==KeyEvent.VK_BACK_SPACE) Delete();
            }
        });
        tfName.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode()==KeyEvent.VK_ENTER)
                    Rename(tfName.getText());
            }
        });
    }
    public void SetRoot(IObject root)
    {
        this.root = root;
        Refresh();
    }
    private DefaultMutableTreeNode NewNode(IObject object)
    {
        DefaultMutableTreeNode node = new DefaultMutableTreeNode(object.name);
        map.put(node, object);
        map0.put(object,node);
        return node;
    }
    private DefaultMutableTreeNode NewNodeModel(IObject object)
    {
        DefaultMutableTreeNode node = NewNode(object);
        IMap<IObject> iMap = object.GetIMap();
        if (iMap!=null) iMap.For(i->node.add(NewNodeModel(i)));
        return node;
    }
    public void Refresh()
    {
        map.clear();
        tree.setModel(new DefaultTreeModel(NewNodeModel(root)));
        tree.getModel().addTreeModelListener(new TreeModelListener() {
            @Override
            public void treeNodesChanged(TreeModelEvent e) {
                Rename(GetSelectedNode().toString());
            }

            @Override
            public void treeNodesInserted(TreeModelEvent e) {

            }

            @Override
            public void treeNodesRemoved(TreeModelEvent e) {

            }

            @Override
            public void treeStructureChanged(TreeModelEvent e) {

            }
        });
    }
    public void SetSelection(IObject object)
    {
        DefaultTreeModel model = (DefaultTreeModel) tree.getModel();
        TreePath path = new TreePath(model.getPathToRoot(map0.get(object)));
        tree.setSelectionPath(path);
        tree.scrollPathToVisible(path);
    }

    private DefaultMutableTreeNode GetSelectedNode()
    {
        return (DefaultMutableTreeNode)tree.getLastSelectedPathComponent();
    }
    private TreeNode GetMainNode()
    {
        DefaultMutableTreeNode node = GetSelectedNode();
        if (node==null || node.isRoot()) return null;
        do {
            DefaultMutableTreeNode parent = (DefaultMutableTreeNode)node.getParent();
            if (parent.isRoot()) return node;
            node = parent;
        }while (true);
    }
    private TreeNode GetNode(IObject object)
    {
        return map0.get(object);
    }
    private T GetObject(TreeNode node)
    {
        return (T)map.get(node);
    }
    public T GetMainObject()
    {
        return GetObject(GetMainNode());
    }
    public T GetParentObject()
    {
        return GetObject(GetSelectedNode().getParent());
    }
    public T GetSelectedObject()
    {
        return GetObject(GetSelectedNode());
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
    private void Delete()
    {
        IObject parent = GetParentObject();
        Util.For(GetSelectedList(),n-> parent.GetIMap().Remove(GetObject(n)));
        Refresh();
        if (parent.GetIMap().Size()>0) SetSelection(parent.GetIMap().Get(0));
        else SetSelection(parent);
    }

    //Clipboard
    private void Select()
    {
        List<IObject> selectedList = new ArrayList<>();
        Util.For(GetSelectedList(),n->selectedList.add(GetObject(n)));
        ClipBoard.i.Select(selectedList);
    }
    private void Paste()
    {
        List<IObject> objects = ClipBoard.i.GetObjects();
        if (objects.size()<=0) return;
        List<IObject> clones = new ArrayList<>();
        Util.For(objects,ob->clones.add(ob.Clone()));
        AddTo(clones);
    }
    private void MoveTo()
    {
        List<IObject> objects = ClipBoard.i.GetObjects();
        if (objects.size()<=0) return;
        TreeNode node = GetNode(objects.get(0));
        IObject parent = GetObject(node.getParent());
        Util.For(objects,i->parent.GetIMap().Remove(i));
        refreshObject.Run((T)parent);
        AddTo(objects);
    }
    private void Move(int dir)
    {
        IObject object = GetSelectedObject();
        IObject parent = GetParentObject();
        parent.GetIMap().Move(object,dir);
        Refresh();
        SetSelection(object);
    }
    private void AddTo(List<IObject> list)
    {
        IObject parent = GetSelectedObject();
        if (parent==null || parent.GetIMap()==null) return;
        Util.For(list,i->parent.GetIMap().Add(i));
        Refresh();
        refreshObject.Run((T)parent);
        SetSelection(list.get(0));
    }
    private void Rename(String name)
    {
        IObject object = GetSelectedObject();
        IObject parent = GetParentObject();
        parent.GetIMap().Rename(name,object);
        Refresh();
        SetSelection(object);
    }
    public void NewObject()
    {
        IObject newOb = newObject.Run();
        if (!tfName.getText().equals("")) newOb.name = tfName.getText();
        IObject object = GetSelectedObject();
        if (object.GetIMap()==null) object = GetParentObject();
        object.GetIMap().Add(newOb);
        Refresh();
        refreshObject.Run((T)newOb);
        SetSelection(newOb);
    }
    public void Clone()
    {
        IObject newOb = GetSelectedObject().Clone();
        newOb.name = tfName.getText();
        GetParentObject().GetIMap().Add(newOb);
        Refresh();
        refreshObject.Run((T)newOb);
        SetSelection(newOb);
    }
    public void Prefab()
    {
        if (ClipBoard.i.GetObjects().size()<=0) return;
        IObject newOb = ClipBoard.i.GetObjects().get(0).Clone();
        Reflect.SetValue("prefab",newOb,newOb.name);
        newOb.name = tfName.getText();
        GetSelectedObject().GetIMap().Add(newOb);
        Refresh();
        refreshObject.Run((T)newOb);
        SetSelection(newOb);
    }
}
