/**
 * 如石子一粒,仰高山之巍峨,但不自惭形秽.
 * 若小草一棵,慕白杨之伟岸,却不妄自菲薄.
 */
package org.javacoo.cowswing.plugin.kbs.exception;

import org.javacoo.cowswing.core.exception.AbstractBaseException;

/**
 * 索引异常
 * <p>说明:</p>
 * <li></li>
 * @author DuanYong
 * @since 2013-7-8 上午9:53:38
 * @version 1.0
 */
public class IndexException extends AbstractBaseException{
	private static final long serialVersionUID = 1L;
	/**
	 * @param code
	 */
	public IndexException(String code, String... para) {
		super(code);
		setExceptionType(ExceptionType.SYSTEM);
		buildException(ExceptionType.SYSTEM, exceptionCode,para);
	}
}
