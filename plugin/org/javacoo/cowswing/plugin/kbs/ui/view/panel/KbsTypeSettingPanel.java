/**
 * 如石子一粒,仰高山之巍峨,但不自惭形秽.
 * 若小草一棵,慕白杨之伟岸,却不妄自菲薄.
 */
package org.javacoo.cowswing.plugin.kbs.ui.view.panel;

import java.util.List;

import javax.annotation.Resource;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;

import org.apache.commons.lang.StringUtils;
import org.javacoo.cowswing.base.loader.LanguageLoader;
import org.javacoo.cowswing.base.service.ICowSwingService;
import org.javacoo.cowswing.base.utils.GeneraterUtil;
import org.javacoo.cowswing.plugin.kbs.constant.KbsConstant;
import org.javacoo.cowswing.plugin.kbs.service.beans.KbsTypeBean;
import org.javacoo.cowswing.plugin.kbs.service.beans.KbsTypeCriteria;
import org.javacoo.cowswing.ui.listener.TextVerifier;
import org.javacoo.cowswing.ui.view.panel.AbstractContentPanel;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

/**
 * 设置知识分类
 * <p>说明:</p>
 * <li></li>
 * @author DuanYong
 * @since 2013-7-13 下午1:35:55
 * @version 1.0
 */
@Component("kbsTypeSettingPanel")
public class KbsTypeSettingPanel extends AbstractContentPanel<KbsTypeBean>{
	private static final long serialVersionUID = 1L;
	/** 知识分类服务 */
	@Resource(name = "kbsTypeService")
	protected ICowSwingService<KbsTypeBean, KbsTypeCriteria> kbsTypeService;
	/**父节点名称标签*/
	private javax.swing.JLabel parentLabel;
	/**父节点名称输入框*/
	private javax.swing.JTextField parentField;
	/**节点名称标签*/
	private javax.swing.JLabel typeCodeLabel;
	/**节点名称输入框*/
	private javax.swing.JTextField typeCodeField;
	/**属性类型标签*/
	private javax.swing.JLabel attributeTypeLabel;
	/**属性类型下拉*/
	private JComboBox attributeTypeCombo;
	/***属性类型默认ComboBoxModel*/
	private DefaultComboBoxModel attributeTypeComboBoxModel;
	/**节点名称标签*/
	private javax.swing.JLabel typeNameLabel;
	/**节点名称输入框*/
	private javax.swing.JTextField typeNameField;
	/**父分类编码*/
	private String parentTypeCode;
	
	/* (non-Javadoc)
	 * @see org.javacoo.cowswing.ui.view.panel.AbstractContentPanel#populateData()
	 */
	@Override
	protected KbsTypeBean populateData() {
		KbsTypeBean kbsTypeBean = new KbsTypeBean();
		kbsTypeBean.setTypeName(typeNameField.getText());
		kbsTypeBean.setTypeCode(typeCodeField.getText());
		KbsTypeBean attribute = (KbsTypeBean)attributeTypeCombo.getSelectedItem();
		kbsTypeBean.setExpandTypeCode(attribute.getTypeCode());
		kbsTypeBean.setExpandTypeName(attribute.getTypeName());
		kbsTypeBean.setParentTypeCode(parentTypeCode);
		return kbsTypeBean;
	}

