package Tool.JFrame;

import GDX11.GDX;
import GDX11.Reflect;
import GDX11.Util;
import Tool.ObjectTool.Data.ClipBoard;

import javax.swing.*;
import javax.swing.tree.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.*;

public class GTree<T> {
    private JTree tree;
    private T root;
    private Map<TreeNode, Object> map = new HashMap<>();
    private Map<Object, TreeNode> map0 = new HashMap<>();

    //event
    public GDX.Runnable1<T> refreshObject;
    public GDX.Func1<Collection<T>,T> getChildren;
    public GDX.Func1<String,T> getName;
    public GDX.Runnable1<T> onSelect;
    public GDX.Runnable2<T,T> parentAdd;//parent,child
    public GDX.Runnable2<T,T> parentRemove;//parent,child
    public GDX.Runnable2<String,T> rename;//(old,new)
    public GDX.Runnable2<T,Integer> move;//moveUp,moveDown
    public GDX.Func1<T,T> clone = Reflect::Clone;

    public GTree(JTree tree,JTextField tfName)
    {
        this.tree = tree;
        tree.addTreeSelectionListener(e->{
            DefaultMutableTreeNode node = GetSelectedNode();
            if (node==null || node.isRoot()) return;
            onSelect.Run(GetObject(node));
        });
        tree.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                if (e.getKeyChar()=='s') Select();
                if (e.getKeyChar()=='p') Paste();
                if (e.getKeyChar()=='m') MoveTo();
                if (e.getKeyChar()=='u') Move(-1);
                if (e.getKeyChar()=='d') Move(1);
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
    public void SetRoot(T root)
    {
        this.root = root;
        Refresh();
    }
    private DefaultMutableTreeNode NewNode(String name,Object object)
    {
        DefaultMutableTreeNode node = new DefaultMutableTreeNode(name);
        map.put(node, object);
        map0.put(object,node);
        return node;
    }
    private DefaultMutableTreeNode GetNode(String name, T object)
    {
        DefaultMutableTreeNode node = NewNode(name,object);
        Collection<T> children = getChildren.Run(object);
        if (children!=null)
            Util.For(children,o->node.add(GetNode(getName.Run(o),o)));
        return node;
    }
    public void Refresh()
    {
        map.clear();
        tree.setModel(new DefaultTreeModel(GetNode("Root",root)));
    }
    public void SetSelection(Object object)
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
    private TreeNode GetNode(T object)
    {
        return map0.get(object);
    }
    private  <T> T GetObject(TreeNode node)
    {
        return (T)map.get(node);
    }
    public <T> T GetMainObject()
    {
        return GetObject(GetMainNode());
    }
    public <T> T GetSelectedObject()
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
        T parent = GetObject(GetSelectedNode().getParent());
        Util.For(GetSelectedList(),n-> parentRemove.Run(parent,GetObject(n)));
        Refresh();
        List<T> children = (List<T>) getChildren.Run(parent);
        if (children.size()>0) SetSelection(children.get(0));
        else SetSelection(parent);
    }

    //Clipboard
    private void Select()
    {
        List<Object> selectedList = new ArrayList<>();
        Util.For(GetSelectedList(),n->selectedList.add(GetObject(n)));
        ClipBoard.i.Select(selectedList, (GDX.Func1<String, Object>) getName);
    }
    private void Paste()
    {
        List<T> objects = (List<T>) ClipBoard.i.GetObjects();
        if (objects.size()<=0) return;
        List<T> clones = new ArrayList<>();
        Util.For(objects,ob->clones.add(clone.Run(ob)));
        AddTo(clones);
    }
    private void MoveTo()
    {
        List<T> objects = (List<T>) ClipBoard.i.GetObjects();
        if (objects.size()<=0) return;
        TreeNode node = GetNode(objects.get(0));
        T parent = GetObject(node.getParent());
        Util.For(objects,i->parentRemove.Run(parent,i));
        refreshObject.Run(parent);
        AddTo(objects);
    }
    private void Move(int dir)
    {
        T object = GetSelectedObject();
        move.Run(object,dir);
        Refresh();
        SetSelection(object);
    }
    private void AddTo(List<T> list)
    {
        T parent = GetSelectedObject();
        if (parent==null || getChildren.Run(parent)==null) return;
        Util.For(list,i->parentAdd.Run(parent,i));
        Refresh();
        refreshObject.Run(parent);
        SetSelection(list.get(0));
    }
    private void Rename(String name)
    {
        T object = GetSelectedObject();
        rename.Run(name,object);
        Refresh();
        SetSelection(object);
    }
}
