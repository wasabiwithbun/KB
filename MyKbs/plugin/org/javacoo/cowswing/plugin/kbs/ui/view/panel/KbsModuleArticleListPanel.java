/**
 * 如石子一粒,仰高山之巍峨,但不自惭形秽.
 * 若小草一棵,慕白杨之伟岸,却不妄自菲薄.
 */
package org.javacoo.cowswing.plugin.kbs.ui.view.panel;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.javacoo.cowswing.base.constant.Constant;
import org.javacoo.cowswing.base.loader.LanguageLoader;
import org.javacoo.cowswing.base.service.ICowSwingService;
import org.javacoo.cowswing.core.event.CowSwingEvent;
import org.javacoo.cowswing.plugin.kbs.constant.KbsConstant;
import org.javacoo.cowswing.plugin.kbs.event.KbsEventObserver;
import org.javacoo.cowswing.plugin.kbs.event.KbsEventType;
import org.javacoo.cowswing.plugin.kbs.service.beans.KbsModuleArticleBean;
import org.javacoo.cowswing.plugin.kbs.service.beans.KbsModuleArticleCriteria;
import org.javacoo.cowswing.plugin.kbs.ui.action.AddArticleAction;
import org.javacoo.cowswing.plugin.kbs.ui.action.CancelShareArticleAction;
import org.javacoo.cowswing.plugin.kbs.ui.action.DeleteArticleAction;
import org.javacoo.cowswing.plugin.kbs.ui.action.ShareArticleAction;
import org.javacoo.cowswing.plugin.kbs.ui.action.UpdateArticleAction;
import org.javacoo.cowswing.plugin.kbs.ui.action.ViewArticleAction;
import org.javacoo.cowswing.plugin.kbs.ui.model.KbsModuleArticleTabelModel;
import org.javacoo.cowswing.ui.util.ColumnResizer;
import org.javacoo.cowswing.ui.view.panel.AbstractListPage;
import org.javacoo.persistence.PaginationSupport;
import org.springframework.stereotype.Component;

/**
 * 文章列表
 * <p>说明:</p>
 * <li></li>
 * @author DuanYong
 * @since 2013-7-27 下午2:04:16
 * @version 1.0
 */
@Component("kbsModuleArticleListPanel")
public class KbsModuleArticleListPanel extends AbstractListPage implements ListSelectionListener{
	private static final long serialVersionUID = 1L;
	/**文章模块服务*/
	@Resource(name="kbsModuleArticleService")
    private ICowSwingService<KbsModuleArticleBean, KbsModuleArticleCriteria> kbsModuleArticleService;
	/**
	 * 资源Table
	 */
	private JTable moduleArticleTable;
	/**
	 * 删除按钮
	 */
	private JButton deleteButton;
	/**
	 * 查看按钮
	 */
	private JButton viewButton;
	/**
	 * 添加按钮
	 */
	private JButton addButton;
	/**
	 * 修改按钮
	 */
	private JButton updateButton;
	/**
	 * 分享按钮
	 */
	private JButton shareButton;
	/**
	 * 取消分享按钮
	 */
	private JButton cancelShareButton;
	/**
	 * 删除Action
	 */
	@Resource(name="deleteArticleAction")
	private DeleteArticleAction deleteArticleAction;
	/**
	 * 查看Action
	 */
	@Resource(name="viewArticleAction")
	private ViewArticleAction viewArticleAction;
	/**
	 * 添加Action
	 */
	@Resource(name="addArticleAction")
	private AddArticleAction addArticleAction;
	/**
	 * 修改Action
	 */
	@Resource(name="updateArticleAction")
	private UpdateArticleAction updateArticleAction;
	/**
	 * 分享Action
	 */
	@Resource(name="shareArticleAction")
	private ShareArticleAction shareArticleAction;
	/**
	 * 取消分享Action
	 */
	@Resource(name="cancelShareArticleAction")
	private CancelShareArticleAction cancelShareArticleAction;
	
	/**当前知识分类*/
	private String typeCode;
	@Override
	protected JComponent getCenterPane() {
		return new JScrollPane(getKbsModuleArticleTable());
	}
	
	@Override
	protected JComponent getTopPane() {
		super.getTopPane();
		buttonBar.add(getAddButton());
		buttonBar.add(getUpdateButton());
		buttonBar.add(getDeleteButton());
		buttonBar.add(getViewButton());	
		buttonBar.add(getShareButton());
		buttonBar.add(getCancelShareButton());
		return buttonBar;
	}
	private JButton getAddButton() {
		if (addButton == null) {
			addArticleAction.initTitle();
			addButton = new JButton(addArticleAction);
		}
		return addButton;
	}
	private JButton getUpdateButton() {
		if (updateButton == null) {
			updateButton = new JButton(updateArticleAction);
		}
		return updateButton;
	}
	private JButton getDeleteButton() {
		if (deleteButton == null) {
			deleteButton = new JButton(deleteArticleAction);
		}
		return deleteButton;
	}