	/* (non-Javadoc)
	 * @see org.javacoo.cowswing.ui.view.panel.AbstractContentPanel#initComponents()
	 */
	@Override
	protected void initComponents() {
		parentLabel = new javax.swing.JLabel();
		parentLabel.setText(LanguageLoader.getString("Kbs.type_setting_parentLabel"));
		add(parentLabel);
		parentLabel.setBounds(20, 15, 80, 15);
		
		parentField = new javax.swing.JTextField();
		parentField.setColumns(20);
		parentField.setEnabled(false);
		parentField.setInputVerifier(new TextVerifier(this,false));
		parentField.setText("");
		add(parentField);
		parentField.setBounds(130, 15, 200, 21);
		
		typeCodeLabel = new javax.swing.JLabel();
		typeCodeLabel.setText(LanguageLoader.getString("Kbs.type_setting_typeCodeLabel"));
		add(typeCodeLabel);
		typeCodeLabel.setBounds(20, 45, 80, 15);
		
		typeCodeField = new javax.swing.JTextField();
		typeCodeField.setColumns(20);
		typeCodeField.setInputVerifier(new TextVerifier(this,false));
		typeCodeField.setText("");
		typeCodeField.setEnabled(false);
		add(typeCodeField);
		typeCodeField.setBounds(130, 45, 200, 21);
		
		attributeTypeLabel = new javax.swing.JLabel();
		attributeTypeLabel.setText(LanguageLoader.getString("Kbs.type_setting_attributeLabel"));
		add(attributeTypeLabel);
		attributeTypeLabel.setBounds(20, 75, 80, 15);
		
		attributeTypeComboBoxModel = new DefaultComboBoxModel();
		attributeTypeCombo = new JComboBox(attributeTypeComboBoxModel);
		attributeTypeCombo.setBounds(130, 75, 200, 21);
		add(attributeTypeCombo);
		
		typeNameLabel = new javax.swing.JLabel();
		typeNameLabel.setText(LanguageLoader.getString("Kbs.type_setting_typeNameLabel"));
		add(typeNameLabel);
		typeNameLabel.setBounds(20, 105, 80, 15);
		
		typeNameField = new javax.swing.JTextField();
		typeNameField.setColumns(20);
		typeNameField.setInputVerifier(new TextVerifier(this,false));
		typeNameField.setText("");
		add(typeNameField);
		typeNameField.setBounds(130, 105, 200, 21);
		
	}
	protected void initActionListener(){
		
	}
	public void initData(KbsTypeBean t){
		if(t == null){
			t = new KbsTypeBean();
		}
		fillComponentData(t);
	}
	/* (non-Javadoc)
	 * @see org.javacoo.cowswing.ui.view.panel.AbstractContentPanel#fillComponentData(java.lang.Object)
	 */
	@Override
	protected void fillComponentData(KbsTypeBean t) {
		parentField.setText(t.getParentTypeName());
		if(StringUtils.isNotBlank(t.getTypeName())){
			typeNameField.setText(t.getTypeName());
		}else{
			typeNameField.setText("");
		}
		if(StringUtils.isNotBlank(t.getTypeCode())){
			typeCodeField.setText(t.getTypeCode());
		}else{
			typeCodeField.setText(GeneraterUtil.genIdByTime());
		}
		parentTypeCode = t.getParentTypeCode();
		
		attributeTypeComboBoxModel.removeAllElements();
		List<KbsTypeBean> childList = getKbsTypeList(KbsConstant.KBS_TYPE_CHILD_ATTRIBUT_CODE);
		attributeTypeComboBoxModel.addElement(new KbsTypeBean());
		if(!CollectionUtils.isEmpty(childList)){
			for(KbsTypeBean kbsTypeBean : childList){
				attributeTypeComboBoxModel.addElement(kbsTypeBean);
			}
		}
		if(StringUtils.isNotBlank(t.getExpandTypeCode())){
			attributeTypeCombo.setSelectedItem(new KbsTypeBean(t.getExpandTypeCode()));
		}
	}
	/**
	 * 取得类型列表
	 * <p>方法说明:</>
	 * <li></li>
	 * @author DuanYong
	 * @since 2013-9-30 下午5:32:07
	 * @version 1.0
	 * @exception 
	 * @param typeCode
	 * @return
	 */
	private List<KbsTypeBean> getKbsTypeList(String typeCode){
		KbsTypeCriteria q = new KbsTypeCriteria();
		q.setTypeCode(typeCode);
		return kbsTypeService.getList(q, KbsConstant.SQLMAP_ID_GET_LIST_KBS_TYPE);
	}
	protected void addCrawlerListener(){
	}
}
