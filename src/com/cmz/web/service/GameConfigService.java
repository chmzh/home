package com.cmz.web.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cmz.web.dao.GameConfigDao;
import com.cmz.web.domain.GameConfig;

@Service
public class GameConfigService {
	
	@Autowired
	private GameConfigDao gameconfigdao;
	
	/**
	 * 添加
	 * @param logconfig
	 * @return
	 */
	public int add(GameConfig gameconfig){
		gameconfig.setGameflag((gameconfig.getGameflag()).toLowerCase());
		int id = gameconfigdao.add(gameconfig);
		return id;
	}
	
	/**
	 * 列表
	 * @param from
	 * @param num
	 * @return
	 */
	public List<GameConfig> getPageList(int from,int num){
		List<GameConfig> gameconfig = gameconfigdao.getPageList(from, num);
		return gameconfig;
	}
	
	
	/**
	 * 获取所有游戏
	 * @param from
	 * @param num
	 * @return
	 */
	public List<GameConfig> getGames(){
		List<GameConfig> gameconfig = gameconfigdao.getGames();
		return gameconfig;
	}
	
	/**
	 * 根据id查找
	 * @param int id
	 * @return
	 */
	public GameConfig getById(int id){
		GameConfig gameconfig = gameconfigdao.getById(id);
		return gameconfig;
	}
	
	/**
	 * 根据Gameflag查找
	 * @param String gameflag
	 * @return
	 */
	public GameConfig getByGameflag(String gameflag){
		GameConfig gameconfig = gameconfigdao.getByGameflag(gameflag);
		return gameconfig;
	}
	
	
	/**
	 * 更新
	 * @param inti id
	 * @return int
	 */
//	public int update(String gamename,String gameflag,String secretkey,int id){
//		gameflag = gameflag.toLowerCase();
//		int result = gameconfigdao.update(gamename,gameflag,secretkey,id);
//		return result;
//	}
	
	public int update(GameConfig gameConfig){
		int result = gameconfigdao.update(gameConfig);
		return result;
	}
	
	/**
	 * delete
	 * @param inti id
	 * @return int
	 */
	public int del(int id){
		int result = gameconfigdao.del(id);
		return result;
	}
	
	/**
	 * count
	 * @return int
	 */
	public int count(){
		int result = gameconfigdao.count();
		return result;
	}

}
