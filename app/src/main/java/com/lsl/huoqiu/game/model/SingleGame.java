package com.lsl.huoqiu.game.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Forrest on 16/8/10.
 */
public class SingleGame {
    //动画相关
    public static final int SPAWN_ANIMATION = -1;
    public static final int MOVE_ANIMATION = 0;
    public static final int MERGE_ANIMATION = 1;

    public static final int FADE_GLOBAL_ANIMATION = 0;
    //动画时间相关
    public static final long MOVE_ANIMATION_TIME = SingleView.BASE_ANIMATION_TIME;
    public static final long SPAWN_ANIMATION_TIME = SingleView.BASE_ANIMATION_TIME;
    public static final long NOTIFICATION_ANIMATION_TIME = SingleView.BASE_ANIMATION_TIME * 5;
    public static final long NOTIFICATION_DELAY_TIME = MOVE_ANIMATION_TIME + SPAWN_ANIMATION_TIME;
    /**最高分数存储的字段*/
    private static final String HIGH_SCORE = "high score";
    //游戏状态
    public static final int GAME_WIN = 1;
    public static final int GAME_LOST = -1;
    public static final int GAME_NORMAL = 0;
    public static final int GAME_NORMAL_WON = 1;
    public static final int GAME_ENDLESS = 2;
    public static final int GAME_ENDLESS_WON = 3;

    public static final int startingMaxValue = 2048;
    /** 游戏数组*/
    public SingleGrid grid = null;
    /** 游戏数组动画*/
    public SingleAnimationGrid aGrid;
    /** 数组的X大小*/
    final int numSquaresX = 4;
    /**数组的Y大小*/
    final int numSquaresY = 4;
    /**开始的SingleTile*/
    final int startTiles = 2;
    /**结束的最大值 */
    public static int endingMaxValue;
    /** 上下文*/
    private Context mContext;
    /** 显示的View*/
    private SingleView mView;
    /**最高分*/
    public long highScore = 0;
    /**分数*/
    public long score = 0;
    /**存储的分数*/
    private long bufferScore = 0;
    /**存储的游戏的状态*/
    private int bufferGameState = 0;
    /**最新的分数*/
    public long lastScore = 0;
    /**最新的游戏状态*/
    public int lastGameState = 0;
    /**游戏状态*/
    public int gameState = 0;
    /**是否可以操作*/
    public boolean canUndo;



    /**构造器*/
    public SingleGame(Context context, SingleView view) {
        mContext = context;
        mView = view;
        endingMaxValue = (int) Math.pow(2, view.numCellTypes - 1);
    }

    public void newGame() {
        if (grid == null) {
            grid = new SingleGrid(numSquaresX, numSquaresY);
        } else {
            prepareUndoState();//预存储
            saveUndoState();//存储数据
            grid.clearGrid();//清楚数据
        }
        aGrid = new SingleAnimationGrid(numSquaresX, numSquaresY);
        highScore = getHighScore();
        if (score >= highScore) {
            highScore = score;
            recordHighScore();
        }
        score = 0;
        gameState = GAME_NORMAL;
        addStartTiles();
        mView.refreshLastTime = true;
        mView.resyncTime();
        mView.invalidate();
    }

