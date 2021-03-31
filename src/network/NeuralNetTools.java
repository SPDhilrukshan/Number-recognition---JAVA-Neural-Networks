package network;

public class NeuralNetTools {

    public static double sigmoid( double val) {
        return 1d / ( 1 + Math.exp(-val));
    }

    public static double derivativeSigmoid(double val) {
        return val * (1 - val);
    }

//    public static double[] createArrayWithSameValue(int size, double initValue){
//        if(size < 1){
//            return null;
//        }
//        double[] ar = new double[size];
//        for(int i = 0; i < size; i++){
//            ar[i] = initValue;
//        }
//        return ar;
//    }

    public static double[] create1DRandomArray(int size, double lowerBound, double upperBound){
        if(size < 1){
            return null;
        }
        double[] ar = new double[size];
        for(int i = 0; i < size; i++){
            ar[i] = returnRandomValue(lowerBound,upperBound);
        }
        return ar;
    }

    public static double[][] create2DRandomArray(int X, int Y, double lowerBound, double upperBound){
        if(X < 1 || Y < 1){
            return null;
        }

        double[][] ar = new double[X][Y];

        for(int i = 0; i < X; i++){
            ar[i] = create1DRandomArray(Y, lowerBound, upperBound);
        }
        return ar;
    }

    public static double returnRandomValue(double lowerBound, double upperBound){
        return Math.random()*(upperBound-lowerBound) + lowerBound;
    }

    public static Integer[] return1DRandomValues(int lowerBound, int upperBound, int amount) {

        lowerBound --;

        if(amount > (upperBound-lowerBound)){
            return null;
        }

        Integer[] arrayValues = new Integer[amount];
        for(int i = 0; i< amount; i++){
            int n = (int)(Math.random() * (upperBound-lowerBound+1) + lowerBound);
            while(containsValue(arrayValues, n)){
                n = (int)(Math.random() * (upperBound-lowerBound+1) + lowerBound);
            }
            arrayValues[i] = n;
        }
        return arrayValues;
    }

    public static <T extends Comparable<T>> boolean containsValue(T[] ar, T value){
        for(int i = 0; i < ar.length; i++){
            if(ar[i] != null){
                if(value.compareTo(ar[i]) == 0){
                    return true;
                }
            }

        }
        return false;
    }

    public static int largestValueIndexInArray(double[] arrayValues){
        int index = 0;
        for(int i = 1; i < arrayValues.length; i++){
            if(arrayValues[i] > arrayValues[index]){
                index = i;
            }
        }
        return index;
    }


}
