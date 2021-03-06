/**
 * 如石子一粒,仰高山之巍峨,但不自惭形秽.
 * 若小草一棵,慕白杨之伟岸,却不妄自菲薄.
 */
package org.javacoo.cowswing.core.runcycle.support;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

import org.apache.log4j.Logger;
import org.javacoo.cowswing.base.loader.ConfigLoader;
import org.javacoo.cowswing.base.loader.ExceptionLoader;
import org.javacoo.cowswing.base.loader.ImageLoader;
import org.javacoo.cowswing.base.loader.LanguageLoader;
import org.javacoo.cowswing.base.loader.PluginsLoader;
import org.javacoo.cowswing.core.context.CowSwingContextData;
import org.javacoo.cowswing.core.launcher.ILauncher;
import org.javacoo.cowswing.core.launcher.SpringLauncherImpl;
import org.javacoo.cowswing.core.runcycle.ICowSwingRunCycle;
import org.javacoo.cowswing.core.runcycle.schedule.Schedule;
import org.javacoo.cowswing.core.runcycle.schedule.ScheduleEnum;
import org.javacoo.cowswing.main.JSplash;
import org.springframework.util.CollectionUtils;




/**
 * CowSwing运行周期管理类
 * <p>说明:</p>
 * <li></li>
 * @author DuanYong
 * @since 2013-6-1 下午8:26:28
 * @version 1.0
 */
public class CowSwingRunCycleManager implements ICowSwingRunCycle{
	protected Logger logger = Logger.getLogger(this.getClass());
	/** spring容器中注册了的所有Schedule */
    private TreeMap<Enum<ScheduleEnum>, Schedule> scheduleTreeMap;
    /** 当前运行周期的计划 */
    private Schedule currentSchedule = null;
    public CowSwingRunCycleManager(){
    	//设置自身引用到CowSwingContextData
    	CowSwingContextData.getInstance().setCowSwingRunCycle(this);
    }
    /**
     * 初始化计划MAP
	 * <p>方法说明:</>
	 * <li></li>
	 * @author DuanYong
	 * @since 2013-6-2 上午11:00:26
	 * @version 1.0
	 * @exception 
	 */
	private void initScheduleMap() {
		Map<String, Schedule> map = CowSwingContextData.getInstance().getApplicationContext().getBeansOfType(Schedule.class);
		scheduleTreeMap = new TreeMap<Enum<ScheduleEnum>, Schedule>();
        if (!CollectionUtils.isEmpty(map)) {
            for (Iterator<String> iterator = map.keySet().iterator(); iterator.hasNext();) {
            	Schedule schedule = map.get(iterator.next());
            	scheduleTreeMap.put(schedule.getSchedule(), schedule);
            }
        } else {
            logger.error("没有在Spring容器中找到Phase的bean!");
        }
    	logger.info("完成初始化计划MAP:"+scheduleTreeMap.toString());
	}
	/**
	 * 初始化Spring容器
	 * <p>方法说明:</>
	 * <li></li>
	 * @author DuanYong
	 * @since 2013-6-2 上午11:00:22
	 * @version 1.0
	 * @exception 
	 */
	private void initSpringContext() {
		logger.info("开始初始化Spring容器.....");
	    long startDate = System.currentTimeMillis();
		ILauncher launcher = new SpringLauncherImpl();
		launcher.launch();
        logger.info("Spring容器初始化成功,消耗时间:" + (System.currentTimeMillis() - startDate));
	}
	/**
     * 初始化系统环境变量配置
     * <p>方法说明:</>
     * <li></li>
     * @author DuanYong
     * @since 2013-6-2 上午10:59:16
     * @version 1.0
     * @exception
     */
    private void initEnv(){
    	//国际化
    	LanguageLoader.setLanguage(Locale.getDefault());
    	JSplash.getInstance().increaseProgress(10, "初始化国际化完成...");
    	//图标
    	ImageLoader.setLanguage(Locale.getDefault());
    	JSplash.getInstance().increaseProgress(10, "初始化图标资源完成...");
    	//异常
    	ExceptionLoader.setLanguage(Locale.getDefault());
    	JSplash.getInstance().increaseProgress(10, "初始化异常信息完成...");
    	//加载插件
    	PluginsLoader.loadPlugins();
    	JSplash.getInstance().increaseProgress(10, "加载插件信息完成...");
    	//加载配置参数
    	ConfigLoader.loadConfig();
    	JSplash.getInstance().increaseProgress(10, "加载配置参数完成...");
    }
	/* (non-Javadoc)
	 * @see org.javacoo.cowswing.core.run.ICowRunCycle#start()
	 */
	@Override
	public void start() {
		initEnv();
		JSplash.getInstance().increaseProgress(10, "开始初始化spring容器...");
		//启动spring容器
        initSpringContext();
        JSplash.getInstance().increaseProgress(10, "初始化spring容器完成...");
	    //加载Schedulemap
	    initScheduleMap();
	    JSplash.getInstance().increaseProgress(10, "初始化运行计划完成...");
	    //执行计划
	    List<ScheduleEnum> states = new ArrayList<ScheduleEnum>();
		states.add(ScheduleEnum.RUN);
		states.add(ScheduleEnum.START);
	    executeSchedule(states);
	}
    /**
     * 执行计划
     * <p>方法说明:</>
     * <li></li>
     * @author DuanYong
     * @since 2013-10-16 下午6:09:29
     * @version 1.0
     * @exception
     */
	private void executeSchedule(List<ScheduleEnum> states){
    	logger.info("执行计划计划:"+states.toString());
		for (Iterator<Enum<ScheduleEnum>> iterator = scheduleTreeMap.keySet().iterator(); iterator.hasNext();) {
            //取得运行计划
		Schedule schedule = scheduleTreeMap.get(iterator.next());
            try {
            	if(null != schedule && states.contains(schedule.getSchedule())){
            		logger.info("执行当前运行计划:"+schedule.getSchedule());
            		 //执行当前运行计划
	            	schedule.execute();
            	}
            } catch (Exception ex) {
            	ex.printStackTrace();
                //this.exceptionProcessor.handleException(phase, ex);
            }
            //将当前阶段赋予全局变量
            currentSchedule = schedule;
        }
	}
	/* (non-Javadoc)
	 * @see org.javacoo.cowswing.core.run.ICowRunCycle#end()
	 */
	@Override
	public void end() {
		synchronized (CowSwingContextData.getInstance().getCowSwingTheadLock()) {
			CowSwingContextData.getInstance().getCowSwingTheadLock().notify();
        }
		//执行计划
		List<ScheduleEnum> states = new ArrayList<ScheduleEnum>();
		states.add(ScheduleEnum.SHUTDOWN);
	    executeSchedule(states);
	}

	/* (non-Javadoc)
	 * @see org.javacoo.cowswing.core.run.ICowRunCycle#getCurrentRunSchedule()
	 */
	@Override
	public Schedule getCurrentSchedule() {
		return this.currentSchedule;
	}
	

}
