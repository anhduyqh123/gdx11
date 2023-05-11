package Tool.ParticleViewer;

import GDX11.*;
import GDX11.IObject.IMap;
import GDX11.IObject.IObject;
import Tool.Swing.GTree2;
import Tool.Swing.UI;
import com.badlogic.gdx.backends.lwjgl.LwjglCanvas;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEmitter;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.ParticleEffectActor;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ViewForm {
    public JPanel panel1;
    private JTree tree1;
    private JPanel pnView;
    private ParticleEffectActor par;
    private IGroupNode data;
    private GTree2 gTree = new GTree2(tree1){
        @Override
        protected void OnKeyTyped(KeyEvent e) {
            if (e.getKeyChar() == KeyEvent.VK_DELETE) Delete();
        }
        @Override
        protected void InitPopupMenu() {
            NewMenuItem("Delete","Delete",this::Delete);
            NewMenuItem("Export","",()->Export());
            NewMenuItem("ClearTrash","",()->ClearTrash());
        }

        @Override
        protected void OnShowPopupMenu() {}
    };

    public ViewForm()
    {
        gTree.onSelect = this::OnSelect;
        InitGDXGame();
    }
    private void InitGDXGame()
    {
        LwjglCanvas game = new LwjglCanvas(new GDXGame(){
            protected void Init()
            {
                new GDX();
                audio = new GAudio();
                scene = NewScene();
                asset = NewAssets();
                Scene.i.GetStage().addActor(asset);
                Install();
            }
            protected Scene NewScene(Batch batch)
            {
                return new Scene(720,1280,batch);
            }
        });
        pnView.add(game.getCanvas(), BorderLayout.CENTER);
    }
    private void Install() {
        data = new IGroupNode();
        gTree.SetRoot(data);
        Scene.i.GetStage().addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (par==null) return false;
                par.setPosition(x,y);
                par.start();
                return true;
            }

            @Override
            public void touchDragged(InputEvent event, float x, float y, int pointer) {
                par.setPosition(x,y);
            }
        });
        InitControlCamera();
    }
    private void InitControlCamera()
    {
        OrthographicCamera camera  = (OrthographicCamera) Scene.i.GetStage().getCamera();
        Scene.i.GetStage().addListener(new ClickListener(){
            @Override
            public boolean scrolled(InputEvent event, float x, float y, float amountX, float amountY) {
                float delta = amountY* GDX.DeltaTime()*2;
                camera.zoom+=delta;
                return super.scrolled(event, x, y, amountX, amountY);
            }
        });
    }
    private void OnSelect(Object object) {
        if (!(object instanceof INode)) return;
        INode iNode = (INode) object;
        ParticleEffect effect = new ParticleEffect();
        effect.load(iNode.file,iNode.file.parent());
        par = new ParticleEffectActor(effect,true);
        par.setPosition(Scene.i.width/2,Scene.i.height/2);
        Scene.i.ui.clearChildren();
        Scene.i.ui.addActor(par);
        par.start();
    }
    private void Export()
    {
        try {
            INode iNode = (INode)gTree.GetSelectedObject();
            iNode.Export(GDX.GetFile("export"));
            UI.NewDialog("success!",panel1);
        }catch (Exception e){
            UI.NewDialog(e.getLocalizedMessage(),panel1);
        }
    }
    private void ClearTrash()
    {
        List<FileHandle> totalImg = GDX.GetFiles(new FileHandle("."),"png");
        HashSet<String> set = new HashSet<>();
        for (IObject p : data.iMap.list) set.addAll(((INode)p).imagePath);
        List<FileHandle> trash = new ArrayList<>();
        for (FileHandle f : totalImg)
            if (!set.contains(f.name())){
                trash.add(f);
                new FileHandle(f.path()).delete();
            }
        totalImg.removeAll(trash);
        UI.NewDialog("Deleted "+trash.size()+" files",panel1);
    }
    public class INode extends IObject
    {
        public FileHandle file;
        public ParticleEffect pe = new ParticleEffect();
        public Set<String> imagePath;

        public INode(FileHandle file)
        {
            this.name = file.nameWithoutExtension();
            this.file = file;
            pe.load(file, file.parent());
            imagePath = GetImages(pe);
        }
        private Set<String> GetImages(ParticleEffect pe)
        {
            Set<String> list = new HashSet<>();
            for (ParticleEmitter i : pe.getEmitters())
            {
                for (String s : i.getImagePaths())
                    list.add(GetImageName(s));
            }
            return list;
        }
        private String GetImageName(String path)
        {
            String[] arr = path.split("\\\\");
            return arr[arr.length-1];
        }
        public void Export(FileHandle dir)
        {
            Clone(dir.path()+"/"+file.name(),file);
            for (String s : imagePath)
            {
                String path = file.parent().path()+"/"+s;
                Clone(dir.path()+"/"+s,new FileHandle(path));
            }
        }
        private void Clone(String path,FileHandle fileHandle)
        {
            FileHandle clone = new FileHandle(path);
            fileHandle.copyTo(clone);
        }
        public void Delete()
        {
            new FileHandle(file.path()).delete();
        }
    }
    public class IGroupNode extends IObject
    {
        public IMap<IObject> iMap = new IMap<>();
        {
            iMap.onRemove = this::OnRemove;
        }
        public IGroupNode()
        {
            name = "all";
            Util.For(GDX.GetFiles(new FileHandle("."),"p"),file->Add(new INode(file)));
        }
        public void Add(IObject node)
        {
            iMap.Add(node);
        }
        private void OnRemove(IObject object)
        {
            INode iNode = (INode) object;
            iNode.Delete();
        }
    }
}
