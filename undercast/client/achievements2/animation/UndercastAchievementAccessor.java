package undercast.client.achievements2.animation;

import undercast.client.achievements2.UndercastAchievement;
import aurelienribon.tweenengine.TweenAccessor;

public class UndercastAchievementAccessor implements TweenAccessor<UndercastAchievement> {

    // The following lines define the different possible tween types.
    // It's up to you to define what you need :-)

    public static final int POSITION_X = 1;
    public static final int ALPHA = 2;

    @Override
    public int getValues(UndercastAchievement target, int tweenType, float[] returnValues) {
        switch (tweenType) {
        case POSITION_X:
            returnValues[0] = target.posX;
            return 1;
        case ALPHA:
            returnValues[0] = target.alpha;
            return 1;
        default:
            assert false;
            return -1;
        }
    }

    @Override
    public void setValues(UndercastAchievement target, int tweenType, float[] newValues) {
        switch (tweenType) {
        case POSITION_X:
            target.posX = newValues[0];
            break;
        case ALPHA:
            target.alpha = newValues[0];
            break;
        default:
            assert false;
            break;
        }
    }
}
