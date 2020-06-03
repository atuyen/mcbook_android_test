package com.bon.fragment;

import android.support.annotation.IntDef;

/**
 * Created by Dang on 5/27/2016.
 */
public class AnimationUtils {
    @IntDef({NONE, MOVE, CUBE, FLIP, PUSHPULL, SIDES, CUBEMOVE, MOVECUBE, PUSHMOVE, MOVEPULL})
    public @interface AnimationStyle {
    }

    public static final int NONE = 0;
    public static final int MOVE = 1;
    public static final int CUBE = 2;
    public static final int FLIP = 3;
    public static final int PUSHPULL = 4;
    public static final int SIDES = 5;
    public static final int CUBEMOVE = 6;
    public static final int MOVECUBE = 7;
    public static final int PUSHMOVE = 8;
    public static final int MOVEPULL = 9;

    @IntDef({NODIR, UP, DOWN, LEFT, RIGHT})
    public @interface AnimationDirection {
    }

    public static final int NODIR = 0;
    public static final int UP = 1;
    public static final int DOWN = 2;
    public static final int LEFT = 3;
    public static final int RIGHT = 4;

    public static final long DURATION = 500;
}
