

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.plaf.FontUIResource;

import java.awt.Font;
import java.io.File;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

//这种注释不正规
/**
 * Created by klevis.ramo on 11/24/2017.
 */
public class Main {

    private final static Logger LOGGER = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) throws Exception {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        UIManager.put("Button.font", new FontUIResource(new Font("Dialog", Font.BOLD, 18)));
        UIManager.put("ProgressBar.font", new FontUIResource(new Font("Dialog", Font.BOLD, 18)));

        LOGGER.info("Application is starting ... ");

        JFrame mainFrame = UI.getNewFrame();
        ProgressBar progressBar = new ProgressBar(mainFrame, true);
        progressBar.showProgressBar("Collecting data this make take several seconds!");
        UI ui = UI.getInstance();
        Executors.newCachedThreadPool().submit(()->{
            try {
                ui.initUI();
            } finally {
                progressBar.setVisible(false);
                mainFrame.dispose();
            }
        });
    }


}
