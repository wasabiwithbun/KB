/**
 * 如石子一粒,仰高山之巍峨,但不自惭形秽.
 * 若小草一棵,慕白杨之伟岸,却不妄自菲薄.
 */
package org.javacoo.cowswing.core.launcher;

import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.javacoo.cowswing.core.context.CowSwingContextData;
import org.javacoo.cowswing.plugin.core.constant.CoreConstant;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * ILauncher接口Spring实现类,负责加载Spring配置文件
 * 
 * @author DuanYong
 * @since 2012-11-30上午9:27:56
 * @version 1.0
 */
public class SpringLauncherImpl implements ILauncher {
	
	private static Enumeration<URL> resources;
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.javacoo.icrawler.launcher.ILauncher#launch()
	 */
	@Override
	public void launch() {
		Map<String, Map<String, String>> plugins = CowSwingContextData.getInstance().getPlugins();
		List<String> activePlugins = new ArrayList<String>();
		activePlugins.add("resources/spring/applicationContext.xml");
		String key = "";
		for(Iterator<String> it = plugins.keySet().iterator();it.hasNext();){
			key = it.next();
			if(Boolean.valueOf(plugins.get(key).get(CoreConstant.PLUGIN_PROPERTIES_KY_ACTIVE))){
				if(StringUtils.isNotBlank(plugins.get(key).get(CoreConstant.PLUGIN_PROPERTIES_KY_BEANXMLPATH))){
					activePlugins.add(plugins.get(key).get(CoreConstant.PLUGIN_PROPERTIES_KY_BEANXMLPATH));
				}
			}
		}
		int i = 0;
		final String[] contextPaths = new String[activePlugins.size()];
		for(String xmlPath : activePlugins){
			contextPaths[i] = xmlPath;
			i++;
		}
		CowSwingContextData.getInstance().setApplicationContext(new ClassPathXmlApplicationContext(contextPaths));
		
//		SwingUtilities.invokeLater(new Runnable() {
//			public void run() {
//				CowSwingContextData.getInstance().setApplicationContext(new ClassPathXmlApplicationContext(contextPaths));
//				CowSwingObserver.getInstance().notifyEvents(
//						new CowSwingEvent(this,CowSwingEventType.SystemSpringInitComplateEvent));
//			}
//		});
		
	}
	
    
}
