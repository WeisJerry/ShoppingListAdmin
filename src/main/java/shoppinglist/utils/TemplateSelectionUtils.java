package shoppinglist.utils;

import java.awt.Component;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * Utilities to assist with selecting templates.
 * @author weis_
 *
 */
public class TemplateSelectionUtils {
	
	public enum TemplateStatus {
		SAVE, OPEN
	};
	
	public static final String EXTENSION = "tpl";
	public static final String DOT_EXTENSION = ".tpl";
	
	private static TemplateSelectionUtils TEMPLATESELECTIONUTILS = new TemplateSelectionUtils();
	
	protected TemplateSelectionUtils() { }
	
	/**
	 * Retrieve the one-and-only instance.
	 * 
	 * @return
	 */
	public static TemplateSelectionUtils getInstance() {
		return TEMPLATESELECTIONUTILS; 		
	}
	
	public String selectTemplate(TemplateStatus mode, Component component) {
		return selectTemplate(mode, component, null);
	}
	
	/**
	 * Provide a dialog to help user select a template to use.
	 * @param mode
	 * @param component
	 * @param title
	 * @return
	 */
	public String selectTemplate(TemplateStatus mode, Component component, String title) {
		String templateName = "";
		JFileChooser fileChooser = new JFileChooser();
		FileNameExtensionFilter filter = new FileNameExtensionFilter("Template File", EXTENSION);
		
		if (title != null && !title.isEmpty()) {
			fileChooser.setDialogTitle(title);
		}
		
		fileChooser.setFileFilter(filter);
		int sel;
		if (mode == TemplateStatus.SAVE) {
			sel = fileChooser.showSaveDialog(DialogUtils.getInstance().getParentFrame(component));
		} else {
			sel = fileChooser.showOpenDialog(DialogUtils.getInstance().getParentFrame(component));
		}

		if (sel == JFileChooser.APPROVE_OPTION) {
			File file = fileChooser.getSelectedFile();
			templateName = file.getAbsolutePath();
		}
		return templateName;
	}

}
