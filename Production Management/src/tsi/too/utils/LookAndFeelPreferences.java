package tsi.too.utils;

import java.util.prefs.Preferences;

import javax.swing.UIManager;

public class LookAndFeelPreferences {
	private Preferences prefs = Preferences.userRoot().node("<temporary>");
	private static final String PREF_LOOK_AND_FEEL = "LookAndFeel";

	public LookAndFeelPreferences() {
		var saved = getSavedLookAndFeelName();
		if (saved == null || saved.isEmpty())
			try {
				saveLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			} catch (Exception ignored) {
			}
	}

	public void saveLookAndFeel(String lookAndFeelName) {
		prefs.put(PREF_LOOK_AND_FEEL, lookAndFeelName);
	}

	public String getSavedLookAndFeelName() {
		return prefs.get(PREF_LOOK_AND_FEEL, null);
	}
}
