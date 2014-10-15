/**
 * 如石子一粒,仰高山之巍峨,但不自惭形秽.
 * 若小草一棵,慕白杨之伟岸,却不妄自菲薄.
 */
package org.javacoo.cowswing.plugin.kbs.service.beans;

import org.javacoo.cowswing.base.service.beans.CowSwingBaseBean;
import org.javacoo.cowswing.plugin.kbs.event.KbsEventObserver;

/**
 * 知识库基础值对象
 * <p>说明:</p>
 * <li></li>
 * @author DuanYong
 * @since 2014-7-29 上午10:17:23
 * @version 1.0
 */
public class KbsBaseBean extends CowSwingBaseBean{
	public KbsBaseBean(){
		this.setObserver(KbsEventObserver.getInstance());
	}
}
