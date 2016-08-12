package com.lsl.huoqiu.game.model;

/**
 * Created by Forrest on 16/8/9.
 */
public class SingleTile extends  SingleCell {
    private int value;
    private SingleTile [] mergedFrom=null;//判断是否合并过，合并过就不在

    public SingleTile(int x, int y,int value) {
        super(x, y);
        this.value=value;
    }

    public SingleTile(SingleCell cell,int value){
        super(cell.getX(),cell.getY());
        this.value=value;
    }

    /**
     * 更新Title的信息
     * @param cell
     */
    public void updatePosition(SingleCell cell) {
        this.setX(cell.getX());
        this.setY(cell.getY());
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public SingleTile[] getMergedFrom() {
        return mergedFrom;
    }

    public void setMergedFrom(SingleTile[] mergedFrom) {
        this.mergedFrom = mergedFrom;
    }
}
