/**
 * @author Xinkai Wang
 * @Created on 10/06/2020
 */
public class AlgorithmFactory {
	
	public Algorithm getAlgorithm(String algorithmType){
		if(algorithmType == null){
			return null;
		}        
		if(algorithmType.equalsIgnoreCase("NN")){
			return new NeuralNetwork();
		} else if(algorithmType.equalsIgnoreCase("CNN")){
		    return new ConvolutionalNeuralNetwork();
		}
		      
		 return null;
	}
}
