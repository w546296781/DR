import com.mortennobel.imagescaling.ResampleFilters;
import com.mortennobel.imagescaling.ResampleOp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.swing.*;
import javax.swing.plaf.FontUIResource;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.concurrent.Executors;

public class UI {
	private static UI instance; 
	private ImageProcessor imageProcessor;
	private final static Logger LOGGER = LoggerFactory.getLogger(UI.class);
	private static final int FRAME_WIDTH = 1200;
    private static final int FRAME_HEIGHT = 628;
    private DrawAreaController drawArea;
    private JFrame mainFrame;
    private JPanel mainPanel;
    private JPanel drawAndDigitPredictionPanel;
    private JPanel resultPanel;
    private JPanel actionPanel;
    
    protected UI(Algorithm neuralNetwork, Algorithm convolutionalNeuralNetwork) throws Exception {
        imageProcessor = new ImageProcessor();
        mainFrame = createMainFrame();
        mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        drawAndDigitPredictionPanel = new JPanel(new GridLayout());
        actionPanel = new JPanel(new GridLayout());
        addActionPanel(neuralNetwork, convolutionalNeuralNetwork);
        addDrawAreaAndPredictionArea();
        mainPanel.add(actionPanel, BorderLayout.NORTH);
        mainPanel.add(drawAndDigitPredictionPanel, BorderLayout.CENTER);
        addSignature();
        mainFrame.add(mainPanel, BorderLayout.CENTER);
        mainFrame.setVisible(true);
    }
    
    public static UI getInstance(Algorithm neuralNetwork, Algorithm convolutionalNeuralNetwork) throws Exception {  
    	if (instance == null) {  
    			instance = new UI(neuralNetwork, convolutionalNeuralNetwork);  
    	}  
    	return instance;  
    } 
    
    private void addActionPanel(Algorithm neuralNetwork, Algorithm convolutionalNeuralNetwork) {
    	JButton about = new JButton("About");
        JButton recognize = new JButton("Recognize Digit With Simple NN");
        JButton recognizeCNN = new JButton("Recognize Digit With Conv NN");
        about.addActionListener(e -> {
            JDialog d = new JDialog(mainFrame, "About");
            JLabel l = new JLabel("Recognize Digit");
            d.setLocationRelativeTo(actionPanel);
            d.add(l);
            d.setSize(300,150);
            d.setVisible(true);
        });
        
        recognize.addActionListener(e -> {
            Image drawImage = drawArea.getImage();
            LabeledImage labeledImage = imageProcessor.GetProcessedImage(drawImage);
            int predict = neuralNetwork.predict(labeledImage);
            JLabel predictNumber = new JLabel("" + predict);
            predictNumber.setForeground(Color.RED);
            predictNumber.setFont(new Font("SansSerif", Font.BOLD, 128));
            resultPanel.removeAll();
            resultPanel.add(predictNumber);
            resultPanel.updateUI();

        });

        recognizeCNN.addActionListener(e -> {
            Image drawImage = drawArea.getImage();
            LabeledImage labeledImage = imageProcessor.GetProcessedImage(drawImage);
            int predict = convolutionalNeuralNetwork.predict(labeledImage);
            JLabel predictNumber = new JLabel("" + predict);
            predictNumber.setForeground(Color.RED);
            predictNumber.setFont(new Font("SansSerif", Font.BOLD, 128));
            resultPanel.removeAll();
            resultPanel.add(predictNumber);
            resultPanel.updateUI();

        });
        
        JButton clear = new JButton("Clear");
        clear.addActionListener(e -> {
            drawArea.setImage(null);
            drawArea.repaint();
            drawAndDigitPredictionPanel.updateUI();
        });
        
        actionPanel.add(about);
        actionPanel.add(recognizeCNN);
        actionPanel.add(recognize);
        actionPanel.add(clear);
        actionPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
    }

    private void addDrawAreaAndPredictionArea() {

        drawArea = DrawAreaController.getInstance();
        drawAndDigitPredictionPanel.add(drawArea, BorderLayout.EAST);
        resultPanel = new JPanel();
        resultPanel.setLayout(new GridBagLayout());
        drawAndDigitPredictionPanel.add(resultPanel, BorderLayout.WEST);
    }

    private JFrame createMainFrame() {
        JFrame mainFrame = new JFrame();
        mainFrame.setTitle("Digit Recognizer");
        mainFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        mainFrame.setSize(FRAME_WIDTH, FRAME_HEIGHT);
        mainFrame.setLocationRelativeTo(null);
        mainFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                System.exit(0);
            }
        });
        ImageIcon imageIcon = new ImageIcon("icon.png");
        mainFrame.setIconImage(imageIcon.getImage());

        return mainFrame;
    }

    private void addSignature() {
        JLabel signature = new JLabel("ramok.tech", JLabel.HORIZONTAL);
        signature.setFont(new Font(Font.SANS_SERIF, Font.ITALIC, 20));
        signature.setForeground(Color.BLUE);
        mainPanel.add(signature, BorderLayout.SOUTH);
    }
    
    public static JFrame getNewFrame() {
    	return new JFrame();
    }
}