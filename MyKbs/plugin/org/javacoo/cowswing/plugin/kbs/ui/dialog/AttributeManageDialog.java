/**
 * 如石子一粒,仰高山之巍峨,但不自惭形秽.
 * 若小草一棵,慕白杨之伟岸,却不妄自菲薄.
 */
package org.javacoo.cowswing.plugin.kbs.ui.dialog;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.swing.JButton;
import javax.swing.JComponent;

import org.javacoo.cowswing.base.loader.LanguageLoader;
import org.javacoo.cowswing.core.event.CowSwingListener;
import org.javacoo.cowswing.plugin.kbs.constant.KbsConstant;
import org.javacoo.cowswing.plugin.kbs.service.beans.SimpleKeyValueBean;
import org.javacoo.cowswing.plugin.kbs.ui.view.panel.AttributeManagePanel;
import org.javacoo.cowswing.ui.view.dialog.AbstractDialog;
import org.springframework.stereotype.Component;

/**
 * 知识属性管理设置
 * 
 * <p>说明:</p>
 * <li></li>
 * @author DuanYong
 * @since 2013-9-29 上午10:30:28
 * @version 1.0
 */
@Component("attributeManageDialog")
public class AttributeManageDialog extends AbstractDialog implements
		CowSwingListener {
	private static final long serialVersionUID = 1L;
	/** 知识属性管理面板 */
	@Resource(name = "attributeManagePanel")
	private AttributeManagePanel attributeManagePanel;

	private AttributeManageDialog indexManageDialog;
	public AttributeManageDialog(){
		super(500,480,true);
	}
	@Override
	public JComponent getCenterPane() {
		if (centerPane == null) {
			centerPane = attributeManagePanel;
		}
		return centerPane;
	}
	public JButton getCancelButton(){
		return null;
	}
	
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.javacoo.cowswing.ui.view.dialog.AbstractDialog#
	 * finishButtonActionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	protected void finishButtonActionPerformed(ActionEvent event) {
		this.dispose();
	}
	

	protected void initData(String type) {
		fillJTabbedPane();
	}
	public void dispose(){
		super.dispose();
		centerPane = null;
	}
	/**
	 * 填充JTabbedPane值
	 * <p>方法说明:</p>
	 * @auther DuanYong
	 * @since 2012-12-3 下午12:20:32
	 * @return void
	 */
	private void fillJTabbedPane(){
		logger.info("知识属性管理设置");
		List<SimpleKeyValueBean> indexList = new ArrayList<SimpleKeyValueBean>();
		indexList.add(new SimpleKeyValueBean(KbsConstant.SYSTEM_MODULE_ARTICLE,LanguageLoader.getString("Kbs.module_article_tab_title")));
		indexList.add(new SimpleKeyValueBean(KbsConstant.SYSTEM_MODULE_SOURCE,LanguageLoader.getString("Kbs.module_source_tab_title")));
		attributeManagePanel.initData(indexList);
	}
	
	

	
}
