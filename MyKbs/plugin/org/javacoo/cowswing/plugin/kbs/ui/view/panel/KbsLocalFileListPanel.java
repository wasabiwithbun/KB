/**
 * 如石子一粒,仰高山之巍峨,但不自惭形秽.
 * 若小草一棵,慕白杨之伟岸,却不妄自菲薄.
 */
package org.javacoo.cowswing.plugin.kbs.ui.view.panel;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.border.EtchedBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.apache.log4j.Logger;
import org.javacoo.cowswing.base.constant.Constant;
import org.javacoo.cowswing.base.loader.LanguageLoader;
import org.javacoo.cowswing.core.event.CowSwingEvent;
import org.javacoo.cowswing.plugin.kbs.service.beans.LocalFileBean;
import org.javacoo.cowswing.plugin.kbs.ui.action.DeleteLocalFileAction;
import org.javacoo.cowswing.plugin.kbs.ui.action.ViewLocalFileAction;
import org.javacoo.cowswing.plugin.kbs.ui.model.LocalFileTabelModel;
import org.javacoo.cowswing.ui.util.ColumnResizer;
import org.javacoo.cowswing.ui.view.panel.AbstractListPage;

/**
 * 本地文件列表面板
 * 
 * <p>说明:</p>
 * <li></li>
 * @author DuanYong
 * @since 2014-7-17 下午5:56:14
 * @version 1.0
 */
@org.springframework.stereotype.Component("kbsLocalFileListPanel")
public class KbsLocalFileListPanel extends AbstractListPage implements ListSelectionListener{
	private static final long serialVersionUID = 1L;
	protected Logger logger = Logger.getLogger(this.getClass());
	/**
	 * 查看按钮
	 */
	private JButton viewButton;
	/**
	 * 删除按钮
	 */
	private JButton deleteButton;
	/**
	 * 查看Action
	 */
	@Resource(name="viewLocalFileAction")
	private ViewLocalFileAction viewLocalFileAction;

	/**
	 * 删除Action
	 */
	@Resource(name="deleteLocalFileAction")
	private DeleteLocalFileAction deleteLocalFileAction;
	/**
	 * 本地文件Table
	 */
	private JTable localFileDataTable;
	/**当前知识分类*/
	private String typeCode;
	
	private File dirFile;
	@Override
	protected JComponent getTopPane() {
		super.getTopPane();
		buttonBar.add(getViewButton());	
		buttonBar.add(getDeleteButton());	
		return buttonBar;
	}
	private JButton getViewButton() {
		if (viewButton == null) {
			viewButton = new JButton(viewLocalFileAction);
		}
		return viewButton;
	}
	private JButton getDeleteButton() {
		if (deleteButton == null) {
			deleteButton = new JButton(deleteLocalFileAction);
		}
		return deleteButton;
	}
	@Override
	protected JComponent getCenterPane() {
		JPanel topPanel = new JPanel();
		topPanel.setLayout(new BorderLayout());
		topPanel.setBorder(new EtchedBorder());
		
		
		topPanel.add(new JScrollPane(getLocalFileDataTable()),BorderLayout.CENTER);
		return topPanel;
	}
	/* (non-Javadoc)
	 * @see org.javacoo.cowswing.core.event.CowSwingListener#update(org.javacoo.cowswing.core.event.CowSwingEvent)
	 */
	@Override
	public void update(CowSwingEvent event) {
		
		
		
	}

	
	

	/* (non-Javadoc)
	 * @see javax.swing.event.ListSelectionListener#valueChanged(javax.swing.event.ListSelectionEvent)
	 */
	@Override
	public void valueChanged(ListSelectionEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public String getPageName() {
		return LanguageLoader.getString("Kbs.local_list_title");
	}

	@Override
	public void disposePage() {
		super.disposePage();
	}
	
	/**
	 * @return crawlerRuleTable
	 * */
	public JTable getLocalFileDataTable() {
		if (localFileDataTable == null) {
			localFileDataTable = new JTable();
			LocalFileTabelModel dataModel = new LocalFileTabelModel(
					getColumnNames());
			localFileDataTable.setModel(dataModel);
			localFileDataTable.setPreferredScrollableViewportSize(new Dimension(
					500, 70));
			localFileDataTable.setFillsViewportHeight(true);

			localFileDataTable.getSelectionModel().addListSelectionListener(
					new ListSelectionListener() {
						@Override
						public void valueChanged(ListSelectionEvent e) {
							SwingUtilities.invokeLater(new Runnable() {
								public void run() {
									if (localFileDataTable.getSelectedRow() != -1) {
										if (localFileDataTable.getSelectedRows().length > 1) {
											viewButton.setEnabled(false);
										} else {
											viewButton.setEnabled(true);
										}
										deleteButton.setEnabled(true);
									} else {
										viewButton.setEnabled(false);
										deleteButton.setEnabled(false);
									}
								}
							});
						}
					});
			localFileDataTable.setAutoCreateRowSorter(true);
		}

		return localFileDataTable;
	}
	public void showData(int startIndex,int pageSize,String... params) {
		((LocalFileTabelModel) getLocalFileDataTable().getModel())
				.setData(getData(startIndex, pageSize));
		final JTable table = getLocalFileDataTable();
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				ColumnResizer.adjustColumnPerferredWidths(table);
			}
		});
	}
	public void getList(String path){
		typeCode = path;
		showData(0,Constant.PAGE_LIMIT,typeCode);
	}
	public void refresh(){
		showData(0,Constant.PAGE_LIMIT,typeCode);
	}
	public List<LocalFileBean> getData(int startIndex,int pageSize) {
		List<LocalFileBean> dataList = new ArrayList<LocalFileBean>();
		if(null != typeCode){
			dirFile = new File(typeCode);
			pageContainer.setTitleLabelText(dirFile.getAbsolutePath());
			File[] children = dirFile.listFiles();
			File tempFile = null;
			if(null != children){
				for (int i = 0; i < children.length; ++i){
					tempFile = children[i];
					if(tempFile.isFile()){
						dataList.add(createLocalFileBean(tempFile));
					}
				}	
			}
		}
		return dataList;
	}
	protected Component getBottomPane() {
		return null;
	}
	/**
	 * 创建本地文件对象
	 * <p>方法说明:</>
	 * <li></li>
	 * @author DuanYong
	 * @since 2014-7-17 下午9:49:55
	 * @version 1.0
	 * @exception 
	 * @param file
	 * @return
	 */
	private LocalFileBean createLocalFileBean(File file){
		LocalFileBean localFileBean = new LocalFileBean();
		localFileBean.setAbsolute(file.isAbsolute());
		localFileBean.setAbsolutePath(file.getAbsolutePath());
		localFileBean.setCanRead(file.canExecute());
		localFileBean.setCanWrite(file.canWrite());
		localFileBean.setDirectory(file.isDirectory());
		localFileBean.setExists(file.exists());
		localFileBean.setFile(file.isFile());
		localFileBean.setHidden(file.isHidden());
		localFileBean.setLastModified((new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(file.lastModified()));
		localFileBean.setLength(file.length());
		localFileBean.setName(file.getName());
		localFileBean.setParent(file.getParent());
		localFileBean.setPath(file.getPath());
		return localFileBean;
	}

	private List<String> getColumnNames() {
		List<String> columnNames = new ArrayList<String>();
		columnNames.add(LanguageLoader.getString("Kbs.local_title"));
		columnNames.add(LanguageLoader.getString("Kbs.local_length"));
		columnNames.add(LanguageLoader.getString("Kbs.local_lastModified"));

		return columnNames;
	}

	
}
