package cn.dcube.ahead.soc.alarm.util;

import cn.dcube.ahead.soc.cep.model.CEPEvent;

/**
 * 
 * 描述：
 * <p>
 * 获取信息类
 * </p>
 * 创建日期：2014-3-25 上午9:25:48<br>
 * 
 * @author：dai_hyan<br>
 * @update：$Date$<br>
 * @version：$Revision$<br>
 * 
 * @since 3.1.0
 */
public class DirectionUtil {

	/* 获取攻击方向 */
	public static int GetDirection(CEPEvent cepevt) {
		int direction = -1;
		/*
		 * 
		 * srcIn、dstIn的值含义： 1、属于资产且在内网; 2、属于资产且在外网; 3、互联网IP(公网); 4、不属于资产且在内网;
		 * 5、不属于资产且在外网 6 属于资产且在DMZ 7 不属于资产且在DMZ
		 */

		/*
		 * 判断攻击方向: 1 内对内; 2 内对公网; 4 内对外; 8 公网对内; 16 公网对公网; 32 公网对外; 64 外对内; 128 外对公网;
		 * 256 外对外;
		 */
		Integer srcIn = cepevt.getSrcin();
		Integer dstIn = cepevt.getDstin();
		if (srcIn == 1 || srcIn == 4) { // 内网
			switch (dstIn) {
			case 1:
				direction = 1;// 内对内
				break;
			case 2:
				direction = 4;// 内对外网
				break;
			case 3:
				direction = 2;// 内对公网
				break;
			case 4:
				direction = 1;// 内对内
				break;
			case 5:
				direction = 4;// 内对外网
				break;
			case 6:
				direction = 4;// 内对外网
				break;
			case 7:
				direction = 4;// 内对外网
				break;
			}
		} else if (srcIn == 3) { // 公网
			switch (dstIn) {
			case 1:
				direction = 8;// 公网对内
				break;
			case 2:
				direction = 32;// 公网对外网
				break;
			case 3:
				direction = 16;// 公网对公网
				break;
			case 4:
				direction = 8;// 公网对内
				break;
			case 5:
				direction = 32;// 公网对外网
				break;
			case 6:
				direction = 32;// 公网对外网
				break;
			case 7:
				direction = 32;// 公网对外网
				break;
			}
		} else if (srcIn == 2 || srcIn == 5 || srcIn == 6 || srcIn == 7) {
			switch (dstIn) {
			case 1:
				direction = 64;// 外对内
				break;
			case 2:
				direction = 256;// 外对外网
				break;
			case 3:
				direction = 128;// 外对公网
				break;
			case 4:
				direction = 64;// 外对内
				break;
			case 5:
				direction = 256;// 外对外网
				break;
			case 6:
				direction = 256;// 外对外网
				break;
			case 7:
				direction = 256;// 外对外网
				break;
			}
		}
		return direction;
	}
}
