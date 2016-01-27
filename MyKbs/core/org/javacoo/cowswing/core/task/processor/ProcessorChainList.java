package org.javacoo.cowswing.core.task.processor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 处理器链集合
 * 
 * <p>说明:</p>
 * <li></li>
 * @author DuanYong
 * @since 2014-7-24 下午2:21:19
 * @version 1.0
 * @param <T> 任务对象
 */
public class ProcessorChainList<T> {
	
	private List<ProcessorChain<T>> chainList = new ArrayList<ProcessorChain<T>>();
	private Map<String, ProcessorChain<T>> chainMap = new HashMap<String,ProcessorChain<T>>();
	
	public ProcessorChainList() {
		super();
	}
	
	public void addProcessorMap(String name, Processor<T> processor) {
		ProcessorChain<T> processorChain = new ProcessorChain<T>(processor);
		ProcessorChain<T> previousChain = getLastChain();
		if (previousChain != null) {
			previousChain.setNextProcessorChain(processorChain);
		}
		chainList.add(processorChain);
		chainMap.put(name, processorChain);
	}

	public ProcessorChain<T> getLastChain() {
		if (size() == 0) {
			return null;
		} else {
			return chainList.get(size() - 1);
		}
	}
	
	public int size() {
		return chainList.size();
	}

	public ProcessorChain<T> getFirstChain() {
		return chainList.get(0);
	}

}
