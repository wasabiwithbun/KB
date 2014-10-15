/**
 * 如石子一粒,仰高山之巍峨,但不自惭形秽.
 * 若小草一棵,慕白杨之伟岸,却不妄自菲薄.
 */
package org.javacoo.cowswing.plugin.kbs.service.task;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.javacoo.cowswing.base.constant.Constant;
import org.javacoo.cowswing.core.task.TaskManagerService;
import org.javacoo.cowswing.core.task.frontier.Frontier;
import org.javacoo.cowswing.core.task.processor.ProcessorChain;
import org.javacoo.cowswing.core.task.processor.ProcessorChainList;
import org.javacoo.cowswing.core.task.thread.ProcessorManager;
import org.javacoo.cowswing.plugin.kbs.lucene.index.Index;
import org.javacoo.cowswing.plugin.kbs.lucene.index.IndexConfigration;
import org.springframework.stereotype.Component;

/**
 * 知识库索引任务控制器对象
 * <p>
 * 说明:
 * </p>
 * <li></li>
 * 
 * @author DuanYong
 * @since 2014-7-24 上午11:02:58
 * @version 1.0
 */
@Component("taskManagerService")
@SuppressWarnings("rawtypes")
public class KbsIndexTaskManagerServiceImpl<KnowledgePoint> implements
		TaskManagerService<KnowledgePoint> {
	protected Logger logger = Logger.getLogger(this.getClass());
	/**索引管理*/
	@Resource(name="indexManager")
    private Index index;
	/** 索引配置参数 */
	@Resource(name = "indexConfigration")
	private IndexConfigration indexConfigration;
	/** 任务边界控制器 */
	private transient Frontier frontier;
	/** 任务线程控制器 */
	private transient ProcessorManager processorManager;
	/** 处理器链 */
	private transient ProcessorChainList processorChainList;
	/** 任务状态：初始状态,准备就绪,运行中,暂停 */
	private transient String state = Constant.TASK_STATE_ORIGINAL;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.javacoo.cowswing.core.task.TaskManagerService#getFirstProcessorChain
	 * ()
	 */
	@Override
	public ProcessorChain<KnowledgePoint> getFirstProcessorChain() {
		return processorChainList.getFirstChain();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.javacoo.cowswing.core.task.TaskManagerService#getState()
	 */
	@Override
	public String getState() {
		return state;
	}
    
	/**
	 * @param state the state to set
	 */
	public void setState(String state) {
		this.state = state;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.javacoo.cowswing.core.task.TaskManagerService#getFrontier()
	 */
	@Override
	public Frontier<KnowledgePoint> getFrontier() {
		return frontier;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.javacoo.cowswing.core.task.TaskManagerService#getSleepTime()
	 */
	@Override
	public long getSleepTime() {
		return 200;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.javacoo.cowswing.core.task.TaskManagerService#getProcessorManager()
	 */
	@Override
	public ProcessorManager getProcessorManager() {
		return processorManager;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.javacoo.cowswing.core.task.TaskManagerService#shutdown()
	 */
	@Override
	public void shutdown() {
		this.destory();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.javacoo.cowswing.core.task.TaskManagerService#start(java.lang.Object)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void start(KnowledgePoint t) {
		this.state = Constant.TASK_STATE_RUNNING;
		frontier = new KbsIndexFrontier(indexConfigration);
		logger.info("=====================初始化任务线程控制器=========");
		this.processorManager = new ProcessorManager(this);
		new Thread(processorManager).start();
		frontier.initialize(this,t);
		logger.info("=====================初始化任务处理器链=========");
		if (null == processorChainList) {
			processorChainList = new ProcessorChainList<KnowledgePoint>();
			processorChainList.addProcessorMap("createIndex", new KbsIndexProcessor(index));
		}
		logger.info("=====================初始化任务状态=========");
		
	}
	private void destory(){
		//保存内存中剩余索引
		index.buildIndex(null,true);
		this.frontier.destory();
		this.frontier = null;
		this.processorChainList = null;
		this.state = Constant.TASK_STATE_ORIGINAL;
	}
}
