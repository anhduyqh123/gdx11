package Tool.Swing;

import GDX11.GDX;
import GDX11.Reflect;
import GDX11.Util;
import com.badlogic.gdx.utils.reflect.Field;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

public class UI {
    private static final Map map = new HashMap();
    public static void SetUserObject(Object ob,Object user)
    {
        map.put(ob,user);
    }
    public static <T> T GetUserObject(Object ob)
    {
        return (T)map.get(ob);
    }
    //Data
    public static String[] ClassToNames(Class[] arr)
    {
        String[] names = new String[arr.length];
        for(int i=0;i<arr.length;i++)
            names[i] = arr[i].getSimpleName();
        return names;
    }
    public static List<String> GetFields(Object object)
    {
        List<String> list = new ArrayList<>();
        for(Field f : Reflect.GetDataFieldMap(object.getClass()).values())
            list.add(f.getName());
        return list;
    }
    //Object Component
    private static void SetField(Field field,Object object,Object value)
    {
        Class<?> type = field.getType();
        if (type == int.class) {
            Reflect.SetValue(field,object,Integer.valueOf(value.toString()));
            return;
        }
        if (type == float.class) {
            Reflect.SetValue(field,object,Float.valueOf(value.toString()));
            return;
        }
        Reflect.SetValue(field,object,value);
    }
    public static void NewComponent(Field field, Object object, JPanel panel)
    {
        Class type = field.getType();
        String name = field.getName();
        Object value = Reflect.GetValue(field, object);
        if (value==null) return;
        if (Reflect.IsAssignableFrom(type,List.class))
        {
            List list = (List)value;
            if ( Reflect.IsAssignableFrom(field.getElementType(0),String.class))
            {
                panel.add(new ListForm(list).panel1);
                return;
            }
            if (list.size()<=5)
                Util.ForIndex(list,i->InitComponents(list.get(i),NewPanel(name+i,panel)));
            return;
        }
        if (type == boolean.class) {
            NewCheckBox(field,object,panel);
            return;
        }
        if (type.isEnum()){
            Enum[] constants = (Enum[])type.getEnumConstants();
            NewComboBox(field,object,constants,panel);
            return;
        }
        if (name.contains("lign"))
        {
            NewAlignComboBox(name,object,panel);
            return;
        }
        if (name.equals("hexColor"))
        {
            NewColorPicker(field,object,panel);
            return;
        }
        if (name.startsWith("st_"))
        {
            NewTextArea(field,object,panel);
            return;
        }
        if (name.startsWith("percent"))
        {
            NewSlider(field,object,panel);
            return;
        }
        if (Reflect.IsBaseType(type))
        {
            NewTextField(field,object,100,20,panel);
            return;
        }
        InitComponents(value, NewPanel(name,panel));
    }
    public static void InitComponents(Object object,JPanel panel)
    {
        for(Field f : Reflect.GetDataFieldMap(object.getClass()).values())
            NewComponent(f,object,panel);
    }
    public static void InitComponents(List<String> fieldNames, Object object, JPanel panel)
    {
        for(String name : fieldNames)
            NewComponent(Reflect.GetDataFieldMap(object.getClass()).get(name),object,panel);
    }
    //Panel
    public static void Repaint(JPanel panel)
    {
        panel.revalidate();
        panel.repaint();
    }
    public static void ClearPanel(JPanel panel)
    {
        panel.removeAll();
        Repaint(panel);
    }
    public static JPanel NewPanel(String name,JPanel panel)
    {
        JPanel pn = NewPanel(panel);
        pn.setMaximumSize(new Dimension(panel.getWidth(),1000));
        pn.setLayout(new WrapLayout());
        pn.setBorder(BorderFactory.createTitledBorder(pn.getBorder(),name));
        return pn;
    }
    public static JPanel NewPanel(JPanel parent)
    {
        JPanel panel = new JPanel();
        parent.add(panel);
        panel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        return panel;
    }
    //ComboBox
    public static JComboBox NewAlignComboBox(String fieldName,Object object,JPanel panel)
    {
        String[] arr = {"","bottomLeft","bottom","bottomRight","left","center","right","topLeft","top","topRight"};
        return NewComboBox(fieldName,arr, Reflect.GetValue(fieldName,object),panel,
                vl->{
                    Reflect.SetValue(fieldName,object,vl);
                    Reflect.OnChange(object);
                });
    }
    public static <T> JComboBox NewComboBox(T[] items, T value, int width,int height)
    {
        JComboBox comboBox = new JComboBox(items);
        comboBox.setSelectedItem(value);
        SetSize(comboBox,width,height);
        return comboBox;
    }
    public static <T> JComboBox NewComboBox(String name, T[] items, T value, int width,int height, JPanel panel)
    {
        JComboBox comboBox = NewComboBox(items,value,width,height);
        LabelComponent(name,comboBox,panel);
        return comboBox;
    }
    public static <T> JComboBox NewComboBox(String name, T[] items, T value, JPanel panel,GDX.Runnable1<T> onChange)
    {
        JComboBox comboBox = NewComboBox(items,value,120,20);
        LabelComponent(name,comboBox,panel);
        comboBox.addActionListener(e->{
            onChange.Run((T)comboBox.getSelectedItem());
        });
        return comboBox;
    }
    public static <T> JComboBox NewComboBox(Field field,Object object,T[] items,JPanel panel)
    {
        return NewComboBox(field.getName(),items,Reflect.GetValue(field,object),panel,
                vl-> {
                    Reflect.SetValue(field,object,vl);
                    Reflect.OnChange(object);
                });
    }
    public static <T> JComboBox NewComboBox(String fieldName,Object object,T[] items,JPanel panel)
    {
        return NewComboBox(Reflect.GetField(object.getClass(),fieldName),object,items,panel);
    }
    public static <T> void ComboBox(JComboBox cb,T[] items)
    {
        cb.setModel(new DefaultComboBoxModel(items));
    }
    public static  <T> void ComboBox(JComboBox cb,T[] items, T value)
    {
        ComboBox(cb,items);
        cb.setSelectedItem(value);
    }
    public static  <T> void ComboBox(JComboBox cb, T[] items, T value, GDX.Runnable1<T> onChange)
    {
        ComboBox(cb,items,value);
        for (ActionListener e : cb.getActionListeners())
            cb.removeActionListener(e);
        cb.addActionListener(e->onChange.Run((T)cb.getSelectedItem()));
        cb.setSelectedItem(value);
    }
    //Button
    public static JButton NewButton(String name,JPanel panel,Runnable onClick)
    {
        JButton button = new JButton(name);
        button.addActionListener(e -> onClick.run());
        panel.add(button);
        return button;
    }
    public static void Button(JButton bt, GDX.Runnable run)
    {
        Util.For(Arrays.asList(bt.getActionListeners()), bt::removeActionListener);
        bt.addActionListener(e->GDX.Try(run,true));
    }

