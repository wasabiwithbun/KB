/**
 * 如石子一粒,仰高山之巍峨,但不自惭形秽.
 * 若小草一棵,慕白杨之伟岸,却不妄自菲薄.
 */
package org.javacoo.cowswing.plugin.kbs.ui.view.panel;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import org.apache.commons.lang.StringUtils;
import org.javacoo.cowswing.base.loader.LanguageLoader;
import org.javacoo.cowswing.base.utils.JsonUtils;
import org.javacoo.cowswing.plugin.kbs.constant.KbsConstant;
import org.javacoo.cowswing.plugin.kbs.service.beans.KbsModuleArticleBean;
import org.javacoo.cowswing.plugin.kbs.service.beans.KbsTypeBean;
import org.javacoo.cowswing.plugin.kbs.service.beans.SimpleKeyValueBean;
import org.javacoo.cowswing.plugin.kbs.ui.model.OriginComboBoxModel;
import org.javacoo.cowswing.plugin.kbs.ui.model.PurviewComboBoxModel;
import org.javacoo.cowswing.ui.listener.TextVerifier;
import org.javacoo.cowswing.ui.view.panel.AbstractContentPanel;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

/**
 * 添加资源
 * <p>说明:</p>
 * <li></li>
 * @author DuanYong
 * @since 2013-7-16 下午5:18:31
 * @version 1.0
 */
