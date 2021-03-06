/**
 * 如石子一粒,仰高山之巍峨,但不自惭形秽.
 * 若小草一棵,慕白杨之伟岸,却不妄自菲薄.
 */
package org.javacoo.cowswing.plugin.core.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.swing.Action;
import javax.swing.ButtonGroup;
import javax.swing.JMenu;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;

import org.javacoo.cowswing.base.loader.ImageLoader;
import org.javacoo.cowswing.base.loader.LanguageLoader;
import org.javacoo.cowswing.main.CowSwingMainFrame;
import org.javacoo.cowswing.plugin.core.ui.action.AboutAction;
import org.javacoo.cowswing.plugin.core.ui.action.CoreExitAction;
import org.javacoo.cowswing.plugin.core.ui.action.ExitAction;
import org.javacoo.cowswing.plugin.core.ui.action.HelpAction;
import org.javacoo.cowswing.plugin.core.ui.action.ShowNetSettingAction;
import org.javacoo.cowswing.plugin.core.ui.action.ShowSystemConfigSettingAction;
import org.javacoo.cowswing.plugin.core.ui.action.ShowSystemTabPanelAction;
import org.javacoo.cowswing.plugin.core.ui.action.UpdateAction;
import org.javacoo.cowswing.plugin.core.ui.action.WelcomeAction;
import org.javacoo.cowswing.ui.plugin.AbstractUiPlugin;
import org.javacoo.cowswing.ui.style.ColorDefinitions;
import org.javacoo.cowswing.ui.style.LookAndFeelSelector;
import org.javacoo.cowswing.ui.util.MenuUtil;
import org.javacoo.cowswing.ui.view.navigator.ListPane;
import org.javacoo.cowswing.ui.view.navigator.NavigatorItem;
import org.javacoo.cowswing.ui.view.panel.IPage;
import org.springframework.stereotype.Component;


/**
 * 系统核心插件
 *@author DuanYong
 *@since 2013-3-6上午8:39:10
 *@version 1.0
 */
@Component("core")
public class Core extends AbstractUiPlugin{
	@Resource(name="cowSwingMainFrame")
	private CowSwingMainFrame cowSwingMainFrame;
	@Resource(name="coreExitAction")
	private CoreExitAction coreExitAction;
	@Resource(name="exitAction")
	private ExitAction exitAction;
	@Resource(name="welcomeAction")
	private WelcomeAction welcomeAction;
	@Resource(name="helpAction")
	private HelpAction helpAction;
	@Resource(name="aboutAction")
	private AboutAction aboutAction;
	@Resource(name="updateAction")
	private UpdateAction updateAction;
	@Resource(name="updateAction")
	private UpdateAction toolBarUpdateAction;

	@Resource(name="showNetSettingAction")
	private ShowNetSettingAction showNetSettingAction;
	@Resource(name="showNetSettingAction")
	private ShowNetSettingAction showSmallNetSettingAction;
	
	@Resource(name="showSystemTabPanelAction")
	private ShowSystemTabPanelAction showSystemTabPanelAction;
	@Resource(name="showSystemConfigSettingAction")
	private ShowSystemConfigSettingAction showSystemConfigSettingAction;
	
	/**文件*/
	private JMenu file;
	/**编辑*/
	private JMenu edit;
	/**样式*/
	private JMenu view;
	/**帮助*/
	private JMenu help;

	protected List<JMenu> populateMenuBarList() {
		List<JMenu> menuList = new ArrayList<JMenu>();
		menuList.add(getFile());
		menuList.add(getEdit());
		menuList.add(getView());
		menuList.add(getHelp());
		return menuList;
	}
	