    //CheckBox
    public static void CheckBox(JCheckBox cb, GDX.Runnable1<Boolean> onChange)
    {
        Util.For(Arrays.asList(cb.getActionListeners()), cb::removeActionListener);
        cb.addActionListener(e->onChange.Run(cb.isSelected()));
    }
    public static JCheckBox NewCheckBox(String name,boolean value,JPanel panel,GDX.Runnable1<Boolean> onChange)
    {
        JCheckBox checkBox = new JCheckBox();
        checkBox.setText(name);
        checkBox.setSelected(value);
        checkBox.addActionListener(e->onChange.Run(checkBox.isSelected()));
        panel.add(checkBox);
        return checkBox;
    }
    public static JCheckBox NewCheckBox(Field field,Object object,JPanel panel)
    {
        return NewCheckBox(field.getName(),Reflect.GetValue(field,object),panel, vl-> {
            Reflect.SetValue(field,object,vl);
            Reflect.OnChange(object);
        });
    }
    //TextField
    public static void TextField(JTextField tf, GDX.Runnable1<String> onChange)
    {
        tf.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                GDX.Try(()->onChange.Run(tf.getText()));
            }
        });
    }
    private static JTextField NewTextField(String value,int width,int height)
    {
        JTextField textField = new JTextField(value);
        SetSize(textField,width,height);
        return textField;
    }
    public static JTextField NewTextField(String name,Object value,int width,int height,JPanel panel,GDX.Runnable1<String> onChange)
    {
        JTextField textField = NewTextField(value.toString(), width,height);
        LabelComponent(name,textField,panel);
        TextField(textField,onChange);
        return textField;
    }
    public static JTextField NewTextField(Field field,Object object,int width,int height,JPanel panel)
    {
        JTextField tf = NewTextField(field.getName(),Reflect.GetValue(field,object),width,height,panel,
                st-> SetField(field,object,st));
        tf.addActionListener(actionEvent ->Reflect.OnChange(object));
        Reflect.AddEvent(object,field,vl-> tf.setText(vl+""));
        return tf;
    }
    //TextArea
    private static JTextArea NewTextArea(String value,int width,int height)
    {
        JTextArea textField = new JTextArea(value);
        SetSize(textField,width,height);
        return textField;
    }
    public static JTextArea NewTextArea(String name,Object value,int width,int height,JPanel panel,GDX.Runnable1<String> onChange)
    {
        JTextArea textArea = NewTextArea(value.toString(), width,height);
        LabelComponent(name,textArea,panel);
        if (onChange!=null)
            textArea.addKeyListener(new KeyAdapter() {
                @Override
                public void keyReleased(KeyEvent e) {
                    try {
                        onChange.Run(textArea.getText());
                    }catch (Exception x){}
                }
            });
        return textArea;
    }
    public static JTextArea NewTextArea(Field field,Object object,JPanel panel)
    {
        return NewTextArea(field.getName(),Reflect.GetValue(field,object),100,30,panel,st->SetField(field,object,st));
    }
    public static JLabel NewLabel(String name,JPanel panel)
    {
        JLabel label = new JLabel(name);
        panel.add(label);
        return label;
    }
    //extend
    private static JLabel LabelComponent(String text, JComponent component, JPanel parent)
    {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel lb = new JLabel(text);
        panel.add(lb);
        panel.add(component);
        parent.add(panel);
        return lb;
    }
    public static void SetSize(JComponent component,int width,int height)
    {
        component.setPreferredSize(new Dimension(width,height));
    }

    //Dialog
    public static void NewDialog(String log,JPanel panel)
    {
        JOptionPane.showMessageDialog(panel,log);
    }

    //<editor-fold desc="ColorChooser">
    public static Color GDXColorToColor(com.badlogic.gdx.graphics.Color cl)
    {
        return new Color(cl.r,cl.g,cl.b,cl.a);
    }
    public static com.badlogic.gdx.graphics.Color ColorToGDXColor(Color cl)
    {
        return new com.badlogic.gdx.graphics.Color(
                cl.getRed()/255f,cl.getGreen()/255f,cl.getBlue()/255f,cl.getAlpha()/255f);
    }
    public static JFrame NewColorChooserWindow(Color color, GDX.Runnable1<Color> onClose)
    {
        JColorChooser chooser = new JColorChooser();
        chooser.setColor(color);
        return NewJFrame("Color",chooser,()->onClose.Run(chooser.getColor()));
    }
    public static void NewColorPicker(JPanel panel,String hexColor ,GDX.Runnable1<String> onChance)
    {
        JPanel pn = NewPanel(panel);
        SetSize(pn,30,30);
        com.badlogic.gdx.graphics.Color gdxColor = com.badlogic.gdx.graphics.Color.valueOf(hexColor);
        Color color = GDXColorToColor(gdxColor);
        pn.setBackground(color);
        LabelComponent("color",pn,panel);
        pn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                NewColorChooserWindow(color,cl->{
                    pn.setBackground(cl);
                    onChance.Run(ColorToGDXColor(cl).toString());
                });
            }
        });
    }
    public static void NewColorPicker(Field field,Object object,JPanel panel)
    {
        NewColorPicker(panel,Reflect.GetValue(field,object),st-> {
            Reflect.SetValue(field,object,st);
            Reflect.OnChange(object);
        });
    }
    //</editor-fold>
    //JFrame
    public static JFrame NewJFrame(String name,Component content,Runnable onClosed)
    {
        JFrame frame = NewJFrame(name,content);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                onClosed.run();
                frame.dispose();
            }
        });
        return frame;
    }
    public static JFrame NewJFrame(String name,Component content)
    {
        JFrame frame = NewJFrame(name);
        frame.add(content);
        frame.pack();
        frame.setLocationRelativeTo(null);
        return frame;
    }
    private static JFrame NewJFrame(String name)
    {
        JFrame frame = new JFrame(name);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setVisible(true);
        return frame;
    }

    //Slider
    public static JSlider NewSlider(String name, float value, JPanel panel , GDX.Runnable1<Float> cb)
    {
        JSlider cp = new JSlider();
        cp.setValue((int)(value*100));
        SetSize(cp,100,20);
        JLabel lb = LabelComponent(name+":"+value,cp,panel);
        SetSize(lb,90,20);
        cp.addChangeListener(e->{
            float vl = cp.getValue()/100f;
            lb.setText(name+":"+vl);
            cb.Run(vl);
        });
        return cp;
    }
    public static JSlider NewSlider(Field field, Object object, JPanel panel)
    {
        return NewSlider(field.getName(),Reflect.GetValue(field,object),panel,
                vl->{
                    Reflect.SetValue(field,object,vl);
                    Reflect.OnChange(object);
                });
    }

    //Menu
    public static JPopupMenu NewPopupMenu(List list, GDX.Func1<String,Object> getName, GDX.Runnable1<Object> onClick)
    {
        JPopupMenu popupMenu = new JPopupMenu();
        Util.For(list,i->popupMenu.add(NewMenuItem(getName.Run(i),()->onClick.Run(i))));
        return popupMenu;
    }
    public static JMenuItem NewMenuItem(String text,Runnable cb)
    {
        JMenuItem menuItem = new JMenuItem(text);
        menuItem.addActionListener(e-> cb.run());
        return menuItem;
    }
}
