/**
 * 如石子一粒,仰高山之巍峨,但不自惭形秽.
 * 若小草一棵,慕白杨之伟岸,却不妄自菲薄.
 */
package org.javacoo.cowswing.plugin.chat.ui;

import java.awt.event.InputEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.swing.Action;
import javax.swing.JMenu;
import javax.swing.KeyStroke;

import org.javacoo.cowswing.base.loader.LanguageLoader;
import org.javacoo.cowswing.plugin.chat.ui.action.ShowKbsChatAction;
import org.javacoo.cowswing.ui.plugin.AbstractUiPlugin;
import org.javacoo.cowswing.ui.util.MenuUtil;
import org.javacoo.cowswing.ui.view.panel.IPage;
import org.springframework.stereotype.Component;

/**
 * 聊天插件
 * <p>说明:</p>
 * <li></li>
 * @author DuanYong
 * @since 2014-3-3 上午9:49:56
 * @version 1.0
 */
@Component("chat")
public class Chat extends AbstractUiPlugin{

	@Resource(name="showKbsChatAction")
	private ShowKbsChatAction showKbsChatAction;
	@Resource(name="showKbsChatAction")
	private ShowKbsChatAction showSmallKbsChatAction;
	
	protected List<JMenu> populateMenuBarList() {
		List<JMenu> menuList = new ArrayList<JMenu>();
		// 添加聊天室加速器快捷键
		KeyStroke chatItemKeyStroke = KeyStroke.getKeyStroke(new Integer('C'), InputEvent.ALT_MASK);
		// 组装
		Map<Action, KeyStroke> actionMap = new HashMap<Action, KeyStroke>();
		showSmallKbsChatAction.setSmallIcon();
		actionMap.put(showSmallKbsChatAction, chatItemKeyStroke);
		
		menuList.add(MenuUtil.createMenu(LanguageLoader.getString("Chat.title"), "K",actionMap));
		return menuList;
	}
	protected List<Action> populateToolBarList() {
		List<Action> actionList = new ArrayList<Action>();
		actionList.add(showKbsChatAction);
		return actionList;
	}
	protected int populatePluginLevel() {
		return 1;
	}
	/* (non-Javadoc)
	 * @see org.javacoo.cowswing.ui.plugin.UiPlugin#getDefaultPage()
	 */
	@Override
	public IPage getDefaultPage() {
		// TODO Auto-generated method stub
		return null;
	}

}
