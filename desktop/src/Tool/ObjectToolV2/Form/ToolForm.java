package Tool.ObjectToolV2.Form;

import Tool.ObjectTool.Core.GenerateAsset;
import Tool.Swing.UI;

import javax.swing.*;

public class ToolForm {
    private JButton btAndroid;
    public JPanel panel1;
    private JProgressBar progressBar1;
    private JComboBox cbPack;
    private JCheckBox cbTiny;

    public ToolForm()
    {
        UI.NewJFrame("Tool",panel1,()->{});

        GenerateAsset generateAsset = new GenerateAsset();
        generateAsset.tiny = ()->cbTiny.isSelected();
        generateAsset.cbProgress = progressBar1::setString;
        generateAsset.onFinish = ()-> UI.NewDialog("completed!",panel1);

        UI.ComboBox(cbPack,generateAsset.GetPacks().toArray(new String[0]));
        UI.Button(btAndroid,()->{
            btAndroid.setEnabled(false);
            generateAsset.Run(cbPack.getSelectedItem()+"");
        });
    }
}
