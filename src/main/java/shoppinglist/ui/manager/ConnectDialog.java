/**
 * 
 */
package shoppinglist.ui.manager;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import net.miginfocom.swing.MigLayout;
import shoppinglist.dataobjects.DBConnectionInfo;
import shoppinglist.utils.DBConnectionUtils;
import shoppinglist.utils.DialogUtils;

/**
 * @author weis_
 * 
 *         dialog to get connection information
 *
 */
public class ConnectDialog extends JDialog {

	private static final long serialVersionUID = -7468372366465271758L;
	private JLabel nameLbl = new JLabel("DB Name:");
	private JLabel hostLbl = new JLabel("Host:");
	private JLabel portLbl = new JLabel("Port:");
	private JLabel userLbl = new JLabel("User:");
	private JLabel pwdLbl = new JLabel("Password:");
	private JTextField nameField = new JTextField(45);
	private JTextField hostField = new JTextField(45);
	private JTextField portField = new JTextField(45);
	private JTextField userField = new JTextField(45);
	private JTextField pwdField = new JTextField(45);
	private JButton okButton = null;
	private JButton cancelButton = null;
	private JCheckBox rememberSettings = new JCheckBox("Remember these settings");

	private DBConnectionInfo dbInfo = null;

	public ConnectDialog(Component parent) {
		super(DialogUtils.getInstance().getParentFrame(parent), "Connect to Database", true);
		initializeUI();
		getOldSettings();
	}

	private void initializeUI() {
		JPanel panel = new JPanel();
		panel.setLayout(new MigLayout());
		panel.add(nameLbl, "align label");
		panel.add(nameField, "wrap");
		panel.add(hostLbl, "align label");
		panel.add(hostField, "wrap");
		panel.add(portLbl, "align label");
		panel.add(portField, "wrap");
		panel.add(userLbl, "align label");
		panel.add(userField, "wrap");
		panel.add(pwdLbl, "align label");
		panel.add(pwdField, "wrap");
		panel.add(rememberSettings, "span");

		okButton = new JButton("Ok");
		cancelButton = new JButton("Cancel");

		panel.add(okButton, "tag ok, span, split 3, sizegroup bttn");
		panel.add(cancelButton, "tag cancel, sizegroup bttn");

		setContentPane(panel);
		setSize(400, 230);
		DialogUtils utils = DialogUtils.getInstance();
		utils.positionComponent(this);
		setResizable(false);

		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dbInfo = null;
				dispose();
			}
		});

		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (validateDialog()) {
					String pwd = pwdField.getText().trim();
					String name = nameField.getText().trim();
					String user = userField.getText().trim();
					String host = hostField.getText().trim();
					String port = portField.getText().trim();
					dbInfo = new DBConnectionInfo(host, port, name, user, pwd);
					if (rememberSettings.isSelected()) {
						boolean saved = dbInfo.saveSettings();
						if (!saved) {
							JOptionPane.showMessageDialog(null, "Failed to save the connection settings.",
									"Save DB Connection Settings", JOptionPane.ERROR_MESSAGE);
						}
					}
					dispose();
				}
			}
		});
	}

	/**
	 * Call this to show the dialog and get the db connection info.
	 * 
	 * @return
	 */
	public DBConnectionInfo showDialog() {
		setVisible(true);
		return dbInfo;
	}

	/**
	 * Validate entries
	 * 
	 * @return
	 */
	protected boolean validateDialog() {
		String pwd = pwdField.getText().trim();
		String name = nameField.getText().trim();
		String user = userField.getText().trim();
		String host = hostField.getText().trim();
		String port = portField.getText().trim();
		String errMsg = "";
		DBConnectionInfo info = new DBConnectionInfo(host, port, name, user, pwd);
		Connection connection = null;
		try {
			connection = DBConnectionUtils.getInstance().createConnection(info);
			if (connection == null) {
				errMsg = "Could not connect using the specified settings.";
				dbInfo = null;
			} else {
				connection.close();
			}
		} catch (SQLException e) {
			errMsg = "An error occurred while connecting with the specified settings.";
			dbInfo = null;
		}

		if (!errMsg.isEmpty()) {
			JOptionPane.showMessageDialog(null, errMsg, "Connect to Database", JOptionPane.ERROR_MESSAGE);
		}

		return errMsg.isEmpty();
	}

	protected void getOldSettings() {
		DBConnectionInfo info = new DBConnectionInfo();
		if (info.restoreOldSettings()) {
			pwdField.setText(info.getPassword());
			nameField.setText(info.getDBName());
			userField.setText(info.getUser());
			hostField.setText(info.getHost());
			portField.setText(info.getPort());
		}

	}

}
