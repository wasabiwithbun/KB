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

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.javacoo.cowswing.base.constant.Constant;
import org.javacoo.cowswing.base.loader.ImageLoader;
import org.javacoo.cowswing.base.loader.LanguageLoader;
import org.javacoo.cowswing.core.event.CowSwingEvent;
import org.javacoo.cowswing.core.event.type.CowSwingEventType;
import org.javacoo.cowswing.plugin.core.constant.CoreConstant;
import org.javacoo.cowswing.plugin.core.net.INetManager;
import org.javacoo.cowswing.plugin.core.net.MsgBean;
import org.javacoo.cowswing.plugin.core.net.NetManager;
import org.javacoo.cowswing.plugin.kbs.constant.KbsConstant;
import org.javacoo.cowswing.plugin.kbs.service.IKbsNetSerachService;
import org.javacoo.cowswing.plugin.kbs.service.beans.KbsModuleArticleBean;
import org.javacoo.cowswing.plugin.kbs.ui.action.ViewNetSearchAction;
import org.javacoo.cowswing.plugin.kbs.ui.model.KbsNetDataModel;
import org.javacoo.cowswing.ui.util.ColumnResizer;
import org.javacoo.cowswing.ui.view.panel.AbstractListPage;
import org.javacoo.persistence.PaginationSupport;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

/**
 * 网络分享面板
 * <p>说明:</p>
 * <li></li>
 * @author DuanYong
 * @since 2013-7-9 下午7:11:30
 * @version 1.0
 */
@Component("kbsNetSearchPanel")
public class KbsNetSearchPanel extends AbstractListPage implements ListSelectionListener{
	private static final long serialVersionUID = 1L;
	protected Logger logger = Logger.getLogger(this.getClass());
    /**网络管理服务*/
	@Resource(name="netManager")
    private NetManager netManager;
	/**网络搜索服务*/
	@Resource(name="kbsNetSearchService")
    private IKbsNetSerachService kbsNetSerachService;
	
