package Tool.ObjectTool.Form;

import GDX11.Asset;
import Tool.JFrame.UI;
import Tool.ObjectTool.Data.GenerateAsset;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

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
