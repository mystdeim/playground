package playground.gui.util;

public interface UserSettingsSavable {
	
	void saveSettings();
	
	default UserSettings getUserSettings() {
		return new UserSettings(this);
	}
}
