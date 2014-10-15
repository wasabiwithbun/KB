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
import org.javacoo.cowswing.base.service.AbstractCowSwingService;
import org.javacoo.cowswing.plugin.kbs.domain.KbsType;
import org.javacoo.cowswing.plugin.kbs.service.beans.KbsTypeBean;
import org.javacoo.cowswing.plugin.kbs.service.beans.KbsTypeCriteria;
import org.javacoo.persistence.PaginationSupport;
import org.springframework.stereotype.Component;


/**
 * 知识分类服务实现类
 * <p>说明:</p>
 * <li></li>
 * @author DuanYong
 * @since 2013-7-11 下午2:24:55
 * @version 1.0
 */
@Component("kbsTypeService")
public class KbsTypeServiceImpl extends AbstractCowSwingService<KbsTypeBean,KbsType,KbsTypeCriteria>{

	/* (non-Javadoc)
	 * @see org.javacoo.cowswing.base.service.AbstractCowSwingService#doInsert(org.javacoo.cowswing.base.service.beans.CowSwingBaseBean, java.lang.String)
	 */
	@Override
	protected int doInsert(KbsTypeBean t, String sqlMapId)
			throws IllegalAccessException, InvocationTargetException {
		KbsType kbsType = new KbsType();
		BeanUtils.copyProperties(kbsType, t);
		this.getPersistService().insertBySqlMap(sqlMapId, kbsType);
		return kbsType.getId();
	}

	/* (non-Javadoc)
	 * @see org.javacoo.cowswing.base.service.AbstractCowSwingService#doUpdate(org.javacoo.cowswing.base.service.beans.CowSwingBaseBean, java.lang.String)
	 */
	@Override
	protected int doUpdate(KbsTypeBean t, String sqlMapId)
			throws IllegalAccessException, InvocationTargetException {
		KbsType kbsType = new KbsType();
		BeanUtils.copyProperties(kbsType, t);
		return this.getPersistService().updateBySqlMap(sqlMapId, kbsType);
	}

	/* (non-Javadoc)
	 * @see org.javacoo.cowswing.base.service.AbstractCowSwingService#doDelete(org.javacoo.cowswing.base.service.beans.CowSwingBaseBean, java.lang.String)
	 */
	@Override
	protected int doDelete(KbsTypeBean t, String sqlMapId)
			throws IllegalAccessException, InvocationTargetException {
		KbsType kbsType = new KbsType();
		BeanUtils.copyProperties(kbsType, t);
		return this.getPersistService().deleteBySqlMap(sqlMapId, kbsType);
	}

	/* (non-Javadoc)
	 * @see org.javacoo.cowswing.base.service.AbstractCowSwingService#doGet(org.javacoo.cowswing.base.service.beans.CowSwingBaseBean, java.lang.String)
	 */
	@Override
	protected KbsType doGet(KbsTypeBean t, String sqlMapId)
			throws IllegalAccessException, InvocationTargetException {
		Map<String,Object> paramMap = new HashMap<String,Object>();
		paramMap.put("typeCode", t.getTypeCode());
		return (KbsType) this.getPersistService().findObjectBySqlMap(sqlMapId, paramMap);
	}

	/* (non-Javadoc)
	 * @see org.javacoo.cowswing.base.service.AbstractCowSwingService#doGetList(java.lang.Object, java.lang.String)
	 */
	@Override
	protected List<KbsType> doGetList(KbsTypeCriteria q, String sqlMapId)
			throws IllegalAccessException, InvocationTargetException {
		return (List<KbsType>) this.getPersistService().findListBySqlMap(sqlMapId, q);
	}

	/* (non-Javadoc)
	 * @see org.javacoo.cowswing.base.service.AbstractCowSwingService#doGetPaginatedList(java.lang.Object, java.lang.String)
	 */
	@Override
	protected PaginationSupport<KbsType> doGetPaginatedList(KbsTypeCriteria q,
			String sqlMapId) throws IllegalAccessException,
			InvocationTargetException {
		return (PaginationSupport<KbsType>) this.getPersistService().findPaginatedListBySqlMap(sqlMapId, q, q.getStartIndex(), q.getPageSize());
	}

	/* (non-Javadoc)
	 * @see org.javacoo.cowswing.base.service.AbstractCowSwingService#translateBean(java.lang.Object)
	 */
	@Override
	protected KbsTypeBean translateBean(KbsType e)
			throws IllegalAccessException, InvocationTargetException {
		KbsTypeBean kbsTypeBean = new KbsTypeBean();
    	BeanUtils.copyProperties(kbsTypeBean, e);
    	return kbsTypeBean;
	}

}
