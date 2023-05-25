import java.util.*;

public class NaiveBayes {
    Map<String, Integer> count;
    Map<String, Integer> totalCount;
    Map<String, Double> probabilities;
    List<Instance> trainingData;
    Set<String> classLabels;
    public void training(String file){
        count = new HashMap<>();
        totalCount = new HashMap<>();
        probabilities = new HashMap<>();
        trainingData = new DataReader(file).getInstances();
        classLabels = new HashSet<>();

        for (Instance instance : trainingData)
            classLabels.add(instance.getLabel());


        // Count the numbers of each class and feature value based on the training instances
        for (Instance instance: trainingData) {
            String classLabel = instance.getLabel(); // Last element is the class label

            // Increment class count
            count.put(classLabel, count.getOrDefault(classLabel, 1) + 1);

            // Increment feature counts
            for (String feature : instance.getFeatures()) {
                String featureValue = instance.getValue(feature);
                String key = featureValue + "," + feature + "," + classLabel;
                count.put(key, count.getOrDefault(key, 1) + 1);
            }
        }

        // Calculate the total/denominators
        int classTotal = 0;
        for (String classLabel :classLabels) {
            classTotal+=count.get(classLabel);
            for (String feature : trainingData.get(0).getFeatures()){
                String key = feature + "," + classLabel;
                totalCount.put(key, 0);
                for (String featureValue : getFeaturesValues(feature)){
                    totalCount.put(key, totalCount.getOrDefault(key, 1) + count.getOrDefault(featureValue + "," + key, 1));
                }
            }
        }

        for (String classLabel : classLabels) {
            probabilities.put(classLabel, (double)(count.get(classLabel)) / ((double)classTotal));
            for (String feature : trainingData.get(0).getFeatures()){
                String key = feature + "," + classLabel;
                for (String featureValue : getFeaturesValues(feature)){
                    probabilities.put(featureValue + "," + key, ((double)count.getOrDefault(featureValue + "," + key, 1)) / ((double)totalCount.get(key)));
                }
            }
        }
    }

    public void testing(String file){
        List<Instance> testingData = new DataReader(file).getInstances();
        int correct = 0;
        int count = 0;
        for (Instance instance : testingData) {
            System.out.println("Input Vector: " + instance.getValues());
            count ++;
            double maxScore = 0;
            String predictedLabel = "";
            for (String label : classLabels){
                double score = calcClassScore(instance, label);
                System.out.format("Score(Class = %s) = %f%n", label, score);
                if (score > maxScore){
                    predictedLabel = label;
                    maxScore = score;
                }
            }

            if (predictedLabel.equals(instance.getLabel()))
                correct++;
        }
        System.out.println("Testing accuracy: " + ((double) correct / count));
        printClassProb();
        printConditionalProb();
    }

    private double calcClassScore(Instance instance, String classLabel){
        double score = probabilities.getOrDefault(classLabel, 1.0);
        for (String feature : instance.getFeatures()){
            score *= probabilities.getOrDefault(instance.getValue(feature) + "," + feature + "," + classLabel, 1.0);
        }
        return score;
    }

    private List<String> getFeaturesValues(String feature){
        switch (feature){
            case "age":
                return Arrays.asList("10-19", "20-29", "30-39", "40-49", "50-59", "60-69", "70-79", "80-89", "90-99");
            case "menopause":
                return Arrays.asList("lt40", "ge40", "premeno");

            case "tumor-size":
                return Arrays.asList("0-4", "5-9", "10-14", "15-19", "20-24", "25-29", "30-34", "35-39", "40-44", "45-49", "50-54", "55-59");

            case "inv-nodes":
                return Arrays.asList( "0-2", "3-5", "6-8", "9-11", "12-14", "15-17", "18-20", "21-23", "24-26", "27-29", "30-32", "33-35", "36-39");

            case "node-caps":
                return Arrays.asList("yes", "no");

            case "irradiat":
                return Arrays.asList("yes", "no");

            case "deg-malig":
                return Arrays.asList("1", "2", "3");

            case "breast":
                return Arrays.asList("left", "right");

            case "breast-quad":
                return Arrays.asList( "left up", "left low", "right up", "right low", "central");

            default:
                return new ArrayList<>();
        }
    }

    public void printConditionalProb(){
        System.out.println("Conditional Prob:");
        for (Map.Entry<String, Double> entry : probabilities.entrySet()) {
            String[] keyParts = entry.getKey().split(",");
            if (keyParts.length != 3) continue;
            System.out.println("P(" + keyParts[1] + " = " + keyParts[0] + " | Class = " + keyParts[2] + ") = " + entry.getValue());
        }
    }

    public void printClassProb(){
        System.out.println("Class Prob:");
        for (Map.Entry<String, Double> entry : probabilities.entrySet()) {
            String[] keyParts = entry.getKey().split(",");
            if (keyParts.length != 1) continue;
            System.out.println("P( Class = " + keyParts[0] + ") = " + entry.getValue());
        }
    }
}
