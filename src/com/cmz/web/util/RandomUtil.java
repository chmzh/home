package com.cmz.web.util;

import java.util.Random;

/**
 * 随机数处理
 */
public class RandomUtil {
	private static Random rand=new Random();

	/**
	 * 返回随机概率的某个下标
	 * @param rates
	 * @return
	 */
	public static int getRandomIndex(final int rates[]){
		int sum = 0;
		for(int rate:rates){
			sum+=rate;
		}
		
		int random = rand.nextInt(sum);
		sum = 0;
		for(int i=0;i<rates.length;i++){
			sum+=rates[i];
			if(random<sum){
				return i;
			}
		}
		return -1;//不可能发生了
	}
    /**
     * 返回0到high(不包括)的随机整数
     * @param high
     * @return
     */
    public static int randInt(int high){
    	return rand.nextInt(high);
    }
    /**
     * 返回指定范围(包括high,low)的随机整数
     * @param high
     * @param low
     * @return
     */
    public static int randInt(int low,int high){
    	int d=high-low+1;
    	return rand.nextInt(d)+low;
    }
    /**
     * 返回指定范围(包括low,不包括high)的随机整数
     * @param high
     * @param low
     * @return
     */
    public static int randIntLow(int low,int high){
    	int d=high-low;
    	return rand.nextInt(d)+low;
    }

}
