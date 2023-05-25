import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        NaiveBayes nb = new NaiveBayes();
        nb.training(args[0]);
        nb.testing(args[1]);
    }
}