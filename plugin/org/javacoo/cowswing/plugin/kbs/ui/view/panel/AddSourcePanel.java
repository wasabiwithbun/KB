/**
 * 如石子一粒,仰高山之巍峨,但不自惭形秽.
 * 若小草一棵,慕白杨之伟岸,却不妄自菲薄.
 */
package org.javacoo.cowswing.plugin.kbs.ui.view.panel;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

import org.apache.commons.lang.StringUtils;
import org.javacoo.cowswing.base.constant.Constant;
import org.javacoo.cowswing.base.loader.ImageLoader;
import org.javacoo.cowswing.base.loader.LanguageLoader;
import org.javacoo.cowswing.base.utils.FileUtils;
import org.javacoo.cowswing.plugin.kbs.constant.KbsConstant;
import org.javacoo.cowswing.plugin.kbs.service.beans.KbsModuleSourceBean;
import org.javacoo.cowswing.plugin.kbs.service.beans.SimpleKeyValueBean;
import org.javacoo.cowswing.plugin.kbs.ui.dialog.AddSourceDialog;
import org.javacoo.cowswing.plugin.kbs.ui.model.PurviewComboBoxModel;
import org.javacoo.cowswing.ui.listener.TextVerifier;
import org.javacoo.cowswing.ui.view.dialog.WaitingDialog;
import org.javacoo.cowswing.ui.view.panel.AbstractContentPanel;
import org.springframework.stereotype.Component;

/**
 * 添加资源
 * <p>说明:</p>
 * <li></li>
 * @author DuanYong
 * @since 2013-7-16 下午5:18:31
 * @version 1.0
 */
@Component("addSourcePanel")
public class AddSourcePanel extends AbstractContentPanel<KbsModuleSourceBean>{
	private static final long serialVersionUID = 1L;
	/**父对象*/
	private AddSourceDialog parentDialog;
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
	/**操作说明标签*/
	private javax.swing.JLabel operDescLabel;
	/** 是否包含子目录 */
	private JCheckBox childDirCheckBox;
	/**选择的文件类别标签*/
	private javax.swing.JLabel selectFileLabel;
	/** 资选择按钮 */
	private JButton addFileBtn;
	/**删除按钮*/
	private JButton removeBtn;
	/**删除全部按钮*/
	private JButton removeAllBtn;
	/**拖拽说明标签*/
	private javax.swing.JLabel dropLabel;
	/**文件列表*/
	private JList fileList;
	/**文件Model*/
	private DefaultListModel fileListModel;
	/**分类编码*/
	private String typeCode = "";
	/**是否包含子目录*/
	private String childDirCehckValue = Constant.NO;
	
	public void setParentDialog(AddSourceDialog parentDialog){
		this.parentDialog = parentDialog;
	}
	/* (non-Javadoc)
	 * @see org.javacoo.cowswing.ui.view.panel.AbstractContentPanel#populateData()
	 */
	@Override
	protected KbsModuleSourceBean populateData() {
		KbsModuleSourceBean kbsModuleSourceBean = new KbsModuleSourceBean();
		kbsModuleSourceBean.setTypeCode(typeCode);
		kbsModuleSourceBean.setTypeName(typeNameField.getText());
		kbsModuleSourceBean.setTitle(titleField.getText());
		kbsModuleSourceBean.setKeyword(titleField.getText());
		kbsModuleSourceBean.setChildDir(this.childDirCehckValue);
		kbsModuleSourceBean.setFilePath("");
		List<String> filePathList = new ArrayList<String>();
		for(int i = 0;i<fileListModel.size();i++){
			filePathList.add(fileListModel.getElementAt(i).toString());
		}
		kbsModuleSourceBean.setFilePathList(filePathList);
		SimpleKeyValueBean purview = (SimpleKeyValueBean)purviewCombo.getSelectedItem();
		kbsModuleSourceBean.setPurview(purview.getKey());
		return kbsModuleSourceBean;
	}

