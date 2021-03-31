package network;

import image.LoadImage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.util.Arrays;
import java.util.Scanner;

public class NeuralNetwork {

    private double[][] OUTPUT; //stores the output for each neuron
    private double[][][] WEIGHTS; //stores the weights for each neuron connection in the format WEIGHTS[layer][currentNeuron][previousNeuron]
    private double[][] BIASES; //stores the biases for each neuron

    private double[][] ERROR; //stores the error val calculated for each neuron
    private double[][] OUTPUT_DERIVATIVE; //stores the derivative val calculated for each neuron

    public final int[] NET_LAYER_SIZE; // stores the number of neurons with layer number as index in 1d array
    public final int   INPUT_SIZE; //number of input neurons
    public final int   OUTPUT_SIZE; //number of input neurons
    public final int   NET_SIZE; // number of layers in neural network

    public double[][][] getWEIGHTS() {
        return WEIGHTS;
    }

    public void setWEIGHTS(double[][][] WEIGHTS) {
        this.WEIGHTS = WEIGHTS;
    }

    public double[][] getBIASES() {
        return BIASES;
    }

    public void setBIASES(double[][] BIASES) {
        this.BIASES = BIASES;
    }

    public NeuralNetwork(int... NET_LAYER_SIZE) {
        this.NET_LAYER_SIZE = NET_LAYER_SIZE;
        this.INPUT_SIZE = NET_LAYER_SIZE[0];
        this.NET_SIZE = NET_LAYER_SIZE.length;
        this.OUTPUT_SIZE = NET_LAYER_SIZE[NET_SIZE-1];

        this.OUTPUT = new double[NET_SIZE][];
        this.WEIGHTS = new double[NET_SIZE][][];
        this.BIASES = new double[NET_SIZE][];

        this.ERROR = new double[NET_SIZE][];
        this.OUTPUT_DERIVATIVE = new double[NET_SIZE][];

        for(int i = 0; i < NET_SIZE; i++) {
            this.OUTPUT[i] = new double[NET_LAYER_SIZE[i]];
            this.ERROR[i] = new double[NET_LAYER_SIZE[i]];
            this.OUTPUT_DERIVATIVE[i] = new double[NET_LAYER_SIZE[i]];

            this.BIASES[i] = NeuralNetTools.create1DRandomArray(NET_LAYER_SIZE[i], -0.5,0.7);

            if(i > 0) {
                WEIGHTS[i] = NeuralNetTools.create2DRandomArray(NET_LAYER_SIZE[i],NET_LAYER_SIZE[i-1], -1,1);
            }
        }
    }

    public void train(DataSet set, int loops, int batch_size) {
        if(set.inputSize != INPUT_SIZE || set.outputSize != OUTPUT_SIZE){
            return;
        }
        for(int i = 0; i < loops; i++) {
            DataSet batch = set.getBatch(batch_size);
            for(int b = 0; b < batch_size; b++) {
                this.train(batch.getInputAtIndex(b), batch.getOutputAtIndex(b), 0.3);
            }
            System.out.println(MSE(batch));
        }
    }

    public void train(double[] input, double[] target, double learningRate) {
        if(input.length != INPUT_SIZE || target.length != OUTPUT_SIZE) {
            return;
        }
        calculate(input);
        backPropagationError(target);
        updateWeights(learningRate);
    }

    public double[] calculate(double... input) {
        if(input.length != this.INPUT_SIZE) {
            return null;
        }
        this.OUTPUT[0] = input;
        for(int layer = 1; layer < NET_SIZE; layer ++) {
            for(int neuron = 0; neuron < NET_LAYER_SIZE[layer]; neuron ++) {

                double sum = BIASES[layer][neuron];
                for(int prevNeuron = 0; prevNeuron < NET_LAYER_SIZE[layer-1]; prevNeuron ++) {
                    sum += OUTPUT[layer-1][prevNeuron] * WEIGHTS[layer][neuron][prevNeuron];
                }
                OUTPUT[layer][neuron] = NeuralNetTools.sigmoid(sum);
                OUTPUT_DERIVATIVE[layer][neuron] = NeuralNetTools.derivativeSigmoid(OUTPUT[layer][neuron]);
            }
        }
        return OUTPUT[NET_SIZE-1];
    }

    public double[] calculateImage1(String imagePath){

        File fileInput = new File(imagePath);

        return calculate(LoadImage.ImageLoader(fileInput));
    }

    public double[] calculateImage(String imagePath){

        String path = new File("").getAbsolutePath();
        File fileInput = new File(path +imagePath);

        return calculate(LoadImage.ImageLoader(fileInput));
    }

