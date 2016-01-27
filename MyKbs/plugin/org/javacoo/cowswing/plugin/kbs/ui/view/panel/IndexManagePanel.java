/**
 * 如石子一粒,仰高山之巍峨,但不自惭形秽.
 * 若小草一棵,慕白杨之伟岸,却不妄自菲薄.
 */
package org.javacoo.cowswing.plugin.kbs.ui.view.panel;

import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JScrollPane;

import org.apache.log4j.Logger;
import org.javacoo.cowswing.base.loader.LanguageLoader;
import org.javacoo.cowswing.plugin.kbs.service.beans.SimpleKeyValueBean;
import org.javacoo.cowswing.ui.view.panel.AbstractContentPanel;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;


/**
 * 词典设置面板
 * 
 * <p>说明:</p>
 * <li></li>
 * @author DuanYong
 * @since 2013-9-26 下午4:00:59
 * @version 1.0
 */
@Component("indexManagePanel")
public class IndexManagePanel extends AbstractContentPanel<List<SimpleKeyValueBean>> {
	private static final long serialVersionUID = 1L;
	protected Logger logger = Logger.getLogger(this.getClass());
	/**操作说明标签*/
	private javax.swing.JLabel operDescLabel;
	/**选择的模块类别标签*/
	private javax.swing.JLabel selectFileLabel;
	/**拖拽说明标签*/
	private javax.swing.JLabel dropLabel;
	/**模块列表*/
	private JList indesJList;
	/**模块Model*/
	private DefaultListModel indexListModel;
	
	/* (non-Javadoc)
	 * @see org.javacoo.cowswing.ui.view.panel.AbstractContentPanel#populateData()
	 */
	@Override
	protected List<SimpleKeyValueBean> populateData() {
		List<SimpleKeyValueBean> indexList = new ArrayList<SimpleKeyValueBean>();
		Object[] selecteds = indesJList.getSelectedValues();
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
		operDescLabel.setText(LanguageLoader.getString("Kbs.dic_manage_operDesc"));
		add(operDescLabel);
		operDescLabel.setBounds(20, 15, 80, 15);
		
		
		dropLabel = new javax.swing.JLabel();
		dropLabel.setText(LanguageLoader.getString("Kbs.index_manage_dropLabel"));
		add(dropLabel);
		dropLabel.setBounds(110, 15, 320, 15);
		
		
		selectFileLabel = new javax.swing.JLabel();
		selectFileLabel.setText(LanguageLoader.getString("Kbs.index_manage_list"));
		add(selectFileLabel);
		selectFileLabel.setBounds(20, 45, 80, 15);
		
		
		indexListModel = new DefaultListModel();
		indesJList = new JList(indexListModel);
		JScrollPane fileListJScrollPane = new JScrollPane(indesJList);
		add(fileListJScrollPane);
		fileListJScrollPane.setBounds(110, 45, 320, 100);
		
		
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
		indexListModel.clear();
		if(!CollectionUtils.isEmpty(t)){
			for(SimpleKeyValueBean s : t){
				indexListModel.addElement(s);
	    	}
		}
	}
	protected void addCrawlerListener(){
		
	}
}
