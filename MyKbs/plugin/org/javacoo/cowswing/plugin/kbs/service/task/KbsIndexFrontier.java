/**
 * 如石子一粒,仰高山之巍峨,但不自惭形秽.
 * 若小草一棵,慕白杨之伟岸,却不妄自菲薄.
 */
package org.javacoo.cowswing.plugin.kbs.service.task;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.javacoo.cowswing.base.constant.Constant;
import org.javacoo.cowswing.base.utils.FileUtils;
import org.javacoo.cowswing.base.utils.JsonUtils;
import org.javacoo.cowswing.core.cache.UserCacheManager;
import org.javacoo.cowswing.core.task.TaskManagerService;
import org.javacoo.cowswing.core.task.data.Task;
import org.javacoo.cowswing.core.task.data.queue.SimpleTaskQueue;
import org.javacoo.cowswing.core.task.data.queue.TaskQueue;
import org.javacoo.cowswing.core.task.frontier.Frontier;
import org.javacoo.cowswing.plugin.kbs.constant.KbsConstant;
import org.javacoo.cowswing.plugin.kbs.domain.KnowledgePoint;
import org.javacoo.cowswing.plugin.kbs.lucene.index.IndexConfigration;
import org.javacoo.cowswing.plugin.kbs.utils.IndexHelper;

/**
 * 
 * <p>说明:</p>
 * <li></li>
 * @author DuanYong
 * @since 2014-7-24 上午11:05:54
 * @version 1.0
 */
public class KbsIndexFrontier implements Frontier<KnowledgePoint>{
	private static Log log =  LogFactory.getLog(KbsIndexFrontier.class);
	/** 用户缓存管理类 */
	protected static UserCacheManager userCacheManager = UserCacheManager.getInstance();
	/**任务控制器*/
	private TaskManagerService<KnowledgePoint> controller;
	/** 索引配置参数 */
	private IndexConfigration indexConfigration;
	/**计划KnowledgePointQueue对象*/
	private TaskQueue<KnowledgePoint> planUrlQueue = new KnowledgePointQueue();
	/**任务对象*/
	private TaskQueue<Task<KnowledgePoint>> taskQueue = new SimpleTaskQueue<KnowledgePoint>();
	/**任务总数*/
    private transient int taskSize = 0;
    /**目录设置*/
    private Map<String,Map<String,String>> dirSetingMap = new HashMap<String,Map<String,String>>();

    public KbsIndexFrontier(IndexConfigration indexConfigration){
		 this.indexConfigration = indexConfigration;
    }
	/* (non-Javadoc)
	 * @see org.javacoo.cowswing.core.task.frontier.Frontier#initialize(org.javacoo.cowswing.core.task.TaskManagerService)
	 */
	@Override
	public void initialize(TaskManagerService<KnowledgePoint> s,KnowledgePoint root) {
		 this.controller = s;
		 getListenerDirsMap();
		 getTotal(new File(root.getFilePath()),root.getFilePath());
		 //启动任务工厂
		 new Thread(new TaskFactory(root)).start();
	}

	/**
	 * 任务对象入队列
	 * @param map 任务对象
	 */
	public void addTask(Task<KnowledgePoint> task){
		taskQueue.addUnExecTask(task);
	}
	/* (non-Javadoc)
	 * @see org.javacoo.cowswing.core.task.frontier.Frontier#next()
	 */
	@Override
	public Task<KnowledgePoint> next() {
		return getTask();
	}
	/**
	 * 任务对象出队列
	 * @param urlQueue 当前线程的队列
	 * @return 任务对象
	 */
	private Task<KnowledgePoint> getTask(){
		return taskQueue.unExecTaskDeQueue();
	}
	/* (non-Javadoc)
	 * @see org.javacoo.cowswing.core.task.frontier.Frontier#isEmpty()
	 */
	@Override
	public boolean isEmpty() {
		return taskIsEmpty();
	}
	/**
	 *  判断当前对象是否为空
	 * @param urlQueue 当前线程的队列
	 * @return true/flase
	 */
	private boolean taskIsEmpty(){
		return taskQueue.isEmpty();
	}
	/* (non-Javadoc)
	 * @see org.javacoo.cowswing.core.task.frontier.Frontier#finished(org.javacoo.cowswing.core.task.data.Task)
	 */
	@Override
	public void finished(Task<KnowledgePoint> task) {
		if(null != task){
			task.finished();
			task = null;
		}
	}

