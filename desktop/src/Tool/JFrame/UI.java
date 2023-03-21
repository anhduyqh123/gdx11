package Tool.JFrame;

import GDX11.GDX;
import GDX11.Reflect;
import com.badlogic.gdx.utils.reflect.Field;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class UI {
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
    public static void NewComponent(Field field, Object object, JPanel parent)
    {
        Class type = field.getType();
        String name = field.getName();
        Object value = Reflect.GetValue(field, object);
        if (value==null) return;
        if (type == boolean.class) {
            NewCheckBox(name,(boolean)value,parent,result-> SetField(field,object,result));
            return;
        }
        if (type.isEnum()){
            Enum[] constants = (Enum[])type.getEnumConstants();
            NewComboBox(name,constants,value,parent,vl->SetField(field,object,vl));
            return;
        }
        if (name.contains("lign"))
        {
            NewAlignComboBox(name,object,parent);
            return;
        }
        if (name.equals("hexColor"))
        {
            NewColorPicker(parent,value.toString(),vl->SetField(field,object,vl));
            return;
        }
        if (name.startsWith("st"))
        {
            NewTextArea(name,value,100,20,parent,st->SetField(field,object,st));
            return;
        }
        if (Reflect.IsBaseType(type))
        {
            NewTextField(name,value,100,20,parent,st->SetField(field,object,st));
            return;
        }
        InitComponents(value, NewPanel(name,parent));
    }
    public static void InitComponents(Object object,JPanel parent)
    {
        for(Field f : Reflect.GetDataFieldMap(object.getClass()).values())
            NewComponent(f,object,parent);
    }
    public static void InitComponents(List<String> fieldNames, Object object, JPanel parent)
    {
        for(String name : fieldNames)
            NewComponent(Reflect.GetDataFieldMap(object.getClass()).get(name),object,parent);
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
    public static JPanel NewPanel(String name,JPanel parent)
    {
        JPanel pn = NewPanel(parent);
        pn.setBorder(BorderFactory.createTitledBorder(pn.getBorder(),name));
        return pn;
    }
    public static JPanel NewPanel(JPanel parent)
    {
        JPanel panel = new JPanel();
        parent.add(panel);
        panel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        panel.setMaximumSize(new Dimension(parent.getWidth(),1000));
        panel.setLayout(new WrapLayout());
        return panel;
    }
    //ComboBox
    public static JComboBox NewAlignComboBox(String fieldName,Object object,JPanel parent)
    {
        String[] arr = {"","bottomLeft","bottom","bottomRight","left","center","right","topLeft","top","topRight"};
        return NewComboBox(fieldName,arr, Reflect.GetValue(fieldName,object),parent,
                vl-> Reflect.SetValue(fieldName,object,vl));
    }
    public static <T> JComboBox NewComboBox(T[] items, T value, int width,int height)
    {
        JComboBox comboBox = new JComboBox(items);
        comboBox.setSelectedItem(value);
        SetSize(comboBox,width,height);
        return comboBox;
    }
    public static <T> JComboBox NewComboBox(String name, T[] items, T value, int width,int height, JPanel parent)
    {
        JComboBox comboBox = NewComboBox(items,value,width,height);
        LabelComponent(name,comboBox,parent);
        return comboBox;
    }
    public static <T> JComboBox NewComboBox(String name, T[] items, T value, JPanel parent,GDX.Runnable1<T> onChange)
    {
        JComboBox comboBox = NewComboBox(items,value,120,20);
        LabelComponent(name,comboBox,parent);
        comboBox.addActionListener(e->onChange.Run((T)comboBox.getSelectedItem()));
        return comboBox;
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
    public static JButton NewButton(String name,JPanel parent,Runnable onClick)
    {
        JButton button = new JButton(name);
        button.addActionListener(e -> onClick.run());
        parent.add(button);
        return button;
    }
    public static void Button(JButton bt, GDX.Runnable run)
    {
        bt.addActionListener(e->GDX.Try(run,true));
    }

    //CheckBox
    public static void CheckBox(JCheckBox cb, GDX.Runnable1<Boolean> onChange)
    {
        cb.addActionListener(e->onChange.Run(cb.isSelected()));
    }
    public static JCheckBox NewCheckBox(String name,boolean value,JPanel parent,GDX.Runnable1<Boolean> onChange)
    {
        JCheckBox checkBox = new JCheckBox();
        checkBox.setText(name);
        checkBox.setSelected(value);
        checkBox.addActionListener(e->onChange.Run(checkBox.isSelected()));
        parent.add(checkBox);
        return checkBox;
    }
    //TextField
    private static JTextField NewTextField(String value,int width,int height)
    {
        JTextField textField = new JTextField(value);
        SetSize(textField,width,height);
        return textField;
    }
    public static JTextField NewTextField(String name,Object value,int width,int height,JPanel parent,GDX.Runnable1<String> onChange)
    {
        JTextField textField = NewTextField(value.toString(), width,height);
        LabelComponent(name,textField,parent);
        if (onChange!=null)
            textField.addKeyListener(new KeyAdapter() {
                @Override
                public void keyReleased(KeyEvent e) {
                    try {
                        onChange.Run(textField.getText());
                    }catch (Exception x){}
                }
            });
        return textField;
    }
    //TextArea
    private static JTextArea NewTextArea(String value,int width,int height)
    {
        JTextArea textField = new JTextArea(value);
        SetSize(textField,width,height);
        return textField;
    }
    public static JTextArea NewTextArea(String name,Object value,int width,int height,JPanel parent,GDX.Runnable1<String> onChange)
    {
        JTextArea textField = NewTextArea(value.toString(), width,height);
        LabelComponent(name,textField,parent);
        if (onChange!=null)
            textField.addKeyListener(new KeyAdapter() {
                @Override
                public void keyReleased(KeyEvent e) {
                    try {
                        onChange.Run(textField.getText());
                    }catch (Exception x){}
                }
            });
        return textField;
    }
    public static JLabel NewLabel(String name,JPanel parent)
    {
        JLabel label = new JLabel(name);
        parent.add(label);
        return label;
    }
    //extend
    private static void LabelComponent(String text, JComponent component, JPanel parent)
    {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel lb = new JLabel(text);
        panel.add(lb);
        panel.add(component);
        parent.add(panel);
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
    private static Color GDXColorToColor(com.badlogic.gdx.graphics.Color cl)
    {
        return new Color(cl.r,cl.g,cl.b,cl.a);
    }
    private static com.badlogic.gdx.graphics.Color ColorToGDXColor(Color cl)
    {
        return new com.badlogic.gdx.graphics.Color(
                cl.getRed()/255f,cl.getGreen()/255f,cl.getBlue()/255f,cl.getAlpha()/255f);
    }
    private static void NewColorChooserWindow(String hex, GDX.Runnable1<String> onClose)
    {
        NewColorChooserWindow(com.badlogic.gdx.graphics.Color.valueOf(hex),onClose);
    }
    public static void NewColorChooserWindow(com.badlogic.gdx.graphics.Color color, GDX.Runnable1<String> onClose)
    {
        JColorChooser chooser = new JColorChooser();
        chooser.setColor(GDXColorToColor(color));
        NewJFrame("Color",chooser,()->onClose.Run(ColorToGDXColor(chooser.getColor()).toString()));
    }
    public static void NewColorPicker(JPanel panel,String hexColor ,GDX.Runnable1<String> onChance)
    {
        NewButton("Color",panel,()->NewColorChooserWindow(hexColor,onChance));
    }
    //</editor-fold>

    //JFrame
    public static JFrame NewJFrame(String name,JComponent content,Runnable onClosed)
    {
        JFrame frame = NewJFrame(name,content);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                onClosed.run();
            }
        });
        return frame;
    }
    private static JFrame NewJFrame(String name,JComponent content)
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
}
