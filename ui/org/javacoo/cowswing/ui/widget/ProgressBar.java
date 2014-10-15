/**
 * 如石子一粒,仰高山之巍峨,但不自惭形秽.
 * 若小草一棵,慕白杨之伟岸,却不妄自菲薄.
 */
package org.javacoo.cowswing.ui.widget;

import javax.swing.JProgressBar;

/**
 * 进度条
 * <p>说明:</p>
 * <li></li>
 * @author DuanYong
 * @since 2014-1-3 上午9:40:12
 * @version 1.0
 */
public class ProgressBar extends JProgressBar{
	private static final long serialVersionUID = 1L;

	public ProgressBar(int maximum,boolean isVertical,boolean stringPainted,boolean indeterminate){
		if(isVertical){
			this.setOrientation(JProgressBar.VERTICAL);
		}else{
			this.setOrientation(JProgressBar.HORIZONTAL);
		}
		// 显示百分比字符
		this.setStringPainted(stringPainted); 
		// 不确定的进度条
		this.setIndeterminate(indeterminate);  
        //设置进度条的最大值和最小值,  
		this.setMinimum(1);   
        //以总任务量作为进度条的最大值  
		this.setMaximum(maximum); 
	}
	
}
