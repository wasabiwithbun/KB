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
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import org.javacoo.cowswing.base.loader.LanguageLoader;
import org.javacoo.cowswing.base.utils.MsgDialogUtil;
import org.javacoo.cowswing.core.event.CowSwingListener;
import org.javacoo.cowswing.plugin.kbs.constant.KbsConstant;
import org.javacoo.cowswing.plugin.kbs.service.IndexWrap;
import org.javacoo.cowswing.plugin.kbs.service.beans.SimpleKeyValueBean;
import org.javacoo.cowswing.plugin.kbs.ui.view.panel.IndexManagePanel;
import org.javacoo.cowswing.ui.view.dialog.AbstractDialog;
import org.javacoo.cowswing.ui.view.dialog.WaitingDialog;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

/**
 * 索引管理设置
 * <p>说明:</p>
 * <li></li>
 * @author DuanYong
 * @since 2013-9-26 下午6:29:21
 * @version 1.0
 */
@Component("indexManageDialog")
public class IndexManageDialog extends AbstractDialog implements
		CowSwingListener {
	private static final long serialVersionUID = 1L;
	/** 索引管理面板 */
	@Resource(name = "indexManagePanel")
	private IndexManagePanel indexManagePanel;
	/**索引包装类*/
	@Resource(name="indexWrap")
    private IndexWrap indexWrap;
	List<SimpleKeyValueBean> indexList;

	private IndexManageDialog indexManageDialog;
	public IndexManageDialog(){
		super(500,300,true);
	}
	@Override
	public JComponent getCenterPane() {
		if (centerPane == null) {
			centerPane = indexManagePanel;
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
		indexList = indexManagePanel.getData();
		indexManageDialog = this;
		if(CollectionUtils.isEmpty(indexList)){
			MsgDialogUtil.createMessageDialog(LanguageLoader.getString("Kbs.index_module_isEmpty"));
		}else{
			int result = JOptionPane.showConfirmDialog(indexManageDialog, LanguageLoader.getString("Kbs.index_rebuild_Confirm"),LanguageLoader.getString("Common.confirm"), JOptionPane.YES_NO_OPTION); 
			if(result == 0){
				final IndexManageDialog indexManageDialog = this;
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						//开启线程重建索引
						Thread currThread = new Thread(new ReBuildIndex(indexList));
						currThread.start();
						Thread waitingThread = new Thread(new WaitingDialog(indexManageDialog,currThread,LanguageLoader.getString("Kbs.index_rebuild_info")));
						waitingThread.start();
					}
				});
			}else{
				this.dispose();
			}
		}
	}
	class ReBuildIndex implements Runnable{
		private List<SimpleKeyValueBean> indexList;
        public ReBuildIndex(List<SimpleKeyValueBean> indexList){
        	this.indexList = indexList;
        }
		/* (non-Javadoc)
		 * @see java.lang.Runnable#run()
		 */
		@Override
		public void run() {
			if(indexList.size() > 1){
				indexWrap.reBuildAllIndex();
			}else{
				SimpleKeyValueBean simpleKeyValueBean = indexList.get(0);
				if(KbsConstant.SYSTEM_MODULE_ARTICLE.equals(simpleKeyValueBean.getKey())){
					indexWrap.reBuildArticleIndex();
				}else{
					indexWrap.reBuildSourceIndex();
				}
			}
			indexManageDialog.dispose();
		}
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
		logger.info("索引管理设置");
		List<SimpleKeyValueBean> indexList = new ArrayList<SimpleKeyValueBean>();
		indexList.add(new SimpleKeyValueBean(KbsConstant.SYSTEM_MODULE_ARTICLE,LanguageLoader.getString("Kbs.module_article_tab_title")));
		indexList.add(new SimpleKeyValueBean(KbsConstant.SYSTEM_MODULE_SOURCE,LanguageLoader.getString("Kbs.module_source_tab_title")));
		indexManagePanel.initData(indexList);
	}
	
	

	
}
