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
import org.javacoo.cowswing.plugin.kbs.domain.KbsModuleSource;
import org.javacoo.cowswing.plugin.kbs.service.beans.KbsModuleSourceBean;
import org.javacoo.cowswing.plugin.kbs.service.beans.KbsModuleSourceCriteria;
import org.javacoo.cowswing.plugin.kbs.service.beans.SimpleKeyValueBean;
import org.javacoo.persistence.PaginationSupport;
import org.springframework.stereotype.Component;

/**
 * 资源模块服务实现类
 * <p>说明:</p>
 * <li></li>
 * @author DuanYong
 * @since 2013-7-15 下午4:11:11
 * @version 1.0
 */
@Component("kbsModuleSourceService")
public class KbsModuleSourceServiceImpl extends AbstractCowSwingService<KbsModuleSourceBean,KbsModuleSource,KbsModuleSourceCriteria>{
	/**权限MAP*/
	private static Map<String,SimpleKeyValueBean> purviewMap = new HashMap<String,SimpleKeyValueBean>();
	static{
		purviewMap.put(KbsConstant.SEARCH_PURVIEW_PERSON,new SimpleKeyValueBean(KbsConstant.SEARCH_PURVIEW_PERSON,LanguageLoader.getString("Kbs.module_source_add_panel_purview_person")));
		purviewMap.put(KbsConstant.SEARCH_PURVIEW_VIEW,new SimpleKeyValueBean(KbsConstant.SEARCH_PURVIEW_VIEW,LanguageLoader.getString("Kbs.module_source_add_panel_purview_view")));
		purviewMap.put(KbsConstant.SEARCH_PURVIEW_DOWNLOAD,new SimpleKeyValueBean(KbsConstant.SEARCH_PURVIEW_DOWNLOAD,LanguageLoader.getString("Kbs.module_source_add_panel_purview_down")));
	}
	/* (non-Javadoc)
	 * @see org.javacoo.cowswing.base.service.AbstractCowSwingService#doInsert(org.javacoo.cowswing.base.service.beans.CowSwingBaseBean, java.lang.String)
	 */
	@Override
	protected int doInsert(KbsModuleSourceBean t, String sqlMapId)
			throws IllegalAccessException, InvocationTargetException {
		KbsModuleSource kbsModuleSource = new KbsModuleSource();
		BeanUtils.copyProperties(kbsModuleSource, t);
		this.getPersistService().insertBySqlMap(sqlMapId, kbsModuleSource);
		return kbsModuleSource.getId();
	}

	/* (non-Javadoc)
	 * @see org.javacoo.cowswing.base.service.AbstractCowSwingService#doUpdate(org.javacoo.cowswing.base.service.beans.CowSwingBaseBean, java.lang.String)
	 */
	@Override
	protected int doUpdate(KbsModuleSourceBean t, String sqlMapId)
			throws IllegalAccessException, InvocationTargetException {
		KbsModuleSource kbsModuleSource = new KbsModuleSource();
		BeanUtils.copyProperties(kbsModuleSource, t);
		return this.getPersistService().updateBySqlMap(sqlMapId, kbsModuleSource);
	}

	/* (non-Javadoc)
	 * @see org.javacoo.cowswing.base.service.AbstractCowSwingService#doDelete(org.javacoo.cowswing.base.service.beans.CowSwingBaseBean, java.lang.String)
	 */
	@Override
	protected int doDelete(KbsModuleSourceBean t, String sqlMapId)
			throws IllegalAccessException, InvocationTargetException {
		KbsModuleSource kbsModuleSource = new KbsModuleSource();
		BeanUtils.copyProperties(kbsModuleSource, t);
		return this.getPersistService().deleteBySqlMap(sqlMapId, kbsModuleSource);
	}

	/* (non-Javadoc)
	 * @see org.javacoo.cowswing.base.service.AbstractCowSwingService#doGet(org.javacoo.cowswing.base.service.beans.CowSwingBaseBean, java.lang.String)
	 */
	@Override
	protected KbsModuleSource doGet(KbsModuleSourceBean t, String sqlMapId)
			throws IllegalAccessException, InvocationTargetException {
		Map<String,Object> paramMap = new HashMap<String,Object>();
		paramMap.put("id", t.getId());
		return (KbsModuleSource) this.getPersistService().findObjectBySqlMap(sqlMapId, paramMap);
	}

	/* (non-Javadoc)
	 * @see org.javacoo.cowswing.base.service.AbstractCowSwingService#doGetList(java.lang.Object, java.lang.String)
	 */
	@Override
	protected List<KbsModuleSource> doGetList(KbsModuleSourceCriteria q, String sqlMapId)
			throws IllegalAccessException, InvocationTargetException {
		return (List<KbsModuleSource>) this.getPersistService().findListBySqlMap(sqlMapId, q);
	}

	/* (non-Javadoc)
	 * @see org.javacoo.cowswing.base.service.AbstractCowSwingService#doGetPaginatedList(java.lang.Object, java.lang.String)
	 */
	@Override
	protected PaginationSupport<KbsModuleSource> doGetPaginatedList(KbsModuleSourceCriteria q,
			String sqlMapId) throws IllegalAccessException,
			InvocationTargetException {
		return (PaginationSupport<KbsModuleSource>) this.getPersistService().findPaginatedListBySqlMap(sqlMapId, q, q.getStartIndex(), q.getPageSize());
	}

	/* (non-Javadoc)
	 * @see org.javacoo.cowswing.base.service.AbstractCowSwingService#translateBean(java.lang.Object)
	 */
	@Override
	protected KbsModuleSourceBean translateBean(KbsModuleSource e)
			throws IllegalAccessException, InvocationTargetException {
		KbsModuleSourceBean kbsModuleSourceBean = new KbsModuleSourceBean();
    	BeanUtils.copyProperties(kbsModuleSourceBean, e);
    	if(null != kbsModuleSourceBean.getUploadDate()){
    		kbsModuleSourceBean.setUploadDateStr(DateUtil.dateToStr(kbsModuleSourceBean.getUploadDate(), "yyyy-MM-dd HH:mm:ss"));
    	}
    	if(StringUtils.isNotBlank(kbsModuleSourceBean.getPurview())){
    		kbsModuleSourceBean.setPurviewStr(purviewMap.get(kbsModuleSourceBean.getPurview()).getValue());
    	}
    	return kbsModuleSourceBean;
	}

}
