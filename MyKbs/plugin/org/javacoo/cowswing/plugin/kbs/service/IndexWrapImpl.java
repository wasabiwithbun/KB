/**
 * 如石子一粒,仰高山之巍峨,但不自惭形秽.
 * 若小草一棵,慕白杨之伟岸,却不妄自菲薄.
 */
package org.javacoo.cowswing.plugin.kbs.service;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.javacoo.cowswing.base.service.ICowSwingService;
import org.javacoo.cowswing.base.utils.DateUtil;
import org.javacoo.cowswing.plugin.kbs.constant.KbsConstant;
import org.javacoo.cowswing.plugin.kbs.domain.KnowledgePoint;
import org.javacoo.cowswing.plugin.kbs.lucene.index.Index;
import org.javacoo.cowswing.plugin.kbs.service.beans.KbsModuleArticleBean;
import org.javacoo.cowswing.plugin.kbs.service.beans.KbsModuleArticleCriteria;
import org.javacoo.cowswing.plugin.kbs.service.beans.KbsModuleSourceBean;
import org.javacoo.cowswing.plugin.kbs.service.beans.KbsModuleSourceCriteria;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.util.HtmlUtils;

/**
 * 索引管理包装类接口实现类
 * <p>说明:</p>
 * <li></li>
 * @author DuanYong
 * @since 2013-9-28 上午11:17:57
 * @version 1.0
 */
@Component("indexWrap")
public class IndexWrapImpl implements IndexWrap{
	/** 日志对象 */
	private final static Log logger = LogFactory.getLog(IndexWrapImpl.class);
	/**索引管理*/
	@Resource(name="indexManager")
    private Index index;
	/**文章模块服务*/
	@Resource(name="kbsModuleArticleService")
    private ICowSwingService<KbsModuleArticleBean, KbsModuleArticleCriteria> kbsModuleArticleService;
	/**资源模块服务*/
	@Resource(name="kbsModuleSourceService")
    private ICowSwingService<KbsModuleSourceBean, KbsModuleSourceCriteria> kbsModuleSourceService;
	/* (non-Javadoc)
	 * @see org.javacoo.cowswing.plugin.kbs.service.IndexWrap#buildArticleIndex(org.javacoo.cowswing.plugin.kbs.service.beans.KbsModuleArticleBean)
	 */
	@Override
	public void buildArticleIndex(KbsModuleArticleBean kbsModuleArticleBean,boolean commit) {
		KnowledgePoint knowledgePoint = new KnowledgePoint();
		knowledgePoint.setId(kbsModuleArticleBean.getId().toString());
		knowledgePoint.setModule(KbsConstant.SYSTEM_MODULE_ARTICLE);
		knowledgePoint.setTitle(kbsModuleArticleBean.getTitle());
		knowledgePoint.setKeyWord(kbsModuleArticleBean.getKeyword());
		knowledgePoint.setIndexDirName(KbsConstant.DEFAULT_INDEX_MODULE_DIR);
		knowledgePoint.setContent(kbsModuleArticleBean.getContent());
		knowledgePoint.setShowDate(DateUtil.dateToStr(new Date()));
		knowledgePoint.setOrigin(kbsModuleArticleBean.getOrigin());
		knowledgePoint.setTypeDesc(kbsModuleArticleBean.getTypeName());
		knowledgePoint.setTypeCode(kbsModuleArticleBean.getTypeCode());
		knowledgePoint.setPurview(kbsModuleArticleBean.getPurview());
		try {
			index.buildIndex(knowledgePoint,commit);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("建立文章索引失败："+e.getMessage());
		}
	}
	/* (non-Javadoc)
	 * @see org.javacoo.cowswing.plugin.kbs.service.IndexWrap#updateArticleIndex(org.javacoo.cowswing.plugin.kbs.service.beans.KbsModuleArticleBean)
	 */
	@Override
	public void updateArticleIndex(KbsModuleArticleBean kbsModuleArticleBean) {
		KnowledgePoint knowledgePoint = new KnowledgePoint();
		knowledgePoint.setId(kbsModuleArticleBean.getId().toString());
		knowledgePoint.setModule(KbsConstant.SYSTEM_MODULE_ARTICLE);
		knowledgePoint.setTitle(kbsModuleArticleBean.getTitle());
		knowledgePoint.setKeyWord(kbsModuleArticleBean.getKeyword());
		knowledgePoint.setIndexDirName(KbsConstant.DEFAULT_INDEX_MODULE_DIR);
		knowledgePoint.setContent(HtmlUtils.htmlUnescape(kbsModuleArticleBean.getContent()));
		knowledgePoint.setShowDate(DateUtil.dateToStr(new Date()));
		knowledgePoint.setOrigin(kbsModuleArticleBean.getOrigin());
		knowledgePoint.setTypeDesc(kbsModuleArticleBean.getTypeName());
		knowledgePoint.setTypeCode(kbsModuleArticleBean.getTypeCode());
		knowledgePoint.setPurview(kbsModuleArticleBean.getPurview());
		try {
			index.updateIndex(knowledgePoint);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("更新文章索引失败："+e.getMessage());
		}
	}
	/* (non-Javadoc)
	 * @see org.javacoo.cowswing.plugin.kbs.service.IndexWrap#buildSourceIndex(org.javacoo.cowswing.plugin.kbs.service.beans.KbsModuleSourceBean)
	 */
	@Override
	public void buildSourceIndex(KbsModuleSourceBean kbsModuleSourceBean,boolean commit) {
		KnowledgePoint knowledgePoint = new KnowledgePoint();
		knowledgePoint.setId(kbsModuleSourceBean.getId().toString());
		knowledgePoint.setModule(KbsConstant.SYSTEM_MODULE_SOURCE);
		knowledgePoint.setTitle(kbsModuleSourceBean.getTitle());
		knowledgePoint.setKeyWord(kbsModuleSourceBean.getKeyword());
		knowledgePoint.setIndexDirName(KbsConstant.DEFAULT_INDEX_MODULE_DIR);
		knowledgePoint.setFileDirs(kbsModuleSourceBean.getDirCode());
		knowledgePoint.setShowDate(DateUtil.dateToStr(new Date()));
		knowledgePoint.setFilePath(kbsModuleSourceBean.getFilePath());
		knowledgePoint.setTypeDesc(kbsModuleSourceBean.getTypeName());
		knowledgePoint.setTypeCode(kbsModuleSourceBean.getTypeCode());
		knowledgePoint.setPurview(kbsModuleSourceBean.getPurview());
		try {
			index.buildIndex(knowledgePoint,true);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("建立资源索引失败："+e.getMessage());
		}
		
	}

