package MNIST;

import network.DataSet;
import network.NeuralNetTools;
import network.NeuralNetwork;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

public class Mnist {

    public NeuralNetwork createNetwork(){
        NeuralNetwork network = new NeuralNetwork(784, 196, 100, 70, 35, 10); //builds the NN layer

        //read the weights and bias from saved model
        network.setBIASES(network.getBiasFromSavedModel(network));
        network.setWEIGHTS(network.getWeightsFromSavedModel(network));
        return network;
    }

    public NeuralNetwork trainNetwork(NeuralNetwork network){
        DataSet set = createTrainSet(0,4999);
        trainData(network, set, 100, 50, 100);
        return network;
    }

    public static void main(String[] args) {
        NeuralNetwork network = new NeuralNetwork(784, 196, 100, 70, 35, 10); //builds the NN layer

        //read the weights and bias from saved model
        network.setBIASES(network.getBiasFromSavedModel(network));
        network.setWEIGHTS(network.getWeightsFromSavedModel(network));

        double[] outputD = network.calculateImage("/res/two.png");
        System.out.println("final Answer " + NeuralNetTools.largestValueIndexInArray(outputD));
        System.out.println("final Answer " + Arrays.toString(outputD));


        DataSet testSet = createTrainSet(5000,9999);
        testTrainSet(network, testSet, 10);
    }

    public static DataSet createTrainSet(int start, int end) {

        DataSet set = new DataSet(28 * 28, 10);

        try {

            String path = new File("").getAbsolutePath();

            MNISTImage m = new MNISTImage(path + "/res/trainImage.idx3-ubyte", "rw");
            MNISTLabel l = new MNISTLabel(path + "/res/trainLabel.idx1-ubyte", "rw");

            for(int i = start; i <= end; i++) {
                if(i % 100 ==  0){
                    System.out.println("prepared: " + i);
                }

                double[] input = new double[28 * 28];
                double[] output = new double[10];

                output[l.readLabel()] = 1d;
                for(int j = 0; j < 28*28; j++){
                    input[j] = (double)m.read() / (double)256;
                }

                set.addDataToDataSet(input, output);
                m.next();
                l.next();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

         return set;
    }

    public static void trainData(NeuralNetwork net,DataSet set, int epochs, int loops, int batch_size) {
        for(int e = 0; e < epochs;e++) {
            net.train(set, loops, batch_size);
            System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>   "+ e+ "   <<<<<<<<<<<<<<<<<<<<<<<<<<");
        }
    }

    public static double testNetwork(NeuralNetwork net){
        NeuralNetwork network = new NeuralNetwork(784, 196, 100, 70, 35, 10); //builds the NN layer

        //read the weights and bias from saved model
        network.setBIASES(network.getBiasFromSavedModel(network));
        network.setWEIGHTS(network.getWeightsFromSavedModel(network));

        DataSet testSet = createTrainSet(5000,9999);
        return testTrainSet(network, testSet, 10);
    }

    public static double testTrainSet(NeuralNetwork net, DataSet set, int printSteps) {
        int correct = 0;
        for(int i = 0; i < set.size(); i++) {

            double outdetected = NeuralNetTools.largestValueIndexInArray(net.calculate(set.getInputAtIndex(i)));
            double actualOutput = NeuralNetTools.largestValueIndexInArray(set.getOutputAtIndex(i));
            if(outdetected  == actualOutput) {

                correct ++ ;
            }
            if(i % printSteps == 0) {
                System.out.println(i + ": " + (double)correct / (double) (i + 1));
            }
        }
        double accuracy = ((double)correct / (double)set.size()) * 100;
        System.out.println("ACCURACY RESULT: " + correct + " / " + set.size()+ "  -> " + accuracy +" %");

        return accuracy;
    }
}
