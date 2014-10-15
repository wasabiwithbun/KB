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
import org.javacoo.cowswing.plugin.kbs.service.beans.KbsModuleSourceBean;
import org.javacoo.cowswing.plugin.kbs.service.beans.KbsModuleSourceCriteria;
import org.javacoo.cowswing.plugin.kbs.ui.action.AddSourceAction;
import org.javacoo.cowswing.plugin.kbs.ui.action.CancelShareSourceAction;
import org.javacoo.cowswing.plugin.kbs.ui.action.DeleteSourceAction;
import org.javacoo.cowswing.plugin.kbs.ui.action.ShareSourceAction;
import org.javacoo.cowswing.plugin.kbs.ui.action.UpdateSourceAction;
import org.javacoo.cowswing.plugin.kbs.ui.action.ViewSourceAction;
import org.javacoo.cowswing.plugin.kbs.ui.model.KbsModuleSourceTabelModel;
import org.javacoo.cowswing.ui.util.ColumnResizer;
import org.javacoo.cowswing.ui.view.panel.AbstractListPage;
import org.javacoo.persistence.PaginationSupport;
import org.springframework.stereotype.Component;

/**
 * 资源列表
 * <p>说明:</p>
 * <li></li>
 * @author DuanYong
 * @since 2013-7-15 下午4:45:36
 * @version 1.0
 */
@Component("kbsModuleSourceListPanel")
public class KbsModuleSourceListPanel extends AbstractListPage implements ListSelectionListener{
	private static final long serialVersionUID = 1L;
	/**资源模块服务*/
	@Resource(name="kbsModuleSourceService")
    private ICowSwingService<KbsModuleSourceBean, KbsModuleSourceCriteria> kbsModuleSourceService;
	/**
	 * 资源Table
	 */
	private JTable moduleSourceTable;
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
	@Resource(name="deleteSourceAction")
	private DeleteSourceAction deleteSourceAction;
	/**
	 * 查看Action
	 */
	@Resource(name="viewSourceAction")
	private ViewSourceAction viewSourceAction;
	/**
	 * 添加Action
	 */
	@Resource(name="addSourceAction")
	private AddSourceAction addSourceAction;
	/**
	 * 修改Action
	 */
	@Resource(name="updateSourceAction")
	private UpdateSourceAction updateSourceAction;
	/**
	 * 分享Action
	 */
	@Resource(name="shareSourceAction")
	private ShareSourceAction shareSourceAction;
	/**
	 * 取消分享Action
	 */
	@Resource(name="cancelShareSourceAction")
	private CancelShareSourceAction cancelShareSourceAction;
	/**当前知识分类*/
	private String typeCode;
	@Override
	protected JComponent getCenterPane() {
		return new JScrollPane(getKbsModuleSourceTable());
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
			addSourceAction.initTitle();
			addButton = new JButton(addSourceAction);
		}
		return addButton;
	}
	private JButton getUpdateButton() {
		if (updateButton == null) {
			updateButton = new JButton(updateSourceAction);
		}
		return updateButton;
	}
	private JButton getDeleteButton() {
		if (deleteButton == null) {
			deleteButton = new JButton(deleteSourceAction);
		}
		return deleteButton;
	}

	private JButton getViewButton() {
		if (viewButton == null) {
			viewButton = new JButton(viewSourceAction);
		}
		return viewButton;
	}
	private JButton getShareButton() {
		if (shareButton == null) {
			shareButton = new JButton(shareSourceAction);
		}
		return shareButton;
	}
	private JButton getCancelShareButton() {
		if (cancelShareButton == null) {
			cancelShareButton = new JButton(cancelShareSourceAction);
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
	public JTable getKbsModuleSourceTable() {
		if (moduleSourceTable == null) {
			moduleSourceTable = new JTable();
			KbsModuleSourceTabelModel dataModel = new KbsModuleSourceTabelModel(
					getColumnNames());
			moduleSourceTable.setModel(dataModel);
			moduleSourceTable.setPreferredScrollableViewportSize(new Dimension(
					500, 70));
			moduleSourceTable.setFillsViewportHeight(true);

			moduleSourceTable.getSelectionModel().addListSelectionListener(
					new ListSelectionListener() {

						@Override
						public void valueChanged(ListSelectionEvent e) {
							SwingUtilities.invokeLater(new Runnable() {
								public void run() {
									if (moduleSourceTable.getSelectedRow() != -1) {
										deleteButton.setEnabled(true);
										shareButton.setEnabled(true);
										cancelShareButton.setEnabled(true);
										if (moduleSourceTable.getSelectedRows().length > 1) {
											viewButton.setEnabled(false);
										} else {
											viewButton.setEnabled(true);
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

			moduleSourceTable.setAutoCreateRowSorter(true);
		}

		return moduleSourceTable;
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
		addSourceAction.setEnabled(enabled);
	}

	public void showData(int startIndex,int pageSize,String... params) {
		((KbsModuleSourceTabelModel) getKbsModuleSourceTable().getModel()).setData(getData(startIndex, pageSize,params));
		final JTable table = getKbsModuleSourceTable();
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				ColumnResizer.adjustColumnPerferredWidths(table);
			}
		});
	}

	public List<KbsModuleSourceBean> getData(int startIndex,int pageSize,String... params) {
		KbsModuleSourceCriteria criteria = new KbsModuleSourceCriteria();
		criteria.setStartIndex(startIndex);
		criteria.setPageSize(pageSize);
		criteria.setTypeCode(params[0]);
		PaginationSupport<KbsModuleSourceBean> result = kbsModuleSourceService.getPaginatedList(criteria,KbsConstant.SQLMAP_ID_GET_LIST_KBS_MODULE_SOURCE);
		paginationBar.setPaginationSupport(result);
		paginationBar.setListPage(this);
		paginationBar.loadData();
		return (List<KbsModuleSourceBean>) result.getData();
	}

	private List<String> getColumnNames() {
		List<String> columnNames = new ArrayList<String>();
		columnNames.add(LanguageLoader.getString("Kbs.module_source_list_id"));
		columnNames.add(LanguageLoader.getString("Kbs.module_source_list_title"));
		columnNames.add(LanguageLoader.getString("Kbs.module_source_list_fileName"));
		columnNames.add(LanguageLoader.getString("Kbs.module_source_list_keyword"));
		columnNames.add(LanguageLoader.getString("Kbs.module_source_list_typeName"));
		columnNames.add(LanguageLoader.getString("Kbs.module_source_list_purview"));
		columnNames.add(LanguageLoader.getString("Kbs.module_source_list_updateDate"));

		return columnNames;
	}
	/* (non-Javadoc)
	 * @see org.javacoo.cowswing.core.event.CowSwingListener#update(org.javacoo.cowswing.core.event.CowSwingEvent)
	 */
	@Override
	public void update(CowSwingEvent event) {
		if (event.getEventType().equals(KbsEventType.KbsModuleSourceAddEvent) || event.getEventType().equals(KbsEventType.KbsModuleSourceUpdateEvent) || event.getEventType().equals(KbsEventType.KbsModuleSourceDeleteEvent)) {
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
	 * @return the kbsModuleSourceService
	 */
	public ICowSwingService<KbsModuleSourceBean, KbsModuleSourceCriteria> getKbsModuleSourceService() {
		return kbsModuleSourceService;
	}
	
	
}