    /**
     * 准备保存数组
     * 将分数保存
     * 将游戏状态保存
     */
    private void prepareUndoState() {
        grid.prepareSaveTiles();
        bufferScore = score;
        bufferGameState = gameState;
    }
    /**
     * 存储数组
     */
    private void saveUndoState() {
        grid.saveTiles();
        canUndo = true;
        lastScore =  bufferScore;
        lastGameState = bufferGameState;
    }
    /** 从存储中获取最高分数*/
    private long getHighScore() {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(mContext);
        return settings.getLong(HIGH_SCORE, -1);
    }
    /**记录最高分数*/
    private void recordHighScore() {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(mContext);
        SharedPreferences.Editor editor = settings.edit();
        editor.putLong(HIGH_SCORE, highScore);
        editor.commit();
    }
    /**添加开始的SingleTile*/
    private void addStartTiles() {
        for (int xx = 0; xx < startTiles; xx++) {
            this.addRandomTile();
        }
    }
    /**随机的添加SingleTile*/
    private void addRandomTile() {
        if (grid.isAvailableCell()) {
            int value = Math.random() < 0.9 ? 2 : 4;
            SingleTile tile = new SingleTile(grid.randomAvailableSingleCell(), value);
            spawnTile(tile);
        }
    }
    /**  在某一个Cell中插入动画*/
    private void spawnTile(SingleTile tile) {
        grid.insertTile(tile);
        aGrid.startAnimation(tile.getX(), tile.getY(), SPAWN_ANIMATION,
                SPAWN_ANIMATION_TIME, MOVE_ANIMATION_TIME, null); //Direction: -1 = EXPANDING
    }
    /**遍历数组 */
    private void prepareTiles() {
        for (SingleTile[] array : grid.field) {
            for (SingleTile tile : array) {
                if (grid.isCellOccupied(tile)) {
                    tile.setMergedFrom(null);
                }
            }
        }
    }
    /**移动Title，将SingleTitle移动至SingleCell*/
    private void moveTile(SingleTile tile, SingleCell cell) {
        grid.field[tile.getX()][tile.getY()] = null;
        grid.field[cell.getX()][cell.getY()] = tile;
        tile.updatePosition(cell);
    }
    /** 重置状态*/
    public void revertUndoState() {
        if (canUndo) {
            canUndo = false;
            aGrid.cancelAnimations();
            grid.revertTiles();
            score = lastScore;
            gameState = lastGameState;
            mView.refreshLastTime = true;
            mView.invalidate();
        }
    }
    /**是否胜利*/
    public boolean gameWon() {
        return (gameState > 0 && gameState % 2 != 0);
    }

    /**是否失败*/
    public boolean gameLost() {
        return (gameState == GAME_LOST);
    }
    /**游戏是否可以继续*/
    public boolean isActive() {
        return !(gameWon() || gameLost());
    }

    public void move(int direction) {
        aGrid.cancelAnimations();//清楚所有的动画效果
        // 0: up, 1: right, 2: down, 3: left
//        new SingleCell(0, -1), // up
//                new SingleCell(1, 0),  // right
//                new SingleCell(0, 1),  // down
//                new SingleCell(-1, 0)  // left
        if (!isActive()) {//判断游戏状态，如果不可以继续就跳出
            return;
        }
        prepareUndoState();//预存储
        SingleCell vector = getVector(direction);//根据传过来的方向来生成一个新的Cell
        List<Integer> traversalsX = buildTraversalsX(vector);
        List<Integer> traversalsY = buildTraversalsY(vector);
        boolean moved = false;

        prepareTiles();
        //遍历整个数组
        for (int xx: traversalsX) {
            for (int yy: traversalsY) {
                SingleCell cell = new SingleCell(xx, yy);
                SingleTile tile = grid.getCellContent(cell);

                if (tile != null) {
                    SingleCell[] positions = findFarthestPosition(cell, vector);
                    SingleTile next = grid.getCellContent(positions[1]);

                    if (next != null && next.getValue() == tile.getValue() && next.getMergedFrom() == null) {
                        SingleTile merged = new SingleTile(positions[1], tile.getValue() * 2);
                        SingleTile[] temp = {tile, next};
                        merged.setMergedFrom(temp);

                        grid.insertTile(merged);
                        grid.removeTile(tile);

                        // Converge the two tiles' positions
                        tile.updatePosition(positions[1]);

                        int[] extras = {xx, yy};
                        aGrid.startAnimation(merged.getX(), merged.getY(), MOVE_ANIMATION,
                                MOVE_ANIMATION_TIME, 0, extras); //Direction: 0 = MOVING MERGED
                        aGrid.startAnimation(merged.getX(), merged.getY(), MERGE_ANIMATION,
                                SPAWN_ANIMATION_TIME, MOVE_ANIMATION_TIME, null);

                        // Update the score
                        score = score + merged.getValue();
                        highScore = Math.max(score, highScore);

                        // The mighty 2048 tile
                        if (merged.getValue() >= winValue() && !gameWon()) {
                            gameState = gameState + GAME_WIN; // Set win state
                            endGame();
                        }
                    } else {
                        moveTile(tile, positions[0]);
                        int[] extras = {xx, yy, 0};
                        aGrid.startAnimation(positions[0].getX(), positions[0].getY(), MOVE_ANIMATION, MOVE_ANIMATION_TIME, 0, extras); //Direction: 1 = MOVING NO MERGE
                    }

                    if (!positionsEqual(cell, tile)) {
                        moved = true;
                    }
                }
            }
        }

        if (moved) {
            saveUndoState();
            addRandomTile();
            checkLose();
        }
        mView.resyncTime();
        mView.invalidate();
    }



