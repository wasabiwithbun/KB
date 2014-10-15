/**
 * 如石子一粒,仰高山之巍峨,但不自惭形秽.
 * 若小草一棵,慕白杨之伟岸,却不妄自菲薄.
 */
package org.javacoo.cowswing.plugin.kbs.ui.model;

import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractListModel;
import javax.swing.ComboBoxModel;

import org.javacoo.cowswing.plugin.kbs.service.beans.SimpleKeyValueBean;

/**
 * 权限下拉选择model
 * <p>说明:</p>
 * <li></li>
 * @author DuanYong
 * @since 2013-7-17 上午10:09:06
 * @version 1.0
 */
public class PurviewComboBoxModel extends AbstractListModel implements ComboBoxModel{
	private static final long serialVersionUID = 1L;
    private SimpleKeyValueBean simpleKeyValueBean;
    private List<SimpleKeyValueBean> purviewList = new ArrayList<SimpleKeyValueBean>();
    public PurviewComboBoxModel(){
    }
    public PurviewComboBoxModel(List<SimpleKeyValueBean> purviewList){
    	this.purviewList.addAll(purviewList);
    }
	/* (non-Javadoc)
	 * @see javax.swing.ListModel#getElementAt(int)
	 */
	@Override
	public Object getElementAt(int index) {
		return this.purviewList.get(index);
	}

	/* (non-Javadoc)
	 * @see javax.swing.ListModel#getSize()
	 */
	@Override
	public int getSize() {
		return this.purviewList.size();
	}

	/* (non-Javadoc)
	 * @see javax.swing.ComboBoxModel#getSelectedItem()
	 */
	@Override
	public Object getSelectedItem() {
		// TODO Auto-generated method stub
		return this.simpleKeyValueBean;
	}

	/* (non-Javadoc)
	 * @see javax.swing.ComboBoxModel#setSelectedItem(java.lang.Object)
	 */
	@Override
	public void setSelectedItem(Object arg0) {
		this.simpleKeyValueBean = (SimpleKeyValueBean) arg0;
	}

}