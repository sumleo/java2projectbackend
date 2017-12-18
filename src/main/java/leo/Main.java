package leo;

import leo.Utils.ConfigLoader;
import leo.Utils.Crawler;
import leo.Utils.UpdateData;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Main {
    public static void main(String [] args){
        ConfigLoader.initial();
        try {
         while (true){
             System.out.println(new Date()+">>>Update begins");
             UpdateData updateData=new UpdateData();
             updateData.update();
             System.out.println(new Date()+">>>Update ends I will sleep for an hour.");
             Thread.sleep(1000*3600);
         }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