    /** 创建一个*/
    private SingleCell getVector(int direction) {
        SingleCell[] map = {
                new SingleCell(0, -1), // up
                new SingleCell(1, 0),  // right
                new SingleCell(0, 1),  // down
                new SingleCell(-1, 0)  // left
        };
        return map[direction];
    }
    /**X方向的遍历*/
    private List<Integer> buildTraversalsX(SingleCell vector) {
        List<Integer> traversals = new ArrayList<Integer>();

        for (int xx = 0; xx < numSquaresX; xx++) {
            traversals.add(xx);
        }
        if (vector.getX() == 1) {//如果是右边
            //这里的反转视为了从右边开始时能从右边第一个开始
            Collections.reverse(traversals);//修改通过颠倒元素的顺序指定列表。

        }

        return traversals;
    }
    /**Y方向的遍历*/
    private List<Integer> buildTraversalsY(SingleCell vector) {
        List<Integer> traversals = new ArrayList<Integer>();

        for (int xx = 0; xx <numSquaresY; xx++) {
            traversals.add(xx);
        }
        if (vector.getY() == 1) {//如果是向下
            Collections.reverse(traversals);
        }

        return traversals;
    }
    /** 寻找最远的位置*/
    private SingleCell[] findFarthestPosition(SingleCell cell, SingleCell vector) {
        SingleCell previous;
        SingleCell nextCell = new SingleCell(cell.getX(), cell.getY());
        do {
            previous = nextCell;
            nextCell = new SingleCell(previous.getX() + vector.getX(),
                    previous.getY() + vector.getY());
        } while (grid.isCellWithinBounds(nextCell) && grid.isCellAvailable(nextCell));

        SingleCell[] answer = {previous, nextCell};
        return answer;
    }
    /**是否是结束游戏*/
    private void endGame() {
        aGrid.startAnimation(-1, -1, FADE_GLOBAL_ANIMATION, NOTIFICATION_ANIMATION_TIME, NOTIFICATION_DELAY_TIME, null);
        if (score >= highScore) {
            highScore = score;
            recordHighScore();
        }
    }
    /**结束条件*/
    private int winValue() {
        if (!canContinue()) {
            return endingMaxValue;
        } else {
            return startingMaxValue;
        }
    }
    /** 是否可以继续*/
    public boolean canContinue() {
        return !(gameState == GAME_ENDLESS || gameState == GAME_ENDLESS_WON);
    }
    /**返回这两个Cell是否相等*/
    private boolean positionsEqual(SingleCell first, SingleCell second) {
        return first.getX() == second.getX() && first.getY() == second.getY();
    }
    /** 监测游戏是否Lost*/
    private void checkLose() {
        if (!movesAvailable() && !gameWon()) {
            gameState = GAME_LOST;
            endGame();
        }
    }
    /**监测是否有*/
    private boolean movesAvailable() {
        return grid.isAvailableCell() || tileMatchesAvailable();
    }

    private boolean tileMatchesAvailable() {
        SingleTile tile;

        for (int xx = 0; xx < numSquaresX; xx++) {
            for (int yy = 0; yy < numSquaresY; yy++) {
                tile = grid.getCellContent(new SingleCell(xx, yy));

                if (tile != null) {
                    for (int direction = 0; direction < 4; direction++) {
                        SingleCell vector = getVector(direction);
                        SingleCell cell = new SingleCell(xx + vector.getX(), yy + vector.getY());

                        SingleTile other = grid.getCellContent(cell);

                        if (other != null && other.getValue() == tile.getValue()) {
                            return true;
                        }
                    }
                }
            }
        }

        return false;
    }
    /** 设置EndLessMode*/
    public void setEndlessMode() {
        gameState = GAME_ENDLESS;
        mView.invalidate();
        mView.refreshLastTime = true;
    }

}
