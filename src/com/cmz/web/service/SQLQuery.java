package com.huotun.service;

import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ArrayListHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Repository;


@Repository
public class SQLQuery extends JdbcDaoSupport{
	private QueryRunner queryRunner;
	public QueryRunner getQueryRunner() {
		return queryRunner;
	}
	
	@Autowired
	public SQLQuery(DataSource serverJdbc) {
		queryRunner = new QueryRunner(serverJdbc);
		super.setDataSource(serverJdbc);
	}
	
	/**
	 * 普通结果集查询
	 * @param sql
	 * @param params
	 * @return
	 */
	public List<Object[]> query(String sql,Object... params)throws Exception{
		return this.getQueryRunner().query(sql, new ArrayListHandler(), params);
	}
	
	/**
	 * 实体映射集合查询
	 * @param sql
	 * @param type
	 * @param params
	 * @return
	 */
	public <T> List<T> query(String sql,Class<T> type,Object... params)throws Exception{
		return this.getQueryRunner().query(sql, new BeanListHandler<T>(type), params);
	}
	
	/**
	 * 查询一列
	 * @param sql
	 * @param type
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public <T> List<T> queryField(String sql,Class<T> type,Object... params)throws Exception{
		List<Object[]> list = this.getQueryRunner().query(sql, new ArrayListHandler(), params);
		List<T> fieldList = new ArrayList<T>();
		if(null != list && list.size() > 0){
			for(Object[] objs : list){
				fieldList.add((T)objs[0]);
			}
		}
		return fieldList;
	}
	
	/**
	 * 查询一行一列
	 * @param sql
	 * @param type
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public <T>T queryFieldOneRow(String sql,Class<T> type,Object... params) throws Exception{
		List<T> list = this.queryField(sql, type, params);
		if(null != list && list.size()>0){
			return list.get(0);
		}else {
			return null;
		}
	}
	
	/**
	 * 实体映射样例查询
	 * @param sql
	 * @param type
	 * @param params
	 * @return
	 */
	public <T> T queryExample(String sql,Class<T> type,Object... params)throws Exception{
		List<T> resultList = this.query(sql, type, params);
		if(null != resultList && resultList.size()>0){
			return resultList.get(0);
		}else {
			return null;
		}
	}
	
	/**
	 * 统计查询 (一个统计结果)
	 * @param sql
	 * @param type
	 * @param params
	 * @return
	 */
	public <T> T queryCount(String sql,Class<T> type,Object... params)throws Exception{
		return this.getQueryRunner().query(sql, new ScalarHandler<T>(), params);
	}
	
	/**
	 * 查询第一条（适用于多字段统计）
	 * @param sql 
	 * @param params
	 * @return
	 */
	public Object[] queryFirst(String sql,Object... params)throws Exception{
		List<Object[]> resultList = this.query(sql, params);
		if(null != resultList && resultList.size()>0){
			return resultList.get(0);
		}else {
			return null;
		}
	}
	
}
