package org.javacoo.cowswing.plugin.kbs.service.task;

import java.util.HashSet;
import java.util.Set;

import org.javacoo.cowswing.core.task.data.queue.BlockingQueue;
import org.javacoo.cowswing.core.task.data.queue.Queue;
import org.javacoo.cowswing.core.task.data.queue.TaskQueue;
import org.javacoo.cowswing.plugin.kbs.domain.KnowledgePoint;


/**
 * 知识队列
 * 
 * <p>说明:</p>
 * <li></li>
 * @author DuanYong
 * @since 2014-7-24 上午11:27:10
 * @version 1.0
 */
public class KnowledgePointQueue implements TaskQueue<KnowledgePoint>{
	/**知识集合*/
	private Set<KnowledgePoint> allUrl = new HashSet<KnowledgePoint>();
	/**已索引知识集合*/
	private Set<KnowledgePoint> visitedUrl = new HashSet<KnowledgePoint>();
	/**待索引知识集合*/
	private Queue<KnowledgePoint> unVisitedUrl = new BlockingQueue<KnowledgePoint>();

	/**
	 * 判断是否为空
	 * @return
	 */
	public boolean isEmpty(){
		return unVisitedUrl.isEmpty();
	}
	/**
	 * 清空URL队列
	 */
	public void clear(){
		unVisitedUrl.clear();
		visitedUrl.clear();
	}
	public void addExecTask(KnowledgePoint t) {
		visitedUrl.add(t);
	}
	public void addAllURI(KnowledgePoint t){
		allUrl.add(t);
	}
	public void addUnExecTask(KnowledgePoint t) {
		if (!allUrl.contains(t)){
			allUrl.add(t);
			unVisitedUrl.enQueue(t);
		}
	}
	public int getExecTaskNum() {
		return visitedUrl.size();
	}
	public Queue<KnowledgePoint> getUnExecTask() {
		return unVisitedUrl;
	}
	public int getUnExecTaskNum() {
		return unVisitedUrl.getSize();
	}
	public void removeExecTask(KnowledgePoint t) {
		visitedUrl.remove(t);
	}
	public KnowledgePoint unExecTaskDeQueue() {
		return unVisitedUrl.deQueue();
	}
}
