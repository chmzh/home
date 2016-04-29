package com.cmz.web.util;

import java.util.Map;

public class PageUtil {
	/**
	 * 
	 * @param pageIndex  当前页
	 * @param count      记录总数
	 * @param num        一页显示多少条
	 * @param url        分页地址
	 * @param params     分页参数
	 * @return
	 */
	public static String getPages(int pageIndex,int count,int num, String url,Map<String, Object> params){
		if(count<=num ){
			return "";
		}
		int showPages = 9;    //每次显示5页
		int pages = 0;        //总共有pages页
		
		pages = count/num;
		if(pages == 0 || (count%num>0)){
			pages = pages + 1;
		}

		int fromIndex = 1;        //从第几页开始显示
		
		if(pageIndex>5){     //当前页码大于5且小于总页码
			if(pageIndex+5<=pages){
				fromIndex = pageIndex-5;
			}else{
				if(pages>10){
					fromIndex = pages - 9;
				}else if(pages>9){
					fromIndex = pages - 8;
				}else if(pages>8){
					fromIndex = pages - 7;
				}else if(pages>7){
					fromIndex = pages - 6;
				}else if(pages>6){
					fromIndex = pages - 5;
				}else if(pages>5){
					fromIndex = pages - 4;
				}
			}
			
		}
		showPages = Math.min(pages, fromIndex+showPages);   //当前显示的页码范围
		
		String param = "";
		if(null!=params && params.size()>0){
			for(Map.Entry<String, Object> entry:params.entrySet()){
				param += "&"+entry.getKey()+"="+entry.getValue();
			}
			
		}
		
		String previous = "";
		if(pageIndex==1){
			previous = "<li class=\"paginate_button previous disabled\" aria-controls=\"dataTables-example\" tabindex=\"0\" id=\"dataTables-example_previous\"><a >Previous</a></li>";
		}else if(pageIndex>1){
			previous = "<li class=\"paginate_button previous\" aria-controls=\"dataTables-example\" tabindex=\"0\" id=\"dataTables-example_previous\"><a href=\""+url+"?page="+(pageIndex-1)+param+"\">Previous</a></li>";
		}else{
			previous = "";
		}
		
		String index = "";
		for(int i=fromIndex;i<=showPages;i++){
			
			if(i==pageIndex){
				index += "<li class=\"paginate_button active\" aria-controls=\"dataTables-example\" tabindex=\"0\"><a href=\""+url+"page="+i+param+"\">"+i+"</a></li>";
			}else{
				index += "<li class=\"paginate_button \" aria-controls=\"dataTables-example\" tabindex=\"0\"><a href=\""+url+"?page="+i+"\">"+i+param+"</a></li>";
			}
			
		}
		String next = "";
		if(pageIndex==pages){
			next = "<li class=\"paginate_button next disabled\" aria-controls=\"dataTables-example\" tabindex=\"0\" id=\"dataTables-example_next\"><a >Next</a></li>";
		}else if(pageIndex<pages){
			next = "<li class=\"paginate_button next\" aria-controls=\"dataTables-example\" tabindex=\"0\" id=\"dataTables-example_next\"><a href=\""+url+"?page="+(pageIndex+1)+param+"\">Next</a></li>";
		}else{
			next = "";
		}
		
		
		return previous + index + next;
	}
}
