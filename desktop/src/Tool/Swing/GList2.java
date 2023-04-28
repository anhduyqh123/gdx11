package Tool.Swing;

import GDX11.Util;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.util.List;

public class GList2 extends BaseTree<String> {
    protected List<String> listID;
    public GList2(JTree tree){
        super(tree);
        InitPopupMenu();
    }
    public void SetData(List<String> listID)
    {
        this.listID = listID;
        SetRoot("root");
    }
    @Override
    protected List GetChildren(String ob) {
        if (ob.equals(root)) return listID;
        return null;
    }

    @Override
    protected void Rename() {
        int index = listID.indexOf(GetSelectedObject());
        String newID = GetSelectedNode().toString();
        listID.set(index,newID);
        Refresh();
        SetSelection(newID);
    }

    public void Delete()
    {
        Util.For(GetSelectedNodes(), n-> listID.remove(GetObject(n)));
        Refresh();
        if (listID.size()>0) SetSelection(listID.get(0));
    }
    public void Clone()
    {
        New("clone");
    }
    public void New()
    {
        New("new");
    }
    protected void New(String name)
    {
        listID.add(name);
        Refresh();
        SetSelection(name);
        StartEditing();
    }

    @Override
    protected void AddTo(List<String> list) {
        Util.For(list, listID::add);
        Refresh();
        SetSelection(list.get(0));
    }

    protected void InitPopupMenu()
    {
        NewMenuItem("New","Ctrl+N",this::New);
        NewMenuItem("Duplicate","Ctrl+D",this::Clone);
        NewMenuItem("Select","Ctrl+C",this::Select);
        NewMenuItem("Paste","Ctrl+V",this::Paste);
        NewMenuItem("Rename","F2",this::StartEditing);
        NewMenuItem("Delete","Delete",this::Delete);
    }
    @Override
    protected void OnKeyTyped(KeyEvent e) {
        if (e.getKeyChar()==KeyEvent.VK_DELETE) Delete();

        if (e.getKeyChar()=='') New();
        if (e.getKeyChar()=='') Clone();
        if (e.getKeyChar()=='') Select();
        if (e.getKeyChar()=='') Paste();
    }
}
