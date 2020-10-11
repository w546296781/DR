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
    private Algorithm neuralNetwork;
    private Algorithm convolutionalNeuralNetwork;
    private DrawAreaController drawArea;
    private JFrame mainFrame;
    private JPanel mainPanel;
    private JPanel drawAndDigitPredictionPanel;
    private SpinnerNumberModel modelTrainSize;
    private JSpinner trainField;
    private int TRAIN_SIZE = 30000;
    private final Font sansSerifBold = new Font("SansSerif", Font.BOLD, 18);
    private int TEST_SIZE = 10000;
    private SpinnerNumberModel modelTestSize;
    private JSpinner testField;
    private JPanel resultPanel;

    public UI() throws Exception {
        imageProcessor = new ImageProcessor();
        AlgorithmFactory algorithmFactory = new AlgorithmFactory();
        neuralNetwork = algorithmFactory.getAlgorithm("NN");
        convolutionalNeuralNetwork = algorithmFactory.getAlgorithm("CNN");
        neuralNetwork.init();
        convolutionalNeuralNetwork.init();
    }
    
    public static UI getInstance() throws Exception {  
    	if (instance == null) {  
    			instance = new UI();  
    	}  
    	return instance;  
    }  

    public void initUI() {
        mainFrame = createMainFrame();

        mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        addTopPanel();

        drawAndDigitPredictionPanel = new JPanel(new GridLayout());
        addActionPanel();
        addDrawAreaAndPredictionArea();
        mainPanel.add(drawAndDigitPredictionPanel, BorderLayout.CENTER);

        addSignature();

        mainFrame.add(mainPanel, BorderLayout.CENTER);
        mainFrame.setVisible(true);

    }

    private void addActionPanel() {
        JButton recognize = new JButton("Recognize Digit With Simple NN");
        JButton recognizeCNN = new JButton("Recognize Digit With Conv NN");
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
        JPanel actionPanel = new JPanel(new GridLayout(8, 1));
        actionPanel.add(recognizeCNN);
        actionPanel.add(recognize);
        actionPanel.add(clear);
        drawAndDigitPredictionPanel.add(actionPanel);
    }

    private void addDrawAreaAndPredictionArea() {

        drawArea = new DrawAreaController();

        drawAndDigitPredictionPanel.add(drawArea);
        resultPanel = new JPanel();
        resultPanel.setLayout(new GridBagLayout());
        drawAndDigitPredictionPanel.add(resultPanel);
    }

    private void addTopPanel() {
        JPanel topPanel = new JPanel(new FlowLayout());
        JButton trainNN = new JButton("Train NN");
        trainNN.addActionListener(e -> {

            int i = JOptionPane.showConfirmDialog(mainFrame, "Are you sure this may take some time to train?");

            if (i == JOptionPane.OK_OPTION) {
                ProgressBar progressBar = new ProgressBar(mainFrame);
                SwingUtilities.invokeLater(() -> progressBar.showProgressBar("Training may take one or two minutes..."));
                Executors.newCachedThreadPool().submit(() -> {
                    try {
                        LOGGER.info("Start of train Neural Network");
                        neuralNetwork.train((Integer) trainField.getValue(), (Integer) testField.getValue());
                        LOGGER.info("End of train Neural Network");
                    } catch (IOException e1) {
                        LOGGER.error("NN not trained " + e1);
                        throw new RuntimeException(e1);
                    } finally {
                        progressBar.setVisible(false);
                    }
                });
            }
        });

        JButton trainCNN = new JButton("Train Convolutional NN");
        trainCNN.addActionListener(e -> {

            int i = JOptionPane.showConfirmDialog(mainFrame, "Are you sure, training requires >10GB memory and more than 1 hour?");

            if (i == JOptionPane.OK_OPTION) {
                ProgressBar progressBar = new ProgressBar(mainFrame);
                SwingUtilities.invokeLater(() -> progressBar.showProgressBar("Training may take a while..."));
                Executors.newCachedThreadPool().submit(() -> {
                    try {
                        LOGGER.info("Start of train Convolutional Neural Network");
                        convolutionalNeuralNetwork.train((Integer) trainField.getValue(), (Integer) testField.getValue());
                        LOGGER.info("End of train Convolutional Neural Network");
                    } catch (IOException e1) {
                        LOGGER.error("CNN not trained " + e1);
                        throw new RuntimeException(e1);
                    } finally {
                        progressBar.setVisible(false);
                    }
                });
            }
        });

        topPanel.add(trainCNN);
        topPanel.add(trainNN);
        JLabel tL = new JLabel("Training Data");
        tL.setFont(sansSerifBold);
        topPanel.add(tL);
        modelTrainSize = new SpinnerNumberModel(TRAIN_SIZE, 10000, 60000, 1000);
        trainField = new JSpinner(modelTrainSize);
        trainField.setFont(sansSerifBold);
        topPanel.add(trainField);

        JLabel ttL = new JLabel("Test Data");
        ttL.setFont(sansSerifBold);
        topPanel.add(ttL);
        modelTestSize = new SpinnerNumberModel(TEST_SIZE, 1000, 10000, 500);
        testField = new JSpinner(modelTestSize);
        testField.setFont(sansSerifBold);
        topPanel.add(testField);

        mainPanel.add(topPanel, BorderLayout.NORTH);
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