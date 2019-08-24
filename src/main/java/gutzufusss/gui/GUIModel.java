package gutzufusss.gui;

import java.io.File;

import javax.swing.JFileChooser;

import gutzufusss.Main;
import gutzufusss.util.Config;
import gutzufusss.util.Logger;
import gutzufusss.wrapper.OCRWrapper;

public class GUIModel {
	private Logger logger;
	private Config config;
	private Main controller; // for communication with the main programm
	private GUIController guiCtrl;

	public GUIModel(Logger logger, Config config, Main m) {
		this.config = config;
		this.logger = logger;
		controller = m;
		guiCtrl = new GUIController(logger, this);
	}

	public void userBrowsePath() {
		// open dialog
		JFileChooser fc = new JFileChooser();
		fc.setDialogTitle("Select the directory you want to scan");
		fc.setDialogType(JFileChooser.OPEN_DIALOG);
		fc.setCurrentDirectory(new java.io.File(".")); // start at application current directory
		fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		int returnVal = fc.showOpenDialog(null);
		if(returnVal == JFileChooser.APPROVE_OPTION) {
			String selectedDir = fc.getSelectedFile().getAbsolutePath();
			guiCtrl.setDirPath(selectedDir);
		}
	}

	public void updateLogLevel(String selectedItem) {
		int newLogLvl = Integer.parseInt(selectedItem.split(":")[0]);
		config.curConfig.logLevel = newLogLvl;
	}

	public void updateDebuggingActive() { config.curConfig.debug = !config.curConfig.debug; }

	public void updateCritConf(int lvl) { config.curConfig.critConf = lvl; }

	public void startScanning(String path) {
		if(new File(path).exists()) {
			OCRWrapper myRunnable = new OCRWrapper(logger, config, controller, controller.getImgDB(), path);
	        Thread t = new Thread(myRunnable);
	        t.start();
		}
		else
			logger.log(Logger.LVL_ERROR, "The selected directory does not seem to exist.");
	}
	
	public Config getConfig() { return config; }
}
