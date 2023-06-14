package GDX11.IObject.IComponent;

import GDX11.Asset;
import GDX11.Config;
import GDX11.GDX;
import GDX11.IObject.IActor.IImage;
import GDX11.Scene;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class IShader extends IComponent {
    public String fragName = "";
    public String vertName = "";
    protected transient ShaderProgram shader;
    protected transient Vector2 resolution = new Vector2(),uv = new Vector2(),uv2 = new Vector2();
    @Override
    public void Refresh() {
        GDX.PostRunnable(()->GDX.Try(this::Init));
    }
    protected void Init()
    {
        resolution.set(GetActor().getWidth(),GetActor().getHeight());
        if (GetIActor() instanceof IImage)
        {
            IImage iImage = GetIActor();
            TextureRegion tr = iImage.GetTexture();
            uv.set(tr.getU(),tr.getV());
            uv2.set(tr.getU2(),tr.getV2());
        }

        ShaderProgram.pedantic = false;
        shader = NewShader();
        if (!shader.isCompiled()) GDX.Error(shader.getLog());
    }
    protected ShaderProgram NewShader()
    {
        String fragment = GDX.GetString(Asset.i.GetNode(fragName).url);
        if (vertName.equals(""))
        {
            Batch batch = Scene.i.GetStage().getBatch();
            return new ShaderProgram(batch.getShader().getVertexShaderSource(),fragment);
        }
        String vertex = GDX.GetStringByKey(vertName);
        return new ShaderProgram(vertex,fragment);
    }

    @Override
    public void Draw(Batch batch, float parentAlpha, Runnable superDraw) {
        if (shader==null){
            superDraw.run();
            return;
        }
        batch.setShader(shader);
        shader.bind();
        DefaultUniform();
        UpdateUniform();
        superDraw.run();

        batch.setShader(null);
    }
    protected void DefaultUniform()
    {
        shader.setUniformf("resolution", resolution);
        shader.setUniformf("uv", uv);
        shader.setUniformf("uv2", uv2);
    }
    protected void UpdateUniform()
    {
        for (String key : GetIActor().iParam.GetData().keySet())
        {
            Object ob = GetUniform(key);
            if (ob==null) continue;
            if (key.startsWith("i_")) shader.setUniformi(key,(int)ob);
            if (key.startsWith("f_")) shader.setUniformf(key,(float)ob);
            if (key.startsWith("v2_")) shader.setUniformf(key,(Vector2)ob);
            if (key.startsWith("v3_")) shader.setUniformf(key,(Vector3)ob);
            if (key.startsWith("v4_")){
                GDX.Vector4 v4 = (GDX.Vector4) ob;
                shader.setUniformf(key,v4.x,v4.y,v4.z,v4.w);
            }
            if (key.startsWith("cl_")) shader.setUniformf(key,(Color) ob);
        }
    }
    private <T> T GetUniform(String key)
    {
        Object ob = Config.Has(key)?Config.Get(key):GetIActor().iParam.Get(key);
        if (ob instanceof String) return GetUniform((String) ob);
        if (ob instanceof GDX.Func) return (T)((GDX.Func<?>) ob).Run();
        return (T)ob;
    }
}
