/**
 * 如石子一粒,仰高山之巍峨,但不自惭形秽.
 * 若小草一棵,慕白杨之伟岸,却不妄自菲薄.
 */
package org.javacoo.cowswing.plugin.kbs.ui;

import java.awt.event.InputEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.swing.Action;
import javax.swing.JMenu;
import javax.swing.KeyStroke;

import org.javacoo.cowswing.base.loader.ImageLoader;
import org.javacoo.cowswing.base.loader.LanguageLoader;
import org.javacoo.cowswing.plugin.kbs.ui.action.MenuShowSearchAction;
import org.javacoo.cowswing.plugin.kbs.ui.action.ShowAttributeManageAction;
import org.javacoo.cowswing.plugin.kbs.ui.action.ShowDicSettingAction;
import org.javacoo.cowswing.plugin.kbs.ui.action.ShowIndexManageAction;
import org.javacoo.cowswing.plugin.kbs.ui.action.ShowSearchAction;
import org.javacoo.cowswing.plugin.kbs.ui.view.panel.KbsSearchPanel;
import org.javacoo.cowswing.plugin.kbs.ui.view.panel.LocalTreeSearchPanel;
import org.javacoo.cowswing.plugin.kbs.ui.view.panel.TypeTreePanel;
import org.javacoo.cowswing.plugin.kbs.ui.view.panel.TypeTreeSearchPanel;
import org.javacoo.cowswing.ui.plugin.AbstractUiPlugin;
import org.javacoo.cowswing.ui.util.MenuUtil;
import org.javacoo.cowswing.ui.view.navigator.ListPane;
import org.javacoo.cowswing.ui.view.navigator.NavigatorItem;
import org.javacoo.cowswing.ui.view.panel.IPage;
import org.springframework.stereotype.Component;

/**
 * 知识库插件
 * <p>说明:</p>
 * <li></li>
 * @author DuanYong
 * @since 2013-7-2 下午3:18:18
 * @version 1.0
 */
@Component("kbs")
public class Kbs extends AbstractUiPlugin{
	@Resource(name="showSearchAction")
	private ShowSearchAction showSearchAction;
	@Resource(name="menuShowSearchAction")
	private MenuShowSearchAction menuShowSearchAction;
	@Resource(name="showDicSettingAction")
	private ShowDicSettingAction showDicSettingAction;
	@Resource(name="showIndexManageAction")
	private ShowIndexManageAction showIndexManageAction;
	@Resource(name="showAttributeManageAction")
	private ShowAttributeManageAction showAttributeManageAction;
	
	
	@Resource(name="typeTreeSearchPanel")
	private TypeTreeSearchPanel typeTreeSearchPanel;
	@Resource(name="localTreeSearchPanel")
	private LocalTreeSearchPanel localTreeSearchPanel;
	@Resource(name="typeTreePanel")
	private TypeTreePanel typeTreePanel;
	@Resource(name="kbsSearchPanel")
    private KbsSearchPanel kbsSearchPanel;
	
	
	protected List<JMenu> populateMenuBarList() {
		List<JMenu> menuList = new ArrayList<JMenu>();
		// 搜索加速器快捷键
		KeyStroke searchItemKeyStroke = KeyStroke.getKeyStroke(new Integer('Q'), InputEvent.ALT_MASK);
		// 添加聊天室加速器快捷键
		KeyStroke chatItemKeyStroke = KeyStroke.getKeyStroke(new Integer('C'), InputEvent.ALT_MASK);
		// 添加词典管理加速器快捷键
		KeyStroke dicItemKeyStroke = KeyStroke.getKeyStroke(new Integer('D'), InputEvent.ALT_MASK);
		// 添加索引管理加速器快捷键
		KeyStroke indexItemKeyStroke = KeyStroke.getKeyStroke(new Integer('I'), InputEvent.ALT_MASK);
		// 添加属性管理加速器快捷键
		KeyStroke attributeItemKeyStroke = KeyStroke.getKeyStroke(new Integer('A'), InputEvent.ALT_MASK);
		// 组装
		Map<Action, KeyStroke> actionMap = new HashMap<Action, KeyStroke>();
		actionMap.put(menuShowSearchAction, searchItemKeyStroke);
		actionMap.put(showDicSettingAction, dicItemKeyStroke);
		actionMap.put(showIndexManageAction, indexItemKeyStroke);
		actionMap.put(showAttributeManageAction, attributeItemKeyStroke);
		
		
		menuList.add(MenuUtil.createMenu(LanguageLoader.getString("Kbs.title"), "K",actionMap));
		return menuList;
	}
	protected List<Action> populateToolBarList() {
		List<Action> actionList = new ArrayList<Action>();
		actionList.add(showSearchAction);
		return actionList;
	}
	
	protected List<NavigatorItem> populateNavigatorItemList() {
		List<NavigatorItem> navigatorList = new ArrayList<NavigatorItem>();
		navigatorList.add(new NavigatorItem(LanguageLoader.getString("Kbs.search"), ImageLoader.getImageIcon("CrawlerResource.kbsSearch"),getFileFolderPane()));
		navigatorList.add(new NavigatorItem(LanguageLoader.getString("Kbs.local"), ImageLoader.getImageIcon("CrawlerResource.kbs_type_root"),getLocalFileFolderPane()));
		navigatorList.add(new NavigatorItem(LanguageLoader.getString("Kbs.manager_title"), ImageLoader.getImageIcon("CrawlerResource.kbs"),getManageFileFolderPane()));
		navigatorList.add(new NavigatorItem(LanguageLoader.getString("Kbs.setting_title"), ImageLoader.getImageIcon("CrawlerResource.navigatorSetting"),getSettingFolderPane()));
		return navigatorList;
	}
	
	private ListPane getFileFolderPane() {
		ListPane p = new ListPane();
		p.addItem(typeTreeSearchPanel);
		typeTreeSearchPanel.init();
		p.setSize(185, 86);
		return p;
	}
	private ListPane getLocalFileFolderPane() {
		ListPane p = new ListPane();
		p.addItem(localTreeSearchPanel);
		localTreeSearchPanel.init();
		p.setSize(185, 86);
		return p;
	}
	private ListPane getManageFileFolderPane() {
		ListPane p = new ListPane();
		p.addItem(typeTreePanel);
		typeTreePanel.init();
		p.setSize(185, 86);
		return p;
	}
	private ListPane getSettingFolderPane() {
		ListPane p = new ListPane();
		p.addItem(LanguageLoader.getString("Kbs.dic_manage"),ImageLoader.getImageIcon("CrawlerResource.kbs_dic_manage"),showDicSettingAction);
		p.addItem(LanguageLoader.getString("Kbs.index_manage"),ImageLoader.getImageIcon("CrawlerResource.kbs_index_manage"),showIndexManageAction);
		p.addItem(LanguageLoader.getString("Kbs.attribute_manage"),ImageLoader.getImageIcon("CrawlerResource.kbs_attribute"),showAttributeManageAction);
		p.setSize(185, 86);
		return p;
	}

	protected int populatePluginLevel() {
		return 0;
	}
	/* (non-Javadoc)
	 * @see org.javacoo.cowswing.ui.plugin.UiPlugin#getDefaultPage()
	 */
	@Override
	public IPage getDefaultPage() {
		// TODO Auto-generated method stub
		return kbsSearchPanel;
	}
}