@Component("addArticlePanel")
public class AddArticlePanel extends AbstractContentPanel<KbsModuleArticleBean>{
	private static final long serialVersionUID = 1L;
	private Box containerBox;
	private Box itemContainerBox;
	private Box extendItemContainerBox;
	private Box contentContainerBox;
	/**节点名称标签*/
	private javax.swing.JLabel typeCodeLabel;
	/**节点名称输入框*/
	private javax.swing.JTextField typeNameField;
	/**标题名称标签*/
	private javax.swing.JLabel titleLabel;
	/**标题名称输入框*/
	private javax.swing.JTextField titleField;
	/**关键字标签*/
	private javax.swing.JLabel keywordLabel;
	/**关键字输入框*/
	private javax.swing.JTextField keywordField;
	/**权限标签*/
	private javax.swing.JLabel purviewLabel;
	/**权限下拉*/
	private JComboBox purviewCombo;
	/***权限默认ComboBoxModel*/
	private DefaultComboBoxModel purviewComboBoxModel;
	/**来源标签*/
	private javax.swing.JLabel originLabel;
	/**来源下拉*/
	private JComboBox originCombo;
	/***来源默认ComboBoxModel*/
	private DefaultComboBoxModel originComboBoxModel;
	/**分类编码*/
	private String typeCode = "";
	/**编辑器*/
	private JEditorPane editorPanel;
	/**权限MAP*/
	private static Map<String,SimpleKeyValueBean> purviewMap = new HashMap<String,SimpleKeyValueBean>();
	static{
		purviewMap.put(KbsConstant.SEARCH_PURVIEW_PERSON,new SimpleKeyValueBean(KbsConstant.SEARCH_PURVIEW_PERSON,LanguageLoader.getString("Kbs.module_article_add_panel_purview_person")));
		purviewMap.put(KbsConstant.SEARCH_PURVIEW_VIEW,new SimpleKeyValueBean(KbsConstant.SEARCH_PURVIEW_VIEW,LanguageLoader.getString("Kbs.module_article_add_panel_purview_view")));
	}
	/**来源MAP*/
	private static Map<String,SimpleKeyValueBean> originMap = new HashMap<String,SimpleKeyValueBean>();
	static{
		originMap.put(KbsConstant.ORIGIN_PERSON,new SimpleKeyValueBean(KbsConstant.ORIGIN_PERSON,LanguageLoader.getString("Kbs.module_article_add_panel_origin_person")));
		originMap.put(KbsConstant.ORIGIN_NETWORK,new SimpleKeyValueBean(KbsConstant.ORIGIN_NETWORK,LanguageLoader.getString("Kbs.module_article_add_panel_origin_newwork")));
	}
	/**FIELD列*/
	private final static int FIELD_COLUMNS = 35;
	/**LABEL宽度*/
	private final static int LABEL_WIDTH = 80;
	/**LABEL高度*/
	private final static int LABEL_HEIGHT = 21;
	private Dimension labelDimension = new Dimension(LABEL_WIDTH, LABEL_HEIGHT);
	/**FIELD宽度*/
	private final static int FIELD_WIDTH = 400;
	/**FIELD高度*/
	private final static int FIELD_HEIGHT = 21;
	private Dimension fieldDimension = new Dimension(FIELD_WIDTH, FIELD_HEIGHT);
	private Map<String,Map<String,List<JComponent>>> compMap = new HashMap<String,Map<String,List<JComponent>>>();
	/* (non-Javadoc)
	 * @see org.javacoo.cowswing.ui.view.panel.AbstractContentPanel#populateData()
	 */
	@Override
	protected KbsModuleArticleBean populateData() {
		KbsModuleArticleBean kbsModuleArticleBean = new KbsModuleArticleBean();
		kbsModuleArticleBean.setTypeCode(typeCode);
		kbsModuleArticleBean.setTypeName(typeNameField.getText());
		kbsModuleArticleBean.setTitle(titleField.getText());
		kbsModuleArticleBean.setKeyword(keywordField.getText());
		SimpleKeyValueBean origin = (SimpleKeyValueBean)originCombo.getSelectedItem();
		kbsModuleArticleBean.setOrigin(origin.getKey());
		SimpleKeyValueBean purview = (SimpleKeyValueBean)purviewCombo.getSelectedItem();
		kbsModuleArticleBean.setPurview(purview.getKey());
		Map<String,String> dynamaicValuesMap = parserDynamicCmp();
		dynamaicValuesMap.put(KbsConstant.KBS_TYPE_CHILD_EDITOR_CODE, editorPanel.getText());
		String json = JsonUtils.formatObjectToJsonString(dynamaicValuesMap);
		logger.info("json="+json);
		kbsModuleArticleBean.setContent(json);
		return kbsModuleArticleBean;
	}
	/**
	 * 解析动态组件值
	 * <p>方法说明:</>
	 * <li></li>
	 * @author DuanYong
	 * @since 2013-10-4 下午3:18:21
	 * @version 1.0
	 * @exception 
	 * @return Map
	 */
	private Map<String,String> parserDynamicCmp(){
		Map<String,String> returnMap = new HashMap<String,String>();
		if(!compMap.isEmpty()){
			String key = "";
			String type = "";
			Map<String,List<JComponent>> itemMap= null;
			for(Iterator<String> it = compMap.keySet().iterator();it.hasNext();){
				key = it.next();
				itemMap = compMap.get(key);
				if(null != itemMap){
					for(Iterator<String> item = itemMap.keySet().iterator();item.hasNext();){
						type = item.next();
						if(CollectionUtils.isEmpty(itemMap.get(type))){
							continue;
						}
						
						if(KbsConstant.KBS_TYPE_CHILD_ATTRIBUT_CMP_TYPE_TEXT_CODE.equals(type)){
							JTextField field = (JTextField)itemMap.get(type).get(0);
							returnMap.put(key, field.getText());
							logger.info("type="+type+",key="+key+",vlaue="+field.getText());
						}else if(KbsConstant.KBS_TYPE_CHILD_ATTRIBUT_CMP_TYPE_TEXTAREA_CODE.equals(type)){
							JTextArea area = (JTextArea)itemMap.get(type).get(0);
							returnMap.put(key, area.getText());
							logger.info("type="+type+",key="+key+",vlaue="+area.getText());
						}else if(KbsConstant.KBS_TYPE_CHILD_ATTRIBUT_CMP_TYPE_DATE_CODE.equals(type)){
							JTextField date = (JTextField)itemMap.get(type).get(0);
							returnMap.put(key, date.getText());
							logger.info("type="+type+",key="+key+",vlaue="+date.getText());
						}else if(KbsConstant.KBS_TYPE_CHILD_ATTRIBUT_CMP_TYPE_RADIOBOX_CODE.equals(type)){
							List<JComponent> cmpList = itemMap.get(type);
							JRadioButton radioButton = null;
							String value = "";
							for(JComponent jcmp : cmpList){
								radioButton = (JRadioButton)jcmp;
								if(radioButton.isSelected()){
									value = radioButton.getText();
									break;
								}
							}
							returnMap.put(key, value);
							logger.info("type="+type+",key="+key+",vlaue="+value);
						}else if(KbsConstant.KBS_TYPE_CHILD_ATTRIBUT_CMP_TYPE_CHECKBOX_CODE.equals(type)){
							List<JComponent> cmpList = itemMap.get(type);
							JCheckBox checkBox = null;
							String value = "";
							for(JComponent jcmp : cmpList){
								checkBox = (JCheckBox)jcmp;
								if(checkBox.isSelected()){
									value = checkBox.getText();
									break;
								}
							}
							returnMap.put(key, value);
							logger.info("type="+type+",key="+key+",vlaue="+value);
						}
					}
				}
			}
		}
		return returnMap;
	}

