package org.javacoo.cowswing.main;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.channels.FileLock;

import javax.swing.SwingUtilities;

import org.javacoo.cowswing.base.constant.Constant;
import org.javacoo.cowswing.core.runcycle.ICowSwingRunCycle;
import org.javacoo.cowswing.core.runcycle.support.CowSwingRunCycleManager;
import org.javacoo.cowswing.ui.style.LookAndFeelSelector;



/**
 * GUI界面入口类
 * 
 * @author javacoo
 * @since 2012-03-13
 */
public class CowSwingMain {
	private static FileLock lock = null;
	private static String lockFile = Constant.SYSTEM_ROOT_PATH+"/lock.lock";
	public CowSwingMain(){
		start();
	}
	public static void main(String[] args) {
		start();
	}
	/**
	 * 启动
	 * <p>方法说明:</>
	 * <li></li>
	 * @author DuanYong
	 * @since 2014-1-2 下午4:46:41
	 * @version 1.0
	 * @exception
	 */
    private static void start(){
    	if(!isLocking()){
			JSplash.getInstance().splashOn();
			JSplash.getInstance().increaseProgress(0, "丑牛正在初始化,请稍候...");
			startCowSwing();
		}
    }
	/**
	 * 启动
	 */
	private static void startCowSwing() {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
		    	LookAndFeelSelector.setLookAndFeel("OfficeBlue2007");
				org.javacoo.cowswing.ui.style.ColorDefinitions.initColors();
				ICowSwingRunCycle cowSwingRunCycle = new CowSwingRunCycleManager();
				cowSwingRunCycle.start();
				JSplash.getInstance().splashOff();
			}
		});
		
	}
	
	/**
	 * 检查系统是否已经启动
	 * <p>方法说明:</>
	 * <li></li>
	 * @author DuanYong
	 * @since 2013-10-8 下午11:33:47
	 * @version 1.0
	 * @exception 
	 * @return
	 */
	public static boolean isLocking() {
		try {
			File flagFile = new File(lockFile);
			if (!flagFile.exists()){
				flagFile.createNewFile();
			}
			lock = new FileOutputStream(lockFile).getChannel().tryLock();

			if (lock == null){
				return true;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return false;
	}
}
