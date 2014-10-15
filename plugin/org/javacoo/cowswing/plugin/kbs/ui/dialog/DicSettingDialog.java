/**
 * 如石子一粒,仰高山之巍峨,但不自惭形秽.
 * 若小草一棵,慕白杨之伟岸,却不妄自菲薄.
 */
package org.javacoo.cowswing.plugin.kbs.ui.dialog;

import java.awt.event.ActionEvent;
import java.util.List;

import javax.annotation.Resource;
import javax.swing.JButton;
import javax.swing.JComponent;

import org.javacoo.cowswing.base.constant.Constant;
import org.javacoo.cowswing.base.loader.LanguageLoader;
import org.javacoo.cowswing.base.utils.FileUtils;
import org.javacoo.cowswing.base.utils.MsgDialogUtil;
import org.javacoo.cowswing.core.event.CowSwingListener;
import org.javacoo.cowswing.plugin.kbs.constant.KbsConstant;
import org.javacoo.cowswing.plugin.kbs.ui.view.panel.DicSettingPanel;
import org.javacoo.cowswing.ui.view.dialog.AbstractDialog;
import org.springframework.stereotype.Component;

/**
 * 词典设置
 * <p>说明:</p>
 * <li></li>
 * @author DuanYong
 * @since 2013-9-26 下午6:29:21
 * @version 1.0
 */
@Component("dicSettingDialog")
public class DicSettingDialog extends AbstractDialog implements
		CowSwingListener {
	private static final long serialVersionUID = 1L;
	/** 词典设置面板 */
	@Resource(name = "dicSettingPanel")
	private DicSettingPanel dicSettingPanel;
	/**词典路径*/
	private static String EXT_DIC_PATH = KbsConstant.SYSTEM_ROOT_PATH +"/plugins/kbs/dic/ext.dic";
	public DicSettingDialog(){
		super(500,400,true);
	}
	@Override
	public JComponent getCenterPane() {
		if (centerPane == null) {
			centerPane = dicSettingPanel;
		}
		return centerPane;
	}
	public JButton getCancelButton(){
		return null;
	}
	
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.javacoo.cowswing.ui.view.dialog.AbstractDialog#
	 * finishButtonActionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	protected void finishButtonActionPerformed(ActionEvent event) {
		List<String> returnList = dicSettingPanel.getData();
		if(FileUtils.writeFile(returnList,EXT_DIC_PATH)){
			MsgDialogUtil.createMessageDialog(LanguageLoader.getString("Kbs.dic_save_succc"));
		}else{
			MsgDialogUtil.createMessageDialog(LanguageLoader.getString("Kbs.dic_save_fail"));
		}
		this.dispose();
	}


	protected void initData(String type) {
		fillJTabbedPane();
	}
	public void dispose(){
		super.dispose();
		centerPane = null;
	}
	/**
	 * 填充JTabbedPane值
	 * <p>方法说明:</p>
	 * @auther DuanYong
	 * @since 2012-12-3 下午12:20:32
	 * @return void
	 */
	private void fillJTabbedPane(){
		logger.info("词典参数设置");
		dicSettingPanel.initData(FileUtils.readFile(EXT_DIC_PATH, Constant.ENCODING_GBK));
	}
	
	

	
}
