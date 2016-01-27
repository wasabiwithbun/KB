/**
 * 如石子一粒,仰高山之巍峨,但不自惭形秽.
 * 若小草一棵,慕白杨之伟岸,却不妄自菲薄.
 */
package org.javacoo.cowswing.plugin.kbs.service;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import net.contentobjects.jnotify.JNotify;
import net.contentobjects.jnotify.JNotifyException;
import net.contentobjects.jnotify.JNotifyListener;

import org.apache.log4j.Logger;
import org.javacoo.cowswing.base.constant.Constant;
import org.javacoo.cowswing.base.utils.JsonUtils;
import org.javacoo.cowswing.core.cache.UserCacheManager;
import org.javacoo.cowswing.core.init.InitService;
import org.javacoo.cowswing.core.task.TaskManagerService;
import org.javacoo.cowswing.plugin.kbs.constant.KbsConstant;
import org.javacoo.cowswing.plugin.kbs.domain.KnowledgePoint;
import org.javacoo.cowswing.plugin.kbs.lucene.index.Index;
import org.javacoo.cowswing.plugin.kbs.lucene.index.IndexConfigration;
import org.javacoo.cowswing.plugin.kbs.utils.IndexHelper;
import org.javacoo.cowswing.ui.view.panel.PageContainer;
import org.javacoo.cowswing.ui.view.panel.SystemTabPanel;
import org.springframework.stereotype.Component;

/**
 * 本地文件索引服务
 * <p>说明:</p>
 * <li></li>
 * @author DuanYong
 * @since 2014-7-18 下午4:38:18
 * @version 1.0
 */
@Component("localFileIndexService")
public class LocalFileIndexService implements InitService{