	/* (non-Javadoc)
	 * @see org.javacoo.cowswing.plugin.kbs.service.IndexWrap#reBuildArticleIndex()
	 */
	@Override
	public void reBuildArticleIndex() {
		//删除原来的索引
		index.deleteIndex(KbsConstant.DEFAULT_INDEX_MODULE_DIR, KbsConstant.SYSTEM_MODULE_ARTICLE);
		//重建索引
		List<KbsModuleArticleBean> returnList = kbsModuleArticleService.getList(new KbsModuleArticleCriteria(),KbsConstant.SQLMAP_ID_GET_LIST_KBS_MODULE_ARTICLE);
		if(!CollectionUtils.isEmpty(returnList)){
			for(KbsModuleArticleBean kbsModuleArticleBean : returnList){
				buildArticleIndex(kbsModuleArticleBean,false);
			}
		}
		//触发保存索引
		index.buildIndex(null,true);
	}

	/* (non-Javadoc)
	 * @see org.javacoo.cowswing.plugin.kbs.service.IndexWrap#reBuildSourceIndex()
	 */
	@Override
	public void reBuildSourceIndex() {
		//删除原来的索引
		index.deleteIndex(KbsConstant.DEFAULT_INDEX_MODULE_DIR, KbsConstant.SYSTEM_MODULE_SOURCE);
		//重建索引
		List<KbsModuleSourceBean> returnList = kbsModuleSourceService.getList(new KbsModuleSourceCriteria(),KbsConstant.SQLMAP_ID_GET_LIST_KBS_MODULE_SOURCE);
		if(!CollectionUtils.isEmpty(returnList)){
			for(KbsModuleSourceBean kbsModuleSourceBean : returnList){
				buildSourceIndex(kbsModuleSourceBean,false);
			}
		}
		//触发保存索引
		index.buildIndex(null,true);
	}
	/* (non-Javadoc)
	 * @see org.javacoo.cowswing.plugin.kbs.service.IndexWrap#reBuildAllIndex()
	 */
	@Override
	public void reBuildAllIndex() {
		//删除原来的索引
		index.deleteIndex(KbsConstant.DEFAULT_INDEX_MODULE_DIR, null);
		//重建索引
		List<KbsModuleArticleBean> returnList = kbsModuleArticleService.getList(new KbsModuleArticleCriteria(),KbsConstant.SQLMAP_ID_GET_LIST_KBS_MODULE_ARTICLE);
		if(!CollectionUtils.isEmpty(returnList)){
			for(KbsModuleArticleBean kbsModuleArticleBean : returnList){
				buildArticleIndex(kbsModuleArticleBean,false);
			}
		}
		List<KbsModuleSourceBean> sourceList = kbsModuleSourceService.getList(new KbsModuleSourceCriteria(),KbsConstant.SQLMAP_ID_GET_LIST_KBS_MODULE_SOURCE);
		if(!CollectionUtils.isEmpty(sourceList)){
			for(KbsModuleSourceBean kbsModuleSourceBean : sourceList){
				buildSourceIndex(kbsModuleSourceBean,false);
			}
		}
		index.buildIndex(null, true);
	}

	

}
