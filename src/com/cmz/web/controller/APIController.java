package com.cmz.web.controller;

import java.io.IOException;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.cmz.web.app.IGameUpdate;
import com.cmz.web.app.ILogTypeUpdate;
import com.cmz.web.constant.GlobalConstant;
import com.cmz.web.domain.GameConfig;
import com.cmz.web.domain.LogConfig;
import com.cmz.web.jsonforma.DvJson;
import com.cmz.web.jsonforma.DvNewJson;
import com.cmz.web.jsonforma.HeaderJson;
import com.cmz.web.jsonforma.OutJson;
import com.cmz.web.service.GameConfigService;
import com.cmz.web.service.LogConfigService;
import com.cmz.web.service.MailService;
import com.cmz.web.util.DesUtil;
import com.cmz.web.util.FlumeHttpClient;
import com.google.gson.Gson;

@Controller
public class APIController implements IGameUpdate, ILogTypeUpdate {
	private static final Logger logger = LoggerFactory.getLogger(APIController.class);
	private static final ConcurrentMap<String, String[]> logConfigMap = new ConcurrentHashMap<>();
	private static final ConcurrentMap<String, String> gameConfigMap = new ConcurrentHashMap<>();

	// ConcurrentMap<游戏ID, 最后请求时间> 警报用
	private static final ConcurrentMap<String, Long> LOG_DETECTION = new ConcurrentHashMap<String, Long>();
	
	private static final ConcurrentMap<String, GameConfig> GAMES = new ConcurrentHashMap<String, GameConfig>();
	
	private static ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();

	@Autowired
	private LogConfigService logconfigservice;
	@Autowired
	private GameConfigService gameConfigService;
	@Autowired
	private MailService mailService;

	@PostConstruct
	public void init() {
		List<GameConfig> gameConfigs = gameConfigService.getGames();
		for (GameConfig gameConfig : gameConfigs) {
			addToGameConfigMap(gameConfig);
		}

		List<LogConfig> logConfigs = logconfigservice.getAll();
		for (LogConfig logConfig : logConfigs) {
			addToLogConfigMap(logConfig);
		}

		for (GameConfig gameConfig : gameConfigs) {
			addToDetection(gameConfig);
			addToGames(gameConfig);
		}

		executor.scheduleAtFixedRate(new Task(), 1, 5, TimeUnit.MINUTES);
	}

	class Task implements Runnable {
		@Override
		public void run() {
			for (Map.Entry<String, Long> entry : LOG_DETECTION.entrySet()) {
				GameConfig gameConfig = GAMES.get(entry.getKey());
				if (!gameConfig.isAlarm()) { // 该游戏是否需要报警
					continue;
				}
					long lastTime = entry.getValue();
					// 如果5分钟没有收到数据，则报警
					long curTime = System.currentTimeMillis();
					int expireTime = 5 * 60 * 1000; // 一分钟
					if (curTime - lastTime > expireTime) {

						try {
							// 报警 发短信 或 发邮件
							sendMail(gameConfig.getMails(), entry.getKey());
							sendMessage(entry.getKey());
							// start 如果对游戏警报则此处 break出去，如果对日志类型警报 则请注释掉 //break;
							break;
							// end 如果对游戏警报则此处 break出去，如果对日志类型警报 则请注释掉 //break;
						} catch (Throwable e) {
							logger.error("游戏:" + gameConfig.getGamename()+"("+gameConfig.getGameflag()+")" + ",超过五分钟没有日志,通知管理员",
									e.getLocalizedMessage());
						} finally {

						}

					}
			}
		}

		/**
		 * 发邮件
		 */
		private void sendMail(String to, String gameId) {
			String subject = "动网先锋日志收集系统";
			String content = "游戏:" + gameId + ",有很长一段时间没有写日志，去看看怎么回事吧！";
			mailService.sendMail(to, subject, content);
		}

		/**
		 * 发手机短信
		 */
		private void sendMessage(String gameId) {
			// TODO
		}

	}

	private void addToGames(GameConfig gameConfig){
		GAMES.put(gameConfig.getGameflag(), gameConfig);
	}

