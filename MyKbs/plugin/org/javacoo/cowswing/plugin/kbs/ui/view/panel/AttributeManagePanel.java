/**
 * 如石子一粒,仰高山之巍峨,但不自惭形秽.
 * 若小草一棵,慕白杨之伟岸,却不妄自菲薄.
 */
package org.javacoo.cowswing.plugin.kbs.ui.view.panel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.javacoo.cowswing.base.loader.ImageLoader;
import org.javacoo.cowswing.base.loader.LanguageLoader;
import org.javacoo.cowswing.base.service.ICowSwingService;
import org.javacoo.cowswing.base.utils.GeneraterUtil;
import org.javacoo.cowswing.base.utils.MsgDialogUtil;
import org.javacoo.cowswing.plugin.kbs.constant.KbsConstant;
import org.javacoo.cowswing.plugin.kbs.service.beans.KbsTypeBean;
import org.javacoo.cowswing.plugin.kbs.service.beans.KbsTypeCriteria;
import org.javacoo.cowswing.plugin.kbs.service.beans.SimpleKeyValueBean;
import org.javacoo.cowswing.ui.view.panel.AbstractContentPanel;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

/**
 * 知识属性设置面板
 * 
 * <p>说明:</p>
 * <li></li>
 * @author DuanYong
 * @since 2013-9-29 上午10:32:06
 * @version 1.0
 */
@Component("attributeManagePanel")
public class AttributeManagePanel extends AbstractContentPanel<List<SimpleKeyValueBean>> {
	private static final long serialVersionUID = 1L;
	protected Logger logger = Logger.getLogger(this.getClass());
	/** 知识分类服务 */
	@Resource(name = "kbsTypeService")
	protected ICowSwingService<KbsTypeBean, KbsTypeCriteria> kbsTypeService;
	/**操作说明标签*/
	private javax.swing.JLabel operDescLabel;
	/**拖拽说明标签*/
	private javax.swing.JLabel dropLabel;
	/**分组标签*/
	private javax.swing.JLabel attributeGroupLabel;
	/**分组名称输入框*/
	private javax.swing.JTextField attributeGroupNameField;
	/**分组编码标签*/
	private javax.swing.JLabel attributeGroupCodeLabel;
	/**分组编码输入框*/
	private javax.swing.JTextField attributeGroupCodeField;
	/**添加属性分组按钮 */
	private JButton addGroupBtn;
	/**删除属性分组按钮*/
	private JButton removeGroupBtn;
	/**修改属性分组按钮*/
	private JButton updateGroupBtn;
	/**添加属性按钮 */
	private JButton addBtn;
	/**删除属性按钮*/
	private JButton removeBtn;
	/**修改属性按钮*/
	private JButton updateBtn;
	/**属性所属组标签*/
	private javax.swing.JLabel parentLabel;
	/**属性所属组名称输入框*/
	private javax.swing.JTextField parentNameField;
	/**属性标签*/
	private javax.swing.JLabel attributeLabel;
	/**属性名称输入框*/
	private javax.swing.JTextField attributeNameField;
	/**属性类型标签*/
	private javax.swing.JLabel attributeTypeLabel;
	/**属性类型下拉*/
	private JComboBox attributeTypeCombo;
	/***属性类型默认ComboBoxModel*/
	private DefaultComboBoxModel attributeTypeComboBoxModel;
	/**属性取值输入域*/
	private javax.swing.JTextArea attributeValueJTextArea;
	/**属性分组列表*/
	private JList attributeGroupJList;
	/**属性分组模块Model*/
	private DefaultListModel attributeGroupListModel;
	/**属性说明标签*/
	private javax.swing.JLabel attributeDescLabel;
	/**属性说明详细标签*/
	private javax.swing.JLabel attributeDescIndoLabel;
	/**属性列表*/
	private JList attributeJList;
	/**属性模块Model*/
	private DefaultListModel attributeListModel;
	/**已经选择的属性分组信息*/
	private KbsTypeBean selectAttributeGroupKbsTypeBean;
	/**已经选择的属性信息*/
	private KbsTypeBean selectAttributeKbsTypeBean;
	
