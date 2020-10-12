import org.deeplearning4j.datasets.iterator.impl.MnistDataSetIterator;
import org.deeplearning4j.earlystopping.EarlyStoppingConfiguration;
import org.deeplearning4j.earlystopping.EarlyStoppingResult;
import org.deeplearning4j.earlystopping.saver.LocalFileModelSaver;
import org.deeplearning4j.earlystopping.termination.MaxEpochsTerminationCondition;
import org.deeplearning4j.earlystopping.termination.MaxTimeIterationTerminationCondition;
import org.deeplearning4j.earlystopping.trainer.EarlyStoppingTrainer;
import org.deeplearning4j.nn.api.OptimizationAlgorithm;
import org.deeplearning4j.nn.conf.MultiLayerConfiguration;
import org.deeplearning4j.nn.conf.NeuralNetConfiguration;
import org.deeplearning4j.nn.conf.Updater;
import org.deeplearning4j.nn.conf.inputs.InputType;
import org.deeplearning4j.nn.conf.layers.ConvolutionLayer;
import org.deeplearning4j.nn.conf.layers.DenseLayer;
import org.deeplearning4j.nn.conf.layers.OutputLayer;
import org.deeplearning4j.nn.conf.layers.SubsamplingLayer;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.nn.weights.WeightInit;
import org.deeplearning4j.util.ModelSerializer;
import org.nd4j.linalg.activations.Activation;
import org.nd4j.linalg.factory.Nd4j;
import org.nd4j.linalg.lossfunctions.LossFunctions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
/**
 * @author agibsonccc
 * @Created on 9/16/15
 */
public class ConvolutionalNeuralNetwork implements Algorithm {

    private static final String OUT_DIR = "resources/cnnCurrentTrainingModels";
    private static final String TRAINED_MODEL_FILE = "resources/cnnTrainedModels/bestModel.bin";

    private static final Logger LOG = LoggerFactory.getLogger(ConvolutionalNeuralNetwork.class);
    private MultiLayerNetwork preTrainedModel;

    @Override
    public void init() throws IOException {
        preTrainedModel = ModelSerializer.restoreMultiLayerNetwork(new File(TRAINED_MODEL_FILE));
    }

    @Override
    public int predict(LabeledImage labeledImage) {
        double[] pixels = labeledImage.getPixels();
        for (int i = 0; i < pixels.length; i++) {
            pixels[i] = pixels[i] / 255d;
        }
        int[] predict = preTrainedModel.predict(Nd4j.create(pixels));

        return predict[0];
    }
}