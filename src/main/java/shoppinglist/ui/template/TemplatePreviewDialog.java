package shoppinglist.ui.template;

import java.awt.Component;

import javax.swing.JDialog;

import shoppinglist.utils.DialogUtils;

public class TemplatePreviewDialog extends JDialog {
	
	
	private static final long serialVersionUID = 4434852630657888643L;
	private String templateName;
	
	public TemplatePreviewDialog(Component parent, String templateName) {
		super(DialogUtils.getInstance().getParentFrame(parent),"Template Preview",true);
		this.templateName = templateName;
		initializeUI();	
	}
	
	protected void initializeUI() {
		TemplateEditorPanel panel = new TemplateEditorPanel(false);
		setContentPane(panel);
		setSize(400,500);
		DialogUtils utils = DialogUtils.getInstance();
		utils.positionComponent(this);
		
		panel.setTemplateName(templateName);
		panel.fillTree(templateName);
		setVisible(true);
	}

}
