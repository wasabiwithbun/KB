/**
 * 如石子一粒,仰高山之巍峨,但不自惭形秽.
 * 若小草一棵,慕白杨之伟岸,却不妄自菲薄.
 */
package org.javacoo.cowswing.plugin.kbs.service;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.javacoo.cowswing.base.service.ICowSwingService;
import org.javacoo.cowswing.plugin.core.constant.CoreConstant;
import org.javacoo.cowswing.plugin.core.net.MsgBean;
import org.javacoo.cowswing.plugin.core.net.NetManager;
import org.javacoo.cowswing.plugin.kbs.constant.KbsConstant;
import org.javacoo.cowswing.plugin.kbs.domain.KnowledgePoint;
import org.javacoo.cowswing.plugin.kbs.lucene.search.Search;
import org.javacoo.cowswing.plugin.kbs.service.beans.KbsModuleArticleBean;
import org.javacoo.cowswing.plugin.kbs.service.beans.KbsModuleArticleCriteria;
import org.javacoo.cowswing.plugin.kbs.service.beans.KbsModuleSourceBean;
import org.javacoo.cowswing.plugin.kbs.service.beans.KbsModuleSourceCriteria;
import org.javacoo.cowswing.plugin.kbs.service.beans.SearchCriteria;
import org.javacoo.persistence.PaginationSupport;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

/**
 * 网络搜索服务
 * <p>说明:</p>
 * <li>对本机搜索</li>
 * @author DuanYong
 * @since 2013-9-17 上午9:36:22
 * @version 1.0
 */
@Component("kbsNetSearchService")
public class KbsNetSearchServiceImpl implements IKbsNetSerachService{
	protected Logger logger = Logger.getLogger(this.getClass());
	/** 搜索管理 */
	@Resource(name = "searchManager")
	private Search searchManager;
	/**网络服务*/
	@Resource(name = "netManager")
	private NetManager netManager;
	/** 文章模块服务 */
	@Resource(name = "kbsModuleArticleService")
	private ICowSwingService<KbsModuleArticleBean, KbsModuleArticleCriteria> kbsModuleArticleService;
	/** 资源模块服务 */
	@Resource(name = "kbsModuleSourceService")
	private ICowSwingService<KbsModuleSourceBean, KbsModuleSourceCriteria> kbsModuleSourceService;

	/**
	 * 搜索本地数据
	 * <p>方法说明:</>
	 * <li>搜索本地资源及文字</li>
	 * @author DuanYong
	 * @since 2013-9-17 上午9:41:11
	 * @version 1.0
	 * @exception 
	 * @param msgBean
	 * @return
	 */
	@Override
	public List<MsgBean> search(MsgBean msgBean) {
		SearchCriteria criteria = new SearchCriteria();
		criteria.setStartIndex(0);
		criteria.setPageSize(1000);
		criteria.setIndexDirName(KbsConstant.DEFAULT_INDEX_MODULE_DIR);
		criteria.setQueryKey(msgBean.getTitle());
		PaginationSupport<KnowledgePoint> resultList = searchManager.find(criteria);
		if (!CollectionUtils.isEmpty(resultList.getData())) {
			return populateSearchDataList((List<KnowledgePoint>) resultList.getData());
		}
		return new ArrayList<MsgBean>();
	}
	
	/**
	 * @return the kbsModuleArticleService
	 */
	public ICowSwingService<KbsModuleArticleBean, KbsModuleArticleCriteria> getKbsModuleArticleService() {
		return kbsModuleArticleService;
	}

