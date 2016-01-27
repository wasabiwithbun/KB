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

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.javacoo.cowswing.base.loader.ImageLoader;
import org.javacoo.cowswing.base.loader.LanguageLoader;
import org.javacoo.cowswing.base.utils.FileUtils;
import org.javacoo.cowswing.base.utils.MsgDialogUtil;
import org.javacoo.cowswing.plugin.kbs.ui.dialog.DicSettingDialog;
import org.javacoo.cowswing.ui.view.dialog.WaitingDialog;
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
@Component("dicSettingPanel")
public class DicSettingPanel extends AbstractContentPanel<List<String>> {
	private static final long serialVersionUID = 1L;
	protected Logger logger = Logger.getLogger(this.getClass());
	/**标题名称标签*/
	private javax.swing.JLabel titleLabel;
	/**标题名称输入框*/
	private javax.swing.JTextField titleField;
	/**操作说明标签*/
	private javax.swing.JLabel operDescLabel;
	/**选择的文件类别标签*/
	private javax.swing.JLabel selectFileLabel;
	/** 添加词语按钮 */
	private JButton addDicBtn;
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
	/**父对象*/
	private DicSettingDialog parentDialog;
	
	public void setParentDialog(DicSettingDialog parentDialog){
		this.parentDialog = parentDialog;
	}
	/* (non-Javadoc)
	 * @see org.javacoo.cowswing.ui.view.panel.AbstractContentPanel#populateData()
	 */
	@Override
	protected List<String> populateData() {
		List<String> filePathList = new ArrayList<String>();
		for(int i = 0;i<fileListModel.size();i++){
			filePathList.add(fileListModel.getElementAt(i).toString());
		}
		return filePathList;
	}

	/* (non-Javadoc)
	 * @see org.javacoo.cowswing.ui.view.panel.AbstractContentPanel#initComponents()
	 */
	@Override
	protected void initComponents() {
		titleLabel = new javax.swing.JLabel();
		titleLabel.setText(LanguageLoader.getString("Kbs.dic_manage_name"));
		add(titleLabel);
		titleLabel.setBounds(20, 15, 80, 15);
		
		titleField = new javax.swing.JTextField();
		titleField.setColumns(20);
		titleField.setText("");
		add(titleField);
		titleField.setBounds(110, 15, 220, 21);
		
		addDicBtn = new JButton(LanguageLoader.getString("Kbs.dic_add_btn"),ImageLoader.getImageIcon("CrawlerResource.kbs_source_browse"));
		addDicBtn.setBounds(340, 15, 90, 21);
		add(addDicBtn);
		
		operDescLabel = new javax.swing.JLabel();
		operDescLabel.setText(LanguageLoader.getString("Kbs.dic_manage_operDesc"));
		add(operDescLabel);
		operDescLabel.setBounds(20, 45, 80, 15);
		
		
		dropLabel = new javax.swing.JLabel();
		dropLabel.setText(LanguageLoader.getString("Kbs.dic_manage_dropLabel"));
		add(dropLabel);
		dropLabel.setBounds(110, 45, 320, 15);
		
		
		selectFileLabel = new javax.swing.JLabel();
		selectFileLabel.setText(LanguageLoader.getString("Kbs.dic_manage_list"));
		add(selectFileLabel);
		selectFileLabel.setBounds(20, 75, 80, 15);
		
		
		addFileBtn = new JButton(LanguageLoader.getString("Kbs.module_source_add_panel_select"),ImageLoader.getImageIcon("CrawlerResource.kbs_source_browse"));
		addFileBtn.setBounds(110, 75, 60, 21);
		add(addFileBtn);
		
		
		removeBtn = new JButton(LanguageLoader.getString("Kbs.module_source_add_panel_delete"),ImageLoader.getImageIcon("CrawlerResource.kbs_source_delete"));
		add(removeBtn);
		removeBtn.setBounds(180, 75, 60, 21);
		
		removeAllBtn = new JButton(LanguageLoader.getString("Kbs.module_source_add_panel_deleteAll"),ImageLoader.getImageIcon("CrawlerResource.kbs_source_delete"));
		add(removeAllBtn);
		removeAllBtn.setBounds(250, 75, 100, 21);
		
		
		fileListModel = new DefaultListModel();
		fileList = new JList(fileListModel);
		JScrollPane fileListJScrollPane = new JScrollPane(fileList);
		add(fileListJScrollPane);
		fileListJScrollPane.setBounds(110, 105, 320, 200);
		
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
		addDicBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(StringUtils.isBlank(titleField.getText())){
					MsgDialogUtil.createMessageDialog(LanguageLoader.getString("Kbs.dic_is_empty"));
				}else if(fileListModel.contains(titleField.getText())){
					MsgDialogUtil.createMessageDialog(LanguageLoader.getString("Kbs.dic_is_exist"));
				}else{
					fileListModel.addElement(titleField.getText());
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
				fileListModel.clear();
			}
		});


	}
	public void initData(List<String> t){
		if(t == null){
			t = new ArrayList<String>();
		}
		fillComponentData(t);
	}
	/* (non-Javadoc)
	 * @see org.javacoo.cowswing.ui.view.panel.AbstractContentPanel#fillComponentData(java.lang.Object)
	 */
	@Override
	protected void fillComponentData(List<String> t) {
		fileListModel.clear();
		if(!CollectionUtils.isEmpty(t)){
			for(String s : t){
				fileListModel.addElement(s);
	    	}
		}
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
		final DicSettingDialog dicSettingDialog = this.parentDialog;
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				//开启线程上传文件及建立索引
				Thread currThread = new Thread(new LoadFileTask(file));
				currThread.start();
				Thread waitingThread = new Thread(new WaitingDialog(dicSettingDialog,currThread,LanguageLoader.getString("Kbs.module_source_add_panel_uploading")));
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
				for(File file : f.listFiles()){
        			if(file.isFile()){
        				allFileList.add(file);
        			}
        		}
			}
			for(File file : allFileList){
				String ext = FilenameUtils.getExtension(file.getName());
				if(StringUtils.isBlank(ext) || !"txt".equals(ext.toLowerCase())){
					continue;
				}
				List<String> dicList = FileUtils.readFile(file.getAbsolutePath(), "GBK");
				if(!CollectionUtils.isEmpty(dicList)){
					for(String str : dicList){
						if(!fileListModel.contains(str)){
							fileListModel.addElement(str);
						}
					}
				}
	    	}
		}
		
	}
	protected void addCrawlerListener(){
		
	}
}
