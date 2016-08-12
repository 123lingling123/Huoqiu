package com.lsl.huoqiu.game.model;

import com.lsl.huoqiu.game.Cell;

/**
 * 每个Single的动画效果
 *
 * Created by Forrest on 16/8/9.
 */
public class SingleAnimationCell extends Cell {

    private int animationType;//动画类型
    private long timeElapsed;//运动时间
    private long animationTime;//动画时间
    private long delayTime;//持续时间
    public int[] extras;// 其他

    /**
     * 每个
     * @param x
     * @param y
     * @param animationType
     * @param timeElapsed
     * @param animationTime
     * @param extras
     */
    public SingleAnimationCell(int x, int y,int animationType,long timeElapsed,long animationTime,int[] extras) {
        super(x, y);
        this.animationType=animationType;
        this.timeElapsed=timeElapsed;
        this.animationTime=timeElapsed;
        this.extras=extras;
    }

    /**
     * 获取动画类型
     * @return
     */
    public int getAnimationType() {
        return animationType;
    }

    public void tick(long timeElapsed){
        this.timeElapsed=this.timeElapsed+timeElapsed;
    }

    public boolean animationDone(){
        return animationTime+delayTime<timeElapsed;
    }

    public double getPercentageDone(){
        return Math.max(0,1.0*(timeElapsed-delayTime)/animationTime);
    }

    public boolean isActive(){
        return timeElapsed>=delayTime;
    }
}
