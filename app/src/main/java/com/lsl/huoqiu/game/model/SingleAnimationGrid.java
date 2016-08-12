package com.lsl.huoqiu.game.model;

import java.util.ArrayList;

/**
 * Created by Forrest on 16/8/9.
 */
public class SingleAnimationGrid {
    public ArrayList<SingleAnimationCell>[][] field;
    int activeAnimations = 0;
    boolean oneMoreFrame = false;
    public ArrayList<SingleAnimationCell> globalAnimation = new ArrayList<SingleAnimationCell>();

    /**
     * 构造器为每一个Single都创建一个SingleAnimationCell
     * @param x
     * @param y
     */
    public SingleAnimationGrid(int x, int y) {
        field = new ArrayList[x][y];

        for (int xx = 0; xx < x; xx++) {
            for (int yy = 0; yy < y; yy++) {
                field[xx][yy] = new ArrayList<SingleAnimationCell>();
            }
        }
    }


    /**
     * 为固定的Cell添加Animation
     * @param x
     * @param y
     * @param animationType
     * @param length
     * @param delay
     * @param extras
     */
    public void startAnimation(int x, int y, int animationType, long length, long delay, int[] extras) {
        SingleAnimationCell animationToAdd = new SingleAnimationCell(x, y, animationType, length, delay, extras);
        if (x == -1 && y == -1) {
            globalAnimation.add(animationToAdd);//如果超出了显示范围就添加到globalAnimation中
        } else {
            field[x][y].add(animationToAdd);
        }
        activeAnimations = activeAnimations + 1;
    }

    /**
     *
     * @param timeElapsed
     */
    public void tickAll(long timeElapsed) {
        ArrayList<SingleAnimationCell> cancelledAnimations = new ArrayList<SingleAnimationCell>();
        for (SingleAnimationCell animation : globalAnimation) {
            animation.tick(timeElapsed);
            if (animation.animationDone()) {
                cancelledAnimations.add(animation);
                activeAnimations = activeAnimations - 1;
            }
        }

        for (ArrayList<SingleAnimationCell>[] array : field) {
            for (ArrayList<SingleAnimationCell> list : array) {
                for (SingleAnimationCell animation : list) {
                    animation.tick(timeElapsed);
                    if (animation.animationDone()) {
                        cancelledAnimations.add(animation);
                        activeAnimations = activeAnimations - 1;
                    }
                }
            }
        }

        for (SingleAnimationCell animation : cancelledAnimations) {
            cancelAnimation(animation);
        }
    }

    /**
     * 移除掉
     * @param animation
     */
    public void cancelAnimation(SingleAnimationCell animation) {
        if (animation.getX() == -1 && animation.getY() == -1) {
            globalAnimation.remove(animation);
        } else {
            field[animation.getX()][animation.getY()].remove(animation);
        }
    }

    /**
     * 动画是否存活
     * @return
     */
    public boolean isAnimationActive() {
        if (activeAnimations != 0) {
            oneMoreFrame = true;
            return true;
        } else if (oneMoreFrame) {
            oneMoreFrame = false;
            return true;
        } else {
            return false;
        }
    }

    /**
     * 得到某一个Cell
     * @param x
     * @param y
     * @return
     */
    public ArrayList<SingleAnimationCell> getAnimationCell(int x, int y) {
        return field[x][y];
    }

    /**
     * 清楚所有的Animation
     */
    public void cancelAnimations() {
        for (ArrayList<SingleAnimationCell>[] array : field) {
            for (ArrayList<SingleAnimationCell> list : array) {
                list.clear();
            }
        }
        globalAnimation.clear();
        activeAnimations = 0;
    }

}