	/**
	 * @return the kbsModuleSourceService
	 */
	public ICowSwingService<KbsModuleSourceBean, KbsModuleSourceCriteria> getKbsModuleSourceService() {
		return kbsModuleSourceService;
	}
	/**
	 * 组装搜索数据集合
	 * <p>
	 * 方法说明:</>
	 * <li></li>
	 * 
	 * @author DuanYong
	 * @since 2013-8-17 下午7:22:07
	 * @version 1.0
	 * @exception
	 * @param dataList
	 * @return
	 */
	private List<MsgBean> populateSearchDataList(List<KnowledgePoint> dataList) {
		List<MsgBean> resultList = new ArrayList<MsgBean>();
		if (!CollectionUtils.isEmpty(dataList)) {
			KbsModuleSourceBean kbsModuleSourceBean = null;
			KbsModuleArticleBean kbsModuleArticleBean = null;
			for (KnowledgePoint knowledgePoint : dataList) {
				if (KbsConstant.SYSTEM_MODULE_SOURCE.equals(knowledgePoint
						.getModule())) {
					kbsModuleSourceBean = getKbsModuleSourceBean(Integer
							.valueOf(knowledgePoint.getId()));
					if (null != kbsModuleSourceBean
							&& KbsConstant.SEARCH_PURVIEW_DOWNLOAD
									.equals(kbsModuleSourceBean.getPurview())) {
						resultList.add(populateSearchData(knowledgePoint));
					}
				} else if (KbsConstant.SYSTEM_MODULE_ARTICLE
						.equals(knowledgePoint.getModule())) {
					kbsModuleArticleBean = getKbsModuleArticleBean(Integer
							.valueOf(knowledgePoint.getId()));
					if (null != kbsModuleArticleBean
							&& KbsConstant.SEARCH_PURVIEW_VIEW
									.equals(kbsModuleArticleBean.getPurview())) {
						resultList.add(populateSearchData(knowledgePoint));
					}
				}
			}
			logger.info("返回搜索结果：" + resultList.size());
		}
		return resultList;
	}
	/**
	 * 根据ID查询资源
	 * <p>
	 * 方法说明:</>
	 * <li></li>
	 * 
	 * @author DuanYong
	 * @since 2013-9-1 下午6:42:29
	 * @version 1.0
	 * @exception
	 * @param id
	 * @return
	 */
	private KbsModuleSourceBean getKbsModuleSourceBean(Integer id) {
		KbsModuleSourceBean kbsModuleSourceBean = new KbsModuleSourceBean();
		kbsModuleSourceBean.setId(id);
		kbsModuleSourceBean = kbsModuleSourceService.get(kbsModuleSourceBean,
				KbsConstant.SQLMAP_ID_GET_KBS_MODULE_SOURCE);
		return kbsModuleSourceBean;
	}

	/**
	 * 根据ID查询文章
	 * <p>
	 * 方法说明:</>
	 * <li></li>
	 * 
	 * @author DuanYong
	 * @since 2013-9-1 下午6:42:45
	 * @version 1.0
	 * @exception
	 * @param id
	 * @return
	 */
	private KbsModuleArticleBean getKbsModuleArticleBean(Integer id) {
		KbsModuleArticleBean kbsModuleArticleBean = new KbsModuleArticleBean();
		kbsModuleArticleBean.setId(id);
		kbsModuleArticleBean = kbsModuleArticleService.get(
				kbsModuleArticleBean,
				KbsConstant.SQLMAP_ID_GET_KBS_MODULE_ARTICLE);
		return kbsModuleArticleBean;
	}
	/**
	 * 组装搜索返回数据
	 * <p>
	 * 方法说明:</>
	 * <li></li>
	 * 
	 * @author DuanYong
	 * @since 2013-8-17 下午7:19:42
	 * @version 1.0
	 * @exception
	 * @param knowledgePoint
	 * @return
	 */
	private MsgBean populateSearchData(KnowledgePoint knowledgePoint) {
		MsgBean msgBean = new MsgBean();
		msgBean.setId(knowledgePoint.getId());
		msgBean.setAuthor(netManager.getServer().getUserName());
		msgBean.setDate(knowledgePoint.getShowDate());
		msgBean.setPurview(knowledgePoint.getPurview());
		msgBean.setTitle(knowledgePoint.getTitle());
		msgBean.setModule(knowledgePoint.getModule());
		msgBean.setFilePath(knowledgePoint.getFilePath());
		msgBean.setAction(CoreConstant.NET_ACTION_TYPE_SEARCH_RETURN);
		msgBean.setPort(netManager.getServer().getPort());
		msgBean.setIp(netManager.getServer().getIp());
		return msgBean;
	}
}
