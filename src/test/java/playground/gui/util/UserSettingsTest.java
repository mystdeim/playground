package playground.gui.util;

import static org.junit.Assert.*;

import org.junit.Test;

public class UserSettingsTest {

	@Test
	public void getPackageStrTest() {
		UserSettings us = new UserSettingsMock(this);
		assertEquals("playground.gui.util.user_settings_test", us.getPackageStr(this));
	}
}