	/**
	 * 查看按钮
	 */
	private JButton viewButton;
	/**
	 * 查看Action
	 */
	@Resource(name="viewNetSearchAction")
	private ViewNetSearchAction viewNetSearchAction;
	/**
	 * 知识点Table
	 */
	private JTable netDataTable;
	/**搜索内容输入JTextField*/
	private JTextField searchField;
	/**当前知识分类*/
	private String typeCode;
	/**搜索全部*/
	private boolean isSearchAll = true;
	@Override
	protected JComponent getTopPane() {
		super.getTopPane();
		buttonBar.add(getViewButton());	
		return buttonBar;
	}
	private JButton getViewButton() {
		if (viewButton == null) {
			viewButton = new JButton(viewNetSearchAction);
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
            	getList();
            }  
        });  
		
		topPanel.add(searchPanel, BorderLayout.NORTH);
		topPanel.add(new JScrollPane(getNetDataTable()),BorderLayout.CENTER);
		return topPanel;
	}
	/* (non-Javadoc)
	 * @see org.javacoo.cowswing.core.event.CowSwingListener#update(org.javacoo.cowswing.core.event.CowSwingEvent)
	 */
	@Override
	public void update(CowSwingEvent event) {
		if (CowSwingEventType.NetDataChangeEvent.equals(event.getEventType())) {
			initData();
		}else if(CowSwingEventType.NetDownLoadReturnEvent.equals(event.getEventType())){
			logger.info("开始接收数据");
			netManager.getServer().receiveFolder("F:\\");
		}else if(CowSwingEventType.NetViewEvent.equals(event.getEventType())){
			logger.info("查看");
			MsgBean msgBean = (MsgBean)event.getEventObject();
			String content = searchArticleData(msgBean);
			if (StringUtils.isNotBlank(content)) {
				logger.info("查询到数据并返回");
				sendViewReturnData(msgBean.getIp(),
						msgBean.getPort(), content);
			}
		}else if(CowSwingEventType.NetSearceEvent.equals(event.getEventType())){
			logger.info("搜索数据");
			MsgBean msgBean = (MsgBean)event.getEventObject();
			List<MsgBean> resultList = kbsNetSerachService.search(msgBean);
			if (!CollectionUtils.isEmpty(resultList)) {
				logger.info("搜索到结果并返回,数据大小："+resultList.size());
				sendNetSearchData(msgBean,resultList);
			}
		}
		
		
	}
	/**
	 * 查询文章数据
	 * <p>
	 * 方法说明:</>
	 * <li></li>
	 * 
	 * @author DuanYong
	 * @since 2013-8-20 下午3:20:02
	 * @version 1.0
	 * @exception
	 * @param msgBean
	 * @return
	 */
	private String searchArticleData(MsgBean msgBean) {
		KbsModuleArticleBean articleBean = new KbsModuleArticleBean();
		articleBean.setId(Integer.valueOf(msgBean.getId()));
		articleBean = kbsNetSerachService
				.getKbsModuleArticleService()
				.get(articleBean, KbsConstant.SQLMAP_ID_GET_KBS_MODULE_ARTICLE);
		return articleBean.getContent();
	}
	/**
	 * 发送返回数据
	 * <p>
	 * 方法说明:</>
	 * <li></li>
	 * 
	 * @author DuanYong
	 * @since 2013-8-20 下午3:17:23
	 * @version 1.0
	 * @exception
	 * @param ip
	 * @param port
	 * @param content
	 */
	private void sendViewReturnData(String ip, int port, String content) {
		MsgBean returnMsgBean = new MsgBean();
		returnMsgBean.setAuthor(netManager.getServer().getUserName());
		returnMsgBean.setAction(CoreConstant.NET_ACTION_TYPE_VIEW_RETURN);
		returnMsgBean.setContent(content);
		returnMsgBean.setPort(netManager.getServer().getPort());
		returnMsgBean.setIp(netManager.getServer().getIp());
		netManager.sendClientData(ip, port, returnMsgBean);
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
	public JTable getNetDataTable() {
		if (netDataTable == null) {
			netDataTable = new JTable();
			KbsNetDataModel dataModel = new KbsNetDataModel(
					getColumnNames());
			netDataTable.setModel(dataModel);
			netDataTable.setPreferredScrollableViewportSize(new Dimension(
					500, 70));
			netDataTable.setFillsViewportHeight(true);

			netDataTable.getSelectionModel().addListSelectionListener(
					new ListSelectionListener() {
						@Override
						public void valueChanged(ListSelectionEvent e) {
							SwingUtilities.invokeLater(new Runnable() {
								public void run() {
									if (netDataTable.getSelectedRow() != -1) {
										if (netDataTable.getSelectedRows().length > 1) {
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
			netDataTable.setAutoCreateRowSorter(true);
		}

		return netDataTable;
	}
	public void showData(int startIndex,int pageSize,String... params) {
		((KbsNetDataModel) getNetDataTable().getModel())
				.setData(getData(startIndex, pageSize));
		final JTable table = getNetDataTable();
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				ColumnResizer.adjustColumnPerferredWidths(table);
			}
		});
	}
	public void getList(){
		MsgBean msgBean = populateSearchBean();
		if(null != msgBean){
			netManager.sendCommonData(msgBean);
			isSearchAll = false;
		}else{
			isSearchAll = true;
		}
		showData(0,Constant.PAGE_LIMIT,typeCode);
	}
	public List<MsgBean> getData(int startIndex,int pageSize) {
		List<MsgBean> dataList = null;
		if(isSearchAll){
			dataList = netManager.getNetDataList();
		}else{
			dataList = netManager.getSearchNetDataList();
		}
		PaginationSupport<MsgBean> resultList = new PaginationSupport(dataList,dataList.size(),0,Constant.PAGE_LIMIT);
		paginationBar.setPaginationSupport(resultList);
		paginationBar.setListPage(this);
		paginationBar.loadData();
		return dataList;
	}
	/**
	 * 组装搜索信息
	 * <p>方法说明:</>
	 * <li></li>
	 * @author DuanYong
	 * @since 2013-8-19 下午2:59:44
	 * @version 1.0
	 * @exception 
	 * @return
	 */
	private MsgBean populateSearchBean() {
		if(StringUtils.isNotBlank(searchField.getText())){
			MsgBean msgBean = new MsgBean();
			msgBean.setAuthor(netManager.getServer().getUserName());
			msgBean.setTitle(searchField.getText());
			msgBean.setAction(CoreConstant.NET_ACTION_TYPE_SEARCH);
			msgBean.setPort(netManager.getServer().getPort());
			msgBean.setIp(netManager.getServer().getIp());
			return msgBean;
		}
		return null;
	}
	private List<String> getColumnNames() {
		List<String> columnNames = new ArrayList<String>();
		columnNames.add(LanguageLoader.getString("Kbs.search_title"));
		columnNames.add(LanguageLoader.getString("Kbs.search_author"));
		columnNames.add(LanguageLoader.getString("Kbs.search_ip"));
		columnNames.add(LanguageLoader.getString("Kbs.search_type"));
		columnNames.add(LanguageLoader.getString("Kbs.search_showdate"));

		return columnNames;
	}
	/**
	 * 发送网络搜索返回数据
	 * <p>方法说明:</>
	 * <li></li>
	 * @author DuanYong
	 * @since 2013-9-19 上午11:22:30
	 * @version 1.0
	 * @exception 
	 * @param targetMsgBean
	 * @param msgBeanList
	 */
	private void sendNetSearchData(MsgBean targetMsgBean,List<MsgBean> msgBeanList){
		MsgBean msgBean = new MsgBean();
		msgBean.setAction(CoreConstant.NET_ACTION_TYPE_SEARCH_RETURN);
		msgBean.setPort(netManager.getServer().getPort());
		msgBean.setIp(netManager.getServer().getIp());
		msgBean.setMsgBeanList(msgBeanList);
		netManager.sendClientData(targetMsgBean.getIp(), targetMsgBean.getPort(), msgBean);
	}
	/**
	 * @return the netManager
	 */
	public INetManager getNetManager() {
		return netManager;
	}

	
}
