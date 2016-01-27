/**
 * 如石子一粒,仰高山之巍峨,但不自惭形秽.
 * 若小草一棵,慕白杨之伟岸,却不妄自菲薄.
 */
package org.javacoo.cowswing.plugin.kbs.ui.action;

import java.awt.event.ActionEvent;
import java.util.Date;

import javax.annotation.Resource;
import javax.swing.AbstractAction;

import org.javacoo.cowswing.base.loader.ImageLoader;
import org.javacoo.cowswing.base.loader.LanguageLoader;
import org.javacoo.cowswing.base.utils.DateUtil;
import org.javacoo.cowswing.plugin.kbs.constant.KbsConstant;
import org.javacoo.cowswing.plugin.kbs.domain.KnowledgePoint;
import org.javacoo.cowswing.plugin.kbs.lucene.index.Index;
import org.springframework.stereotype.Component;

/**
 * 添加索引
 * <p>说明:</p>
 * <li></li>
 * @author DuanYong
 * @since 2013-7-9 下午7:09:05
 * @version 1.0
 */
@Component("addIndexAction")
public class AddIndexAction extends AbstractAction{
	private static final long serialVersionUID = 1L;
	@Resource(name="indexManager")
    private Index index;
	
	public AddIndexAction(){
		super(LanguageLoader.getString("Kbs.addIndex"),ImageLoader.getImageIcon("CrawlerResource.navigatorList"));
		putValue(SHORT_DESCRIPTION, LanguageLoader.getString("Kbs.addIndex"));
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
//		pageContainer.addPage(searchPanel, searchPanel.getPageId());
//		searchPanel.init();
		KnowledgePoint knowledgePoint = new KnowledgePoint();
		knowledgePoint.setId("1");
		knowledgePoint.setModule(KbsConstant.SYSTEM_MODULE_SOURCE);
		knowledgePoint.setTitle("岗位对照表.xlsx");
		knowledgePoint.setKeyWord("岗位对照表");
		knowledgePoint.setIndexDirName("default");
		knowledgePoint.setFileDirs("default");
		knowledgePoint.setShowDate(DateUtil.dateToStr(new Date()));
		knowledgePoint.setFilePath("doc/study/crawler/岗位对照表.xlsx");
		knowledgePoint.setPurview(KbsConstant.SEARCH_PURVIEW_VIEW);
		try {
			index.buildIndex(knowledgePoint,true);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
}