	private JButton getViewButton() {
		if (viewButton == null) {
			viewButton = new JButton(viewArticleAction);
		}
		return viewButton;
	}
	private JButton getShareButton() {
		if (shareButton == null) {
			shareButton = new JButton(shareArticleAction);
		}
		return shareButton;
	}
	private JButton getCancelShareButton() {
		if (cancelShareButton == null) {
			cancelShareButton = new JButton(cancelShareArticleAction);
		}
		return cancelShareButton;
	}
	/**
	 * 取得资源table
	 * <p>方法说明:</>
	 * <li></li>
	 * @author DuanYong
	 * @since 2013-7-15 下午5:01:26
	 * @version 1.0
	 * @exception 
	 * @return
	 */
	public JTable getKbsModuleArticleTable() {
		if (moduleArticleTable == null) {
			moduleArticleTable = new JTable();
			KbsModuleArticleTabelModel dataModel = new KbsModuleArticleTabelModel(
					getColumnNames());
			moduleArticleTable.setModel(dataModel);
			moduleArticleTable.setPreferredScrollableViewportSize(new Dimension(
					500, 70));
			moduleArticleTable.setFillsViewportHeight(true);

			moduleArticleTable.getSelectionModel().addListSelectionListener(
					new ListSelectionListener() {

						@Override
						public void valueChanged(ListSelectionEvent e) {
							SwingUtilities.invokeLater(new Runnable() {
								public void run() {
									if (moduleArticleTable.getSelectedRow() != -1) {
										deleteButton.setEnabled(true);
										shareButton.setEnabled(true);
										cancelShareButton.setEnabled(true);
										if (moduleArticleTable.getSelectedRows().length > 1) {
											viewButton.setEnabled(false);
											updateButton.setEnabled(false);
										} else {
											viewButton.setEnabled(true);
											updateButton.setEnabled(true);
										}
									} else {
										deleteButton.setEnabled(false);
										viewButton.setEnabled(false);
										shareButton.setEnabled(false);
										cancelShareButton.setEnabled(false);
									}
								}
							});
						}
					});

			moduleArticleTable.setAutoCreateRowSorter(true);
		}

		return moduleArticleTable;
	}
	/**
	 * 根据知识分类查询资源列表
	 * <p>方法说明:</>
	 * <li></li>
	 * @author DuanYong
	 * @since 2013-7-15 下午5:34:48
	 * @version 1.0
	 * @exception 
	 * @param typeCode
	 */
	public void getList(String typeCode){
		this.typeCode = typeCode;
		showData(0,Constant.PAGE_LIMIT,typeCode);
	}
	/**
	 * 设置按钮
	 * <p>方法说明:</>
	 * <li></li>
	 * @author DuanYong
	 * @since 2013-7-17 下午2:46:32
	 * @version 1.0
	 * @exception 
	 * @param enabled
	 */
	public void setButtonEnabled(boolean enabled){
		addArticleAction.setEnabled(enabled);
	}

	public void showData(int startIndex,int pageSize,String... params) {
		((KbsModuleArticleTabelModel) getKbsModuleArticleTable().getModel()).setData(getData(startIndex, pageSize,params));
		final JTable table = getKbsModuleArticleTable();
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				ColumnResizer.adjustColumnPerferredWidths(table);
			}
		});
	}

	public List<KbsModuleArticleBean> getData(int startIndex,int pageSize,String... params) {
		KbsModuleArticleCriteria criteria = new KbsModuleArticleCriteria();
		criteria.setStartIndex(startIndex);
		criteria.setPageSize(pageSize);
		criteria.setTypeCode(params[0]);
		PaginationSupport<KbsModuleArticleBean> result = kbsModuleArticleService.getPaginatedList(criteria,KbsConstant.SQLMAP_ID_GET_LIST_KBS_MODULE_ARTICLE);
		paginationBar.setPaginationSupport(result);
		paginationBar.setListPage(this);
		paginationBar.loadData();
		return (List<KbsModuleArticleBean>) result.getData();
	}

	private List<String> getColumnNames() {
		List<String> columnNames = new ArrayList<String>();
		columnNames.add(LanguageLoader.getString("Kbs.module_article_list_id"));
		columnNames.add(LanguageLoader.getString("Kbs.module_article_list_title"));
		columnNames.add(LanguageLoader.getString("Kbs.module_article_list_origin"));
		columnNames.add(LanguageLoader.getString("Kbs.module_article_list_keyword"));
		columnNames.add(LanguageLoader.getString("Kbs.module_article_list_typeName"));
		columnNames.add(LanguageLoader.getString("Kbs.module_article_list_purview"));
		columnNames.add(LanguageLoader.getString("Kbs.module_article_list_updateDate"));

		return columnNames;
	}
	/* (non-Javadoc)
	 * @see org.javacoo.cowswing.core.event.CowSwingListener#update(org.javacoo.cowswing.core.event.CowSwingEvent)
	 */
	@Override
	public void update(CowSwingEvent event) {
		if (event.getEventType().equals(KbsEventType.KbsModuleArticleAddEvent) || event.getEventType().equals(KbsEventType.KbsModuleArticleUpdateEvent) || event.getEventType().equals(KbsEventType.KbsModuleArticleDeleteEvent)) {
			getList(this.typeCode);
		}
	}
	protected void addCrawlerListener(){
		KbsEventObserver.getInstance().addCrawlerListener(this);
	}
	/* (non-Javadoc)
	 * @see javax.swing.event.ListSelectionListener#valueChanged(javax.swing.event.ListSelectionEvent)
	 */
	@Override
	public void valueChanged(ListSelectionEvent e) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * @return the kbsModuleArticleService
	 */
	public ICowSwingService<KbsModuleArticleBean, KbsModuleArticleCriteria> getKbsModuleArticleService() {
		return kbsModuleArticleService;
	}
	
	
}
