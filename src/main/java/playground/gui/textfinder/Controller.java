package playground.gui.textfinder;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;

public class Controller implements Initializable, UserSettingsSavable {
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		loadSettings();
		
//		columnName.setCellValueFactory(new TreeItemPropertyValueFactory<>("name"));
		columnName.setCellValueFactory(c -> c.getValue().getValue().name);
		columnCount.setCellValueFactory(c -> c.getValue().getValue().count);
		
		TreeItem<Row> root = new TreeItem<Controller.Row>(new Row("c:", 1));
		root.setExpanded(true);
		treeTableFiles.setRoot(root);
		
		treeTableFiles.setOnMousePressed(e -> {
//			e.
		});
		treeTableFiles.getSelectionModel().selectedItemProperty().addListener(listener -> {
//			listener.getValue();
//			if (listener.)
		});
		treeTableFiles.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<TreeItem<Row>>() {
			@Override
			public void changed(ObservableValue<? extends TreeItem<Row>> observable, TreeItem<Row> oldValue,
					TreeItem<Row> newValue) {
				System.out.println(newValue.getValue().name.get());
				String path = getPath(newValue);
				System.out.println(path);
//				Path path = newValue.getValue().name;
				try {
					textAreaViewer.setText(new String(Files.readAllBytes(Paths.get(path))));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		
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
	@FXML private TextArea textAreaViewer;
	
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
				setCount(path, count);
			}
		});
		
		long ns = System.nanoTime();
		try {
			tf.run();
			
//			treeTableFiles.set
		} catch (IOException e) {
			e.printStackTrace();
		}
		double d = (System.nanoTime() - ns) / 1_000_000.0;
		System.out.printf("Scanned: %d Accepted: %d Macthes: %d for %.2f s \n", 
				tf.getScannedCounter(), tf.getAcceptedCounter(), tf.getMatchedCounter(), d);
	}
	
	// Helpers
	// -----------------------------------------------------------------------------------------------------------------
	
	private synchronized void setCount(String path, long count) {
//		TreeItem<Row> node = getRecursiveItem(treeTableFiles.getRoot(), path);
		
		String splitter = ("\\/");
		if (System.getProperty("os.name").toLowerCase().startsWith("win")) splitter = "\\\\";
		String[] files = path.split(splitter);
		if (null == treeTableFiles.getRoot()) {
			treeTableFiles.setRoot(new TreeItem<Row>(new Row(files[0], 0L)));
		}
		TreeItem<Row> root = treeTableFiles.getRoot();
		TreeItem<Row> node = getRecursiveItem(root, files, 1);
		
		node.getValue().setCount(count);
		recalculateParentCount(node);
	}
	
	private void recalculateParentCount(TreeItem<Row> node) {
		if (node.getChildren().size() > 0) {
			long sum = node.getChildren().stream().mapToLong(n -> n.getValue().count.get()).sum();
			node.getValue().setCount(sum);
		}
		if (null != node.getParent()) {
			recalculateParentCount(node.getParent());
		}
	}
	
	private TreeItem<Row> getRecursiveItem(TreeItem<Row> root, String[] path, int deep) {
		TreeItem<Row> result = null;
		boolean empty = true;
		
		for (TreeItem<Row> child : root.getChildren()) {
			if (child.getValue().name.get().equals(path[deep])) {
				empty = false;
				if (deep < path.length-1) result = getRecursiveItem(child, path, deep+1);
				else result = child;
				break;
			}
		}
		
		if (empty) {
			result = new TreeItem<Controller.Row>(new Row(path[deep], 0));
			
			// TODO: delete!
			result.setExpanded(true);
			
			root.getChildren().add(result);
			
			if (deep < path.length-1) {
				result = getRecursiveItem(result, path, deep+1);
			}
		}
		
		return result;
	}
	
	private String getPath(TreeItem<Row> item) {
//		if (null != item.getParent()) {
//			return getPath(item.getParent(), item.getValue().name.get().concat("/").concat(root));
//		} else {
//			return item.getValue().name.get().concat("/").concat(root);
//		}
//		StringBuilder sb = new StringBuilder();
//		
//		return sb.toString();
		return getPath(item, null);
//		return getPath(item, "");
	}
	
	private String getPath(TreeItem<Row> item, String right) {
		if (null != item.getParent()) {
			String next = item.getValue().name.get();
			if (null != right) next = next.concat("/").concat(right);
			return getPath(item.getParent(), next);
//			return getPath(item.getParent(), item.getValue().name.get().concat("/").concat(root));
		} else {
			return item.getValue().name.get().concat("/").concat(right);
		}
		
	}
	
//	private String getPath(TreeItem<Row> item, String right) {
//		if (null != item.getParent()) {
//			return getPath(item.getParent(), "/".concat(item.getValue().name.get()));
//		} else {
//			return item.getValue().name.get().concat("/").concat(right);
//		}
//		
//	}
	
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
		public void setCount(long count) {
			this.count.set(count);
		}
//		public StringProperty getName() {
//			return name;
//		}
	}

}
