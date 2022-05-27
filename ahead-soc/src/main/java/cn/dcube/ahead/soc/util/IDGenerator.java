package cn.dcube.ahead.soc.util;

import java.util.concurrent.atomic.AtomicInteger;

import cn.dcube.goku.commons.convert.ByteConvert;

/**
 * ID生成器
 * @author yangfei
 *
 */
public class IDGenerator {

	static final int MAX_SEQ = 16777215;

	/**
	 * 当前引擎的最大ID
	 */
	private static AtomicInteger maxId = new AtomicInteger(0);

	private static long lastTime;

	public static long getId(byte engineSeq) {
		// 4个字节的时间戳
		long currentTime = System.currentTimeMillis() / 1000;
		byte[] timeStamp = generateTimestamp(currentTime);
		if (currentTime != lastTime) {
			lastTime = currentTime;
			maxId.set(0);
		}
		// 时间秒级一致
		int currentSeq = maxId.addAndGet(1);
		if (currentSeq > MAX_SEQ) {
			throw new RuntimeException("当前序列号超过" + MAX_SEQ + ",系统不支持!");
		} else {
			byte[] seq = ByteConvert.intToBytes(currentSeq);
			byte[] id = new byte[8];
			id[7] = seq[3];
			id[6] = seq[2];
			id[5] = seq[1];// 舍弃掉最高位
			id[4] = engineSeq;
			id[3] = timeStamp[3];
			id[2] = timeStamp[2];
			id[1] = timeStamp[1];
			id[0] = timeStamp[0];
			return ByteConvert.bytesToLong(id);
		}
	}
	
	/**
	 * 创建时间戳,单位秒
	 * 
	 * @return 精确到秒级别的时间戳
	 */
	public static byte[] generateTimestamp(long currentMillis) {
		byte[] result = new byte[4];
		long timestamp = currentMillis;
		byte[] temp = ByteConvert.longToBytes(timestamp);

		result[0] = temp[4];
		result[1] = temp[5];
		result[2] = temp[6];
		result[3] = temp[7];

		return result;
	}

}
