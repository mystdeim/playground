package playground.gui.ipcalc;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.Initializable;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import playground.gui.util.UserSettings;
import playground.gui.util.UserSettingsSavable;
import playground.helper.Formatter;
import playground.helper.IPHelper;
import javafx.scene.control.Label;

public class Controller implements Initializable, UserSettingsSavable {
	
	// Properties
	// -----------------------------------------------------------------------------------------------------------------
	
	private boolean isStable = false;
	
	// FXML Properties
	// -----------------------------------------------------------------------------------------------------------------

	@FXML private TextField fieldIPReadable;
	@FXML private TextField fieldIPBinary;
	@FXML private TextField fieldIPInt;
	@FXML private TextField fieldNetmaskBin;
	@FXML private TextField fieldNetmaskInt;
	
	@FXML private Label lblHosts;
	@FXML private Label lblHostMin;
	@FXML private Label lblHostMinBin;
	@FXML private Label lblHostMax;
	@FXML private Label lblHostMaxBin;
	@FXML private Label lblHostMinInt;
	@FXML private Label lblHostMaxInt;
	@FXML private Label lblNetmask;
	@FXML private Label lblNetmaskBin;
	
	// FXML
	// -----------------------------------------------------------------------------------------------------------------

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		// Address
		fieldIPReadable.textProperty().addListener(l -> {
			runBlockingOperation(() -> {
				if (fieldIPReadable.getText().length() > 0) {
					int ip = IPHelper.toInt(fieldIPReadable.getText());
					fieldIPInt.setText(Formatter.numberToHumanable(Integer.toUnsignedLong(ip)));
					fieldIPBinary.setText(toBinString(ip));
					updateRange();
				}
			});
		});
		fieldIPBinary.textProperty().addListener(l -> {
			runBlockingOperation(() -> {
				int ip = Integer.parseUnsignedInt(fieldIPBinary.getText().replaceAll("\\s", ""), 2);
				fieldIPReadable.setText(IPHelper.toString(ip));
				fieldIPInt.setText(Formatter.numberToHumanable(Integer.toUnsignedLong(ip)));
				updateRange();
			});
		});
		fieldIPInt.textProperty().addListener(l -> {
			runBlockingOperation(() -> {
				int ip = Integer.parseUnsignedInt(fieldIPInt.getText().replaceAll("\\s", ""));
				fieldIPReadable.setText(IPHelper.toString(ip));
				fieldIPBinary.setText(toBinString(ip));
				updateRange();
			});
		});
		
		// Netmask
		fieldNetmaskInt.textProperty().addListener(l -> {
			runBlockingOperation(() -> {
				if (fieldNetmaskInt.getText().length() > 0) {
					int mask_bytes = Integer.parseInt(fieldNetmaskInt.getText());
					int mask = IPHelper.maskToInt(mask_bytes);
					lblNetmask.setText(IPHelper.toString(mask));
					lblNetmaskBin.setText(toBinString(mask));
					updateRange();
				} else {
					lblNetmask.setText("");
					lblNetmaskBin.setText("");
				}
			});
		});
		
		isStable = true;
		loadSettings();
		Platform.runLater(() -> {
			fieldIPInt.requestFocus();
		});
	}
	
	// Helpers
	// -----------------------------------------------------------------------------------------------------------------
		
	private void runBlockingOperation(Runnable task) {
		if (isStable) {
			isStable = false;
			try {
				task.run();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				isStable = true;
			}
		}
	}
	
	private void updateRange() {
		if (fieldIPReadable.getText().length() > 0 && fieldNetmaskInt.getText().length() > 0) {
			int[] range = IPHelper.getRange(String.format("%s/%s", 
					fieldIPReadable.getText().trim(), fieldNetmaskInt.getText().trim()));
			long hosts = Integer.toUnsignedLong(range[1]) - Integer.toUnsignedLong(range[0]) + 1L;
			lblHosts.setText(Formatter.numberToHumanable(hosts));
			lblHostMin.setText(IPHelper.toString(range[0]));
			lblHostMinBin.setText(toBinString(range[0]));
			lblHostMinInt.setText(Formatter.numberToHumanable(Integer.toUnsignedLong(range[0])));
			lblHostMax.setText(IPHelper.toString(range[1]));
			lblHostMaxBin.setText(toBinString(range[1]));
			lblHostMaxInt.setText(Formatter.numberToHumanable(Integer.toUnsignedLong(range[1])));
		} else {
			lblHosts.setText("");
			lblHostMin.setText("");
			lblHostMinBin.setText("");
			lblHostMax.setText("");
			lblHostMaxBin.setText("");
		}
	}
	
	private String toBinString(int ip) {
		byte[] bs = IPHelper.toByteArray(ip);
		return String.format("%s %s %s %s", 
				Formatter.toBinary(bs[0] & 0xff, 8),
				Formatter.toBinary(bs[1] & 0xff, 8),
				Formatter.toBinary(bs[2] & 0xff, 8),
				Formatter.toBinary(bs[3] & 0xff, 8)
		);
	}
	
	// Settings
	// -----------------------------------------------------------------------------------------------------------------

	public static final String USER_SETTINGS_ADDRESS = "address";
	public static final String USER_SETTINGS_NETWORK = "network";
	
	private void loadSettings() {
		UserSettings settings = getUserSettings();
		fieldIPReadable.setText(settings.get(USER_SETTINGS_ADDRESS, ""));
		fieldNetmaskInt.setText(settings.get(USER_SETTINGS_NETWORK, ""));
	}
	
	@Override
	public void saveSettings() {
		UserSettings settings = getUserSettings();
		settings.put(USER_SETTINGS_ADDRESS, fieldIPReadable.getText().trim());
		settings.put(USER_SETTINGS_NETWORK, fieldNetmaskInt.getText().trim());
	}
}
