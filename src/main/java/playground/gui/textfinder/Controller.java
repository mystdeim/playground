package playground.gui.textfinder;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.Initializable;
import playground.gui.util.UserSettings;
import playground.gui.util.UserSettingsSavable;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.RadioButton;

public class Controller implements Initializable, UserSettingsSavable {
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		loadSettings();
	}
	
	// FXML Properties
	// -----------------------------------------------------------------------------------------------------------------

	@FXML private TextField fieldFind;
	@FXML private TextField fieldFilter;
	@FXML private TextField fieldDir;
	@FXML private RadioButton radioFindExtended;
	@FXML private RadioButton radioFindRegexp;
	@FXML private RadioButton radioFilterWildcard;
	@FXML private RadioButton radioFilterRegexp;
	
	// UserSettings
	// -----------------------------------------------------------------------------------------------------------------
	
	public static final String USER_SETTINGS_FIND_WHAT = "find_what";
	public static final String USER_SETTINGS_FILTER = "filter";
	public static final String USER_SETTINGS_DIR = "dir";
	
	public static final String USER_SETTINGS_RADIO_FIND_EXT = "radio_find_ext";
	public static final String USER_SETTINGS_RADIO_FIND_REGEXP = "radio_find_regexp";
	
	public static final String USER_SETTINGS_RADIO_FILTER_EXT = "radio_filter_ext";
	public static final String USER_SETTINGS_RADIO_FILTER_REGEXP = "radio_filter_regexp";
	
	private void loadSettings() {
		UserSettings settings = getUserSettings();
		fieldFind.setText(settings.get(USER_SETTINGS_FIND_WHAT));
		fieldFilter.setText(settings.get(USER_SETTINGS_FILTER));
		fieldDir.setText(settings.get(USER_SETTINGS_DIR));
		
		radioFindExtended.setSelected(settings.get(USER_SETTINGS_RADIO_FIND_EXT, Boolean.class, true));
		radioFindRegexp.setSelected(settings.get(USER_SETTINGS_RADIO_FIND_REGEXP, Boolean.class, false));
		radioFilterWildcard.setSelected(settings.get(USER_SETTINGS_RADIO_FILTER_EXT, Boolean.class, true));
		radioFilterRegexp.setSelected(settings.get(USER_SETTINGS_RADIO_FILTER_REGEXP, Boolean.class, false));
	}
	
	@Override
	public void saveSettings() {
		UserSettings settings = getUserSettings();
		settings.put(USER_SETTINGS_FIND_WHAT, fieldFind.getText());
		settings.put(USER_SETTINGS_FILTER, fieldFilter.getText());
		settings.put(USER_SETTINGS_DIR, fieldDir.getText());
		
		settings.put(USER_SETTINGS_RADIO_FIND_EXT, radioFindExtended.isSelected());
		settings.put(USER_SETTINGS_RADIO_FIND_REGEXP, radioFindRegexp.isSelected());
		settings.put(USER_SETTINGS_RADIO_FILTER_EXT, radioFilterWildcard.isSelected());
		settings.put(USER_SETTINGS_RADIO_FILTER_REGEXP, radioFilterRegexp.isSelected());
	}

}