	protected Logger logger = Logger.getLogger(this.getClass());
	/**索引管理*/
	@Resource(name="indexManager")
    private Index index;
	/**索引任务管理*/
	@Resource(name="taskManagerService")
    private TaskManagerService<KnowledgePoint> taskManagerService;
	/**系统TabPanel*/
	@Resource(name="systemTabPanel")
    private SystemTabPanel systemTabPanel;
	/**系统容器*/
	@Resource(name="pageContainer")
    private PageContainer pageContainer;
	/** 索引配置参数 */
	private IndexConfigration indexConfigration;
	/** 用户缓存管理类 */
	protected static UserCacheManager userCacheManager = UserCacheManager.getInstance();
	/** 目录监听MAP */
	private Map<String,Integer> listeners = new HashMap<String,Integer>();
	/** 监听事件*/
	private int mask = JNotify.FILE_CREATED | JNotify.FILE_DELETED | JNotify.FILE_MODIFIED | JNotify.FILE_RENAMED;  
	/** 是否监听子目录 */
	private boolean watchSubtree = true;  
	/* (non-Javadoc)
	 * @see org.javacoo.cowswing.core.init.InitService#init()
	 */
	@Override
	public void init() {
		startListener();
	}
	/**
	 * 启动监听
	 * <p>方法说明:</>
	 * <li></li>
	 * @author DuanYong
	 * @since 2014-7-22 下午6:13:37
	 * @version 1.0
	 * @exception
	 */
	private void startListener(){
		String listenerDirs = userCacheManager.getValue(KbsConstant.KBS_LOCAL_FILE_PROPERTY_KEY_LISTENER_DIRS);
		Object obj = JsonUtils.formatStringToObject(listenerDirs, Map.class);
		Map<String,Map<String,String>> dirSetingMap = null;
		if(null != obj){
			dirSetingMap = (Map<String, Map<String, String>>)obj;
			for(String dir : dirSetingMap.keySet()){
				addListener(dir,false);
			}
		}
	}
	/**
	 * 添加监听目录
	 * <p>方法说明:</>
	 * <li></li>
	 * @author DuanYong
	 * @since 2014-7-22 下午6:12:48
	 * @version 1.0
	 * @exception 
	 * @param dir 监听目录
	 * @param index 是否建立索引
	 */
	public void addListener(String dir,boolean createIndex){
		String dirHashCode = String.valueOf(dir.hashCode());
		if(!listeners.containsKey(dirHashCode)){
			try {
				int watchID = JNotify.addWatch(dir, mask, watchSubtree, new Listener());
				listeners.put(dirHashCode, watchID);
				logger.info("添加监听目录："+dir);
				if(createIndex){
					createIndex(dir);
				}
			} catch (JNotifyException e) {
				e.printStackTrace();
			}  
		}
	}
	/**
	 * 删除监听目录
	 * <p>方法说明:</>
	 * <li></li>
	 * @author DuanYong
	 * @since 2014-7-22 下午6:13:03
	 * @version 1.0
	 * @exception 
	 * @param dir 监听目录
	 * @param index 是否删除索引
	 */
	public void removeListener(String dir,boolean index){
		String dirHashCode = String.valueOf(dir.hashCode());
		if(listeners.containsKey(dirHashCode)){
			try {
				boolean res = JNotify.removeWatch(listeners.get(dirHashCode));
				if (!res) {  
					logger.error("删除目录："+dir+"监听失败!");
			    }  
				listeners.remove(dirHashCode);
				logger.info("删除监听目录："+dir);
				if(index){
					deleteIndex(dir);
				}
			} catch (JNotifyException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}  
		}
	}
	/**
	 * 创建索引
	 * <p>方法说明:</>
	 * <li></li>
	 * @author DuanYong
	 * @since 2014-7-19 下午6:59:00
	 * @version 1.0
	 * @exception
	 */
	private void createIndex(String rootDir){
		if(!Constant.TASK_STATE_ORIGINAL.equals(taskManagerService.getState())){
			return;
		}
		showTabPanel(2);
		KnowledgePoint kp = new KnowledgePoint();
		kp.setFilePath(rootDir);
		taskManagerService.start(kp);
	}
	/**
	 * 删除索引
	 * <p>方法说明:</>
	 * <li></li>
	 * @author DuanYong
	 * @since 2014-7-22 下午6:48:55
	 * @version 1.0
	 * @exception 
	 * @param id
	 */
	private void deleteIndex(String rootDir){
		KnowledgePoint knowledgePoint = new KnowledgePoint();
		//knowledgePoint.setId(id);
		knowledgePoint.setFileDirs(String.valueOf(rootDir.hashCode()));
		knowledgePoint.setIndexDirName(KbsConstant.DEFAULT_INDEX_MODULE_DIR);
		knowledgePoint.setModule(KbsConstant.SYSTEM_MODULE_SOURCE);
		try {
			index.deleteIndex(knowledgePoint);
		} catch (Exception e) {
			e.printStackTrace();
		}
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
		pageContainer.addPage(systemTabPanel, systemTabPanel.getPageId());
		//pageContainer.show(systemTabPanel.getPageId());
		systemTabPanel.getTabbedPane().setSelectedIndex(index);
	}
	/**
	 * 目录监听类
	 * 
	 * <p>说明:</p>
	 * <li></li>
	 * @author DuanYong
	 * @since 2014-7-22 下午6:14:14
	 * @version 1.0
	 */
	class Listener implements JNotifyListener {  
		
	    public void fileRenamed(int wd, String rootPath, String oldName,  
	            String newName) {  
	    	logger.info("重命名文件： " + rootPath + " : " + oldName + " -> " + newName);  
	    	//先删除原来索引
	    	String path = rootPath+Constant.SYSTEM_SEPARATOR+oldName;
			try {
				index.deleteIndex(IndexHelper.createDeleteKnowledgePoint(path));
			} catch (Exception e) {
				e.printStackTrace();
			}
	    }  

	    public void fileModified(int wd, String rootPath, String name) {  
	    	logger.info("modified " + rootPath + " : " +"hashcode:"+rootPath.hashCode()+",path="+ name);  
	    }  

	    public void fileDeleted(int wd, String rootPath, String name) {  
	    	logger.info("删除文件： " + rootPath + " : " + name); 
	        String path = rootPath+Constant.SYSTEM_SEPARATOR+name;
			try {
				index.deleteIndex(IndexHelper.createDeleteKnowledgePoint(path));
			} catch (Exception e) {
				e.printStackTrace();
			}
	    }  

	    public void fileCreated(int wd, String rootPath, String name) {
	    	try {
	    		logger.info("发现新文件 "+rootPath + " : " + name+",1秒钟后建立索引");  
				Thread.sleep(1000);
				File f = new File(rootPath+Constant.SYSTEM_SEPARATOR+name);
				index.buildIndex(IndexHelper.createKnowledgePoint(f ,rootPath,(f.length() > indexConfigration.getMaxFileSize() ? true:false)), true);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
	        
	    }  

	    void print(String msg) {  
	        System.err.println(msg);  
	    }  

	}  
	
}

