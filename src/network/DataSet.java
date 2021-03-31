package network;

import java.util.ArrayList;

public class DataSet {


    public final int inputSize;
    public final int outputSize;

    //double[][] <- index1: 0 = input, 1 = output
    private ArrayList<double[][]> data = new ArrayList<>();

    public DataSet(int inputSize, int outputSize) {
        this.inputSize = inputSize;
        this.outputSize = outputSize;
    }

    public void addDataToDataSet(double[] in, double[] expected) {
        if(in.length != inputSize || expected.length != outputSize) return;
        data.add(new double[][]{in, expected});
    }

    public DataSet getBatch(int size) {
        if(size > 0 && size <= this.size()) {
            DataSet set = new DataSet(inputSize, outputSize);
            Integer[] ids = NeuralNetTools.return1DRandomValues(0,this.size() - 1, size);
            for(Integer i:ids) {
                set.addDataToDataSet(this.getInputAtIndex(i),this.getOutputAtIndex(i));
            }
            return set;
        }else return this;
    }

    public int size() {
        return data.size();
    }

    public double[] getInputAtIndex(int index) {
        if(index >= 0 && index < size())
            return data.get(index)[0];
        else return null;
    }

    public double[] getOutputAtIndex(int index) {
        if(index >= 0 && index < size())
            return data.get(index)[1];
        else return null;
    }
}
