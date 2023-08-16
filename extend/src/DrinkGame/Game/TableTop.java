package DrinkGame.Game;

import DrinkGame.Base.BaseGame;
import DrinkGame.Base.VControl;
import Extend.Box2D.IBody;
import Extend.Box2D.IBodyListener;
import GDX11.Config;
import GDX11.GAudio;
import GDX11.GDX;
import GDX11.IObject.IActor.IActor;
import GDX11.IObject.IActor.IGroup;
import GDX11.Scene;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class TableTop extends BaseGame {
    private final IGroup board = game.FindIGroup("board");
    private IActor preBall;
    private boolean startCheck;
    private int id = -1;
    public TableTop() {
        super("tabletop");
        //game.FindActor("box2d").debug();

        Config.i.SetRun1("newBall", this::NewBall);
        NewGame();
    }

    @Override
    protected void NewGame() {
        board.FindIActor("img").Run("body_off");
        board.RunAction("newBall");
    }

    private void NewBall(IGroup ball){
        ball.FindIImage("ball").SetTexture("coin"+NewID());
        VControl control = new VControl(ball);
        control.SetLimitDegree(-60,60);
        startCheck = false;
        control.onStart = ()->{
          if (preBall!=null) preBall.GetActor().remove();
        };
        control.onWaitingForEnd = ()->startCheck=true;
        IBody iBody = ball.iComponents.GetIComponent("body");
        iBody.active = true;
        List<Contact> contacts = new ArrayList<>();
        iBody.AddEvent(new IBodyListener() {
            @Override
            public void OnBeginContact(IBody iBodyB, Fixture fixtureB, Contact contact) {
                contacts.add(contact);
                GAudio.i.PlaySingleSound("drop_ball",0.6f);
            }

            @Override
            public void OnEndContact(IBody iBodyB, Fixture fixtureB, Contact contact) {
                contacts.remove(contact);
            }

            @Override
            public void OnUpdate(float delta) {
                if (!startCheck) return;
                if (iBody.body.getLinearVelocity().len()<=0.1f){
                    iBody.body.setLinearVelocity(0,0);
                    iBody.body.setFixedRotation(true);
                    iBody.active = false;
                    //board.FindIActor("img").Run("body_on");
                    ball.Run(()->board.FindIActor("img").Run("body_on"),0.2f);
                    ball.Run(()->{
                        preBall = ball;
                        if (contacts.size()<19) Drop(ball,iBody);
                        else Completed();
                    },0.4f);
                }
            }
        });
    }
    private void Completed(){
        GAudio.i.PlaySound("completed");
        NewGame();
    }
    private void Drop(IGroup ball,IBody iBody){
        Vector2 pos = iBody.GetBodyPos();
        if (ball.GetPosition().sub(pos).len()>10) GAudio.i.PlaySound("drop2");
        float rotate = Scene.StageToLocalRotation(iBody.GetBodyRotate(),ball.GetActor());
        Action ac00 = Actions.moveTo(pos.x,pos.y,0.4f, Interpolation.bounceOut);
        Action ac01 = Actions.rotateTo(rotate,0.4f);
        Action ac0 = Actions.parallel(ac00,ac01);
        Action ac1 = Actions.run(this::Completed);
        ball.GetActor().addAction(Actions.sequence(ac0,ac1));
    }
    private int NewID(){
        List<Integer> list = new ArrayList<>(Arrays.asList(1,2,3,4,5,6));
        list.remove((Object)id);
        Collections.shuffle(list);
        id = list.get(0);
        return id;
    }
}