	public JMenu getFile() {
		if(null == file){
			// 加速器快捷键
			KeyStroke fileExitItemKeyStroke = KeyStroke.getKeyStroke(new Integer('X'), InputEvent.CTRL_MASK);
			// 加速器快捷键
			KeyStroke fileUpdateItemKeyStroke = KeyStroke.getKeyStroke(new Integer('U'), InputEvent.CTRL_MASK);
			// 添加网络设置加速器快捷键
			KeyStroke netSettingItemKeyStroke = KeyStroke.getKeyStroke(new Integer('S'), InputEvent.ALT_MASK);
			
			// 组装
			Map<Action, KeyStroke> fileActionMap = new HashMap<Action, KeyStroke>();
			fileActionMap.put(coreExitAction,fileExitItemKeyStroke);
			updateAction.setSmallIcon();
			fileActionMap.put(updateAction,fileUpdateItemKeyStroke);
			showSmallNetSettingAction.setSmallIcon();
			fileActionMap.put(showSmallNetSettingAction, netSettingItemKeyStroke);
			file = MenuUtil.createMenu(LanguageLoader.getString("CrawlerMainFrame.File"),"F", fileActionMap);
		}
		return file;
	}
	public JMenu getEdit() {
		if(null == edit){
			edit = MenuUtil.createMenu(LanguageLoader.getString("CrawlerMainFrame.Edit"),"E",null);
		}
		return edit;
	}
	public JMenu getView() {
		if(null == view){
			view =  MenuUtil.createMenu(LanguageLoader.getString("CrawlerMainFrame.Style"),"V",null);
			// LookAndFeel
			ButtonGroup lafGroup = new ButtonGroup();
			List<String> lf = LookAndFeelSelector.getListOfSkins();
			Iterator<String> it = lf.iterator();
			while (it.hasNext()) {
				String lfName = it.next();
				JRadioButtonMenuItem item = new JRadioButtonMenuItem();
				item.setText(lfName);
				item.setName(lfName);
				
				item.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent ae) {
						JRadioButtonMenuItem rb2 = (JRadioButtonMenuItem)ae.getSource();
						LookAndFeelSelector.setLookAndFeel(rb2.getName());
						ColorDefinitions.initColors();
						SwingUtilities.updateComponentTreeUI(cowSwingMainFrame);
					}
				});
				view.add(item);
				lafGroup.add(item);
			}
		}
		return view;
	}
	public JMenu getHelp() {
		if(null == help){
			Map<Action,KeyStroke> helpActionMap = new HashMap<Action,KeyStroke>();
			helpActionMap.put(welcomeAction, null);
			helpActionMap.put(helpAction, null);
			helpActionMap.put(aboutAction, null);
			
			help = MenuUtil.createMenu(LanguageLoader.getString("CrawlerMainFrame.About"),"H",helpActionMap);
			
		}
		return help;
	}
	
	public List<NavigatorItem> populateNavigatorItemList() {
		List<NavigatorItem> navigatorList = new ArrayList<NavigatorItem>();
		navigatorList.add(new NavigatorItem(LanguageLoader
				.getString("CrawlerMainFrame.Home"), ImageLoader
				.getImageIcon("CrawlerResource.toolbarHouse"),
				getHomePanel()));
		return navigatorList;
	}
	
	private ListPane getHomePanel() {
		ListPane p = new ListPane();
		p.addItem(LanguageLoader.getString("Home.systemInfo"),ImageLoader.getImageIcon("CrawlerResource.toolbarAbout"),showSystemTabPanelAction);
		p.addItem(LanguageLoader.getString("Core.config"),ImageLoader.getImageIcon("CrawlerResource.navigatorSettingCore"),showSystemConfigSettingAction);
		p.setSize(200, 117);
		return p;
	}

	protected List<Action> populateToolBarList() {
		List<Action> actionList = new ArrayList<Action>();
		actionList.add(showNetSettingAction);
		actionList.add(toolBarUpdateAction);
		actionList.add(exitAction);
		return actionList;
	}


	protected int populatePluginLevel() {
		return 2;
	}

	/* (non-Javadoc)
	 * @see org.javacoo.cowswing.ui.plugin.UiPlugin#getDefaultPage()
	 */
	@Override
	public IPage getDefaultPage() {
		return showSystemTabPanelAction.getSystemTabPanel();
	}

}
