/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Statistics;

import java.util.Arrays;
import java.util.LinkedList;

/**
 *
 * @author id11ab
 */
public class Statistics {
    
    /**
     * Singleton.
     */
    private static Statistics __singleton = new Statistics();

    /**
     * Constructor
     */
    public Statistics(){ }
    
    
    public static LinkedList<Double> tipi(LinkedList<Double> tipiValues, String user){
        
        LinkedList<Double> resultTipi = new LinkedList<Double>();

        resultTipi.add((tipiValues.get(0) + (8.0d - tipiValues.get(5)) ) / 2.0d);
        resultTipi.add((tipiValues.get(6) + (8.0d - tipiValues.get(1)) ) / 2.0d);
        resultTipi.add((tipiValues.get(2) + (8.0d - tipiValues.get(7)) ) / 2.0d);
        resultTipi.add((tipiValues.get(8) + (8.0d - tipiValues.get(3)) ) / 2.0d);
        resultTipi.add((tipiValues.get(4) + (8.0d - tipiValues.get(9)) ) / 2.0d);
        
        printVector(resultTipi, user);
        
        return resultTipi;
    }
    
    public static Double cosineSimilarity(LinkedList<Double> x, LinkedList<Double> y){
        
        if(x.size() == y.size()){
            
            Double numerator = 0.0;
            Double denominatorX = 0.0;
            Double denominatorY = 0.0;
            for(int i=0; i < x.size(); i++){
                numerator += x.get(i) * y.get(i);
                denominatorX += x.get(i) * x.get(i);
                denominatorY += y.get(i) * y.get(i);
            }
        
            return numerator / (Math.sqrt(denominatorX) * Math.sqrt(denominatorY));
        
        }else   return -999.99;

    }

    //Similarity with Euclidean distance
    public static Double euclideanDistanceSimilarity(LinkedList<Double> x, LinkedList<Double> y){
        Double euclideanDistance = 0.0;

        if(x.size() == y.size()){
            for(int i=0; i < x.size(); i++){
                Double value = x.get(i) - y.get(i);
                euclideanDistance += value * value;
            }
        }
        
        return Math.sqrt(euclideanDistance);
    }

    //Euclidean normalization
    public static LinkedList<Double> normalizeVector(LinkedList<Double> v){

        LinkedList normalize = new LinkedList();

        //Euclidean norm for the vector
        Double norm = 0.0;
        for(Double value: v)
            norm += value * value;

        norm = Math.sqrt(norm);

        //Euclidean normalized values
        for(int i=0; i< v.size(); i++)
            normalize.add(v.get(i)/norm);

        return normalize;
    }
    
    /**
     * Pearson Correlation function
     * @param x Vector of double values
     * @param y Vector of double values
     * @return Double (Correlation value)
     */
    public static Double pearsonCorrelation(LinkedList<Double> x, LinkedList<Double> y){
        //Vectors means
        Double xMean = vectorMean(x);
        Double yMean = vectorMean(y);
        
        //Variables needed
        Double denominator = 0.0;
        Double numeratorX = 0.0;
        Double numeratorY = 0.0;
        
        if(x.size() == y.size()){
            for(int i=0; i< x.size(); i++){
                denominator += (x.get(i)-xMean) * (y.get(i) - yMean);
                numeratorX += Math.pow(x.get(i)-xMean,2); 
                numeratorY += Math.pow(y.get(i)-yMean,2);
            }       
        }
        //Return the correlation value
        return denominator / (Math.sqrt(numeratorX) * Math.sqrt(numeratorY));
    }
    
    /**
     * Return the mean value of a vector
     * @param x Vector of values
     * @return Mean of the vector values
     */
    private static Double vectorMean(LinkedList<Double> x){
        Double mean = 0.0;
        for(Double value: x)
            mean += value;
        
        return mean/x.size();
        
    }


    public static double SpearmanCorrelation(LinkedList<Double> X, LinkedList<Double> Y) {

                // Set up the Ranking elements

                LinkedList<Double> XList = X;
                LinkedList<Double> YList = Y;

                // Sort the Ranking lists
                Arrays.sort(XList.toArray());
                Arrays.sort(YList.toArray());

                LinkedList<Integer> xRank = new LinkedList<Integer>();
                LinkedList<Integer> yRank = new LinkedList<Integer>();

                LinkedList<Integer> d1Values = new LinkedList<Integer>();
                LinkedList<Integer> d2Values = new LinkedList<Integer>();

                Integer d2 = 0;

                for(int i=0; i<XList.size(); i++) {
                       xRank.add(XList.indexOf(X.get(i)));
                       yRank.add(YList.indexOf(Y.get(i)));

                       d1Values.add(xRank.getLast()-yRank.getLast());
                       d2Values.add(d1Values.getLast() * d1Values.getLast());

                       d2 += d2Values.getLast();
                }

                double n = X.size();
                double den = n * ((n * n) -1);
                double num = 6.0 * d2;

                double rho = 1- (num/den);
                return rho;
        }//end: GetCorrelation(X,Y)
    
    
    private static void printVector(LinkedList<Double> list, String user){
        System.out.print(user + ": ");
        for (Double value : list) {
             System.out.print(value + " - "); 
        }
        System.out.println( "" );
    }
}
