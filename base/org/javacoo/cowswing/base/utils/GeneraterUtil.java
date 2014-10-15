/**
 * 如石子一粒,仰高山之巍峨,但不自惭形秽.
 * 若小草一棵,慕白杨之伟岸,却不妄自菲薄.
 */
package org.javacoo.cowswing.base.utils;

/**
 * 生成帮助类
 * <p>说明:</p>
 * <li></li>
 * @author DuanYong
 * @since 2013-10-2 上午9:08:42
 * @version 1.0
 */
public class GeneraterUtil {
	/**随机类型编码*/
	public final static String RANDOM_CODE_FORMAT = "yyyyMMddHHmmssSSS";
	/**
	 * 根据当前时间生成编码
	 * <p>方法说明:</>
	 * <li></li>
	 * @author DuanYong
	 * @since 2013-10-2 上午9:11:19
	 * @version 1.0
	 * @exception 
	 * @return
	 */
	public static String genIdByTime(){
		return DateUtil.dateToStr(DateUtil.getNow(),RANDOM_CODE_FORMAT);
	}

}
