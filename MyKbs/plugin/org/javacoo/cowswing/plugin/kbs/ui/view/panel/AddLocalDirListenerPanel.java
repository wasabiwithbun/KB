/**
 * 如石子一粒,仰高山之巍峨,但不自惭形秽.
 * 若小草一棵,慕白杨之伟岸,却不妄自菲薄.
 */
package org.javacoo.cowswing.plugin.kbs.ui.view.panel;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.swing.JCheckBox;

import org.apache.commons.lang.StringUtils;
import org.javacoo.cowswing.base.constant.Constant;
import org.javacoo.cowswing.base.loader.LanguageLoader;
import org.javacoo.cowswing.plugin.kbs.constant.KbsConstant;
import org.javacoo.cowswing.plugin.kbs.lucene.index.IndexConfigration;
import org.javacoo.cowswing.ui.listener.TextVerifier;
import org.javacoo.cowswing.ui.view.panel.AbstractContentPanel;
import org.springframework.stereotype.Component;

/**
 * 设置知识分类
 * <p>说明:</p>
 * <li></li>
 * @author DuanYong
 * @since 2013-7-13 下午1:35:55
 * @version 1.0
 */
@Component("addLocalDirListenerPanel")
public class AddLocalDirListenerPanel extends AbstractContentPanel<Map<String,String>>{
	private static final long serialVersionUID = 1L;
	/** 索引配置参数 */
	@Resource(name = "indexConfigration")
	private IndexConfigration indexConfigration;
	/**目录标签*/
	private javax.swing.JLabel dirLabel;
	/**目录输入框*/
	private javax.swing.JTextField dirField;
	/**默认监听类型标签*/
	private javax.swing.JLabel defFileTypeLabel;
	/**默认监听类型输入框*/
	private javax.swing.JTextField defFileTypeField;
	/**自定义监听类型标签*/
	private javax.swing.JLabel diyFileTypeLabel;
	/**自定义监听类型输入框*/
	private javax.swing.JTextField diyFileTypeField;
	/**文件大小标签*/
	private javax.swing.JLabel fileNumLabel;
	/**文件大小输入框*/
	private javax.swing.JTextField fileNumField;
	/** 是否包含子目录 */
	private JCheckBox childDirCheckBox;
	/**是否包含子目录*/
	private String childDirCehckValue = Constant.NO;
	/**描述标签*/
	private javax.swing.JLabel descLabel;
	
	/* (non-Javadoc)
	 * @see org.javacoo.cowswing.ui.view.panel.AbstractContentPanel#populateData()
	 */
	@Override
	protected Map<String,String> populateData() {
		Map<String,String> returnMap = new HashMap<String,String>();
		returnMap.put(KbsConstant.KBS_LOCAL_SEETING_SURR_DIYFILETYPE_KEY, indexConfigration.getFileContentType()+","+diyFileTypeField.getText());
		returnMap.put(KbsConstant.KBS_LOCAL_SEETING_SURR_MAXFILESIZE_KEY, fileNumField.getText());
		return returnMap;
	}

	/* (non-Javadoc)
	 * @see org.javacoo.cowswing.ui.view.panel.AbstractContentPanel#initComponents()
	 */
	@Override
	protected void initComponents() {
		dirLabel = new javax.swing.JLabel();
		dirLabel.setText(LanguageLoader.getString("Kbs.local_add_listener_setting_dirLabel"));
		add(dirLabel);
		dirLabel.setBounds(20, 15, 80, 15);
		
		dirField = new javax.swing.JTextField();
		dirField.setColumns(20);
		dirField.setEnabled(false);
		dirField.setInputVerifier(new TextVerifier(this,false));
		dirField.setText("");
		add(dirField);
		dirField.setBounds(100, 15, 230, 21);
		
		
		defFileTypeLabel = new javax.swing.JLabel();
		defFileTypeLabel.setText(LanguageLoader.getString("Kbs.local_add_listener_setting_defFileTypeLabel"));
		add(defFileTypeLabel);
		defFileTypeLabel.setBounds(20, 45, 80, 15);
		
		defFileTypeField = new javax.swing.JTextField();
		defFileTypeField.setColumns(20);
		defFileTypeField.setEnabled(false);
		defFileTypeField.setInputVerifier(new TextVerifier(this,false));
		add(defFileTypeField);
		defFileTypeField.setBounds(100, 45, 230, 21);
		
		diyFileTypeLabel = new javax.swing.JLabel();
		diyFileTypeLabel.setText(LanguageLoader.getString("Kbs.local_add_listener_setting_diyFileTypeLabel"));
		add(diyFileTypeLabel);
		diyFileTypeLabel.setBounds(20, 75, 80, 15);
		
		diyFileTypeField = new javax.swing.JTextField();
		diyFileTypeField.setColumns(20);
		diyFileTypeField.setText("");
		add(diyFileTypeField);
		diyFileTypeField.setBounds(100, 75, 230, 21);
		
		fileNumLabel = new javax.swing.JLabel();
		fileNumLabel.setText(LanguageLoader.getString("Kbs.local_add_listener_setting_fileNumLabel"));
		add(fileNumLabel);
		fileNumLabel.setBounds(20, 105, 80, 15);
		
		fileNumField = new javax.swing.JTextField();
		fileNumField.setColumns(20);
		fileNumField.setInputVerifier(new TextVerifier(this,false));
		add(fileNumField);
		fileNumField.setBounds(100, 105, 230, 21);
		
		descLabel =  new javax.swing.JLabel();
		descLabel.setText(LanguageLoader.getString("Kbs.local_add_listener_setting_desc"));
		add(descLabel);
		descLabel.setBounds(20, 135, 300, 55);
		
//		childDirCheckBox = new JCheckBox(LanguageLoader.getString("Kbs.local_add_listener_setting_subDirLabel"));
//		childDirCheckBox.setBounds(20, 75, 120, 15);
//		add(childDirCheckBox);
	}
	protected void initActionListener(){
//		childDirCheckBox.addActionListener(new ActionListener() {
//			@Override
//			public void actionPerformed(ActionEvent e) {
//				if(childDirCheckBox.isSelected()){
//					childDirCehckValue = Constant.YES;
//				}else{
//					childDirCehckValue = Constant.NO;
//				}
//			}
//		});
	}
	public void initData(Map<String,String> t){
		if(t == null){
			t = new HashMap<String,String>();
		}
		fillComponentData(t);
	}
	/* (non-Javadoc)
	 * @see org.javacoo.cowswing.ui.view.panel.AbstractContentPanel#fillComponentData(java.lang.Object)
	 */
	@Override
	protected void fillComponentData(Map<String,String> t) {
		dirField.setText(t.get(KbsConstant.KBS_LOCAL_SEETING_SURR_DIR_KEY));
		defFileTypeField.setText(indexConfigration.getFileContentType());
		diyFileTypeField.setText(StringUtils.isNotBlank(t.get(KbsConstant.KBS_LOCAL_SEETING_SURR_DIYFILETYPE_KEY)) ? t.get(KbsConstant.KBS_LOCAL_SEETING_SURR_DIYFILETYPE_KEY).replaceAll(indexConfigration.getFileContentType()+",", "") : "");
		fileNumField.setText(StringUtils.isNotBlank(t.get(KbsConstant.KBS_LOCAL_SEETING_SURR_MAXFILESIZE_KEY)) ? t.get(KbsConstant.KBS_LOCAL_SEETING_SURR_MAXFILESIZE_KEY) :String.valueOf(indexConfigration.getMaxFileSize()/(1024 * 1024)));
	}

}
