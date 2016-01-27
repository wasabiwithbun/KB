/**
 * 如石子一粒,仰高山之巍峨,但不自惭形秽.
 * 若小草一棵,慕白杨之伟岸,却不妄自菲薄.
 */
package org.javacoo.cowswing.plugin.kbs.service;

import java.io.File;
import java.io.FileFilter;

import net.contentobjects.jnotify.JNotify;

/**
 * 
 * <p>说明:</p>
 * <li></li>
 * @author DuanYong
 * @since 2014-7-30 下午3:08:55
 * @version 1.0
 */
public final class FileMonitorConfig {
	public enum MASK {  
        /** 
         * A file created 
         */  
        CREATED(JNotify.FILE_CREATED),  
        /** 
         * A file deleted 
         */  
        DELETED(JNotify.FILE_DELETED),  
        /** 
         * A file modified 
         */  
        MODIFIED(JNotify.FILE_MODIFIED),  
        /** 
         * A file renamed 
         */  
        RENAMED(JNotify.FILE_RENAMED);  
        private final int mask;  
  
        private MASK(int mask) {  
            this.mask = mask;  
        }  
  
        /** 
         * @return the mask 
         */  
        public int getMask() {  
            return mask;  
        }  
    }  
  
    final String path;  
    final boolean watchSubtree;  
    int mask;  
    FileFilter filter;  
    int watchId;  
    File file;  
  
    public FileMonitorConfig(String filename, MASK... masks) {  
        file = new File(filename);  
        if (!file.isFile()) {  
            throw new IllegalArgumentException("Not a file: " + filename);  
        }  
        this.filter = new FileFilter() {  
            public boolean accept(File file1) {  
                return file.equals(file1);  
            }  
        };  
        this.path = file.getParent();  
        this.watchSubtree = false;  
        if (masks == null || masks.length == 0) {  
            this.mask = JNotify.FILE_ANY;  
        } else {  
            for (MASK m : masks) {  
                mask |= m.getMask();  
            }  
        }  
    }  
  
    public FileMonitorConfig(String path, boolean watchSubtree, MASK... masks) {  
        file = new File(path);  
        if (!file.isDirectory())  
            throw new IllegalArgumentException("Not a directory: " + path);  
        this.path = path;  
        this.watchSubtree = watchSubtree;  
        if (masks == null || masks.length == 0) {  
            this.mask = JNotify.FILE_ANY;  
        } else {  
            for (MASK m : masks) {  
                mask |= m.getMask();  
            }  
        }  
    }  
  
    /** 
     * @return the filter 
     */  
    public FileFilter getFilter() {  
        return filter;  
    }  
  
    /** 
     * Tests whether or not the specified abstract pathname should be monitored. 
     *  
     * @param pathname 
     * @return 
     * @see java.io.FileFilter#accept(java.io.File) 
     */  
    public boolean accept(File pathname) {  
        return filter == null ? true : filter.accept(pathname);  
    }  
  
    /** 
     * @param filter 
     *            the filter to set 
     */  
    public void setFilter(FileFilter filter) {  
        if (file.isFile())  
            throw new UnsupportedOperationException("It is a file,set filter for a directory.");  
        this.filter = filter;  
    }  
  
    /** 
     * @return the mask 
     */  
    public int getMask() {  
        return mask;  
    }  
  
    /** 
     * @return the path 
     */  
    public String getPath() {  
        return path;  
    }  
  
    /** 
     * @return the watchSubtree 
     */  
    public boolean isWatchSubtree() {  
        return watchSubtree;  
    }  
  
    /** 
     * @return the file 
     */  
    public File getFile() {  
        return file;  
    }  
}
