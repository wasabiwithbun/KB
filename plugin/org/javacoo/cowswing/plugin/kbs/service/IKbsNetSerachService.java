/**
 * 如石子一粒,仰高山之巍峨,但不自惭形秽.
 * 若小草一棵,慕白杨之伟岸,却不妄自菲薄.
 */
package org.javacoo.cowswing.plugin.kbs.service;

import java.util.List;

import org.javacoo.cowswing.base.service.ICowSwingService;
import org.javacoo.cowswing.plugin.core.net.MsgBean;
import org.javacoo.cowswing.plugin.kbs.domain.KnowledgePoint;
import org.javacoo.cowswing.plugin.kbs.service.beans.KbsModuleArticleBean;
import org.javacoo.cowswing.plugin.kbs.service.beans.KbsModuleArticleCriteria;
import org.javacoo.cowswing.plugin.kbs.service.beans.KbsModuleSourceBean;
import org.javacoo.cowswing.plugin.kbs.service.beans.KbsModuleSourceCriteria;

/**
 * 网络搜索服务接口
 * <p>说明:</p>
 * <li></li>
 * @author DuanYong
 * @since 2013-9-17 上午9:38:20
 * @version 1.0
 */
public interface IKbsNetSerachService {
	/**
	 * 搜索本地数据
	 * <p>方法说明:</>
	 * <li>搜索本地资源及文字</li>
	 * @author DuanYong
	 * @since 2013-9-17 上午9:41:11
	 * @version 1.0
	 * @exception 
	 * @param msgBean
	 * @return List<MsgBean>
	 */
	List<MsgBean> search(MsgBean msgBean);
	/**
	 * 取得文章模块服务
	 * <p>方法说明:</>
	 * <li></li>
	 * @author DuanYong
	 * @since 2013-9-17 上午10:26:46
	 * @version 1.0
	 * @exception 
	 * @return
	 */
	ICowSwingService<KbsModuleArticleBean, KbsModuleArticleCriteria> getKbsModuleArticleService();
	/**
	 * 取得资源模块服务
	 * <p>方法说明:</>
	 * <li></li>
	 * @author DuanYong
	 * @since 2013-9-17 上午10:27:00
	 * @version 1.0
	 * @exception 
	 * @return
	 */
	ICowSwingService<KbsModuleSourceBean, KbsModuleSourceCriteria> getKbsModuleSourceService();
}
