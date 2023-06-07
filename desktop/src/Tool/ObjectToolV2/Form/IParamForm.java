package Tool.ObjectToolV2.Form;

import GDX11.IObject.IActor.IActor;
import GDX11.Util;
import Tool.Swing.GList2;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IParamForm {
    public JPanel panel1;
    private JTree tree;
    private JPanel pnInfo;
    private JTextArea textArea1;

    private IActor iActor;
    private Map<String,String> data;
    private GParamList gList = new GParamList(tree);

    public IParamForm(){
        gList.onSelect = key-> textArea1.setText(data.get(key));
        textArea1.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                if (gList.IsRoot()) return;
                String key = gList.GetSelectedObject();
                String vl = textArea1.getText();
                data.put(key,vl);
            }
        });
    }
    public void SetIActor(IActor iActor) {
        this.iActor = iActor;
        data = iActor.iParam.dataMap;
        gList.SetData();
    }
    public class GParamList extends GList2{
        private final Map<String,String> selectedMap = new HashMap<>();
        public GParamList(JTree tree) {
            super(tree);
        }
        public void SetData() {
            SetData(null);
        }

        @Override
        public void Refresh() {
            listID = new ArrayList<>(data.keySet());
            super.Refresh();
        }

        @Override
        protected void Rename() {
            String value = data.get(GetSelectedObject());
            data.remove(GetSelectedObject());
            data.put(GetSelectedNode().toString(),value);
            super.Rename();
        }

        @Override
        public void Delete() {
            Util.For(GetSelectedNodes(), n-> data.remove(GetObject(n)));
            super.Delete();
        }

        @Override
        public void New() {
            data.put("new","0");
            super.New();
        }

        @Override
        public void Clone() {
            data.put("clone",data.get(GetSelectedObject()));
            super.Clone();
        }

        @Override
        protected void AddTo(List<String> list) {
            Util.For(list,key->data.put(key, selectedMap.get(key)));
            super.AddTo(list);
        }

        @Override
        protected void Select() {
            GetClipSelectedList().clear();
            Util.For(GetSelectedNodes(), n->{
                String ob = GetObject(n);
                GetClipSelectedList().add(ob);
                selectedMap.put(ob,data.get(ob));
            });
        }
    }
}
