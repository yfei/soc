package cn.dcube.ahead.soc.util;

/**
 * @date：2021-12-29 19:51<br>
 * 
 * @author：yangfei<br>
 * @version: v1.0
 */
public class Constant {

	// CEP规则类型-统计规则
	public static final String RULE_STATISTICS = "STATISTICS";
	// CEP规则类型-分析规则
	public static final String RULE_ANALYSIS = "ANALYSIS";

	public static final int SUBCLASS_STATISTIC = 1;

	public static final int SUBCLASS_ANALYSIS = 2;

	public static final int SUBCLASS_EXCEPTION = 3;

	public static final int SUBCLASS_BASELINE = 4;
	// 白名单确认
	public static final String TAG_WHITE = "-1";
	// 一般事件
	public static final String TAG_NORMAL = "0";
	// 轻度可疑
	public static final String TAG_QDKY = "1";
	// 高度可疑
	public static final String TAG_GDKY = "2";
	// 确认事件
	public static final String TAG_CONFIRM = "3";
	// 确认阻断
	public static final String TAG_CONFIRM_DENY = "4";
	// 多种确认阻断
	public static final String TAG_CONFIRM_MULTI_DENY = "5";
}
