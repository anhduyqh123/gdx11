package Tool.Swing;

import javax.swing.*;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListForm {
    public JPanel panel1;
    private JTree tree1;
    private List<String> data;
    private Map<TreeNode,String> map = new HashMap<>();
    private Map<String,TreeNode> map0 = new HashMap<>();
    public ListForm(List<String> data)
    {
        this.data = data;
        tree1.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                if (e.getKeyChar()=='') New();//ctr+n
                if (e.getKeyChar()==KeyEvent.VK_BACK_SPACE) Delete();
            }
        });

        Refresh();
    }
    private void Refresh()
    {
        DefaultMutableTreeNode node = NewNode("list");
        for (String name : data){
            DefaultMutableTreeNode n = NewNode(name);
            map.put(n,name);
            map0.put(name,n);
            node.add(n);
        }
        tree1.setModel(new DefaultTreeModel(node));
        tree1.getModel().addTreeModelListener(new TreeModelListener() {
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
    private DefaultMutableTreeNode NewNode(String name)
    {
        return new DefaultMutableTreeNode(name);
    }
    private String GetSelectedObject()
    {
        return map.get(GetSelectedNode());
    }
    private TreeNode GetSelectedNode()
    {
        return (TreeNode) tree1.getLastSelectedPathComponent();
    }
    private List<Object> GetSelectedList()
    {
        List<Object> list = new ArrayList<>();
        for(TreePath path : tree1.getSelectionPaths())
        {
            TreeNode node = (TreeNode) path.getLastPathComponent();
            list.add(map.get(node));
        }
        return list;
    }
    private void SetSelection(Object object)
    {
        DefaultTreeModel model = (DefaultTreeModel) tree1.getModel();
        TreePath path = new TreePath(model.getPathToRoot(map0.get(object)));
        tree1.setSelectionPath(path);
        tree1.scrollPathToVisible(path);
    }

    private void Rename()
    {
        int index = data.indexOf(GetSelectedObject());
        String name = GetSelectedNode().toString();
        data.set(index,name);
        Refresh();
        SetSelection(name);
    }
    private void Delete()
    {
        data.removeAll(GetSelectedList());
        Refresh();
        if (data.size()>0) SetSelection(data.get(0));
    }
    private void New()
    {
        data.add("new");
        Refresh();
        SetSelection("new");
    }
}
