/**
 * 如石子一粒,仰高山之巍峨,但不自惭形秽.
 * 若小草一棵,慕白杨之伟岸,却不妄自菲薄.
 */
package org.javacoo.cowswing.plugin.kbs.exception;

import org.javacoo.cowswing.core.exception.AbstractBaseException;

/**
 * lucune池异常
 * <p>说明:</p>
 * <li></li>
 * @author DuanYong
 * @since 2013-7-2 下午5:06:29
 * @version 1.0
 */
public class PoolException extends AbstractBaseException{
	private static final long serialVersionUID = 1L;
	/**
	 * @param code
	 */
	public PoolException(String code, String... para) {
		super(code);
		setExceptionType(ExceptionType.SYSTEM);
		buildException(ExceptionType.SYSTEM, exceptionCode,para);
	}
}
