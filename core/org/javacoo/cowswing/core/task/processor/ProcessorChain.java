package org.javacoo.cowswing.core.task.processor;
/**
 * 任务处理器链
 * 
 * <p>说明:</p>
 * <li></li>
 * @author DuanYong
 * @since 2014-7-24 下午2:20:01
 * @version 1.0
 * @param <T> 任务对象
 */
public class ProcessorChain<T> {
    /**下一个处理器链*/
	private ProcessorChain<T> nextProcessorChain;
	/**第一个处理器*/
    private Processor<T> firstProcessor;
    
    public ProcessorChain(Processor<T> firstProcessor) {
		super();
		this.firstProcessor = firstProcessor;
	}
    
	
	public ProcessorChain<T> getNextProcessorChain() {
		return nextProcessorChain;
	}


	public void setNextProcessorChain(ProcessorChain<T> nextProcessorChain) {
		this.nextProcessorChain = nextProcessorChain;
	}


	public Processor<T> getFirstProcessor() {
		return firstProcessor;
	}
	public void setFirstProcessor(Processor<T> firstProcessor) {
		this.firstProcessor = firstProcessor;
	}
    
    

}
