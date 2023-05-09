package Tool.Swing;

import GDX11.GDX;
import GDX11.Reflect;
import GDX11.Util;

import javax.swing.*;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class BaseTree<T> {
    private final static Map<JTree,Runnable> treeSelectedEvent = new HashMap<>();
    private final static Map<JTree, GDX.Runnable1<MouseEvent>> treeClickedEvent = new HashMap<>();
    private final static Map<JTree,GDX.Runnable1<KeyEvent>> treeKeyTypeEvent = new HashMap<>();
    private static void SetTreeSelectedEvent(JTree tree, Runnable event)
    {
        if (!treeSelectedEvent.containsKey(tree))
            tree.addTreeSelectionListener(e-> treeSelectedEvent.get(tree).run());
        treeSelectedEvent.put(tree,event);
    }
    private static void SetTreeClickedEvent(JTree tree, GDX.Runnable1<MouseEvent> event)
    {
        if (!treeClickedEvent.containsKey(tree))
            tree.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    event.Run(e);
                }
            });
        treeClickedEvent.put(tree,event);
    }
    private static void SetTreeKeyTypeEvent(JTree tree, GDX.Runnable1<KeyEvent> event)
    {
        if (!treeKeyTypeEvent.containsKey(tree))
            tree.addKeyListener(new KeyAdapter() {
                @Override
                public void keyTyped(KeyEvent e) {
                    event.Run(e);
                }
            });
        treeKeyTypeEvent.put(tree,event);
    }

    protected JTree tree;
    protected T root;
    private Map<TreeNode, T> map = new HashMap<>();
    private Map<T, TreeNode> map0 = new HashMap<>();
    protected JPopupMenu popupMenu = new JPopupMenu();
    protected Map<String,JMenuItem> itemMap = new HashMap<>();
    public GDX.Runnable1<T> onSelect = ob->{};

    public BaseTree(JTree tree)
    {
        this.tree = tree;
        SetTreeSelectedEvent(tree,()->{
            DefaultMutableTreeNode node = GetSelectedNode();
            if (node==null || node.isRoot()) return;
            onSelect.Run(GetObject(node));
        });
        SetTreeClickedEvent(tree,e->{
            if (e.getButton()!=3 || root==null) return;
            if (GetSelectedNodes().size()>1)
            {
                popupMenu.show(tree,e.getX(),e.getY());
                return;
            }
            int selRow = tree.getRowForLocation(e.getX(), e.getY());
            TreePath selPath = tree.getPathForLocation(e.getX(), e.getY());
            tree.setSelectionPath(selPath);
            if (selRow>-1 &&  selPath.getPathCount()>1){
                tree.setSelectionRow(selRow);
                OnShowPopupMenu();
                popupMenu.show(tree,e.getX(),e.getY());
                return;
            }
            SetSelection(root);
            OnShowPopupMenu();
            popupMenu.show(tree,0,0);
        });
        SetTreeKeyTypeEvent(tree, this::OnKeyTyped);
    }
    protected void Select()
    {
        GetClipSelected().clear();
        Util.For(GetSelectedNodes(), n->GetClipSelected().add(GetObject(n)));
    }
    protected void Paste()
    {
        List clones = new ArrayList<>();
        Util.For(GetClipSelected(),ob->clones.add(Reflect.Clone(ob)));
        AddTo(clones);
    }
    protected abstract void AddTo(List<T> list);
    protected List<T> GetClipSelected()
    {
        return GetClipSelected(tree);
    }
    protected void OnKeyTyped(KeyEvent e)
    {

    }
    protected void StartEditing()
    {
        tree.startEditingAtPath(tree.getSelectionPath());
    }
    public boolean IsRoot()
    {
        return GetSelectedNode().isRoot();
    }
    public void SetRoot(T root)
    {
        this.root = root;
        Refresh();
        SetSelection(root);
    }

    protected DefaultMutableTreeNode InitNode(T ob)
    {
        return new DefaultMutableTreeNode(ob);
    }
    private DefaultMutableTreeNode NewNode(T ob)
    {
        DefaultMutableTreeNode node = InitNode(ob);
        map.put(node, ob);
        map0.put(ob,node);
        return node;
    }
    private DefaultMutableTreeNode NewNodeModel(T ob)
    {
        DefaultMutableTreeNode node = NewNode(ob);
        List<T> list = GetChildren(ob);
        if (list!=null) Util.For(list, i->node.add(NewNodeModel(i)));
        return node;
    }
    protected abstract List GetChildren(T ob);
    public void Refresh()
    {
        tree.setModel(new DefaultTreeModel(NewNodeModel(root)));
        tree.getModel().addTreeModelListener(new TreeModelListener() {
            @Override
            public void treeNodesChanged(TreeModelEvent treeModelEvent) {
                Rename();
            }

            @Override
            public void treeNodesInserted(TreeModelEvent treeModelEvent) {

            }

            @Override
            public void treeNodesRemoved(TreeModelEvent treeModelEvent) {

            }

            @Override
            public void treeStructureChanged(TreeModelEvent treeModelEvent) {

            }
        });
    }
    public void SetSelection(T object)
    {
        DefaultTreeModel model = (DefaultTreeModel) tree.getModel();
        TreePath path = new TreePath(model.getPathToRoot(map0.get(object)));
        tree.setSelectionPath(path);
        tree.scrollPathToVisible(path);
    }
    protected TreeNode GetMainNode()
    {
        DefaultMutableTreeNode node = GetSelectedNode();
        if (node==null || node.isRoot()) return null;
        do {
            DefaultMutableTreeNode parent = (DefaultMutableTreeNode)node.getParent();
            if (parent.isRoot()) return node;
            node = parent;
        }while (true);
    }
    protected DefaultMutableTreeNode GetSelectedNode()
    {
        return (DefaultMutableTreeNode)tree.getLastSelectedPathComponent();
    }
    protected TreeNode GetNode(T object)
    {
        return map0.get(object);
    }
    protected T GetObject(TreeNode node)
    {
        return map.get(node);
    }
    public T GetMainObject()
    {
        return GetObject(GetMainNode());
    }
    public T GetSelectedObject()
    {
        return GetObject(GetSelectedNode());
    }

    protected List<TreeNode> GetSelectedNodes()
    {
        List<TreeNode> list = new ArrayList<>();
        if (tree.getSelectionPaths()==null) return list;
        for(TreePath path : tree.getSelectionPaths())
        {
            TreeNode node = (TreeNode) path.getLastPathComponent();
            list.add(node);
        }
        return list;
    }
    protected T GetParentObject()
    {
        return GetObject(GetSelectedNode().getParent());
    }

    protected abstract void Rename();
    protected void InitPopupMenu()
    {

    }
    protected void OnShowPopupMenu()
    {

    }
    protected JMenuItem NewItem(String name,String shortcut,Runnable cb)
    {
        JMenuItem item = new JMenuItem(shortcut.equals("")?name:name+"("+shortcut+")");
        item.addActionListener(e->cb.run());
        itemMap.put(name,item);
        return item;
    }
    public void NewMenuItem(String name,String shortcut,Runnable cb)
    {
        popupMenu.add(NewItem(name,shortcut,cb));
    }
    protected JMenuItem GetItem(String name)
    {
        return itemMap.get(name);
    }

    //static
    private static final Map<JTree,List> listMap = new HashMap<>();
    protected static List GetClipSelected(JTree tree)
    {
        if (!listMap.containsKey(tree)) listMap.put(tree,new ArrayList());
        return listMap.get(tree);
    }
}
