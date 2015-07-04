package com.example.bt;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;
import com.alibaba.fastjson.*;

/**
 * Created by ian on 15/7/2.
 */
public class Analysis {
    private ArrayList<Protocol> listRuleMD001;

    private static Analysis instance;
    public static Analysis getInstance(){
        if(instance==null){
            instance = new Analysis();
            instance.createMD001Rule();
        }
        return instance;
    }



    private ArrayList<Protocol> createMD001Rule(){
        listRuleMD001 = new ArrayList<Protocol>();
        //#1
        listRuleMD001.add(new Protocol("#1", 6, "引导模式", "GrubMode"));
        listRuleMD001.add(new Protocol("#1", 7, "电池电压", "Voltage"));

        //#3
        listRuleMD001.add(new Protocol("#3", 0, "前电机设定", "FrontElecSet"));
        listRuleMD001.add(new Protocol("#3", 1, "左电机设定", "LeftElecSet"));
        listRuleMD001.add(new Protocol("#3", 2, "右电机设定", "RightElecSet"));
        listRuleMD001.add(new Protocol("#3", 3, "后电机设定", "BackElecSet"));

        //#4
        listRuleMD001.add(new Protocol("#4", 3, "飞行时间", "FlyingTime"));

        //#5
        listRuleMD001.add(new Protocol("#5", 3, "GPS 定位精度", "GPSPrecision"));
        listRuleMD001.add(new Protocol("#5", 4, "卫星颗数", "GPSstar"));

        //#6
        listRuleMD001.add(new Protocol("#6", 0, "南北朝向的速度", "NorthEarthSpeed"));
        listRuleMD001.add(new Protocol("#6", 1, "东西朝向的速度", "EastEarthSpeed"));
        listRuleMD001.add(new Protocol("#6", 2, "往下的速度", "DownSpeed"));

        //#7
        listRuleMD001.add(new Protocol("#7", 0, "滚转角度", "Roll"));
        listRuleMD001.add(new Protocol("#7", 1, "俯仰角度", "Pitch"));
        listRuleMD001.add(new Protocol("#7", 2, "航向角度", "Yaw"));

        //#8
        listRuleMD001.add(new Protocol("#8", 0, "相对高度", "AtmosPreHeight"));
        listRuleMD001.add(new Protocol("#8", 1, "绝对高度", "GPSHeight"));
        listRuleMD001.add(new Protocol("#8", 2, "温度", "Tempreture"));


        JSONObject jsonObject =new JSONObject();
        for (int i=0;i<listRuleMD001.size();i++ ){
            Protocol tempP =  listRuleMD001.get(i);

            List<Protocol> tempList= (List<Protocol>) jsonObject.get(tempP.block);
            if(tempList!=null){
                    
            }


            jsonObject.put(tempP.block, tempP);
        }
       Log.i("Analysis",jsonObject.toJSONString());

        return listRuleMD001;
    }




    private class Protocol{
        private String block;//block名称
        private int pindex;//第几位参数
        private String name;//字段名称
        private String filed;//关键字段

          public Protocol(String block,int pindex,String name,String filed){
              this.block = block;
              this.pindex = pindex;
              this.name = name;
              this.filed = filed;
          }
    }


}
