package Tool.ObjectTool.Form;

import Tool.Swing.UI;
import Tool.ObjectTool.Core.GenerateAsset;

import javax.swing.*;

public class ToolForm {
    private JButton btAndroid;
    public JPanel panel1;
    private JProgressBar progressBar1;
    private JComboBox cbPack;

    public ToolForm()
    {
        GenerateAsset generateAsset = new GenerateAsset();
        generateAsset.cbProgress = progressBar1::setString;
        generateAsset.onFinish = ()-> UI.NewDialog("completed!",panel1);

        UI.ComboBox(cbPack,generateAsset.GetPacks().toArray(new String[0]));
        UI.Button(btAndroid,()->{
            btAndroid.setEnabled(false);
            generateAsset.Run(cbPack.getSelectedItem()+"");
        });
    }
}
