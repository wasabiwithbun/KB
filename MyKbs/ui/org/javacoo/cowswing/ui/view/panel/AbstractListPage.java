package org.javacoo.cowswing.ui.view.panel;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;

import javax.annotation.Resource;
import javax.swing.JPanel;

import org.apache.log4j.Logger;
import org.javacoo.cowswing.base.constant.Constant;
import org.javacoo.cowswing.core.event.CowSwingObserver;
import org.javacoo.cowswing.main.JSplash;
import org.javacoo.cowswing.ui.util.PaginationBar;

/**
 * 抽象类
 *@author DuanYong
 *@since 2012-11-5下午3:27:54
 *@version 1.0
 */
public abstract class AbstractListPage extends JPanel implements IPage {
	protected Logger logger = Logger.getLogger(this.getClass());
	
	private static final long serialVersionUID = 1L;
    
	private boolean hasInit = false;
	
	private boolean isDefaultPage;

	
	protected JPanel buttonBar;
	

	/**系统TabPanel*/
	@Resource(name="systemTabPanel")
    private SystemTabPanel systemTabPanel;
	/**系统容器*/
	@Resource(name="pageContainer")
	protected PageContainer pageContainer;

	/**
	 * 分页状态栏
	 */
	@Resource(name="paginationBar")
	protected PaginationBar paginationBar;

	/**
	 * Layout for this AbstractPage is BorderLayout. <br/>
	 * getTopPane() -- north <br/>
	 * getCenterPane() -- center <br/>
	 * getBottomPane() -- south <br/>
	 * getLeftPane() -- west <br/>
	 * getRightPane() -- ease <br/>
	 * */
	protected AbstractListPage() {
		super();
	}
	public void init(){
		if(!hasInit){
			BorderLayout layout = new BorderLayout();
			this.setLayout(layout);

			//top pane.
			if (getTopPane() != null) {
				this.add(getTopPane(), BorderLayout.NORTH);
			}
			//center pane.
			if (getCenterPane() != null) {
				this.add(getCenterPane(), BorderLayout.CENTER);
			}
	        //bottom pane.
			if (getBottomPane() != null) {
				this.add(getBottomPane(), BorderLayout.SOUTH);
			}
			//left pane.
			if (getLeftPane() != null) {
				this.add(getLeftPane(), BorderLayout.WEST);
			}
	       //right pane.
			if (getRightPane() != null) {
				this.add(getRightPane(), BorderLayout.EAST);
			}
			//注册监听
			addCrawlerListener();
			hasInit = true;
		}
		initData();
	}
	/**
	 * 默认注册
	 * <p>方法说明:</>
	 * <li></li>
	 * @author DuanYong
	 * @since 2014-7-29 上午10:26:24
	 * @version 1.0
	 * @exception
	 */
	protected void addCrawlerListener(){
		CowSwingObserver.getInstance().addCrawlerListener(this);
	}
	/**
	 * 初始化数据
	 * <p>方法说明:</p>
	 * @auther DuanYong
	 * @since 2012-12-10 上午11:02:11
	 * @return void
	 */
	protected void initData(){
    	showData(0,Constant.PAGE_LIMIT,"");
    }
	/**
	 * @return buttonBar. layout for this pane is FlowLayout.
	 * */
	protected Component getButtonBar() {
		if (buttonBar == null) {
			buttonBar = new JPanel();
			FlowLayout layou = new FlowLayout(FlowLayout.LEADING, 3, 2);
			buttonBar.setLayout(layou);
		}
		return buttonBar;
	}

	/**
	 * @return getTopPane
	 * */
	protected Component getTopPane() {
		return getButtonBar();
	}
	
	/**
	 * @return getCenterPane
	 * */
	protected Component getCenterPane() {
		return null;
	}

	/**
	 * @return getBottomPane If is not created, not layout to this page.
	 * */
	protected Component getBottomPane() {
		return paginationBar;
	}
	
	protected Component getLeftPane() {
		return null;
	}
	
	protected Component getRightPane() {
		return null;
	}
	
	public String getPageId() {
		return this.getClass().getName();
	}

	public String getPageName() {
		return this.getClass().getSimpleName();
	}

	public void disposePage() {
		CowSwingObserver.getInstance().removeCrawlerListener(this);
	}

	@Override
	public boolean isDefaultPage() {
		return isDefaultPage;
	}

	@Override
	public void setDefaultPage(boolean bool) {
		isDefaultPage = bool;
	}
	public void showData(int pageNumber,int pageSize,String... params){
		
	}
	/**
	 * 显示系统
	 * <p>方法说明:</>
	 * <li></li>
	 * @author DuanYong
	 * @since 2013-4-30 上午10:11:27
	 * @version 1.0
	 * @exception 
	 * @param index
	 */
	public void showTabPanel(int index){
		pageContainer.show(systemTabPanel.getPageId());
		systemTabPanel.getTabbedPane().setSelectedIndex(index);
	}

}
