/**
 * 如石子一粒,仰高山之巍峨,但不自惭形秽.
 * 若小草一棵,慕白杨之伟岸,却不妄自菲薄.
 */
package org.javacoo.cowswing.plugin.kbs.service;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.javacoo.cowswing.base.loader.LanguageLoader;
import org.javacoo.cowswing.base.service.AbstractCowSwingService;
import org.javacoo.cowswing.base.utils.DateUtil;
import org.javacoo.cowswing.plugin.kbs.constant.KbsConstant;
import org.javacoo.cowswing.plugin.kbs.domain.KbsModuleArticle;
import org.javacoo.cowswing.plugin.kbs.service.beans.KbsModuleArticleBean;
import org.javacoo.cowswing.plugin.kbs.service.beans.KbsModuleArticleCriteria;
import org.javacoo.cowswing.plugin.kbs.service.beans.SimpleKeyValueBean;
import org.javacoo.persistence.PaginationSupport;
import org.springframework.stereotype.Component;

/**
 * 文章模块服务实现类
 * <p>说明:</p>
 * <li></li>
 * @author DuanYong
 * @since 2013-7-26 下午5:43:16
 * @version 1.0
 */
@Component("kbsModuleArticleService")
public class KbsModuleArticleServiceImpl extends AbstractCowSwingService<KbsModuleArticleBean,KbsModuleArticle,KbsModuleArticleCriteria>{
	/**权限MAP*/
	private static Map<String,SimpleKeyValueBean> purviewMap = new HashMap<String,SimpleKeyValueBean>();
	static{
		purviewMap.put(KbsConstant.SEARCH_PURVIEW_PERSON,new SimpleKeyValueBean(KbsConstant.SEARCH_PURVIEW_PERSON,LanguageLoader.getString("Kbs.module_article_add_panel_purview_person")));
		purviewMap.put(KbsConstant.SEARCH_PURVIEW_VIEW,new SimpleKeyValueBean(KbsConstant.SEARCH_PURVIEW_VIEW,LanguageLoader.getString("Kbs.module_article_add_panel_purview_view")));
	}
	/* (non-Javadoc)
	 * @see org.javacoo.cowswing.base.service.AbstractCowSwingService#doInsert(org.javacoo.cowswing.base.service.beans.CowSwingBaseBean, java.lang.String)
	 */
	@Override
	protected int doInsert(KbsModuleArticleBean t, String sqlMapId)
			throws IllegalAccessException, InvocationTargetException {
		KbsModuleArticle kbsModuleArticle = new KbsModuleArticle();
		BeanUtils.copyProperties(kbsModuleArticle, t);
		this.getPersistService().insertBySqlMap(sqlMapId, kbsModuleArticle);
		return kbsModuleArticle.getId();
	}

	/* (non-Javadoc)
	 * @see org.javacoo.cowswing.base.service.AbstractCowSwingService#doUpdate(org.javacoo.cowswing.base.service.beans.CowSwingBaseBean, java.lang.String)
	 */
	@Override
	protected int doUpdate(KbsModuleArticleBean t, String sqlMapId)
			throws IllegalAccessException, InvocationTargetException {
		KbsModuleArticle kbsModuleArticle = new KbsModuleArticle();
		BeanUtils.copyProperties(kbsModuleArticle, t);
		return this.getPersistService().updateBySqlMap(sqlMapId, kbsModuleArticle);
	}

	/* (non-Javadoc)
	 * @see org.javacoo.cowswing.base.service.AbstractCowSwingService#doDelete(org.javacoo.cowswing.base.service.beans.CowSwingBaseBean, java.lang.String)
	 */
	@Override
	protected int doDelete(KbsModuleArticleBean t, String sqlMapId)
			throws IllegalAccessException, InvocationTargetException {
		KbsModuleArticle kbsModuleArticle = new KbsModuleArticle();
		BeanUtils.copyProperties(kbsModuleArticle, t);
		return this.getPersistService().deleteBySqlMap(sqlMapId, kbsModuleArticle);
	}

	/* (non-Javadoc)
	 * @see org.javacoo.cowswing.base.service.AbstractCowSwingService#doGet(org.javacoo.cowswing.base.service.beans.CowSwingBaseBean, java.lang.String)
	 */
	@Override
	protected KbsModuleArticle doGet(KbsModuleArticleBean t, String sqlMapId)
			throws IllegalAccessException, InvocationTargetException {
		Map<String,Object> paramMap = new HashMap<String,Object>();
		paramMap.put("id", t.getId());
		return (KbsModuleArticle) this.getPersistService().findObjectBySqlMap(sqlMapId, paramMap);
	}

	/* (non-Javadoc)
	 * @see org.javacoo.cowswing.base.service.AbstractCowSwingService#doGetList(java.lang.Object, java.lang.String)
	 */
	@Override
	protected List<KbsModuleArticle> doGetList(KbsModuleArticleCriteria q, String sqlMapId)
			throws IllegalAccessException, InvocationTargetException {
		return (List<KbsModuleArticle>) this.getPersistService().findListBySqlMap(sqlMapId, q);
	}

	/* (non-Javadoc)
	 * @see org.javacoo.cowswing.base.service.AbstractCowSwingService#doGetPaginatedList(java.lang.Object, java.lang.String)
	 */
	@Override
	protected PaginationSupport<KbsModuleArticle> doGetPaginatedList(KbsModuleArticleCriteria q,
			String sqlMapId) throws IllegalAccessException,
			InvocationTargetException {
		return (PaginationSupport<KbsModuleArticle>) this.getPersistService().findPaginatedListBySqlMap(sqlMapId, q, q.getStartIndex(), q.getPageSize());
	}

	/* (non-Javadoc)
	 * @see org.javacoo.cowswing.base.service.AbstractCowSwingService#translateBean(java.lang.Object)
	 */
	@Override
	protected KbsModuleArticleBean translateBean(KbsModuleArticle e)
			throws IllegalAccessException, InvocationTargetException {
		KbsModuleArticleBean kbsModuleArticleBean = new KbsModuleArticleBean();
    	BeanUtils.copyProperties(kbsModuleArticleBean, e);
    	if(null != kbsModuleArticleBean.getReleaseDate()){
    		kbsModuleArticleBean.setReleaseDateStr(DateUtil.dateToStr(kbsModuleArticleBean.getReleaseDate(), "yyyy-MM-dd HH:mm:ss"));
    	}
    	if(StringUtils.isNotBlank(kbsModuleArticleBean.getPurview())){
    		kbsModuleArticleBean.setPurviewStr(purviewMap.get(kbsModuleArticleBean.getPurview()).getValue());
    	}
    	return kbsModuleArticleBean;
	}

}
