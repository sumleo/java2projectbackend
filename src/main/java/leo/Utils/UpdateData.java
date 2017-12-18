package leo.Utils;

import leo.Model.Earthquake;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class UpdateData {
    private Connection connection;
    private Statement statement;
    private long date;
    public UpdateData(){
        initial();
    }
    private  void initial(){
        try{
            Class.forName(ConfigLoader.dbDriver);
            connection = DriverManager.getConnection(ConfigLoader.dbPath);
            statement=connection.createStatement();
            newestDate();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public void update(){
        System.out.println(new Date()+" I'm start working");
        int page=1;
        ArrayList <Earthquake> earthquakes;
        Crawler crawler=new Crawler(ConfigLoader.baseUrl+page);
        crawler.doCrawling();
        if (crawler.getMax()<date){
            System.out.println("The data is already newest!");
            return;
        }
        long tempMin=new Date().getTime();
        int count=0;
        while (true){
            if (tempMin<date){
                break;
            }
            earthquakes=crawler.getEarthquakes();
            for (Earthquake earthquake:earthquakes){
                if (earthquake.getDate()>date&&tempMin>earthquake.getDate()){
                    insert(earthquake);
                    count++;
                }
            }
            System.out.println("I'm updating the page "+ConfigLoader.baseUrl+page);
            page++;
            tempMin=crawler.getMin();
            crawler=null;
            crawler=new Crawler(ConfigLoader.baseUrl+page);
            crawler.doCrawling();
        }
        System.out.println(">>>"+count+" items updated!");
        System.out.println(new Date()+" Everything is new");
    }
    private boolean insert(Earthquake earthquake){
        try{
            String sql="insert into quakes values("+earthquake.getId()+",'"+earthquake.getUtcDate()+"',"+earthquake.getLatitude()+","+earthquake.getLongitude()
                    +","+earthquake.getDepth()+","+earthquake.getMagnitude()+",'"+earthquake.getRegion()+"',"+earthquake.getArea_id()+");";
            return statement.execute(sql);
        }catch (Exception e){
            System.out.println(earthquake);
            e.printStackTrace();
        }
        return true;
    }
    private void newestDate(){
        try{
            date=statement.executeQuery("SELECT * FROM quakes ORDER BY utc_date DESC ").getTimestamp("utc_date").getTime();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
