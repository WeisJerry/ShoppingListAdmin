/**
 * 
 */
package shoppinglist.ui.manager;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import net.miginfocom.swing.MigLayout;
import shoppinglist.dataobjects.UserInfo;
import shoppinglist.ui.template.TemplatePreviewDialog;
import shoppinglist.utils.DialogUtils;
import shoppinglist.utils.TemplateSelectionUtils;
import shoppinglist.utils.TemplateSelectionUtils.TemplateStatus;

/**
 * @author weis_
 * 
 * Dialog to add a new user account
 *
 */
public class AddUserAccountDialog extends JDialog {
	
	public static final String NONE = "<no template>";

	private static final long serialVersionUID = 9104612341816292595L;
	private UserInfo userinfo = null;
	
	private JLabel nameLbl = new JLabel("Name:");
	private JLabel pwdLbl = new JLabel("Password (8 char min):");
	private JLabel pwd2Lbl = new JLabel("Re-enter Password:");
	private JLabel hintLbl = new JLabel("Password Hint:");
	private JTextField nameField = new JTextField(45);
	private JTextField pwdField = new JTextField(45);
	private JTextField pwd2Field = new JTextField(45);
	private JTextField hintField = new JTextField(45);
	
	private JLabel templateLbl = new JLabel("Use Template:");
	private JCheckBox templateCheckbox = new JCheckBox();
	private JTextField templateField = new JTextField(45);
	private JButton selectBtn = new JButton("...");
	private JButton previewTemplateButton = new JButton("Preview");
	
	private JButton okButton = null;
	private JButton cancelButton = null;
	private Component parent;
	
	public AddUserAccountDialog(Component parent) {
		super(DialogUtils.getInstance().getParentFrame(parent),"Add User",true);
		initializeUI();
		this.parent = parent;	
	}
	

	private void initializeUI() {
		JPanel panel = new JPanel();
		panel.setLayout(new MigLayout());
		panel.add(nameLbl,"align right");
		panel.add(nameField, "wrap");
		panel.add(pwdLbl,"align right");
		panel.add(pwdField, "wrap");
		panel.add(pwd2Lbl,"align right");
		panel.add(pwd2Field, "wrap");
		panel.add(hintLbl,"align right");
		panel.add(hintField, "wrap");
		panel.add(templateLbl,"align right");
		panel.add(templateCheckbox,"wrap");
		
		panel.add(new JLabel(" "), "align right");
		panel.add(templateField);
		panel.add(selectBtn, "wrap");
		panel.add(new JLabel(" "), "align right");
		panel.add(previewTemplateButton, "wrap");
		panel.add(new JLabel(" "), "wrap");
		
		templateField.setEditable(false);
		previewTemplateButton.setEnabled(false);
		
		selectBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				TemplateSelectionUtils util = TemplateSelectionUtils.getInstance();
				String msg = "Select Shopping List Template";
				String template = util.selectTemplate(TemplateStatus.OPEN, parent, msg);
				boolean enabled = false;
				if (template != null && !template.isEmpty()) {
					templateField.setText(template);
					enabled = true;
				}
				previewTemplateButton.setEnabled(enabled);
			}
			
		});
		
		previewTemplateButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				new TemplatePreviewDialog(parent,templateField.getText().trim());
			}
			
		});
		
		templateCheckbox.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				boolean activeTemplate = templateCheckbox.isSelected();
				selectBtn.setEnabled(activeTemplate);
				previewTemplateButton.setEnabled(false);
				if (!activeTemplate) {
					templateField.setText(NONE);
				}
				else {
					templateField.setText("");
				}
			}
			
		});
		
		okButton = new JButton("Ok");
		cancelButton = new JButton("Cancel");
		
		panel.add(okButton, "tag ok, span, split 3, sizegroup bttn");
        panel.add(cancelButton, "tag cancel, sizegroup bttn");

		setContentPane(panel);
		setSize(395,285);
		DialogUtils utils = DialogUtils.getInstance();
		utils.positionComponent(this);
		setResizable(false);
		
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				userinfo = null;
				dispose();
			}
		});
		
		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (validateDialog()) {
					String pwd = pwdField.getText().trim();
					String name = nameField.getText().trim();
					String hint = hintField.getText().trim();
					String template = templateField.getText().trim();
					
					userinfo = new UserInfo(name, pwd, hint);
					if (!template.isEmpty() && !template.equalsIgnoreCase(NONE)) {
						userinfo.setTemplate(template);
					}
					
					dispose();
				}
			}
		});
	}
	
	public UserInfo showDialog() {
		setVisible(true);
		return userinfo;
	}
	
	/**
	 * Validate entries
	 * @return
	 */
	protected boolean validateDialog() {
		String p1 = pwdField.getText().trim();
		String p2 = pwd2Field.getText().trim();
		String name = nameField.getText().trim();
		boolean valid = true;
		String errMsg = "";
		
		if (name.isEmpty()) {
			valid = false;
			errMsg = "The user name cannot be empty.";
		}
		else if (p1.length() < 8) {
			valid = false;
			errMsg = "The password must be at least eight characters.";
		}
		else if (!p1.equals(p2)) {
			valid = false;
			errMsg = "The passwords do not match.";
		}
		
		if (!valid) {
			JOptionPane.showMessageDialog(null, errMsg, "User Accounts", JOptionPane.INFORMATION_MESSAGE);
		}
			
		return valid;
	}

}
