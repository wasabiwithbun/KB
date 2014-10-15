/**
 * 如石子一粒,仰高山之巍峨,但不自惭形秽.
 * 若小草一棵,慕白杨之伟岸,却不妄自菲薄.
 */
package org.javacoo.cowswing.plugin.kbs.ui.view.panel;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.EtchedBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.javacoo.cowswing.base.constant.Constant;
import org.javacoo.cowswing.base.loader.ImageLoader;
import org.javacoo.cowswing.base.loader.LanguageLoader;
import org.javacoo.cowswing.core.event.CowSwingEvent;
import org.javacoo.cowswing.plugin.kbs.constant.KbsConstant;
import org.javacoo.cowswing.plugin.kbs.domain.KnowledgePoint;
import org.javacoo.cowswing.plugin.kbs.lucene.search.Search;
import org.javacoo.cowswing.plugin.kbs.service.beans.SearchCriteria;
import org.javacoo.cowswing.plugin.kbs.ui.action.ViewSearchAction;
import org.javacoo.cowswing.plugin.kbs.ui.model.KnowledgePointTabelModel;
import org.javacoo.cowswing.ui.util.ColumnResizer;
import org.javacoo.cowswing.ui.view.panel.AbstractListPage;
import org.javacoo.persistence.PaginationSupport;
import org.springframework.stereotype.Component;

/**
 * 搜索面板
 * <p>说明:</p>
 * <li></li>
 * @author DuanYong
 * @since 2013-7-9 下午7:11:30
 * @version 1.0
 */
@Component("kbsLocalSearchPanel")
public class KbsLocalSearchPanel extends AbstractListPage implements ListSelectionListener{
	private static final long serialVersionUID = 1L;
    /**搜索管理*/
	@Resource(name="searchManager")
    private Search searchManager;
	/**
	 * 查看按钮
	 */
	private JButton viewButton;
	/**
	 * 查看Action
	 */
	@Resource(name="viewSearchAction")
	private ViewSearchAction viewSearchAction;
	/**
	 * 知识点Table
	 */
	private JTable knowledgePointTable;
	/**搜索内容输入JTextField*/
	private JTextField searchField;
	/**当前知识分类*/
	private String typeCode;
	/**是否包含分类*/
	private JCheckBox incluedTypeCheckBox;
	
	private String incluedTypeCheckValue = Constant.YES;
	@Override
	protected JComponent getTopPane() {
		super.getTopPane();
		buttonBar.add(getViewButton());	
		return buttonBar;
	}
	private JButton getViewButton() {
		if (viewButton == null) {
			viewButton = new JButton(viewSearchAction);
		}
		return viewButton;
	}
	@Override
	protected JComponent getCenterPane() {
		JPanel topPanel = new JPanel();
		topPanel.setLayout(new BorderLayout());
		topPanel.setBorder(new EtchedBorder());
		
		JPanel searchPanel = new JPanel();
		searchPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 0,0));
		JLabel searchJLabel = new JLabel(LanguageLoader.getString("Kbs.search_searchTitle"));
		searchPanel.add(searchJLabel);
		searchField = new JTextField();
		searchField.setPreferredSize(new Dimension(300, 25));
		searchPanel.add(searchField);
		JButton searchBtn = new JButton(LanguageLoader.getString("Kbs.search_search"),ImageLoader.getImageIcon("CrawlerResource.kbsSearch"));
		searchPanel.add(searchBtn);
		searchBtn.addActionListener(new ActionListener() {  
            public void actionPerformed(ActionEvent e) {  
            	initData();
            }  
        });  
		incluedTypeCheckBox = new JCheckBox(LanguageLoader.getString("Kbs.search_check"));
		incluedTypeCheckBox.setSelected(true);
		searchPanel.add(incluedTypeCheckBox);
		incluedTypeCheckBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(incluedTypeCheckBox.isSelected()){
					incluedTypeCheckValue = Constant.YES;
				}else{
					incluedTypeCheckValue = Constant.NO;
				}
			}
		});
		topPanel.add(searchPanel, BorderLayout.NORTH);
		topPanel.add(new JScrollPane(getKnowledgePointTable()),BorderLayout.CENTER);
		return topPanel;
	}
	/* (non-Javadoc)
	 * @see org.javacoo.cowswing.core.event.CowSwingListener#update(org.javacoo.cowswing.core.event.CowSwingEvent)
	 */
	@Override
	public void update(CowSwingEvent event) {
		// TODO Auto-generated method stub
		
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
		return LanguageLoader.getString("Kbs.title");
	}

	@Override
	public void disposePage() {
		super.disposePage();
	}
	
	/**
	 * @return crawlerRuleTable
	 * */
	public JTable getKnowledgePointTable() {
		if (knowledgePointTable == null) {
			knowledgePointTable = new JTable();
			KnowledgePointTabelModel dataModel = new KnowledgePointTabelModel(
					getColumnNames());
			knowledgePointTable.setModel(dataModel);
			knowledgePointTable.setPreferredScrollableViewportSize(new Dimension(
					500, 70));
			knowledgePointTable.setFillsViewportHeight(true);

			knowledgePointTable.getSelectionModel().addListSelectionListener(
					new ListSelectionListener() {
						@Override
						public void valueChanged(ListSelectionEvent e) {
							SwingUtilities.invokeLater(new Runnable() {
								public void run() {
									if (knowledgePointTable.getSelectedRow() != -1) {
										if (knowledgePointTable.getSelectedRows().length > 1) {
											viewButton.setEnabled(false);
										} else {
											viewButton.setEnabled(true);
										}
									} else {
										viewButton.setEnabled(false);
									}
								}
							});
						}
					});
			knowledgePointTable.setAutoCreateRowSorter(true);
			// set data.
			// showData(CalendarUtils.getCurrentMonthInstance());
		}

		return knowledgePointTable;
	}
	public void showData(int startIndex,int pageSize,String... params) {
		((KnowledgePointTabelModel) getKnowledgePointTable().getModel())
				.setData(getData(startIndex, pageSize));
		final JTable table = getKnowledgePointTable();
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				ColumnResizer.adjustColumnPerferredWidths(table);
			}
		});
	}
	public void getList(String typeCode){
		this.typeCode = typeCode;
		showData(0,Constant.PAGE_LIMIT,typeCode);
	}
	public List<KnowledgePoint> getData(int startIndex,int pageSize) {
		SearchCriteria criteria = new SearchCriteria();
		criteria.setStartIndex(startIndex);
		criteria.setPageSize(pageSize);
		if(Constant.YES.equals(incluedTypeCheckValue)){
			criteria.setTypecode(this.typeCode);
		}
		criteria.setIndexDirName(KbsConstant.DEFAULT_INDEX_MODULE_DIR);
		//criteria.setModule(KbsConstant.SYSTEM_MODULE_SOURCE);
		criteria.setQueryKey(searchField.getText());
		PaginationSupport<KnowledgePoint> resultList = searchManager.find(criteria);
		paginationBar.setPaginationSupport(resultList);
		paginationBar.setListPage(this);
		paginationBar.loadData();
		return (List<KnowledgePoint>) resultList.getData();
	}
	private List<String> getColumnNames() {
		List<String> columnNames = new ArrayList<String>();
		columnNames.add(LanguageLoader.getString("Kbs.search_title"));
		columnNames.add(LanguageLoader.getString("Kbs.search_type"));
		columnNames.add(LanguageLoader.getString("Kbs.search_module"));
		columnNames.add(LanguageLoader.getString("Kbs.search_showdate"));

		return columnNames;
	}
	protected void addCrawlerListener(){
		
	}
}