    public void backPropagationError(double[] target) {
        for(int neuron = 0; neuron < NET_LAYER_SIZE[NET_SIZE-1]; neuron ++) {
            ERROR[NET_SIZE-1][neuron] = (OUTPUT[NET_SIZE-1][neuron] - target[neuron]) * OUTPUT_DERIVATIVE[NET_SIZE-1][neuron];
        }
        for(int layer = NET_SIZE-2; layer > 0; layer --) {
            for(int neuron = 0; neuron < NET_LAYER_SIZE[layer]; neuron ++){
                double sum = 0;
                for(int nextNeuron = 0; nextNeuron < NET_LAYER_SIZE[layer+1]; nextNeuron ++) {
                    sum += WEIGHTS[layer + 1][nextNeuron][neuron] * ERROR[layer + 1][nextNeuron];
                }
                this.ERROR[layer][neuron] = sum * OUTPUT_DERIVATIVE[layer][neuron];
            }
        }
    }

    public void updateWeights(double learningRate) {
        for(int layer = 1; layer < NET_SIZE; layer++) {
            for(int neuron = 0; neuron < NET_LAYER_SIZE[layer]; neuron++) {

                double error = - learningRate * ERROR[layer][neuron];
                BIASES[layer][neuron] += error;

                for(int prevNeuron = 0; prevNeuron < NET_LAYER_SIZE[layer-1]; prevNeuron ++) {
                    WEIGHTS[layer][neuron][prevNeuron] += error * OUTPUT[layer-1][prevNeuron];
                }
            }
        }
    }

    public double MSE(double[] input, double[] target) {
        if(input.length != INPUT_SIZE || target.length != OUTPUT_SIZE) {
            return 0;
        }
        calculate(input);
        double v = 0;
        for(int i = 0; i < target.length; i++) {
            v += (target[i] - OUTPUT[NET_SIZE-1][i]) * (target[i] - OUTPUT[NET_SIZE-1][i]);
        }
        return v / (2d * target.length);
    }

    public double MSE(DataSet set) {
        double v = 0;
        for(int i = 0; i< set.size(); i++) {
            v += MSE(set.getInputAtIndex(i), set.getOutputAtIndex(i));
        }
        return v / set.size();
    }

    public void saveModel(NeuralNetwork network){

        String path = new File("").getAbsolutePath();

        //save trained WEIGHTS to model
        try (FileWriter myWriter = new FileWriter(path +"/res/new/weightsNEW.txt")) {
            double[][][] WEIGHTS = network.getWEIGHTS();

            for(int layer = 1; layer < network.NET_SIZE; layer ++) {
                for(int neuron = 0; neuron < network.NET_LAYER_SIZE[layer]; neuron ++) {

                    myWriter.write(Arrays.toString(WEIGHTS[layer][neuron]));
                    myWriter.write("\n");
                }
            }

        } catch (Exception e){
            e.printStackTrace();
        }

        //saves BIASES to trained model
        try (FileWriter myWriter = new FileWriter(path +"/res/new/biasNEW.txt")) {
            double[][] BIASES = network.getBIASES();

            for(int layer = 1; layer < network.NET_SIZE; layer ++) {
                myWriter.write(Arrays.toString(BIASES[layer]));
                myWriter.write("\n");
            }

        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public double[][][] getWeightsFromSavedModel(NeuralNetwork network){

        String path = new File("").getAbsolutePath();
        try {
            File myObj = new File(path +"/res/new/weightsNEW.txt");
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {

                double[][][] WEIGHTS = network.getWEIGHTS();

                for(int layer = 1; layer < network.NET_SIZE; layer ++) {
                    for(int neuron = 0; neuron < network.NET_LAYER_SIZE[layer]; neuron ++) {

                        String data = myReader.nextLine().replaceAll("\\s+","");
                        String[] row = data.substring(1, data.length() - 1).split(",");
                        WEIGHTS[layer][neuron] = Arrays.stream(row)
                                .mapToDouble(Double::parseDouble)
                                .toArray();

//                        System.out.println(data+ '\n'+ '\n'+ '\n'+ '\n');
                    }
                }

                return WEIGHTS;
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
            return null;
        }
        return null;
    }

    public double[][] getBiasFromSavedModel(NeuralNetwork network){

        String path = new File("").getAbsolutePath();
        //set BIASES from saved model
        try {
            File myObj = new File(path +"/res/new/biasNEW.txt");
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {

                double[][] BIASES = network.getBIASES();

                for(int layer = 1; layer < network.NET_SIZE; layer ++) {
                    String data = myReader.nextLine();
                    String[] row = data.substring(1, data.length() - 1).split(",");
                    BIASES[layer] = Arrays.stream(row)
                            .mapToDouble(Double::parseDouble)
                            .toArray();
                }

                return BIASES;
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("Exception occurred");
            e.printStackTrace();
            return null;
        }
        return null;
    }
}