	/* (non-Javadoc)
	 * @see org.javacoo.cowswing.core.task.frontier.Frontier#getTaskSize()
	 */
	@Override
	public int getTaskSize() {
		return taskSize;
	}

	/* (non-Javadoc)
	 * @see org.javacoo.cowswing.core.task.frontier.Frontier#getUnExecTaskNum()
	 */
	@Override
	public int getUnExecTaskNum() {
		return taskQueue.getUnExecTaskNum();
	}

	/* (non-Javadoc)
	 * @see org.javacoo.cowswing.core.task.frontier.Frontier#getExecTaskNum()
	 */
	@Override
	public int getExecTaskNum() {
		return taskQueue.getExecTaskNum();
	}

	/* (non-Javadoc)
	 * @see org.javacoo.cowswing.core.task.frontier.Frontier#destory()
	 */
	@Override
	public void destory() {
		taskQueue.clear();
		planUrlQueue.clear();
		controller = null;
		taskSize = 0;
	}
	private void getTotal(File dir,String rootDir){
		File[] children = dir.listFiles();
		File tempFile = null;
		for (int i = 0; i < children.length; ++i){
			tempFile = children[i];
			if(tempFile.isDirectory()){
				if(!hasIndex(tempFile.getPath())){
					getTotal(tempFile,rootDir);
				}
			}else{
				if(tempFile.isHidden() || !tempFile.canRead()){
					log.info("过滤文件,大小:"+FileUtils.FormetFileSize(tempFile.length())+"-路径："+tempFile.getPath());
					continue;
				}
				if(!containsFileType(rootDir,FilenameUtils.getExtension(tempFile.getPath()).toLowerCase())){
					continue;
				}
				taskSize ++;
			}
		}
	}
	/**
	 * 是否包含此文件类型
	 * <p>方法说明:</>
	 * <li></li>
	 * @author DuanYong
	 * @since 2014-10-14 下午3:39:11
	 * @version 1.0
	 * @exception 
	 * @param rootDir
	 * @param fileType
	 * @return
	 */
    private boolean containsFileType(String rootDir,String fileType){
		if(!dirSetingMap.isEmpty()){
			if(dirSetingMap.containsKey(rootDir) && null != dirSetingMap.get(rootDir)){
				if(dirSetingMap.get(rootDir).get(KbsConstant.KBS_LOCAL_SEETING_SURR_DIYFILETYPE_KEY).contains(fileType)){
					return true;
				}
			}
		}
		return false;
    }
    /**
     * 取得文件大小限制
     * <p>方法说明:</>
     * <li></li>
     * @author DuanYong
     * @since 2014-10-14 下午5:01:13
     * @version 1.0
     * @exception 
     * @param rootDir
     * @return
     */
    private long getMaxFileSize(String rootDir){
    	if(!dirSetingMap.isEmpty()){
			if(dirSetingMap.containsKey(rootDir) && null != dirSetingMap.get(rootDir)){
				try{
					return Long.valueOf(dirSetingMap.get(rootDir).get(KbsConstant.KBS_LOCAL_SEETING_SURR_MAXFILESIZE_KEY))* 1024 * 1024;
				}catch(Exception e){
					return indexConfigration.getMaxFileSize();
				}
			}
		}
		return indexConfigration.getMaxFileSize();
    }
    /**
     * 检查改目录是否索引
     * <p>方法说明:</>
     * <li></li>
     * @author DuanYong
     * @since 2014-10-14 下午5:03:19
     * @version 1.0
     * @exception 
     * @param curDir
     * @return
     */
    private boolean hasIndex(String curDir){
    	if(!dirSetingMap.isEmpty()){
			return dirSetingMap.containsKey(curDir);
		}
		return false;
    }
    /**
     * 取得监听目录参数配置
     * <p>方法说明:</>
     * <li></li>
     * @author DuanYong
     * @since 2014-10-14 下午5:18:36
     * @version 1.0
     * @exception 
     * @return
     */
    private Map<String,Map<String,String>> getListenerDirsMap(){
		Object obj = JsonUtils.formatStringToObject(userCacheManager.getValue(KbsConstant.KBS_LOCAL_FILE_PROPERTY_KEY_LISTENER_DIRS), Map.class);
		if(null != obj){
			dirSetingMap = (Map<String, Map<String, String>>)obj;
			return dirSetingMap;
		}
		return dirSetingMap;
    }
    
