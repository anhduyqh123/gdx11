package Tool.Swing;

import GDX11.GDX;
import GDX11.IObject.IMap;
import GDX11.IObject.IObject;
import GDX11.Reflect;
import GDX11.Util;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;
import java.awt.event.KeyEvent;
import java.util.List;

public class GTree2<T extends IObject> extends BaseTree<T> {
    //private final List<IObject> selectedList = new ArrayList<>();

    //event
    public GDX.Runnable1<T> refreshObject = ob->{};
    public GDX.Runnable1<T> onNew = ob->{};

    public GTree2(JTree tree)
    {
        super(tree);
        InitPopupMenu();
    }

    @Override
    protected List GetChildren(T ob) {
        IMap<IObject> iMap = ob.GetIMap();
        if (iMap==null) return null;
        return iMap.list;
    }

    @Override
    protected DefaultMutableTreeNode InitNode(T ob) {
        return new DefaultMutableTreeNode(ob.name);
    }

    protected void Delete()
    {
        T parent = GetParentObject();
        Util.For(GetSelectedNodes(), n-> parent.GetIMap().Remove(GetObject(n)));
        Refresh();
        if (parent.GetIMap().Size()>0) SetSelection((T)parent.GetIMap().Get(0));
        else SetSelection(parent);
    }
//    private void Paste()
//    {
//        List<IObject> clones = new ArrayList<>();
//        Util.For(GetClipSelected(),ob->clones.add(ob.Clone()));
//        AddTo(clones);
//    }
    private void MoveTo()
    {
        List<IObject> list = (List<IObject>) GetClipSelected();
        TreeNode node = GetNode((T)list.get(0));
        IObject parent = GetObject(node.getParent());
        Util.For(list,i->parent.GetIMap().Remove(i));
        refreshObject.Run((T)parent);
        AddTo((List<T>) list);
    }

    @Override
    protected void AddTo(List<T> list) {
        IObject parent = GetSelectedObject();
        if (parent==null || parent.GetIMap()==null) return;
        Util.For(list,i->parent.GetIMap().Add(i));
        Refresh();
        refreshObject.Run((T)parent);
        SetSelection(list.get(0));
    }

    private void Move(int dir)
    {
        IObject object = GetSelectedObject();
        IObject parent = GetParentObject();
        parent.GetIMap().Move(object,dir);
        Refresh();
        refreshObject.Run((T) parent);
        SetSelection((T)object);
    }
    protected void Rename()
    {
        IObject object = GetSelectedObject();
        IObject parent = GetParentObject();
        parent.GetIMap().Rename(GetSelectedNode().toString(),object);
        Refresh();
        SetSelection((T)object);
    }
    private void NewObject(IObject newOb)
    {
        newOb.name = "new";
        IObject object = GetSelectedObject();
        if (object.GetIMap()==null) object = GetParentObject();
        object.GetIMap().Add(newOb);
        Refresh();
        refreshObject.Run((T)newOb);
        SetSelection((T)newOb);
        StartEditing();
        onNew.Run((T)newOb);
    }
    private void Clone()
    {
        IObject newOb = GetSelectedObject().Clone();
        newOb.name = NewName();
        GetParentObject().GetIMap().Add(newOb);
        Refresh();
        refreshObject.Run((T)newOb);
        SetSelection((T)newOb);
        StartEditing();
    }
    private String NewName()
    {
        String name0 = GetSelectedObject().name;
        String ex = name0.replaceAll("[^0-9]","");
        if (ex.equals("")) ex = "0";
        String name = name0.replace(ex,"");
        return name+(Integer.parseInt(ex)+1);
    }
    public void Prefab()
    {
        IObject newOb = GetClipSelected().get(0).Clone();
        Reflect.SetValue("prefab",newOb,newOb.name);
        newOb.name = newOb.name+"prefab";
        GetSelectedObject().GetIMap().Add(newOb);
        Refresh();
        refreshObject.Run((T)newOb);
        SetSelection((T)newOb);
        StartEditing();
    }

    //MenuItem
    private final JMenu newMenu = new JMenu("New");
    protected void InitPopupMenu()
    {
        popupMenu.add(newMenu);
        NewMenuItem("Duplicate","Ctrl+D",this::Clone);
        NewMenuItem("Select","Ctrl+C",this::Select);
        NewMenuItemActiveBySelect("Paste","Ctrl+V",this::Paste);
        NewMenuItemActiveBySelect("MoveTo","Ctrl+B",this::MoveTo);
        NewMenuItem("Up","1",()->Move(-1));
        NewMenuItem("Down","2",()->Move(1));
        NewMenuItem("Rename","F2",this::StartEditing);
        NewMenuItem("Delete","Delete",this::Delete);
    }

    @Override
    protected void OnKeyTyped(KeyEvent e) {
        if (e.getKeyChar()=='1') Move(-1);
        if (e.getKeyChar()=='2') Move(1);
        if (e.getKeyChar()==KeyEvent.VK_DELETE) Delete();

        if (e.getKeyChar()=='') Clone();
        if (e.getKeyChar()=='') Select();
        if (e.getKeyChar()=='' && GetClipSelected().size()>0) Paste();
        if (e.getKeyChar()=='' && GetClipSelected().size()>0) MoveTo();
    }

    @Override
    protected void OnShowPopupMenu() {
        List list = GetClipSelected();
        GetItem("Paste").setEnabled(list.size()>0);
        GetItem("MoveTo").setEnabled(list.size()>0);
    }
    public void NewMenuItem(int index,String name,String shortcut,Runnable cb)
    {
        popupMenu.add(NewItem(name,shortcut,cb),index);
    }
    public void NewMenuItemActiveBySelect(String name,String shortcut,Runnable cb)
    {
        JMenuItem item = NewItem(name,shortcut,cb);
        popupMenu.add(item);
    }
    public void NewMenuItemBySelect(int index,String name,String shortcut,Runnable cb)
    {
        JMenuItem item = NewItem(name,shortcut,cb);
        popupMenu.add(item,index);
    }
    public void SetTypes(String menuName,List<Class> list)
    {
        JMenu jMenu = new JMenu(menuName);
        newMenu.add(jMenu);
        AddTypes(jMenu,list);
    }
    public void SetTypes(List<Class> list)
    {
        AddTypes(newMenu,list);
    }
    private void AddTypes(JMenu menu,List<Class> list)
    {
        Util.For(list,ob->{
            JMenuItem item = new JMenuItem(ob.getSimpleName());
            item.addActionListener(e-> NewObject(Reflect.NewInstance(ob)));
            menu.add(item);
        });
    }
}
