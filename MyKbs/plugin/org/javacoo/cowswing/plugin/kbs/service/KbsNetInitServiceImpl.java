/**
 * 如石子一粒,仰高山之巍峨,但不自惭形秽.
 * 若小草一棵,慕白杨之伟岸,却不妄自菲薄.
 */
package org.javacoo.cowswing.plugin.kbs.service;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.javacoo.cowswing.core.init.InitService;
import org.javacoo.cowswing.plugin.core.constant.CoreConstant;
import org.javacoo.cowswing.plugin.core.net.MsgBean;
import org.javacoo.cowswing.plugin.core.net.NetManager;
import org.javacoo.cowswing.plugin.kbs.constant.KbsConstant;
import org.javacoo.cowswing.plugin.kbs.service.beans.KbsModuleArticleBean;
import org.javacoo.cowswing.plugin.kbs.service.beans.KbsModuleArticleCriteria;
import org.javacoo.cowswing.plugin.kbs.service.beans.KbsModuleSourceBean;
import org.javacoo.cowswing.plugin.kbs.service.beans.KbsModuleSourceCriteria;
import org.javacoo.persistence.PaginationSupport;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

/**
 * 网络知识服务
 * <p>说明:</p>
 * <li></li>
 * @author DuanYong
 * @since 2013-8-12 下午4:03:41
 * @version 1.0
 */
@Component("kbsNetInitService")
public class KbsNetInitServiceImpl implements InitService{
	protected Logger logger = Logger.getLogger(this.getClass());
	@Resource(name = "netManager")
	private NetManager netManager;
	/** 搜索服务 */
	@Resource(name = "kbsNetSearchService")
	private IKbsNetSerachService kbsNetSearchService;
	/* (non-Javadoc)
	 * @see org.javacoo.cowswing.core.init.InitService#init()
	 */
	@Override
	public void init() {
		//分享数据
		shareData();
	}
	
	/**
	 * 分享数据
	 * <p>
	 * 方法说明:</>
	 * <li>系统启动时调用</li>
	 * 
	 * @author DuanYong
	 * @since 2013-8-19 下午2:25:07
	 * @version 1.0
	 * @exception
	 */
	public void shareData() {
		List<MsgBean> dataList = getDataList();
		if (!CollectionUtils.isEmpty(dataList)) {
			for (MsgBean data : dataList) {
				netManager.sendCommonData(data);
			}
			logger.info("总共分享数据:" + dataList.size() + "条");
		}
	}
	
	/**
	 * 取得共享数据
	 * <p>
	 * 方法说明:</>
	 * <li></li>
	 * 
	 * @author DuanYong
	 * @since 2013-8-14 上午8:40:01
	 * @version 1.0
	 * @exception
	 * @return
	 */
	public List<MsgBean> getDataList() {
		List<MsgBean> resultList = new ArrayList<MsgBean>();
		List<KbsModuleSourceBean> sourceList = getSourceData(0, 1000);
		List<KbsModuleArticleBean> articleList = getArticleData(0, 1000);
		if (!CollectionUtils.isEmpty(sourceList)) {
			for (KbsModuleSourceBean sourceBean : sourceList) {
				resultList.add(populateSourceMsg(sourceBean));
			}
		}
		if (!CollectionUtils.isEmpty(articleList)) {
			for (KbsModuleArticleBean articleBean : articleList) {
				resultList.add(populateArticleMsg(articleBean));
			}
		}
		return resultList;
	}
	