	/* (non-Javadoc)
	 * @see org.javacoo.cowswing.ui.view.panel.AbstractContentPanel#populateData()
	 */
	@Override
	protected List<SimpleKeyValueBean> populateData() {
		List<SimpleKeyValueBean> indexList = new ArrayList<SimpleKeyValueBean>();
		Object[] selecteds = attributeGroupJList.getSelectedValues();
		for(int i = 0;i<selecteds.length;i++){
			indexList.add((SimpleKeyValueBean)selecteds[i]);
		}
		return indexList;
	}

	/* (non-Javadoc)
	 * @see org.javacoo.cowswing.ui.view.panel.AbstractContentPanel#initComponents()
	 */
	@Override
	protected void initComponents() {
		operDescLabel = new javax.swing.JLabel();
		operDescLabel.setText(LanguageLoader.getString("Kbs.attribute_group_desc"));
		add(operDescLabel);
		operDescLabel.setBounds(20, 15, 60, 15);
		
		
		dropLabel = new javax.swing.JLabel();
		dropLabel.setText(LanguageLoader.getString("Kbs.attribute_group_desc_info"));
		add(dropLabel);
		dropLabel.setBounds(90, 15, 320, 15);
		
		
		attributeGroupLabel = new javax.swing.JLabel();
		attributeGroupLabel.setText(LanguageLoader.getString("Kbs.attribute_add_group_label"));
		add(attributeGroupLabel);
		attributeGroupLabel.setBounds(20, 45, 60, 15);
		
		
		attributeGroupNameField = new javax.swing.JTextField();
		attributeGroupNameField.setColumns(20);
		attributeGroupNameField.setText("");
		add(attributeGroupNameField);
		attributeGroupNameField.setBounds(90, 45, 130, 21);
		
		
		attributeGroupCodeLabel = new javax.swing.JLabel();
		attributeGroupCodeLabel.setText(LanguageLoader.getString("Kbs.attribute_add_group_code_label"));
		add(attributeGroupCodeLabel);
		attributeGroupCodeLabel.setBounds(20, 75, 60, 15);
		
		
		attributeGroupCodeField = new javax.swing.JTextField();
		attributeGroupCodeField.setColumns(20);
		attributeGroupCodeField.setText("");
		attributeGroupCodeField.setEditable(false);
		add(attributeGroupCodeField);
		attributeGroupCodeField.setBounds(90, 75, 130, 21);
		
		
		
		attributeGroupListModel = new DefaultListModel();
		attributeGroupJList = new JList(attributeGroupListModel);
		JScrollPane attributeGroupJScrollPane = new JScrollPane(attributeGroupJList);
		add(attributeGroupJScrollPane);
		attributeGroupJScrollPane.setBounds(260, 45, 220, 80);
		
		addGroupBtn = new JButton(LanguageLoader.getString("Kbs.attribute_add_group"),ImageLoader.getImageIcon("CrawlerResource.kbs_source_browse"));
		addGroupBtn.setBounds(20, 105, 60, 21);
		add(addGroupBtn);
		
		
		removeGroupBtn = new JButton(LanguageLoader.getString("Kbs.attribute_del_group"),ImageLoader.getImageIcon("CrawlerResource.kbs_source_delete"));
		add(removeGroupBtn);
		removeGroupBtn.setEnabled(false);
		removeGroupBtn.setBounds(90, 105, 60, 21);
		
		
		updateGroupBtn = new JButton(LanguageLoader.getString("Kbs.attribute_update_group"),ImageLoader.getImageIcon("CrawlerResource.kbs_source_edit"));
		add(updateGroupBtn);
		updateGroupBtn.setEnabled(false);
		updateGroupBtn.setBounds(160, 105, 60, 21);
		
		
		attributeDescLabel = new javax.swing.JLabel();
		attributeDescLabel.setText(LanguageLoader.getString("Kbs.attribute_desc_label"));
		add(attributeDescLabel);
		attributeDescLabel.setBounds(20, 165, 200, 15);
		
		attributeDescIndoLabel = new javax.swing.JLabel();
		attributeDescIndoLabel.setText(LanguageLoader.getString("Kbs.attribute_desc_info_label"));
		add(attributeDescIndoLabel);
		attributeDescIndoLabel.setBounds(90, 165, 200, 15);
		
		
		parentLabel = new javax.swing.JLabel();
		parentLabel.setText(LanguageLoader.getString("Kbs.attribute_add_parent_label"));
		add(parentLabel);
		parentLabel.setBounds(20, 195, 60, 15);
		
		
		
		parentNameField = new javax.swing.JTextField();
		parentNameField.setColumns(20);
		parentNameField.setText("");
		parentNameField.setEditable(false);
		add(parentNameField);
		parentNameField.setBounds(90, 195, 130, 21);
		
		attributeLabel = new javax.swing.JLabel();
		attributeLabel.setText(LanguageLoader.getString("Kbs.attribute_add_label"));
		add(attributeLabel);
		attributeLabel.setBounds(20, 225, 60, 15);
		
		
		attributeNameField = new javax.swing.JTextField();
		attributeNameField.setColumns(20);
		attributeNameField.setText("");
		add(attributeNameField);
		attributeNameField.setBounds(90, 225, 130, 21);
		
		
		attributeTypeLabel = new javax.swing.JLabel();
		attributeTypeLabel.setText(LanguageLoader.getString("Kbs.attribute_type_add_label"));
		add(attributeTypeLabel);
		attributeTypeLabel.setBounds(20, 255, 60, 15);
		
		attributeTypeComboBoxModel = new DefaultComboBoxModel();
		attributeTypeCombo = new JComboBox(attributeTypeComboBoxModel);
		attributeTypeCombo.setBounds(90, 255, 130, 21);
		add(attributeTypeCombo);
		
		
		attributeValueJTextArea = new javax.swing.JTextArea();
		attributeValueJTextArea.setText(LanguageLoader.getString("Kbs.attribute_value_desc"));
		add(attributeValueJTextArea);
		attributeValueJTextArea.setEnabled(false);
		attributeValueJTextArea.setBounds(20, 285, 200, 60);
		
		addBtn = new JButton(LanguageLoader.getString("Kbs.attribute_add"),ImageLoader.getImageIcon("CrawlerResource.kbs_source_browse"));
		addBtn.setBounds(20, 355, 60, 21);
		addBtn.setEnabled(false);
		add(addBtn);
		
		
		removeBtn = new JButton(LanguageLoader.getString("Kbs.attribute_del"),ImageLoader.getImageIcon("CrawlerResource.kbs_source_delete"));
		add(removeBtn);
		removeBtn.setEnabled(false);
		removeBtn.setBounds(90, 355, 60, 21);
		
		
		updateBtn = new JButton(LanguageLoader.getString("Kbs.attribute_update"),ImageLoader.getImageIcon("CrawlerResource.kbs_source_edit"));
		add(updateBtn);
		updateBtn.setEnabled(false);
		updateBtn.setBounds(160, 355, 60, 21);
		
		
		attributeListModel = new DefaultListModel();
		attributeJList = new JList(attributeListModel);
		JScrollPane attributeJScrollPane = new JScrollPane(attributeJList);
		add(attributeJScrollPane);
		attributeJScrollPane.setBounds(260, 195, 220, 180);
		
		
	}
	/**
	 * 初始化事件
	 * <p>方法说明:</p>
	 * @auther DuanYong
	 * @since 2012-11-16 上午11:00:17
	 * @return void
	 */
	protected void initActionListener(){
		
		addGroupBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(StringUtils.isNotBlank(attributeGroupNameField.getText())){
					KbsTypeBean kbsTypeBean = new KbsTypeBean();
					kbsTypeBean.setParentTypeCode(KbsConstant.KBS_TYPE_CHILD_ATTRIBUT_CODE);
					kbsTypeBean.setTypeCode(attributeGroupCodeField.getText());
					kbsTypeBean.setTypeName(attributeGroupNameField.getText());
					kbsTypeBean = insert(kbsTypeBean);
					attributeGroupListModel.addElement(kbsTypeBean);
					reFlush();
				}else{
					MsgDialogUtil.createMessageDialog(LanguageLoader.getString("Kbs.attribute_add_group_name_is_empty"));
				}
			}
		});
		removeGroupBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int result = JOptionPane.showConfirmDialog(null, LanguageLoader.getString("Kbs.attribute_del_Confirm"),LanguageLoader.getString("Common.deleteConfirm"), JOptionPane.YES_NO_OPTION); 
				if(result == 0){
					int[] selected = attributeGroupJList.getSelectedIndices();
					KbsTypeBean kbsTypeBean = null;
					int index = 0;
					for (int i = 0; i < selected.length; ++i) {
						index = selected[i] - i;
						kbsTypeBean = (KbsTypeBean)attributeGroupListModel.get(index);
						delete(kbsTypeBean);
						deleteByParentTypeCode(kbsTypeBean);
						attributeGroupListModel.removeElementAt(index);
					}
				}
			}
		});
		updateGroupBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(null != selectAttributeGroupKbsTypeBean){
					selectAttributeGroupKbsTypeBean.setTypeName(attributeGroupNameField.getText());
					update(selectAttributeGroupKbsTypeBean);
					attributeGroupJList.updateUI();
				}
			}
		});
		
		addBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(StringUtils.isNotBlank(attributeNameField.getText())){
					KbsTypeBean kbsTypeBean = new KbsTypeBean();
					kbsTypeBean.setParentTypeCode(selectAttributeGroupKbsTypeBean.getTypeCode());
					kbsTypeBean.setTypeCode(GeneraterUtil.genIdByTime());
					kbsTypeBean.setTypeName(attributeNameField.getText());
					SimpleKeyValueBean simpleKeyValueBean = (SimpleKeyValueBean)attributeTypeCombo.getSelectedItem();
					kbsTypeBean.setExpandTypeCode(simpleKeyValueBean.getKey());
					kbsTypeBean.setExpandTypeName(simpleKeyValueBean.getValue());
					kbsTypeBean.setExpandValue(attributeValueJTextArea.getText());
					kbsTypeBean = insert(kbsTypeBean);
					attributeListModel.addElement(kbsTypeBean);
				}else{
					MsgDialogUtil.createMessageDialog(LanguageLoader.getString("Kbs.attribute_add_name_is_empty"));
				}
			}
		});
		removeBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int[] selected = attributeJList.getSelectedIndices();
				KbsTypeBean kbsTypeBean = null;
				int index = 0;
				for (int i = 0; i < selected.length; ++i) {
					index = selected[i] - i;
					kbsTypeBean = (KbsTypeBean)attributeListModel.get(index);
					delete(kbsTypeBean);
					attributeListModel.removeElementAt(index);
				}
			}
		});
		updateBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(null != selectAttributeKbsTypeBean){
					selectAttributeKbsTypeBean.setTypeName(attributeNameField.getText());
					SimpleKeyValueBean simpleKeyValueBean = (SimpleKeyValueBean)attributeTypeCombo.getSelectedItem();
					selectAttributeKbsTypeBean.setExpandTypeCode(simpleKeyValueBean.getKey());
					selectAttributeKbsTypeBean.setExpandTypeName(simpleKeyValueBean.getValue());
					selectAttributeKbsTypeBean.setExpandValue(attributeValueJTextArea.getText());
					update(selectAttributeKbsTypeBean);
					attributeJList.updateUI();
				}
			}
		});
		
		attributeGroupJList.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				Object[] selecteds = attributeGroupJList.getSelectedValues();
				if(null != selecteds && selecteds.length > 0){
					removeGroupBtn.setEnabled(true);
					updateGroupBtn.setEnabled(true);
					addBtn.setEnabled(true);
					
					selectAttributeGroupKbsTypeBean = (KbsTypeBean)selecteds[0];
					attributeListModel.clear();
					List<KbsTypeBean> childList = getKbsTypeList(selectAttributeGroupKbsTypeBean.getTypeCode());
					if(!CollectionUtils.isEmpty(childList)){
						for(KbsTypeBean kbsTypeBean : childList){
							attributeListModel.addElement(kbsTypeBean);
						}
					}
					attributeGroupNameField.setText(selectAttributeGroupKbsTypeBean.getTypeName());
					parentNameField.setText(selectAttributeGroupKbsTypeBean.getTypeName());
				}else{
					removeGroupBtn.setEnabled(false);
					updateGroupBtn.setEnabled(false);
					addBtn.setEnabled(false);
					
					selectAttributeGroupKbsTypeBean = null;
					attributeListModel.clear();
					attributeGroupNameField.setText("");
					parentNameField.setText("");
					
				}
			}
		});
		
		attributeJList.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				Object[] selecteds = attributeJList.getSelectedValues();
				if(null != selecteds && selecteds.length > 0){
					removeBtn.setEnabled(true);
					updateBtn.setEnabled(true);
					
					selectAttributeKbsTypeBean = (KbsTypeBean)selecteds[0];
					attributeNameField.setText(selectAttributeKbsTypeBean.getTypeName());
					attributeTypeCombo.setSelectedItem(new SimpleKeyValueBean(selectAttributeKbsTypeBean.getExpandTypeCode(),selectAttributeKbsTypeBean.getExpandTypeName()));
					attributeValueJTextArea.setText(selectAttributeKbsTypeBean.getExpandValue());
				}else{
					removeBtn.setEnabled(false);
					updateBtn.setEnabled(false);
					
					selectAttributeKbsTypeBean = null;
					attributeNameField.setText("");
					attributeValueJTextArea.setText("");
				}
			}
		});
		
		attributeTypeCombo.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				SimpleKeyValueBean simpleKeyValueBean = (SimpleKeyValueBean)attributeTypeCombo.getSelectedItem();
				if(KbsConstant.KBS_TYPE_CHILD_ATTRIBUT_CMP_TYPE_RADIOBOX_CODE.equals(simpleKeyValueBean.getKey()) || KbsConstant.KBS_TYPE_CHILD_ATTRIBUT_CMP_TYPE_CHECKBOX_CODE.equals(simpleKeyValueBean.getKey())){
					attributeValueJTextArea.setEnabled(true);
					attributeValueJTextArea.setText("");
				}else{
					attributeValueJTextArea.setEnabled(false);
				}
				
			}
		});

	}
	/**
	 * 插入
	 * <p>方法说明:</>
	 * <li></li>
	 * @author DuanYong
	 * @since 2013-9-30 下午3:54:28
	 * @version 1.0
	 * @exception 
	 * @param kbsTypeBean
	 */
	private KbsTypeBean insert(KbsTypeBean kbsTypeBean){
		kbsTypeBean.setTypeCode(kbsTypeBean.getParentTypeCode()+"_"+kbsTypeBean.getTypeCode());
		int id = kbsTypeService.insert(kbsTypeBean,KbsConstant.SQLMAP_ID_INSERT_KBS_TYPE);
		kbsTypeBean.setId(id);
		return kbsTypeBean;
	}
	/**
	 * 删除
	 * <p>方法说明:</>
	 * <li></li>
	 * @author DuanYong
	 * @since 2013-9-30 下午3:54:41
	 * @version 1.0
	 * @exception 
	 * @param kbsTypeBean
	 */
	private void delete(KbsTypeBean kbsTypeBean){
		kbsTypeService.delete(kbsTypeBean,KbsConstant.SQLMAP_ID_DELETE_BY_ID_KBS_TYPE);
	}
	/**
	 * 删除
	 * <p>方法说明:</>
	 * <li></li>
	 * @author DuanYong
	 * @since 2013-9-30 下午3:54:41
	 * @version 1.0
	 * @exception 
	 * @param kbsTypeBean
	 */
	private void deleteByParentTypeCode(KbsTypeBean kbsTypeBean){
		kbsTypeService.delete(kbsTypeBean,KbsConstant.SQLMAP_ID_DELETE_BY_PARENTTYPECODE_KBS_TYPE);
	}
	/**
	 * 修改
	 * <p>方法说明:</>
	 * <li></li>
	 * @author DuanYong
	 * @since 2013-9-30 下午3:55:18
	 * @version 1.0
	 * @exception 
	 * @param kbsTypeBean
	 */
	private void update(KbsTypeBean kbsTypeBean){
		kbsTypeService.update(kbsTypeBean,KbsConstant.SQLMAP_ID_UPDATE_KBS_TYPE);
	}
	public void initData(List<SimpleKeyValueBean> t){
		if(t == null){
			t = new ArrayList<SimpleKeyValueBean>();
		}
		fillComponentData(t);
	}
	/* (non-Javadoc)
	 * @see org.javacoo.cowswing.ui.view.panel.AbstractContentPanel#fillComponentData(java.lang.Object)
	 */
	@Override
	protected void fillComponentData(List<SimpleKeyValueBean> t) {
		attributeGroupListModel.clear();
		List<KbsTypeBean> childList = getKbsTypeList(KbsConstant.KBS_TYPE_CHILD_ATTRIBUT_CODE);
		if(!CollectionUtils.isEmpty(childList)){
			for(KbsTypeBean kbsTypeBean : childList){
				attributeGroupListModel.addElement(kbsTypeBean);
			}
		}
		attributeTypeComboBoxModel.removeAllElements();
		attributeTypeComboBoxModel.addElement(new SimpleKeyValueBean(KbsConstant.KBS_TYPE_CHILD_ATTRIBUT_CMP_TYPE_TEXT_CODE,LanguageLoader.getString("Kbs.attribute_cmp_type_name_text")));
		attributeTypeComboBoxModel.addElement(new SimpleKeyValueBean(KbsConstant.KBS_TYPE_CHILD_ATTRIBUT_CMP_TYPE_TEXTAREA_CODE,LanguageLoader.getString("Kbs.attribute_cmp_type_name_textarea")));
		attributeTypeComboBoxModel.addElement(new SimpleKeyValueBean(KbsConstant.KBS_TYPE_CHILD_ATTRIBUT_CMP_TYPE_DATE_CODE,LanguageLoader.getString("Kbs.attribute_cmp_type_name_date")));
		attributeTypeComboBoxModel.addElement(new SimpleKeyValueBean(KbsConstant.KBS_TYPE_CHILD_ATTRIBUT_CMP_TYPE_RADIOBOX_CODE,LanguageLoader.getString("Kbs.attribute_cmp_type_name_radiobox")));
		attributeTypeComboBoxModel.addElement(new SimpleKeyValueBean(KbsConstant.KBS_TYPE_CHILD_ATTRIBUT_CMP_TYPE_CHECKBOX_CODE,LanguageLoader.getString("Kbs.attribute_cmp_type_name_checkbox")));
		reFlush();
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
	/**
	 * 刷新
	 * <p>方法说明:</>
	 * <li></li>
	 * @author DuanYong
	 * @since 2013-9-30 上午10:34:25
	 * @version 1.0
	 * @exception
	 */
	private void reFlush(){
		attributeGroupCodeField.setText(GeneraterUtil.genIdByTime());
		attributeGroupNameField.setText("");
	}
	protected void addCrawlerListener(){
		
	}
}
