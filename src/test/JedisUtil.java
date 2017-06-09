package test;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class JedisUtil {
	
	private static JedisPool jedisPool = null;
	
	//ip地址
	private static String ip = "localhost";
	
	//redis端口号
	private static int port = 6379;
	
	static{
		try {
			JedisPoolConfig config = new JedisPoolConfig();
			//链接耗尽时是否阻塞，false报异常，true阻塞到超时，默认true
			config.setBlockWhenExhausted(true);
			//设置的逐出策略类型，默认当连接超过最大空闲时间
			config.setEvictionPolicyClassName("org.apache.commons.pool2.impl.DefaultEvictionPolicy");
			//是否启用pool的jmx管理功能
			config.setJmxEnabled(true);
			//设置最大空闲连接数
			config.setMaxIdle(8);
			//设置最大连接数
			config.setMaxTotal(200);
			//设置最大的等待时间，如果超时，则抛出JedisConnectionException
			config.setMaxWaitMillis(1000*100);
			//在borrow一个jedis实例时，是否提前进行validate操作，如果为true，则得到的jedis实例均是可用的
			config.setTestOnBorrow(true);
			jedisPool = new JedisPool(config,ip,port,3000);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 获得jedis实例
	 * @return
	 */
	public synchronized static Jedis getJedis(){
		try {
			if(jedisPool!=null){
				Jedis jedis = jedisPool.getResource();
				return jedis;
			}else{
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * 释放jedis资源
	 * @param jedis
	 */
	public void closeJedis(Jedis jedis){
		if(jedis!=null){
			jedis.close();
		}
	}
	
	public static void main(String[] args) {
		Jedis jedis = JedisUtil.getJedis();
		System.out.println(">>>>>>>>>>>>>>>>>>>"+jedis);
		JedisUtil.testString(jedis);
	}

	/**
	 * 字符串测试
	 * @param jedis
	 */
	private static void testString(Jedis jedis) {
		jedis.set("hello", "world");
		System.out.println(jedis.get("hello"));
		
		jedis.append("hello", "!!!");
		System.out.println(jedis.get("hello"));
		
		jedis.del("hello");
		System.out.println(jedis.get("hello"));
		
		jedis.mset("name","hutao","age","22","qq","838533527");
		System.out.println(jedis.get("name"));
		System.out.println(jedis.get("age"));
		System.out.println(jedis.get("qq"));
	}
}
