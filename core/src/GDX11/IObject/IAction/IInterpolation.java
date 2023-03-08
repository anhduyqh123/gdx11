package GDX11.IObject.IAction;

import com.badlogic.gdx.math.Interpolation;

public enum IInterpolation {
    linear(Interpolation.linear),
    fade(Interpolation.fade),
    smooth(Interpolation.smooth),
    swing(Interpolation.swing),
    swingIn(Interpolation.swingIn),
    swingOut(Interpolation.swingOut),
    bounce(Interpolation.bounce),
    bounceIn(Interpolation.bounceIn),
    bounceOut(Interpolation.bounceOut),
    circle(Interpolation.circle),
    circleIn(Interpolation.circleIn),
    circleOut(Interpolation.circleOut),
    fastSlow(Interpolation.fastSlow),
    sine(Interpolation.sine),
    sineIn(Interpolation.sineIn),
    sineOut(Interpolation.sineOut),
    elastic(Interpolation.elastic),
    elasticIn(Interpolation.elasticIn),
    elasticOut(Interpolation.elasticOut),
    pow2(Interpolation.pow2),
    pow2In(Interpolation.pow2In),
    pow2Out(Interpolation.pow2Out);

    public Interpolation value;
    IInterpolation(Interpolation value)
    {
        this.value = value;
    }
}
