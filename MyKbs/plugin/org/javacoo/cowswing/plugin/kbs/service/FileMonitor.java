/**
 * 如石子一粒,仰高山之巍峨,但不自惭形秽.
 * 若小草一棵,慕白杨之伟岸,却不妄自菲薄.
 */
package org.javacoo.cowswing.plugin.kbs.service;

import java.io.File;
import java.util.HashMap;

import net.contentobjects.jnotify.JNotify;
import net.contentobjects.jnotify.JNotifyException;
import net.contentobjects.jnotify.JNotifyListener;

/**
 * 
 * <p>说明:</p>
 * <li></li>
 * @author DuanYong
 * @since 2014-7-30 下午3:07:51
 * @version 1.0
 */
public class FileMonitor {
	private final HashMap<File, FileMonitorConfig> configs;  
    private JNotifyListener listener;  
    private boolean running;  
    private final Object waiter = new Object();  
    private long minWatchedInterval = 100;  
    private File lastWatchedFile;  
    private long lastWatchedTime;  
  
    public FileMonitor() {  
        configs = new HashMap<File, FileMonitorConfig>();  
        listener = new JNotifyListener() {  
            public void fileRenamed(int wd, String parent, String oldName, String newName) {  
                if (accept(parent, oldName, newName)) {  
                    renamed(parent, oldName, newName);  
                }  
            }  
  
            public void fileModified(int wd, String parent, String name) {  
                if (accept(parent, name)) {  
                    modified(parent, name);  
                }  
            }  
  
            public void fileDeleted(int wd, String parent, String name) {  
                if (accept(parent, name)) {  
                    deleted(parent, name);  
                }  
            }  
  
            public void fileCreated(int wd, String parent, String name) {  
                if (accept(parent, name)) {  
                    created(parent, name);  
                }  
            }  
        };  
    }  
  
    public boolean addWatch(FileMonitorConfig config) {  
        configs.put(config.getFile(), config);  
        try {  
            config.watchId = (JNotify.addWatch(config.getPath(), config.getMask(), config  
                    .isWatchSubtree(), listener));  
        } catch (JNotifyException e) {  
            return false;  
        }  
        return true;  
    }  
  
    private boolean accept(String parent, String name) {  
        File file = new File(parent, name);  
        if (file.equals(lastWatchedFile)  
                && System.currentTimeMillis() - lastWatchedTime < minWatchedInterval) {  
            return false;  
        }  
        FileMonitorConfig config = configs.get(file);  
        if (config == null) {  
            config = configs.get(new File(parent));  
        }  
        if (config != null && config.accept(file)) {  
            lastWatchedFile = file;  
            lastWatchedTime = System.currentTimeMillis();  
            return true;  
        } else  
            return false;  
    }  
  
    private boolean accept(String parent, String oldName, String newName) {  
        File file = new File(parent, oldName);  
        FileMonitorConfig config = configs.get(file);  
        boolean isFile = true;  
        if (config == null) {  
            config = configs.get(new File(parent));  
            isFile = false;  
        }  
        if (config != null && config.accept(file)) {  
            lastWatchedFile = new File(parent, newName);  
            if (isFile) {  
                configs.remove(file);  
                config.file = lastWatchedFile;  
                configs.put(lastWatchedFile, config);  
            }  
            lastWatchedTime = System.currentTimeMillis();  
            return true;  
        } else  
            return false;  
    }  
  
    public void start() throws JNotifyException {  
        running = true;  
        Thread t = new Thread() {  
            public void run() {  
                while (running) {  
                    synchronized (waiter) {  
                        try {  
                            waiter.wait();  
                        } catch (InterruptedException e) {  
                        }  
                    }  
                }  
            }  
        };  
        t.setDaemon(true);  
        t.start();  
    }  
  
    public void stop() {  
        running = false;  
        synchronized (waiter) {  
            waiter.notify();  
        }  
    }  
  
    public boolean removeWatch(File file) {  
        FileMonitorConfig config = configs.remove(file);  
        if (config == null)  
            return false;  
        try {  
            JNotify.removeWatch(config.watchId);  
        } catch (JNotifyException e) {  
            return false;  
        }  
        return true;  
    }  
  
    protected void renamed(String parent, String oldName, String newName) {  
    	System.err.println("renamed " + parent + " : " + oldName + " -> " + newName);
    }  
  
    protected void modified(String parent, String name) {  
    	System.err.println("modified " + parent + " : " +"hashcode:"+parent.hashCode()+",path="+ name);  
    }  
  
    protected void deleted(String parent, String name) {  
    	System.err.println("deleted " + parent + " : " +",path="+ name);
    }  
  
    protected void created(String parent, String name) { 
    	System.err.println("created " + parent + " : " +",path="+ name);  
    }  
  
    /** 
     * @return the minWatchedInterval 
     */  
    public long getMinWatchedInterval() {  
        return minWatchedInterval;  
    }  
  
    /** 
     * @param minWatchedInterval 
     *            the minWatchedInterval to set 
     */  
    public void setMinWatchedInterval(long minWatchedInterval) {  
        this.minWatchedInterval = minWatchedInterval;  
    }  
  
    /** 
     * @return the lastWatchedFile 
     */  
    public File getLastWatchedFile() {  
        return lastWatchedFile;  
    }  
  
    /** 
     * @return the lastWatchedTime 
     */  
    public long getLastWatchedTime() {  
        return lastWatchedTime;  
    }  
}