	/**
	 * 添加到报警容器
	 * 
	 * @param gameConfig
	 * @param logConfig
	 */
	private void addToDetection(GameConfig gameConfig) {
		long time = System.currentTimeMillis();
		LOG_DETECTION.putIfAbsent(gameConfig.getGameflag(), time);
	}

	private void addToGameConfigMap(GameConfig gameConfig) {
		try {
			gameConfigMap.put((gameConfig.getGameflag()).toLowerCase(),
					DesUtil.decryptDES(GlobalConstant.GAME_KEY, gameConfig.getSecretkey()));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void addToLogConfigMap(LogConfig logConfig) {
		String[] info = new String[2];
		info[0] = logConfig.getSelector();
		info[1] = logConfig.getLogfields();
		logConfigMap.put((logConfig.getLogtypeflag()).toLowerCase(), info);
	}

	// 获取日志类型加载到map中，减少读取数据库次数
	public String[] getLogConfigByFlag(String logtypeflag) {
		logtypeflag = logtypeflag.toLowerCase();
		String[] logs = logConfigMap.get(logtypeflag);
		if (logs == null) {
			LogConfig logconfigs = logconfigservice.getByLogtypeflag(logtypeflag);
			addToLogConfigMap(logconfigs);
			//logger.info("查询数据库logconfig：条件Logtypeflag=" + logtypeflag);
		} else {
			//logger.info("查询logConfigMap：条件Logtypeflag=" + logtypeflag);
		}
		return logConfigMap.get(logtypeflag);

	}

	private static void putGameKey(String gameFlag, String secretKey) {
		try {
			gameConfigMap.put(gameFlag.toLowerCase(), secretKey);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	// 获取游戏的加密验证密钥
	public String getGameKey(String gameflag) {
		gameflag = gameflag.toLowerCase();
		String gameKey = gameConfigMap.get(gameflag);
		if (gameKey == null) {
			GameConfig gameConfig = gameConfigService.getByGameflag(gameflag);
			try {

				addToGameConfigMap(gameConfig);
			} catch (Exception e) {
				e.printStackTrace();
			}
			//logger.info("查询数据库gameconfig：条件gameflag=" + gameflag);
		} else {
			//logger.info("查询gameConfigMap：条件gameflag=" + gameflag);
		}
		return gameConfigMap.get(gameflag);
	}

	@RequestMapping(value = "/json", method = RequestMethod.POST)
	public void json(@RequestBody String json, HttpServletRequest request, HttpServletResponse response) {

		int code = 0;
		// 解析json
		Gson gson = new Gson();
		DvJson retList = null;
		try {
			retList = gson.fromJson(json, DvJson.class);
		} catch (Throwable e) {
			OutJson m = new OutJson();
			m.setCode(8);
			m.setMsg("Json 格式不对");
			outPutJson(response, gson, m);
			return;
		}

		if (retList.getHeaders().getTimestamp() > 1990000000) { // 2033-01-22
			OutJson m = new OutJson();
			m.setCode(-1);
			m.setMsg("10位时间戳");
			outPutJson(response, gson, m);
			return;
		}

		// 传递给flume的新json对象
		DvNewJson[] dvNewJson = new DvNewJson[1];

		if (StringUtils.isEmpty(retList.getHeaders().getGameId())) {
			OutJson m = new OutJson();
			m.setCode(5);
			m.setMsg("GAMID不能为空");
			outPutJson(response, gson, m);
			return;
		}
		
		GameConfig gameConfig = GAMES.get(retList.getHeaders().getGameId().toLowerCase());
		
		if(gameConfig==null || !gameConfig.isOpen()){
			OutJson m = new OutJson();
			m.setCode(5);
			m.setMsg("该游戏已经关闭日志功能");
			outPutJson(response, gson, m);
			return;
		}		
		if (StringUtils.isEmpty(retList.getHeaders().getLogType())) {
			OutJson m = new OutJson();
			m.setCode(5);
			m.setMsg("日志类型不能为空");
			outPutJson(response, gson, m);
			return;
		}
		
		if(!logConfigMap.containsKey(retList.getHeaders().getLogType().toLowerCase())){
			OutJson m = new OutJson();
			m.setCode(5);
			m.setMsg("日志类型不正确");
			outPutJson(response, gson, m);
			return;
		}

		
		
		String _key = getGameKey(retList.getHeaders().getGameId());
		if (StringUtils.isEmpty(retList.getHeaders().getLogType())) {
			OutJson m = new OutJson();
			m.setCode(5);
			m.setMsg("系统获取key失败");
			outPutJson(response, gson, m);
			return;
		}
		String newsign = retList.getHeaders().getGameId().toLowerCase()
				+ retList.getHeaders().getLogType().toLowerCase() + retList.getHeaders().getTimestamp() + _key;
		// 验证sign
		String sign = DigestUtils.md5DigestAsHex(newsign.getBytes());
		if (!retList.getHeaders().getSign().equals(sign)) {
			//logger.info("签名：原始字符串:" + newsign + ",客户端生成:" + retList.getHeaders().getSign() + ",服务端生成:" + sign);
			OutJson m = new OutJson();
			m.setCode(5);
			m.setMsg("sign验证failed");

			outPutJson(response, gson, m);
			return;
		}

		// 记录日志发送时间

		String gameId = retList.getHeaders().getGameId().toLowerCase();
		String logType = retList.getHeaders().getLogType().toLowerCase();

		// 获取flume sink选择器类型
		String[] result = getLogConfigByFlag((retList.getHeaders().getLogType()).toLowerCase());
		String selectorHeader = (result[0]).toUpperCase();

		// 解析字段，重新组合
		String _field = result[1];

		String[] fieldArr = _field.split(",");
		String[] newfield = new String[fieldArr.length];
		String[] types = new String[fieldArr.length];
		for (int j = 0; j < fieldArr.length; j++) {
			newfield[j] = StringUtils.split(fieldArr[j], ":")[0];
			types[j] = StringUtils.split(fieldArr[j], ":")[1];
		}

		// 重新生成新的headers
		HeaderJson headerJson = new HeaderJson();
		headerJson.setColumns((StringUtils.join(newfield, ',')).toLowerCase());
		headerJson.setGameId((retList.getHeaders().getGameId()).toLowerCase());
		headerJson.setFamily("info");
		headerJson.setLogType((retList.getHeaders().getLogType()).toLowerCase());
		headerJson.setTable((retList.getHeaders().getGameId() + "_" + retList.getHeaders().getLogType()).toLowerCase());
		headerJson.setSelectorHeader(selectorHeader);

		// 发送到flume初始化
		FlumeHttpClient flumeHttpClient = new FlumeHttpClient();

		// 取出收到json里面的body数组
		String body = retList.getBody();
		if(body == null || body.equals("")){
			OutJson m = new OutJson();
			m.setCode(5);
			m.setMsg("body不能为空");

			outPutJson(response, gson, m);
			return;
		}
		// hbase 的rowkey，必须唯一的
		Date now = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
		Random random = new Random();
		// int s = random.nextInt(1000)%(1000-10+1) + 10;
		int s = random.nextInt(10000000);
		headerJson.setRowKey(formatter.format(now) + "_" + s);

		// 设置新的headers
		dvNewJson[0] = new DvNewJson();
		String[] ba = body.split(",");
		// ============================================规则检查
		// start==============================================
		// 字段长度和值长度
		int balen = ba.length;
		if (fieldArr.length != balen) {
			OutJson m = new OutJson();
			m.setCode(-1);
			m.setMsg("数据长度不一致丢失，请检查");
			outPutJson(response, gson, m);
			return;
		}
		StringBuilder builder = new StringBuilder();
		for(int i=0;i<balen;i++){
			ba[i] = StringUtils.trim(ba[i]);
			builder.append(ba[i]);
			if(i!=balen-1){
				builder.append(",");
			}
		}
		body = builder.toString();
		
		// 检查时间戳是否10位数字
		boolean timebol = false;
		for (int i2 = 0; i2 < newfield.length; i2++) {
			String v11 = ba[i2];

			if (newfield[i2].equals("ts")) {
				if (v11.length() != 10) {
					timebol = true;
					OutJson m = new OutJson();
					m.setCode(-1);
					m.setMsg("10位时间戳");
					outPutJson(response, gson, m);
					return;
				} else {
					try {
						int v = Integer.valueOf(v11);
						if (v > 1990000000) { // 2033-01-22
							timebol = true;
							OutJson m = new OutJson();
							m.setCode(-1);
							m.setMsg("时间戳类型不正确");
							outPutJson(response, gson, m);
							return;
						}
					} catch (NumberFormatException e) {
						timebol = true;
						OutJson m = new OutJson();
						m.setCode(-1);
						m.setMsg("时间戳类型不正确");
						outPutJson(response, gson, m);
						return;
					}
				}
				if (timebol) {
					break;
				}
			}
		}

		// 类型检查，只检查数值类型
		boolean typebol = false;
		for (int i1 = 0; i1 < types.length; i1++) {

			String v11 = ba[i1];

			switch (types[i1]) {
			case "int":
				try {
					Integer.valueOf(v11);
				} catch (NumberFormatException e) {
					typebol = true;
					OutJson m = new OutJson();
					m.setCode(-1);
					m.setMsg(newfield[i1] + ":数据类型丢失，请检查");
					outPutJson(response, gson, m);
					return;
				}
				break;
			case "float":
				try {
					Float.valueOf(v11);
				} catch (NumberFormatException e) {
					typebol = true;
					OutJson m = new OutJson();
					m.setCode(-1);
					m.setMsg(newfield[i1] + ":数据类型丢失，请检查");
					outPutJson(response, gson, m);
					return;
				}
				break;
			case "double":
				try {
					Double.valueOf(v11);
				} catch (NumberFormatException e) {
					typebol = true;
					OutJson m = new OutJson();
					m.setCode(-1);
					m.setMsg(newfield[i1] + ":数据类型丢失，请检查");
					outPutJson(response, gson, m);
					return;
				}
				break;
			default:
				break;
			}

			if (typebol) {
				break;
			}
		}

		// ============================================规则检查
		// end==============================================

		long timestamp = Long.valueOf(ba[ba.length - 1]);
		headerJson.setTimestamp(timestamp * 1000);
		dvNewJson[0].setHeaders(headerJson);

		dvNewJson[0].setBody(body);

		String json1 = gson.toJson(dvNewJson);
		//logger.info(json1);
		// 发送到flume
		code = flumeHttpClient.postJson(json1);

		// 0.成功 1.执行方法失败 2.协议错误 3.网络错误
		if (code == 0) {
			// 更新日志时间
			LOG_DETECTION.put(gameId,System.currentTimeMillis());
			// 更新日志时间
			OutJson m = new OutJson();
			m.setCode(code);
			m.setMsg("成功");
			outPutJson(response, gson, m);
			return;

		} else if (code == 1) {
			OutJson m = new OutJson();
			m.setCode(code);
			m.setMsg("执行失败");
			outPutJson(response, gson, m);
			return;
		} else if (code == 3) {
			OutJson m = new OutJson();
			m.setCode(code);
			m.setMsg("执行失败2");
			outPutJson(response, gson, m);
			return;
		} else {
			OutJson m = new OutJson();
			m.setCode(6);
			m.setMsg("未知错误");
			outPutJson(response, gson, m);
			return;
		}

	}

	//@RequestMapping(value="logs",method = RequestMethod.POST)
	public void log(@RequestBody String logs, HttpServletRequest request, HttpServletResponse response){
		
		int code = 0;
		// 解析json
		Gson gson = new Gson();
		DvJson retList = null;
		try {
			retList = gson.fromJson(logs, DvJson.class);
		} catch (Throwable e) {
			OutJson m = new OutJson();
			m.setCode(8);
			m.setMsg("Json 格式不对");
			outPutJson(response, gson, m);
			return;
		}

		if (StringUtils.isEmpty(retList.getHeaders().getGameId())) {
			OutJson m = new OutJson();
			m.setCode(5);
			m.setMsg("GAMID不能为空");
			outPutJson(response, gson, m);
			return;
		}
		
		GameConfig gameConfig = GAMES.get(retList.getHeaders().getGameId().toLowerCase());
		
		if(gameConfig==null || !gameConfig.isOpen()){
			OutJson m = new OutJson();
			m.setCode(5);
			m.setMsg("该游戏已经关闭日志功能");
			outPutJson(response, gson, m);
			return;
		}		
		if (StringUtils.isEmpty(retList.getHeaders().getLogType())) {
			OutJson m = new OutJson();
			m.setCode(5);
			m.setMsg("日志类型不能为空");
			outPutJson(response, gson, m);
			return;
		}
		
		if(!logConfigMap.containsKey(retList.getHeaders().getLogType().toLowerCase())){
			OutJson m = new OutJson();
			m.setCode(5);
			m.setMsg("日志类型不正确");
			outPutJson(response, gson, m);
			return;
		}

		
		
		String _key = getGameKey(retList.getHeaders().getGameId());
		if (StringUtils.isEmpty(retList.getHeaders().getLogType())) {
			OutJson m = new OutJson();
			m.setCode(5);
			m.setMsg("系统获取key失败");
			outPutJson(response, gson, m);
			return;
		}
		String newsign = retList.getHeaders().getGameId().toLowerCase()
				+ retList.getHeaders().getLogType().toLowerCase() + retList.getHeaders().getTimestamp() + _key;
		// 验证sign
		String sign = DigestUtils.md5DigestAsHex(newsign.getBytes());
		if (!retList.getHeaders().getSign().equals(sign)) {
			//logger.info("签名：原始字符串:" + newsign + ",客户端生成:" + retList.getHeaders().getSign() + ",服务端生成:" + sign);
			OutJson m = new OutJson();
			m.setCode(5);
			m.setMsg("sign验证failed");

			outPutJson(response, gson, m);
			return;
		}
		
		
		String gameId = retList.getHeaders().getGameId().toLowerCase();
		String logType = retList.getHeaders().getLogType().toLowerCase();
		
		
		long timestamp = retList.getHeaders().getTimestamp();
		// 获取flume sink选择器类型
		String[] result = getLogConfigByFlag((retList.getHeaders().getLogType()).toLowerCase());
		String selectorHeader = (result[0]).toUpperCase();

		// 解析字段，重新组合
		String _field = result[1];

		String[] fieldArr = _field.split(",");
		String[] newfield = new String[fieldArr.length];
		String[] types = new String[fieldArr.length];
		for (int j = 0; j < fieldArr.length; j++) {
			newfield[j] = StringUtils.split(fieldArr[j], ":")[0];
			types[j] = StringUtils.split(fieldArr[j], ":")[1];
		}
		
		
		// ============================================规则检查
		// start==============================================
		String body1 = retList.getBody();
		if(body1 == null || body1.equals("")){
			OutJson m = new OutJson();
			m.setCode(5);
			m.setMsg("body不能为空");

			outPutJson(response, gson, m);
			return;
		}
		String[] lines = body1.split("\r\n");
		if(lines==null){
			OutJson m = new OutJson();
			m.setCode(5);
			m.setMsg("body不能为空");

			outPutJson(response, gson, m);
			return;
		}
		
		for(String body:lines){
			String[] ba = body.split(",");
			
			// 字段长度和值长度
			int balen = ba.length;
			if (fieldArr.length != balen) {
				OutJson m = new OutJson();
				m.setCode(-1);
				m.setMsg("数据长度不一致丢失，请检查");
				outPutJson(response, gson, m);
				return;
			}
			StringBuilder builder = new StringBuilder();
			for(int i=0;i<balen;i++){
				ba[i] = StringUtils.trim(ba[i]);
				builder.append(ba[i]);
				if(i!=balen-1){
					builder.append(",");
				}
			}
			body = builder.toString();
			
			// 检查时间戳是否10位数字
			boolean timebol = false;
			for (int i2 = 0; i2 < newfield.length; i2++) {
				String v11 = ba[i2];

				if (newfield[i2].equals("ts")) {
					if (v11.length() != 10) {
						timebol = true;
						OutJson m = new OutJson();
						m.setCode(-1);
						m.setMsg("10位时间戳");
						outPutJson(response, gson, m);
						return;
					} else {
						try {
							int v = Integer.valueOf(v11);
							if (v > 1990000000) { // 2033-01-22
								timebol = true;
								OutJson m = new OutJson();
								m.setCode(-1);
								m.setMsg("时间戳类型不正确");
								outPutJson(response, gson, m);
								return;
							}
						} catch (NumberFormatException e) {
							timebol = true;
							OutJson m = new OutJson();
							m.setCode(-1);
							m.setMsg("时间戳类型不正确");
							outPutJson(response, gson, m);
							return;
						}
					}
					if (timebol) {
						break;
					}
				}
			}

			// 类型检查，只检查数值类型
			boolean typebol = false;
			for (int i1 = 0; i1 < types.length; i1++) {

				String v11 = ba[i1];

				switch (types[i1]) {
				case "int":
					try {
						Integer.valueOf(v11);
					} catch (NumberFormatException e) {
						typebol = true;
						OutJson m = new OutJson();
						m.setCode(-1);
						m.setMsg(newfield[i1] + ":数据类型丢失，请检查");
						outPutJson(response, gson, m);
						return;
					}
					break;
				case "float":
					try {
						Float.valueOf(v11);
					} catch (NumberFormatException e) {
						typebol = true;
						OutJson m = new OutJson();
						m.setCode(-1);
						m.setMsg(newfield[i1] + ":数据类型丢失，请检查");
						outPutJson(response, gson, m);
						return;
					}
					break;
				case "double":
					try {
						Double.valueOf(v11);
					} catch (NumberFormatException e) {
						typebol = true;
						OutJson m = new OutJson();
						m.setCode(-1);
						m.setMsg(newfield[i1] + ":数据类型丢失，请检查");
						outPutJson(response, gson, m);
						return;
					}
					break;
				default:
					break;
				}

				if (typebol) {
					break;
				}
			}
		}
		
		

		// ============================================规则检查
		// end==============================================
		
		
		
		// 重新生成新的headers
		HeaderJson headerJson = new HeaderJson();
		headerJson.setColumns((StringUtils.join(newfield, ',')).toLowerCase());
		headerJson.setGameId((retList.getHeaders().getGameId()).toLowerCase());
		headerJson.setFamily("info");
		headerJson.setLogType((retList.getHeaders().getLogType()).toLowerCase());
		headerJson.setTable((retList.getHeaders().getGameId() + "_" + retList.getHeaders().getLogType()).toLowerCase());
		headerJson.setSelectorHeader(selectorHeader);
		
		// 取出收到json里面的body数组
		String body = retList.getBody();

		// hbase 的rowkey，必须唯一的
		headerJson.setRowKey("");
		
		headerJson.setTimestamp(timestamp * 1000);
		// 传递给flume的新json对象
		DvNewJson[] dvNewJson = new DvNewJson[1];
		dvNewJson[0] = new DvNewJson();
		dvNewJson[0].setHeaders(headerJson);

		dvNewJson[0].setBody(body);

		String json1 = gson.toJson(dvNewJson);
		// 发送到flume初始化
		FlumeHttpClient flumeHttpClient = new FlumeHttpClient();
		code = flumeHttpClient.postJson(json1);
		
		if (code == 0) {
			LOG_DETECTION.put(gameId,System.currentTimeMillis());
			OutJson m = new OutJson();
			m.setCode(code);
			m.setMsg("成功");
			outPutJson(response, gson, m);
			return;

		} else if (code == 1) {
			OutJson m = new OutJson();
			m.setCode(code);
			m.setMsg("执行失败");
			outPutJson(response, gson, m);
			return;
		} else if (code == 3) {
			OutJson m = new OutJson();
			m.setCode(code);
			m.setMsg("执行失败2");
			outPutJson(response, gson, m);
			return;
		} else {
			OutJson m = new OutJson();
			m.setCode(6);
			m.setMsg("未知错误");
			outPutJson(response, gson, m);
			return;
		}
	}
	
	
	public void outPutJson(HttpServletResponse response, Gson gson, OutJson message) {
		String result = gson.toJson(message);

		ServletOutputStream os;
		try {
			os = response.getOutputStream();
			os.write(result.getBytes(Charset.forName("utf-8")));
			os.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	@Override
	public void update(LogConfig logConfig) {
		addToLogConfigMap(logConfig);
	}

	@Override
	public void update(GameConfig config) {
		addToGames(config);
		addToDetection(config);
		try {
			putGameKey(config.getGameflag(), DesUtil.decryptDES(GlobalConstant.GAME_KEY, config.getSecretkey()));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