	/**
	 * 任务构建工厂
	 * 
	 * <p>说明:</p>
	 * <li></li>
	 * @author DuanYong
	 * @since 2014-7-25 下午2:32:23
	 * @version 1.0
	 */
	class TaskFactory implements Runnable{
	    private Task<KnowledgePoint> task;
	    private KnowledgePoint rootKnowledgePoint;
	    private int taskNum = 0;
	    public TaskFactory(KnowledgePoint rootKnowledgePoint){
	    	this.rootKnowledgePoint = rootKnowledgePoint;
	    }
		/* (non-Javadoc)
		 * @see java.lang.Runnable#run()
		 */
		@Override
		public void run() {
			//int taskNum = 0;
			scanDirectory(new File(this.rootKnowledgePoint.getFilePath()),this.rootKnowledgePoint.getFilePath());
			controller.setState(Constant.TASK_STATE_PAUSE);
		}
		/**
		 * 扫描目录
		 * <p>方法说明:</>
		 * <li></li>
		 * @author DuanYong
		 * @since 2014-7-26 下午1:49:06
		 * @version 1.0
		 * @exception 
		 * @param dir 待扫描的目录
		 * @param rootDir 目录名称
		 * @param taskNum 数量
		 */
		private void scanDirectory(File dir,String rootDir){
			File[] children = dir.listFiles();
			File tempFile = null;
			boolean isBigFile = false;
			for (int i = 0; i < children.length; ++i){
				tempFile = children[i];
				isBigFile = false;
				if(tempFile.isDirectory()){
					if(!hasIndex(tempFile.getPath())){
						scanDirectory(tempFile,rootDir);
					}
				}else{
					if(tempFile.isHidden() || !tempFile.canRead()){
						log.info("过滤文件,大小:"+FileUtils.FormetFileSize(tempFile.length())+"-路径："+tempFile.getPath());
						continue;
					}
					if(tempFile.length() > getMaxFileSize(rootDir)){
						isBigFile = true;
					}
					if(!containsFileType(rootDir,FilenameUtils.getExtension(tempFile.getPath()).toLowerCase())){
						continue;
					}
					task = createTask(task,IndexHelper.createKnowledgePoint(tempFile,rootDir,isBigFile));
					addTask(task);
					log.info("加入队列,大小："+FileUtils.FormetFileSize(tempFile.length())+"-路径："+tempFile.getPath());
					log.info("任务数量："+taskNum);
					try {
						Thread.sleep(controller.getSleepTime());
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}
		
		/**
		 * 创建任务
		 * <p>方法说明:</>
		 * <li></li>
		 * @author DuanYong
		 * @since 2014-7-26 下午1:46:02
		 * @version 1.0
		 * @exception 
		 * @param task 任务对象
		 * @param point 知识点
		 * @param taskNum 任务数量
		 * @return 新任务
		 */
		private Task<KnowledgePoint> createTask(Task<KnowledgePoint> task,KnowledgePoint point){
			task = new Task<KnowledgePoint>();
			task.setController(controller);
			task.setTaskNum(taskNum++);
			task.setTaskObj(point);
			task.setCurrTaskTotalNum(taskSize);
			return task;
		}
		
		
	}


}
