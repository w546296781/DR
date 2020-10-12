import org.apache.spark.ml.classification.MultilayerPerceptronClassificationModel;
import org.apache.spark.ml.classification.MultilayerPerceptronClassifier;
import org.apache.spark.ml.evaluation.MulticlassClassificationEvaluator;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;
import java.util.List;
/**
 * @author klevis.ramo
 * @Created on 11/27/2017
 */
public class NeuralNetwork implements Algorithm {
	private final static Logger LOGGER = LoggerFactory.getLogger(NeuralNetwork.class);
    private SparkSession sparkSession;
    private MultilayerPerceptronClassificationModel model;

    @Override
    public void init() throws IOException {
        initSparkSession();
        if (model == null) {
            LOGGER.info("Loading the Neural Network from saved model ... ");
            model = MultilayerPerceptronClassificationModel.load("resources/nnTrainedModels/ModelWith60000");
            LOGGER.info("Loading from saved model is done");
        }
    }

    private void initSparkSession() {
        if (sparkSession == null) {
            sparkSession = SparkSession.builder()
                    .master("local[*]")
                    .appName("Digit Recognizer")
                    .getOrCreate();
        }
        sparkSession.sparkContext().setCheckpointDir("checkPoint");
    }

    @Override
    public int predict(LabeledImage labeledImage) {
        double predict = model.predict(labeledImage.getFeatures());
        labeledImage.setLabel(predict);
        return (int)labeledImage.getLabel();
    }
}
