package com.lsl.huoqiu.game.model;

import java.util.ArrayList;

/**
 * 用于存放信息的数组
 *
 * Created by Forrest on 16/8/9.
 */
public class SingleGrid {

    public SingleTile[][] field;
    public SingleTile [][] undoField;
    private SingleTile[][] bufferField;


    public SingleGrid(int sizeX,int sizeY){
        field=new SingleTile[sizeX ][sizeY];
        undoField=new SingleTile[sizeX ][sizeY];
        bufferField=new SingleTile[sizeX ][sizeY];
        clearGrid();
        clearUndoGrid();
    }

    /**
     * 随机在空的SingleCell中挑选一个SingleCell
     * @return
     */
    public SingleCell randomAvailableSingleCell(){
        ArrayList<SingleCell> availableCell=getAvailableCell();
        if (availableCell.size()>1){
            return availableCell.get((int) Math.floor(Math.random()*availableCell.size()));
        }
        return null;
    }

    /**
     * 获取到空的SingleCell
     * @return
     */
    public ArrayList<SingleCell> getAvailableCell() {
        ArrayList<SingleCell> availableCell=new ArrayList<SingleCell>();
        for (int xx=0;xx<field.length;xx++){
            for (int yy=0;yy<field[0].length;yy++) {
                if (field[xx][yy]==null) {
                    availableCell.add(new SingleCell(xx,yy));
                }
            }
        }
        return availableCell;
    }

    /**
     * 是否有多余的SingleCell
     * @return
     */
    public boolean isAvailableCell(){
        return getAvailableCell().size()>=1;
    }


    public boolean isCellAvailable(SingleCell cell) {
        return !isCellOccupied(cell);
    }

    public boolean isCellOccupied(SingleCell cell) {
        return (getCellContent(cell) != null);
    }

    /***
     * 如果Cell不等于null，并且在显示范围之内的话返回这个Cell的详细信息
     * @param cell
     * @return
     */
    public SingleTile getCellContent(SingleCell cell) {
        if (cell != null && isCellWithinBounds(cell)) {
            return field[cell.getX()][cell.getY()];
        } else {
            return null;
        }
    }

    /**
     * 得到Cell 的Context
     * @param x
     * @param y
     * @return
     */
    public SingleTile getCellContent(int x, int y) {
        if (isCellWithinBounds(x, y)) {
            return field[x][y];
        } else {
            return null;
        }
    }


    /**
     * 是Cell并且在显示范围之内
     * @param cell
     * @return
     */
    public boolean isCellWithinBounds(SingleCell cell) {
        return 0 <= cell.getX() && cell.getX() < field.length
                && 0 <= cell.getY() && cell.getY() < field[0].length;
    }




    /**
     * 某个位置是Cell并且在显示范围之内
     * @param x
     * @param y
     * @return
     */
    public boolean isCellWithinBounds(int x, int y) {
        return 0 <= x && x < field.length
                && 0 <= y && y < field[0].length;
    }


    /**
     * 插入一个Cell
     * @param tile
     */
    public void insertTile(SingleTile tile) {
        field[tile.getX()][tile.getY()] = tile;
    }

    /**
     * 移除一个Cell
     * @param tile
     */
    public void removeTile(SingleTile tile) {
        field[tile.getX()][tile.getY()] = null;
    }



    /**
     * 转存将bufferField的内容转存到undoField中
     */
    public void saveTiles() {
        for (int xx = 0; xx < bufferField.length; xx++) {
            for (int yy = 0; yy < bufferField[0].length; yy++) {
                if (bufferField[xx][yy] == null) {
                    undoField[xx][yy] = null;
                } else {
                    undoField[xx][yy] = new SingleTile(xx, yy, bufferField[xx][yy].getValue());
                }
            }
        }
    }

    /**
     * 将field中的内容转存到bufferField中
     */
    public void prepareSaveTiles() {
        for (int xx = 0; xx < field.length; xx++) {
            for (int yy = 0; yy < field[0].length; yy++) {
                if (field[xx][yy] == null) {
                    bufferField[xx][yy] = null;
                } else {
                    bufferField[xx][yy] = new SingleTile(xx, yy, field[xx][yy].getValue());
                }
            }
        }
    }

    /**
     * 将undoField中的内容存到field中
     */
    public void revertTiles() {
        for (int xx = 0; xx < undoField.length; xx++) {
            for (int yy = 0; yy < undoField[0].length; yy++) {
                if (undoField[xx][yy] == null) {
                    field[xx][yy] = null;
                } else {
                    field[xx][yy] = new SingleTile(xx, yy, undoField[xx][yy].getValue());
                }
            }
        }
    }




    /**
     * 清空数组
     */
    public void clearGrid() {
        for (int xx = 0; xx < field.length; xx++) {
            for (int yy = 0; yy < field[0].length; yy++) {
                field[xx][yy] = null;
            }
        }
    }

    /**
     * 清空
     */
    public void clearUndoGrid() {
        for (int xx = 0; xx < field.length; xx++) {
            for (int yy = 0; yy < field[0].length; yy++) {
                undoField[xx][yy] = null;
            }
        }
    }



}
