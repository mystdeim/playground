package playground.gui.textfinder;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.Initializable;
import playground.gui.util.UserSettings;
import playground.gui.util.UserSettingsSavable;
import playground.util.TextFinder;
import playground.util.TextFinder.Listener;
import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;
import javafx.scene.control.RadioButton;

public class Controller implements Initializable, UserSettingsSavable {
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		loadSettings();
		
//		columnName.setCellValueFactory(new TreeItemPropertyValueFactory<>("name"));
		columnName.setCellValueFactory(c -> c.getValue().getValue().name);
		columnCount.setCellValueFactory(c -> c.getValue().getValue().count);
		
		TreeItem<Row> root = new TreeItem<Controller.Row>(new Row("c:", 1));
		treeTableFiles.setRoot(root);
		
//		treeTableFiles.get
	}
	
	// FXML Properties
	// -----------------------------------------------------------------------------------------------------------------

	@FXML private TreeTableView<Row> treeTableFiles;
	@FXML private TreeTableColumn<Row, String> columnName;
	@FXML private TreeTableColumn<Row, Number> columnCount;
	
	@FXML private TextField fieldFind;
	@FXML private TextField fieldFilter;
	@FXML private TextField fieldDir;
	@FXML private RadioButton radioFindExtended;
	@FXML private RadioButton radioFindRegexp;
	@FXML private RadioButton radioFilterWildcard;
	@FXML private RadioButton radioFilterRegexp;
	
	// FXML Handlers
	// -----------------------------------------------------------------------------------------------------------------
	
	@FXML 
	public void onStart(ActionEvent event) {
		TextFinder tf = new TextFinder();
		tf.setPath(fieldDir.getText());
		tf.setFilter(fieldFilter.getText());
		tf.setPattern(fieldFind.getText());
		
		tf.addListener(new Listener() {
			@Override
			public void scanning(String path) {
				// TODO Auto-generated method stub
			}
			@Override
			public void accepted(String path) {
//				System.out.printf("File: %s : %d \n", path, count);
			}
			@Override
			public void matched(String path, long count) {
				System.out.printf("Matches: %s : %d \n", path, count);
			}
		});
		
		long ns = System.nanoTime();
		try {
			tf.run();
		} catch (IOException e) {
			e.printStackTrace();
		}
		double d = (System.nanoTime() - ns) / 1_000_000.0;
		System.out.printf("Scanned: %d Accepted: %d Macthes: %d for %.2f s \n", 
				tf.getScannedCounter(), tf.getAcceptedCounter(), tf.getMatchedCounter(), d);
	}
	
	// Helpers
	// -----------------------------------------------------------------------------------------------------------------
	
	private void getItemByPath(String path) {
		
	}
	
	private TreeItem<Row> getRecursiveItem(TreeItem<Row> root, String path) {
		return root;
	}
	
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
	
	// TreeTable
	// -----------------------------------------------------------------------------------------------------------------
	
	private class Row {
		private final StringProperty name;
		public final LongProperty count;
		public Row(String name, long count) {
			this.name = new SimpleStringProperty(name);
			this.count = new SimpleLongProperty(count);
		}
//		public StringProperty getName() {
//			return name;
//		}
	}

}