	/* (non-Javadoc)
	 * @see org.javacoo.cowswing.ui.view.panel.AbstractContentPanel#initComponents()
	 */
	@Override
	protected void initComponents() {
		containerBox = new Box(BoxLayout.Y_AXIS);
		itemContainerBox = new Box(BoxLayout.Y_AXIS);
		extendItemContainerBox = new Box(BoxLayout.Y_AXIS);
		
		
		typeCodeLabel = new javax.swing.JLabel();
		typeCodeLabel.setText(LanguageLoader.getString("Kbs.module_source_add_panel_typeName"));
		
		
		typeNameField = new javax.swing.JTextField();
		typeNameField.setColumns(FIELD_COLUMNS);
		typeNameField.setText("");
		typeNameField.setEnabled(false);
		
		itemContainerBox.add(createJPanel(typeCodeLabel,typeNameField));
		
		titleLabel = new javax.swing.JLabel();
		titleLabel.setPreferredSize(labelDimension);
		titleLabel.setText(LanguageLoader.getString("Kbs.module_article_add_panel_title"));
		
		
		titleField = new javax.swing.JTextField();
		titleField.setColumns(FIELD_COLUMNS);
		titleField.setInputVerifier(new TextVerifier(this,false));
		titleField.setText("");
		titleField.setPreferredSize(fieldDimension);
		
		itemContainerBox.add(createJPanel(titleLabel,titleField));
		
		keywordLabel = new javax.swing.JLabel();
		keywordLabel.setPreferredSize(labelDimension);
		keywordLabel.setText(LanguageLoader.getString("Kbs.module_article_add_panel_keyword"));
		
		
		keywordField = new javax.swing.JTextField();
		keywordField.setColumns(FIELD_COLUMNS);
		keywordField.setInputVerifier(new TextVerifier(this,false));
		keywordField.setText("");
		keywordField.setPreferredSize(fieldDimension);
		
		itemContainerBox.add(createJPanel(keywordLabel,keywordField));
		
		
		
		
		purviewLabel = new javax.swing.JLabel();
		purviewLabel.setPreferredSize(labelDimension);
		purviewLabel.setText(LanguageLoader.getString("Kbs.module_article_add_panel_purview"));
		
		
		purviewComboBoxModel = new DefaultComboBoxModel();
		purviewCombo = new JComboBox(purviewComboBoxModel);
		
		itemContainerBox.add(createJPanel(purviewLabel,purviewCombo));
		
		
		
		originLabel = new javax.swing.JLabel();
		originLabel.setText(LanguageLoader.getString("Kbs.module_article_add_panel_origin"));
		
		
		originComboBoxModel = new DefaultComboBoxModel();
		originCombo = new JComboBox(originComboBoxModel);
		itemContainerBox.add(createJPanel(originLabel,originCombo));
		
		contentContainerBox = new Box(BoxLayout.Y_AXIS);
		editorPanel = new JEditorPane();
		editorPanel.setEditable(true);
		//editorPanel.setContentType("text/html");
		JScrollPane editorScrollPane = new JScrollPane(editorPanel);
		editorScrollPane.setPreferredSize(new Dimension(420, 160));
		contentContainerBox.add(editorScrollPane);
		

		containerBox.add(itemContainerBox);
		containerBox.add(extendItemContainerBox);
		containerBox.add(contentContainerBox);
		
		JScrollPane topScrollPane = new JScrollPane(containerBox);
		topScrollPane.setBounds(0, 0, 515, 370);
		add(topScrollPane);
	}
	/**
	 * 根据label和JComponent组件创建JPanel
	 * <p>方法说明:</>
	 * <li></li>
	 * @author DuanYong
	 * @since 2013-10-4 上午11:09:48
	 * @version 1.0
	 * @exception 
	 * @param label
	 * @param comp
	 * @return
	 */
	private JPanel createJPanel(JLabel label,JComponent comp){
		JPanel contentPanel = new JPanel();
		contentPanel.setLayout(new FlowLayout(FlowLayout.LEFT,5,5));
		label.setPreferredSize(new Dimension(LABEL_WIDTH, LABEL_HEIGHT));
		contentPanel.add(label);
		if(null != comp){
			comp.setPreferredSize(new Dimension(FIELD_WIDTH, FIELD_HEIGHT));
			contentPanel.add(comp);	
		}
		return contentPanel;
	}
	/**
	 * 初始化事件
	 * <p>方法说明:</p>
	 * @auther DuanYong
	 * @since 2012-11-16 上午11:00:17
	 * @return void
	 */
	protected void initActionListener(){
		
		

	}
	public void initData(KbsModuleArticleBean t){
		if(t == null){
			t = new KbsModuleArticleBean();
		}
		fillComponentData(t);
	}
	/* (non-Javadoc)
	 * @see org.javacoo.cowswing.ui.view.panel.AbstractContentPanel#fillComponentData(java.lang.Object)
	 */
	@Override
	protected void fillComponentData(KbsModuleArticleBean t) {
		List<SimpleKeyValueBean> purviewList = new ArrayList<SimpleKeyValueBean>();
		String tempPurviewKey = "";
		for(Iterator<String> it = purviewMap.keySet().iterator();it.hasNext();){
			tempPurviewKey = it.next();
			purviewList.add(purviewMap.get(tempPurviewKey));
		}
		purviewCombo.setModel(new PurviewComboBoxModel(purviewList));
		if(StringUtils.isNotBlank(t.getPurview())){
			purviewCombo.setSelectedItem(purviewMap.get(t.getPurview()));
		}else{
			purviewCombo.setSelectedIndex(0);
		}
		purviewCombo.repaint();
		List<SimpleKeyValueBean> originList = new ArrayList<SimpleKeyValueBean>();
		String tempOriginKey = "";
		for(Iterator<String> it = originMap.keySet().iterator();it.hasNext();){
			tempOriginKey = it.next();
			originList.add(originMap.get(tempOriginKey));
		}
		originCombo.setModel(new OriginComboBoxModel(originList));
		if(StringUtils.isNotBlank(t.getOrigin())){
			originCombo.setSelectedItem(originMap.get(t.getOrigin()));
		}else{
			originCombo.setSelectedIndex(0);
		}
		originCombo.repaint();
		if(StringUtils.isNotBlank(t.getTypeName())){
			typeCode = t.getTypeCode();
			typeNameField.setText(t.getTypeName());
		}
		if(StringUtils.isNotBlank(t.getTitle())){
			titleField.setText(t.getTitle());
		}else{
			titleField.setText("");
		}
		if(StringUtils.isNotBlank(t.getKeyword())){
			keywordField.setText(t.getTitle());
		}else{
			keywordField.setText("");
		}
		Map<String,String> valueMap =  new HashMap<String,String>();
		if(StringUtils.isNotBlank(t.getContent())){
			valueMap = (Map<String, String>) JsonUtils.formatStringToObject(t.getContent(), Map.class);
			editorPanel.setText(valueMap.get(KbsConstant.KBS_TYPE_CHILD_EDITOR_CODE));
		}else{
			editorPanel.setText("");
		}
		createDynamicCmp(t,valueMap);
		
	}
	/**
	 * 动态生成组件
	 * <p>方法说明:</>
	 * <li></li>
	 * @author DuanYong
	 * @since 2013-10-4 下午3:28:37
	 * @version 1.0
	 * @exception 
	 * @param t
	 * @param valueMap
	 */
	private void createDynamicCmp(KbsModuleArticleBean t,Map<String,String> valueMap){
		extendItemContainerBox.removeAll();
		compMap.clear();
		if(!CollectionUtils.isEmpty(t.getKbsTypeBeanList())){
			List<KbsTypeBean> kbsTypeList = t.getKbsTypeBeanList();
			String cmpKey = "";
			String value = "";
			Map<String,List<JComponent>> cmpsMap = null;
			List<JComponent> cmps = null;
			for(KbsTypeBean kbsTypeBean : kbsTypeList){
				cmps = new ArrayList<JComponent>();
				cmpsMap = new HashMap<String,List<JComponent>>();
				if(!valueMap.isEmpty() && valueMap.containsKey(kbsTypeBean.getTypeCode())){
					value = valueMap.get(kbsTypeBean.getTypeCode());
				}else{
					value = "";
				}
				if(KbsConstant.KBS_TYPE_CHILD_ATTRIBUT_CMP_TYPE_TEXT_CODE.equals(kbsTypeBean.getExpandTypeCode())){
					JTextField field = new javax.swing.JTextField();
					field.setColumns(FIELD_COLUMNS);
					field.setText(value);
					
					JLabel label = new javax.swing.JLabel();
					label.setText(kbsTypeBean.getTypeName());
					extendItemContainerBox.add(createJPanel(label,field));
					cmps.add(field);
				}else if(KbsConstant.KBS_TYPE_CHILD_ATTRIBUT_CMP_TYPE_TEXTAREA_CODE.equals(kbsTypeBean.getExpandTypeCode())){
					JTextArea area = new javax.swing.JTextArea();
					area.setColumns(FIELD_COLUMNS);
					area.setRows(5);
					area.setText(value);
					
					JLabel label = new javax.swing.JLabel();
					label.setText(kbsTypeBean.getTypeName());
					extendItemContainerBox.add(createJPanel(label,area));
					cmps.add(area);
				}else if(KbsConstant.KBS_TYPE_CHILD_ATTRIBUT_CMP_TYPE_DATE_CODE.equals(kbsTypeBean.getExpandTypeCode())){
					JTextField date = new javax.swing.JTextField();
					date.setColumns(FIELD_COLUMNS);
					date.setText(value);
					
					JLabel label = new javax.swing.JLabel();
					label.setText(kbsTypeBean.getTypeName());
					extendItemContainerBox.add(createJPanel(label,date));
					cmps.add(date);
				}else if(KbsConstant.KBS_TYPE_CHILD_ATTRIBUT_CMP_TYPE_RADIOBOX_CODE.equals(kbsTypeBean.getExpandTypeCode())){
					if(StringUtils.isNotBlank(kbsTypeBean.getExpandValue())){
						ButtonGroup group= new ButtonGroup();
						String[] values = kbsTypeBean.getExpandValue().split(",");
						JRadioButton radioButton = null;
						
						JLabel label = new javax.swing.JLabel();
						label.setText(kbsTypeBean.getTypeName());
						JPanel panel = createJPanel(label,null);
						for(String val : values){
							radioButton = new JRadioButton(val);
							if(val.equals(value)){
								radioButton.setSelected(true);
							}
							group.add(radioButton);
							panel.add(radioButton);
							cmps.add(radioButton);
						}
						extendItemContainerBox.add(panel);
					}
				}else if(KbsConstant.KBS_TYPE_CHILD_ATTRIBUT_CMP_TYPE_CHECKBOX_CODE.equals(kbsTypeBean.getExpandTypeCode())){
					if(StringUtils.isNotBlank(kbsTypeBean.getExpandValue())){
						ButtonGroup group= new ButtonGroup();
						String[] values = kbsTypeBean.getExpandValue().split(",");
						JCheckBox checkBox = null;
						
						JLabel label = new javax.swing.JLabel();
						label.setText(kbsTypeBean.getTypeName());
						JPanel panel = createJPanel(label,null);
						for(String val: values){
							checkBox = new JCheckBox(val);
							if(val.equals(value)){
								checkBox.setSelected(true);
							}
							group.add(checkBox);
							panel.add(checkBox);
							cmps.add(checkBox);
						}
						extendItemContainerBox.add(panel);
					}
				}
				cmpKey = kbsTypeBean.getTypeCode();
				cmpsMap.put(kbsTypeBean.getExpandTypeCode(), cmps);
				compMap.put(cmpKey, cmpsMap);
			}
			
		}
	}
	protected void addCrawlerListener(){
		
	}

}
