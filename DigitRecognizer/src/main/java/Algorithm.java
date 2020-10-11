import java.io.IOException;
/**
 * @author Xinkai Wang
 * @Created on 10/06/2020
 */
public interface Algorithm {
	
	void init() throws IOException;
	
	int predict(LabeledImage labeledImage);
	
	void train(Integer trainData, Integer testFieldValue) throws IOException;
	
}