	/* (non-Javadoc)
	 * @see org.javacoo.cowswing.ui.view.panel.AbstractContentPanel#initComponents()
	 */
	@Override
	protected void initComponents() {
		
		typeCodeLabel = new javax.swing.JLabel();
		typeCodeLabel.setText(LanguageLoader.getString("Kbs.module_source_add_panel_typeName"));
		add(typeCodeLabel);
		typeCodeLabel.setBounds(20, 15, 80, 15);
		
		typeNameField = new javax.swing.JTextField();
		typeNameField.setColumns(20);
		typeNameField.setText("");
		typeNameField.setEnabled(false);
		add(typeNameField);
		typeNameField.setBounds(110, 15, 320, 21);
		
		
		titleLabel = new javax.swing.JLabel();
		titleLabel.setText(LanguageLoader.getString("Kbs.module_source_add_panel_title"));
		add(titleLabel);
		titleLabel.setBounds(20, 45, 80, 15);
		
		titleField = new javax.swing.JTextField();
		titleField.setColumns(20);
		titleField.setInputVerifier(new TextVerifier(this,false));
		titleField.setText("");
		add(titleField);
		titleField.setBounds(110, 45, 320, 21);
		
		keywordLabel = new javax.swing.JLabel();
		keywordLabel.setText(LanguageLoader.getString("Kbs.module_source_add_panel_keyword"));
		add(keywordLabel);
		keywordLabel.setBounds(20, 75, 80, 15);
		
		keywordField = new javax.swing.JTextField();
		keywordField.setColumns(20);
		keywordField.setInputVerifier(new TextVerifier(this,false));
		keywordField.setText("");
		add(keywordField);
		keywordField.setBounds(110, 75, 320, 21);
		
		purviewLabel = new javax.swing.JLabel();
		purviewLabel.setText(LanguageLoader.getString("Kbs.module_source_add_panel_purview"));
		add(purviewLabel);
		purviewLabel.setBounds(20, 105, 80, 15);
		
		purviewComboBoxModel = new DefaultComboBoxModel();
		purviewCombo = new JComboBox(purviewComboBoxModel);
		purviewCombo.setBounds(110, 105, 320, 21);
		add(purviewCombo);
		
		operDescLabel = new javax.swing.JLabel();
		operDescLabel.setText(LanguageLoader.getString("Kbs.module_source_add_panel_operDesc"));
		add(operDescLabel);
		operDescLabel.setBounds(20, 135, 80, 15);
		
		
		dropLabel = new javax.swing.JLabel();
		dropLabel.setText(LanguageLoader.getString("Kbs.module_source_add_panel_dropLabel"));
		add(dropLabel);
		dropLabel.setBounds(110, 135, 320, 15);
		
		
		selectFileLabel = new javax.swing.JLabel();
		selectFileLabel.setText(LanguageLoader.getString("Kbs.module_source_add_panel_selectFileLabel"));
		add(selectFileLabel);
		selectFileLabel.setBounds(20, 165, 80, 15);
		
		
		addFileBtn = new JButton(LanguageLoader.getString("Kbs.module_source_add_panel_select"),ImageLoader.getImageIcon("CrawlerResource.kbs_source_browse"));
		addFileBtn.setBounds(110, 165, 60, 21);
		add(addFileBtn);
		
		
		removeBtn = new JButton(LanguageLoader.getString("Kbs.module_source_add_panel_delete"),ImageLoader.getImageIcon("CrawlerResource.kbs_source_delete"));
		add(removeBtn);
		removeBtn.setBounds(180, 165, 60, 21);
		
		removeAllBtn = new JButton(LanguageLoader.getString("Kbs.module_source_add_panel_deleteAll"),ImageLoader.getImageIcon("CrawlerResource.kbs_source_delete"));
		add(removeAllBtn);
		removeAllBtn.setBounds(250, 165, 100, 21);
		
		childDirCheckBox = new JCheckBox(LanguageLoader.getString("Kbs.module_source_add_panel_childDir"));
		childDirCheckBox.setBounds(355, 165, 150, 21);
		add(childDirCheckBox);
		
		
		fileListModel = new DefaultListModel();
		fileList = new JList(fileListModel);
		JScrollPane fileListJScrollPane = new JScrollPane(fileList);
		add(fileListJScrollPane);
		fileListJScrollPane.setBounds(110, 195, 320, 120);
		
		new DropTarget(fileList, new FileDropTargetListener());  
		
	}
	/**
	 * 初始化事件
	 * <p>方法说明:</p>
	 * @auther DuanYong
	 * @since 2012-11-16 上午11:00:17
	 * @return void
	 */
	protected void initActionListener(){
		childDirCheckBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(childDirCheckBox.isSelected()){
					childDirCehckValue = Constant.YES;
				}else{
					childDirCehckValue = Constant.NO;
				}
			}
		});
		
		addFileBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser jfc = new JFileChooser();
				jfc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
				jfc.setDialogTitle(LanguageLoader.getString("Kbs.module_source_select_title"));
				int result = jfc.showOpenDialog(null);
				if (result == 1) {
					return; // 撤销则返回
				} else {
					File f = jfc.getSelectedFile();// f为选择到的目录
					loadFile(f);

					fileList.updateUI();
				}
			}
		});
		
		removeBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int[] selected = fileList.getSelectedIndices();
				for (int i = 0; i < selected.length; ++i) {
					fileListModel.removeElementAt(selected[i] - i);
				}
			}
		});
		
		removeAllBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//fileListModel.removeAllElements();
				fileListModel.clear();
			}
		});


	}
	public void initData(KbsModuleSourceBean t){
		if(t == null){
			t = new KbsModuleSourceBean();
		}
		fillComponentData(t);
	}
	/* (non-Javadoc)
	 * @see org.javacoo.cowswing.ui.view.panel.AbstractContentPanel#fillComponentData(java.lang.Object)
	 */
	@Override
	protected void fillComponentData(KbsModuleSourceBean t) {
		List<SimpleKeyValueBean> purviewList = new ArrayList<SimpleKeyValueBean>();
		purviewList.add(new SimpleKeyValueBean(KbsConstant.SEARCH_PURVIEW_PERSON,LanguageLoader.getString("Kbs.module_source_add_panel_purview_person")));
		purviewList.add(new SimpleKeyValueBean(KbsConstant.SEARCH_PURVIEW_DOWNLOAD,LanguageLoader.getString("Kbs.module_source_add_panel_purview_down")));
		purviewCombo.setModel(new PurviewComboBoxModel(purviewList));
		purviewCombo.setSelectedIndex(0);
		purviewCombo.repaint();
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
			keywordField.setText(t.getKeyword());
		}else{
			keywordField.setText("");
		}
		fileListModel.clear();
	}
	
	/**
	 * 文件拖拽监听
	 * <p>说明:</p>
	 * <li></li>
	 * @author DuanYong
	 * @since 2013-7-18 下午2:15:45
	 * @version 1.0
	 */
	class FileDropTargetListener implements DropTargetListener {  
       
        public void dragEnter(DropTargetDragEvent event) {  
            if (!isDragAcceptable(event)) {  
                event.rejectDrag();  
                return;  
            }  
        }  
        public void dragExit(DropTargetEvent event) {  
        }  
      
        public void dragOver(DropTargetDragEvent event) {  
        }  
      
        public void dropActionChanged(DropTargetDragEvent event) {  
            if (!isDragAcceptable(event)) {  
                event.rejectDrag();  
                return;  
            }  
        }  
      
        public void drop(DropTargetDropEvent event) {  
            if (!isDropAcceptable(event)) {  
                event.rejectDrop();  
                return;  
            }  
            event.acceptDrop(DnDConstants.ACTION_COPY);  
            Transferable transferable = event.getTransferable();  
            DataFlavor[] flavors = transferable.getTransferDataFlavors();  
            for (int i = 0; i < flavors.length; i++) {  
                DataFlavor d = flavors[i];  
                try {  
                    if (d.equals(DataFlavor.javaFileListFlavor)) {  
                        List<?> fileList = (List<?>) transferable  
                                .getTransferData(d);  
                        Iterator<?> iterator = fileList.iterator();  
                        while (iterator.hasNext()) {  
                            File f = (File) iterator.next();  
                            loadFile(f);
                        }  
                    } 
                } catch (Exception e) {  
                	e.printStackTrace();
                }  
            }  
            event.dropComplete(true);  
        } 
        public boolean isDragAcceptable(DropTargetDragEvent event) {  
            return (event.getDropAction() & DnDConstants.ACTION_COPY_OR_MOVE) != 0;  
        }  
      
        public boolean isDropAcceptable(DropTargetDropEvent event) {  
            return (event.getDropAction() & DnDConstants.ACTION_COPY_OR_MOVE) != 0;  
        }  
	}
	/**
	 * 加载文件
	 * <p>方法说明:</>
	 * <li></li>
	 * @author DuanYong
	 * @since 2013-7-18 下午2:23:33
	 * @version 1.0
	 * @exception 
	 * @param file
	 */
	private void loadFile(final File file){
		final AddSourceDialog addSourceDialog = this.parentDialog;
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				//开启线程上传文件及建立索引
				Thread currThread = new Thread(new LoadFileTask(file));
				currThread.start();
				Thread waitingThread = new Thread(new WaitingDialog(addSourceDialog,currThread,LanguageLoader.getString("Kbs.module_source_add_panel_uploading")));
				waitingThread.start();
			}
		});
	}
	
	/**
	 * 加载选择的文件任务对象
	 * <p>说明:</p>
	 * <li></li>
	 * @author DuanYong
	 * @since 2013-7-18 下午2:25:29
	 * @version 1.0
	 */
	class LoadFileTask implements Runnable{
		private File file;
		public LoadFileTask(File file){
			this.file = file;
		}
		/* (non-Javadoc)
		 * @see java.lang.Runnable#run()
		 */
		@Override
		public void run() {
			populateFileListModel(this.file);
		}
		/**
		 * 组装FileListModel
		 * <p>方法说明:</>
		 * <li></li>
		 * @author DuanYong
		 * @since 2013-7-18 上午11:25:56
		 * @version 1.0
		 * @exception 
		 * @param f
		 */
		private void populateFileListModel(File f){
			List<File> allFileList = new ArrayList<File>();
			if(f.isFile()){
				allFileList.add(f);
			}else if(f.isDirectory()){
				//如果包子目录
	        	if(Constant.YES.equals(childDirCehckValue)){
	        		allFileList = FileUtils.getFileList(new ArrayList<File>(), f);
	        	}else{
	        		for(File file : f.listFiles()){
	        			if(file.isFile()){
	        				allFileList.add(file);
	        			}
	        		}
	        	}
			}
			for(File file : allFileList){
				fileListModel.addElement(file.getAbsolutePath());
	    	}
		}
		
	}
	protected void addCrawlerListener(){
		
	}
}
