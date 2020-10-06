import java.io.IOException;

public interface Algorithm {
	
	void init() throws IOException;
	
	int predict(LabeledImage labeledImage);
	
	void train(Integer trainData, Integer testFieldValue) throws IOException;
	
}
