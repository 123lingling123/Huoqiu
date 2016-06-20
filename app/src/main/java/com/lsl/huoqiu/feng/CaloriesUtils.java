package com.lsl.huoqiu.feng;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Forrest on 16/5/3.
 */
public class CaloriesUtils {
    private static ArrayList<CaloriesModel> mDatas;
    //这个就是充当数据库的作用，将数据添加到这个List里面
    public static ArrayList<CaloriesModel> getData(){
        mDatas=new ArrayList<CaloriesModel>();
        mDatas.add(new CaloriesModel(1,"大米",1000));
        mDatas.add(new CaloriesModel(2,"苹果",200));
        mDatas.add(new CaloriesModel(3,"西红柿",400));
        mDatas.add(new CaloriesModel(4,"黄瓜",600));
        mDatas.add(new CaloriesModel(5,"茄子",400));
        mDatas.add(new CaloriesModel(6,"梨",500));
        mDatas.add(new CaloriesModel(7,"椰子",600));
        return  mDatas;
    }
    //这个方法是为了得到某种事物的卡路里，首先该食物必须在该列表中存在，如果存在就返回对应事物的卡路里，否则就会返回0
    public static int getFoodCalories(String name){
        for (int i = 0; i < mDatas.size(); i++) {
            if (mDatas.get(i).getName().equals(name)){
                return mDatas.get(i).getCalories();
            }
        }

        return 0;
    }


    static class CaloriesModel{
        //定义一个model的数据 有id、name和calories
        private int id;
        private String name;
        private int calories;

        /**
         *  卡路里 model
         * @param id
         * @param name 名称
         * @param calories 卡路里值
         */
        public CaloriesModel(int id, String name, int calories) {
            this.id = id;
            this.name = name;
            this.calories = calories;
        }
        //以下是get方法和set方法
        //get方法就是为了得到某个数据，
        //set方法就是为了设置某个值
        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getCalories() {
            return calories;
        }

        public void setCalories(int calories) {
            this.calories = calories;
        }
    }
}
