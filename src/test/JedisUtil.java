package test;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class JedisUtil {
	
	private static JedisPool jedisPool = null;
	
	//ip��ַ
	private static String ip = "localhost";
	
	//redis�˿ں�
	private static int port = 6379;
	
	static{
		try {
			JedisPoolConfig config = new JedisPoolConfig();
			//���Ӻľ�ʱ�Ƿ�������false���쳣��true��������ʱ��Ĭ��true
			config.setBlockWhenExhausted(true);
			//���õ�����������ͣ�Ĭ�ϵ����ӳ���������ʱ��
			config.setEvictionPolicyClassName("org.apache.commons.pool2.impl.DefaultEvictionPolicy");
			//�Ƿ�����pool��jmx��������
			config.setJmxEnabled(true);
			//����������������
			config.setMaxIdle(8);
			//�������������
			config.setMaxTotal(200);
			//�������ĵȴ�ʱ�䣬�����ʱ�����׳�JedisConnectionException
			config.setMaxWaitMillis(1000*100);
			//��borrowһ��jedisʵ��ʱ���Ƿ���ǰ����validate���������Ϊtrue����õ���jedisʵ�����ǿ��õ�
			config.setTestOnBorrow(true);
			jedisPool = new JedisPool(config,ip,port,3000);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * ���jedisʵ��
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
	 * �ͷ�jedis��Դ
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
	 * �ַ�������
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