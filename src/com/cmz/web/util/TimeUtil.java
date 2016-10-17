package com.huotun.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;





public class TimeUtil {
	public static String addDay(String date,int day) throws ParseException{ 
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");  
        Date dt = sdf.parse(date);  
        Calendar rightNow = Calendar.getInstance();  
        rightNow.setTime(dt);  
  
        rightNow.add(Calendar.DAY_OF_MONTH, day);  
        Date dt1 = rightNow.getTime();  
        String reStr = sdf.format(dt1);  
        return reStr;  
	}
	
	public static int subDay(String startDate,String endDate) throws ParseException{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		
		Date dt1 = sdf.parse(startDate);
		Calendar rightNow = Calendar.getInstance();  
        rightNow.setTime(dt1);
        long time1 = rightNow.getTimeInMillis();
        
        Date dt2 = sdf.parse(endDate);  
        rightNow.setTime(dt2);
        long time2 = rightNow.getTimeInMillis();
        
        int day = (int)((time2 - time1)/(1000*24*3600));
        
        
        return day;
	}
	
	public static String curDate(){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		
		Date date = new Date();
		date.setTime(System.currentTimeMillis());
		return sdf.format(date);
	}
	
	public static void main(String[] args) {

		try {
			int day = subDay("2016-10-01", "2016-10-10");
			System.out.println(day);
			String date = addDay("2016-10-01", -2);
			System.out.println(date);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
