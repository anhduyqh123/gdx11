package Tool.ObjectTool.Form;

import GDX11.IObject.IActor.IActor;
import GDX11.Util;
import Tool.JFrame.UI;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.List;
import java.util.Map;

public class IParamForm {
    private JList list1;
    private JTextArea textArea1;
    private JTextField tfName;
    private JButton btNew;
    private JButton btDelete;
    private JButton btPaste;
    private JButton btClone;
    public JPanel panel1;

    private IActor iActor;
    private Map<String,String> data;

    public IParamForm()
    {
        list1.addListSelectionListener(e->{
            int index = list1.getSelectedIndex();
            if (index<0) return;
            String key = list1.getSelectedValue()+"";
            tfName.setText(key);
            textArea1.setText(data.get(key));
        });
        textArea1.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                if (list1.getSelectedIndex()==-1) return;
                String key = list1.getSelectedValue()+"";
                String vl = textArea1.getText();
                data.put(key,vl);
            }
        });
        UI.Button(btNew,()->New(tfName.getText()));
        UI.Button(btDelete,this::Delete);
    }
    public void SetIActor(IActor iActor)
    {
        this.iActor = iActor;
        data = iActor.iParam.dataMap;
        RefreshData();
        list1.setSelectedIndex(0);
    }
    private void RefreshData()
    {
        DefaultListModel l = new DefaultListModel<>();
        Util.For(data.keySet(),s->l.addElement(s));
        list1.setModel(l);
    }
    private void New(String name)
    {
        data.put(name,"");
        RefreshData();
        list1.setSelectedValue(name,true);
    }
    private void Delete()
    {
        Util.For(list1.getSelectedValuesList(),i-> data.remove(i));
        RefreshData();
        if (data.size()>0) list1.setSelectedIndex(0);
    }
}
