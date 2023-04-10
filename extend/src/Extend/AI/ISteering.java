package Extend.AI;

import GDX11.IObject.IActor.IActor;
import GDX11.IObject.IComponent.IComponent;
import com.badlogic.gdx.ai.steer.behaviors.*;
import com.badlogic.gdx.ai.steer.proximities.FieldOfViewProximity;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class ISteering extends IComponent {
    public float maxLinearSpeed = 100;
    public float maxLinearAcceleration = 200;
    public float maxAngularSpeed = 5;
    public float maxAngularAcceleration = 10;
    public float separationDecayCoefficient = 500;
    private transient SteeringAgent character;

    @Override
    public void SetIActor(IActor iActor) {
        super.SetIActor(iActor);
    }

    @Override
    public void Refresh() {
        character = new SteeringAgent(GetActor(),false);
        character.setMaxLinearSpeed(maxLinearSpeed);
        character.setMaxLinearAcceleration(maxLinearAcceleration);
        character.setMaxAngularSpeed(maxAngularSpeed);
        character.setMaxAngularAcceleration(maxAngularAcceleration);

        Array<SteeringAgent> characters = new Array<>();

        FieldOfViewProximity<Vector2> proximity = new FieldOfViewProximity<Vector2>(character, characters, 140,
                270 * MathUtils.degreesToRadians);

        Alignment<Vector2> groupAlignmentSB = new Alignment<Vector2>(character, proximity);
        Cohesion<Vector2> groupCohesionSB = new Cohesion<Vector2>(character, proximity);
        Separation<Vector2> groupSeparationSB = new Separation<Vector2>(character, proximity) {
            @Override
            public float getDecayCoefficient () {
                // We want all the agents to use the same decay coefficient
                return separationDecayCoefficient;
            }

            @Override
            public Separation<Vector2> setDecayCoefficient (float decayCoefficient) {
                separationDecayCoefficient = decayCoefficient;
                return this;
            }

        };

        BlendedSteering<Vector2> blendedSteering = new BlendedSteering<Vector2>(character) //
                .add(groupAlignmentSB, .2f) //
                .add(groupCohesionSB, .06f) //
                .add(groupSeparationSB, 1.7f);

        // TODO set more proper values
        Wander<Vector2> wanderSB = new Wander<Vector2>(character) //
                // Don't use Face internally because independent facing is off
                .setFaceEnabled(false) //
                // No need to call setAlignTolerance, setDecelerationRadius and setTimeToTarget because we don't use internal Face
                .setWanderOffset(60) //
                .setWanderOrientation(10) //
                .setWanderRadius(40) //
                .setWanderRate(MathUtils.PI2 * 4);

        PrioritySteering<Vector2> prioritySteeringSB = new PrioritySteering<Vector2>(character, 0.0001f) //
                .add(blendedSteering) //
                .add(wanderSB);

        character.setSteeringBehavior(prioritySteeringSB);
        characters.add(character);
    }

    @Override
    public void Update(float delta) {
        if (character ==null) return;
        character.Update(delta);
    }
}
