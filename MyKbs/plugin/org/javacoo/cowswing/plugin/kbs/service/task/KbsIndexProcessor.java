/**
 * 如石子一粒,仰高山之巍峨,但不自惭形秽.
 * 若小草一棵,慕白杨之伟岸,却不妄自菲薄.
 */
package org.javacoo.cowswing.plugin.kbs.service.task;

import org.apache.log4j.Logger;
import org.javacoo.cowswing.core.task.data.Task;
import org.javacoo.cowswing.core.task.processor.AbstractProcessor;
import org.javacoo.cowswing.plugin.kbs.domain.KnowledgePoint;
import org.javacoo.cowswing.plugin.kbs.lucene.index.Index;


/**
 * 索引创建处理
 * <p>说明:</p>
 * <li></li>
 * @author DuanYong
 * @since 2014-7-24 下午2:07:32
 * @version 1.0
 */
public class KbsIndexProcessor extends AbstractProcessor<KnowledgePoint>{
	protected Logger logger = Logger.getLogger(this.getClass());
	/**索引管理*/
    private Index index;
    /**当前任务对象*/
    private Task<KnowledgePoint> curTask;
	public KbsIndexProcessor(Index index) {
		super();
		this.index = index;
	}
	/* (non-Javadoc)
	 * @see org.javacoo.cowswing.core.task.processor.AbstractProcessor#innerProcess(org.javacoo.cowswing.core.task.data.Task)
	 */
	@Override
	protected void innerProcess(Task<KnowledgePoint> task) {
		try {
			this.curTask = task;
			index.buildIndex(task.getTaskObj(),false);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("建立资源索引失败："+e.getMessage());
		}
	}
	protected  String logInfo(){
		return "进度："+this.curTask.getTaskNum() + "/"+this.curTask.getCurrTaskTotalNum()+"---文件："+this.curTask.getTaskObj().getFilePath()+"，建立索引完成，耗时："+(endIndex - startIndex)+"毫秒";
	}
}
