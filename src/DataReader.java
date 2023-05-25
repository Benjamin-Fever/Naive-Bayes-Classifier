import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class DataReader {
    private final List<Instance> instances = new ArrayList<>();

    DataReader(String fname){
        try {
            Scanner lineScanner = new Scanner(new File(fname));
            Scanner scanner = new Scanner(lineScanner.next());
            scanner.useDelimiter(",");
            scanner.next(); // Ignore class label

            List<String> attributes = new ArrayList<>();

            for (int i = 0; i < 9; i++)
                attributes.add(scanner.next());


            while (lineScanner.hasNext()){
                List<String> values = new ArrayList<>();
                scanner = new Scanner(lineScanner.next());
                scanner.useDelimiter(",");
                scanner.next(); // Ignore line number

                String classification = scanner.next();

                while (scanner.hasNext())
                    values.add(scanner.next());

                Instance i = new Instance(attributes, classification, values);
                instances.add(i);
            }
        }
        catch(FileNotFoundException e){
            System.out.println("File not found");
        }
    }

    public List<Instance> getInstances(){
        return instances;
    }
}

