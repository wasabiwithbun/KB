package org.javacoo.cowswing.ui.view.bar;


import java.util.List;

import javax.swing.Action;
import javax.swing.JToolBar;

import org.apache.commons.collections.CollectionUtils;
import org.javacoo.cowswing.base.loader.LanguageLoader;
import org.springframework.stereotype.Component;

/**
 * 工具栏
 *@author DuanYong
 *@since 2012-11-4下午4:24:32
 *@version 1.0
 */
@Component("appToolBar")
public class AppToolBar extends JToolBar{
	private static final long serialVersionUID = 1L;
	
	
	public AppToolBar() {
		super(LanguageLoader.getString("CrawlerMainFrame.Tool"));
	}

	
	/**
	 * 加载插件工具栏
	 * <p>方法说明:</p>
	 * @auther DuanYong
	 * @since 2012-12-13 下午5:16:22
	 * @param toolBarList
	 * @return void
	 */
	public void loadPluginToolBar(List<Action> toolBarList){
		if(CollectionUtils.isNotEmpty(toolBarList)){
			for(Action action : toolBarList){
				this.add(action);
			}
			addSeparator();
		}
	}

}
