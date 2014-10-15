/**
 * 如石子一粒,仰高山之巍峨,但不自惭形秽.
 * 若小草一棵,慕白杨之伟岸,却不妄自菲薄.
 */
package org.javacoo.cowswing.plugin.kbs.service.beans;

/**
 * 本地磁盘文件对象
 * <p>说明:</p>
 * <li></li>
 * @author DuanYong
 * @since 2014-7-17 下午8:16:40
 * @version 1.0
 */
public class LocalFileBean {
	/**文件ID*/
	private String id;
	/**是否存在*/
	private boolean exists;
	/**文件名*/
	private String name;
	/**上级目录*/
	private String parent;
	/**是否可读*/
	private boolean canRead;
	/**是否可写*/
	private boolean canWrite;
	/**绝对路径*/
	private String absolutePath;
	/**相对路径*/
	private String path;
	/**是否为绝对路径*/
	private boolean absolute;
	/**是否为目录*/
	private boolean directory;
	/**是否为文件*/
	private boolean file;
	/**是否为隐藏文件*/
	private boolean hidden;
	/**最后修改时间*/
	private String lastModified;
	/**文件长度*/
	private long length;
	
	/**
	 * @return the id
	 */
	public String getId() {
		return String.valueOf(path.hashCode());
	}
	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}
	/**
	 * @return the exists
	 */
	public boolean isExists() {
		return exists;
	}
	/**
	 * @param exists the exists to set
	 */
	public void setExists(boolean exists) {
		this.exists = exists;
	}
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the parent
	 */
	public String getParent() {
		return parent;
	}
	/**
	 * @param parent the parent to set
	 */
	public void setParent(String parent) {
		this.parent = parent;
	}
	/**
	 * @return the canRead
	 */
	public boolean isCanRead() {
		return canRead;
	}
	/**
	 * @param canRead the canRead to set
	 */
	public void setCanRead(boolean canRead) {
		this.canRead = canRead;
	}
	/**
	 * @return the canWrite
	 */
	public boolean isCanWrite() {
		return canWrite;
	}
	/**
	 * @param canWrite the canWrite to set
	 */
	public void setCanWrite(boolean canWrite) {
		this.canWrite = canWrite;
	}
	/**
	 * @return the absolutePath
	 */
	public String getAbsolutePath() {
		return absolutePath;
	}
	/**
	 * @param absolutePath the absolutePath to set
	 */
	public void setAbsolutePath(String absolutePath) {
		this.absolutePath = absolutePath;
	}
	/**
	 * @return the path
	 */
	public String getPath() {
		return path;
	}
	/**
	 * @param path the path to set
	 */
	public void setPath(String path) {
		this.path = path;
	}
	/**
	 * @return the absolute
	 */
	public boolean isAbsolute() {
		return absolute;
	}
	/**
	 * @param absolute the absolute to set
	 */
	public void setAbsolute(boolean absolute) {
		this.absolute = absolute;
	}
	/**
	 * @return the directory
	 */
	public boolean isDirectory() {
		return directory;
	}
	/**
	 * @param directory the directory to set
	 */
	public void setDirectory(boolean directory) {
		this.directory = directory;
	}
	/**
	 * @return the file
	 */
	public boolean isFile() {
		return file;
	}
	/**
	 * @param file the file to set
	 */
	public void setFile(boolean file) {
		this.file = file;
	}
	/**
	 * @return the hidden
	 */
	public boolean isHidden() {
		return hidden;
	}
	/**
	 * @param hidden the hidden to set
	 */
	public void setHidden(boolean hidden) {
		this.hidden = hidden;
	}
	
	/**
	 * @return the lastModified
	 */
	public String getLastModified() {
		return lastModified;
	}
	/**
	 * @param lastModified the lastModified to set
	 */
	public void setLastModified(String lastModified) {
		this.lastModified = lastModified;
	}
	/**
	 * @return the length
	 */
	public long getLength() {
		return length;
	}
	/**
	 * @param length the length to set
	 */
	public void setLength(long length) {
		this.length = length;
	}
	
	

}
