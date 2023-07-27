package Extend.Box2D;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.Actor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GBox2D extends Actor {
    static{
        Box2D.init();
    }
    private static final float PTM = 0.01f;

    public World world;
    public boolean active = true;
    public final List<String> categories = new ArrayList<>();
    private final float TIME_STEP = 1/60f;
    private final int VELOCITY_ITERATIONS = 6, POSITION_ITERATIONS = 2;

    //debug
    private OrthographicCamera debugCamera;
    private Box2DDebugRenderer debugRenderer;
    private float accumulator;

    public GBox2D(){
        world = new World(new Vector2(0, -10f), true);
    }
    @Override
    public void act(float delta) {
        if (active)
            DoPhysicsStep(delta);
    }
    @Override
    public void drawDebug(ShapeRenderer shapes) {
        if (debugRenderer==null) SetDebug();
        OrthographicCamera camera = (OrthographicCamera)getStage().getCamera();
        debugCamera.zoom = camera.zoom;
        debugCamera.position.set(GameToPhysics(camera.position));
        debugCamera.update();
        debugRenderer.render(world, debugCamera.combined);
    }
    //category
    public void SetCategory(String data){//player,object...
        String[] arr = {"player","object"};
        if (data!=null) arr = data.split(",");
        categories.clear();
        categories.addAll(Arrays.asList(arr));
    }
    public String GetCategory(int bit)
    {
        int index = (int) MathUtils.log2(bit);
        return categories.get(index);
    }
    public short GetCategory(String name)
    {
        int index = categories.indexOf(name);
        return (short) Math.pow(2,index);
    }
    public short GetBit(List<String> list)
    {
        short bit = 0;
        for(String s : list)
            bit+=GetCategory(s);
        return bit;
    }
    public List<String> GetCategoryList(int bit)
    {
        List<String> list = new ArrayList<>();
        if (bit<0){
            list.addAll(categories);
            return list;
        }
        String st = Integer.toBinaryString(bit);
        int len = st.length();
        for (int i=0;i<st.length();i++)
            if (st.charAt(len-i-1)=='1') list.add(categories.get(i));
        return list;
    }
    //Function
    private void SetDebug()
    {
        if (debugRenderer!=null) return;
        OrthographicCamera camera = (OrthographicCamera)getStage().getCamera();
        debugCamera = new OrthographicCamera();
        debugCamera.setToOrtho(false,camera.viewportWidth*PTM,camera.viewportHeight*PTM);
        debugRenderer = new Box2DDebugRenderer();
    }
    private void DoPhysicsStep(float deltaTime) {
        //world.step( 1/60f, VELOCITY_ITERATIONS, POSITION_ITERATIONS);//to front
        accumulator += Math.min(deltaTime, 0.25f);
        if (accumulator >= TIME_STEP) {
            accumulator -= TIME_STEP;
            world.step(TIME_STEP, VELOCITY_ITERATIONS, POSITION_ITERATIONS);
        }
    }
    //
    public Body NewBody(BodyDef bodyDef){
        return world.createBody(bodyDef);
    }
    public void Destroy(Body body){
        world.destroyBody(body);
    }

    //Convert
    public static Vector3 GameToPhysics(Vector3 vec)
    {
        return new Vector3(vec).scl(PTM);
    }
    public static Vector2 GameToPhysics(Vector2 vec)
    {
        return new Vector2(vec).scl(PTM);
    }
    public static float GameToPhysics(float value) {
        return value*PTM;
    }
    public static Vector3 PhysicsToGame(Vector3 pos) {
        return new Vector3(pos).scl(1f/PTM);
    }
    public static Vector2 PhysicsToGame(Vector2 pos) {
        return new Vector2(pos).scl(1f/PTM);
    }
    public static float PhysicsToGame(float value){
        return value/PTM;
    }
}