	/**
	 * 取得资源数据
	 * <p>
	 * 方法说明:</>
	 * <li></li>
	 * 
	 * @author DuanYong
	 * @since 2013-8-14 上午8:37:04
	 * @version 1.0
	 * @exception
	 * @param startIndex
	 * @param pageSize
	 * @return
	 */
	private List<KbsModuleSourceBean> getSourceData(int startIndex, int pageSize) {
		KbsModuleSourceCriteria criteria = new KbsModuleSourceCriteria();
		criteria.setStartIndex(startIndex);
		criteria.setPageSize(pageSize);
		List<String> purviewList = new ArrayList<String>();
		purviewList.add(KbsConstant.SEARCH_PURVIEW_VIEW);
		purviewList.add(KbsConstant.SEARCH_PURVIEW_DOWNLOAD);
		criteria.setPurviewList(purviewList);
		PaginationSupport<KbsModuleSourceBean> result = kbsNetSearchService.getKbsModuleSourceService()
				.getPaginatedList(criteria,
						KbsConstant.SQLMAP_ID_GET_LIST_KBS_MODULE_SOURCE);
		return (List<KbsModuleSourceBean>) result.getData();
	}
	
	/**
	 * 取得文章数据
	 * <p>
	 * 方法说明:</>
	 * <li></li>
	 * 
	 * @author DuanYong
	 * @since 2013-8-14 上午8:37:19
	 * @version 1.0
	 * @exception
	 * @param startIndex
	 * @param pageSize
	 * @return
	 */
	private List<KbsModuleArticleBean> getArticleData(int startIndex,
			int pageSize) {
		KbsModuleArticleCriteria criteria = new KbsModuleArticleCriteria();
		criteria.setStartIndex(startIndex);
		criteria.setPageSize(pageSize);
		List<String> purviewList = new ArrayList<String>();
		purviewList.add(KbsConstant.SEARCH_PURVIEW_VIEW);
		purviewList.add(KbsConstant.SEARCH_PURVIEW_DOWNLOAD);
		criteria.setPurviewList(purviewList);
		PaginationSupport<KbsModuleArticleBean> result = kbsNetSearchService.getKbsModuleArticleService()
				.getPaginatedList(criteria,
						KbsConstant.SQLMAP_ID_GET_LIST_KBS_MODULE_ARTICLE);
		return (List<KbsModuleArticleBean>) result.getData();
	}
	/**
	 * 组装资源数据
	 * <p>
	 * 方法说明:</>
	 * <li></li>
	 * 
	 * @author DuanYong
	 * @since 2013-8-14 上午8:36:20
	 * @version 1.0
	 * @exception
	 * @param sourceBean
	 * @return
	 */
	private MsgBean populateSourceMsg(KbsModuleSourceBean sourceBean) {
		MsgBean msgBean = new MsgBean();
		msgBean.setId(sourceBean.getId().toString());
		msgBean.setAuthor(netManager.getServer().getUserName());
		msgBean.setDate(sourceBean.getUploadDateStr());
		msgBean.setPurview(sourceBean.getPurview());
		msgBean.setTitle(sourceBean.getTitle());
		msgBean.setModule(KbsConstant.SYSTEM_MODULE_SOURCE);
		msgBean.setAction(CoreConstant.NET_ACTION_TYPE_ADD);
		msgBean.setPort(netManager.getServer().getPort());
		msgBean.setIp(netManager.getServer().getIp());
		return msgBean;
	}

	/**
	 * 组装文章数据
	 * <p>
	 * 方法说明:</>
	 * <li></li>
	 * 
	 * @author DuanYong
	 * @since 2013-8-14 上午8:39:11
	 * @version 1.0
	 * @exception
	 * @param articleBean
	 * @return
	 */
	private MsgBean populateArticleMsg(KbsModuleArticleBean articleBean) {
		MsgBean msgBean = new MsgBean();
		msgBean.setId(articleBean.getId().toString());
		msgBean.setAuthor(netManager.getServer().getUserName());
		msgBean.setDate(articleBean.getReleaseDateStr());
		msgBean.setPurview(articleBean.getPurview());
		msgBean.setTitle(articleBean.getTitle());
		msgBean.setModule(KbsConstant.SYSTEM_MODULE_ARTICLE);
		msgBean.setAction(CoreConstant.NET_ACTION_TYPE_ADD);
		msgBean.setPort(netManager.getServer().getPort());
		msgBean.setIp(netManager.getServer().getIp());
		return msgBean;
	}
	
	
	

}
