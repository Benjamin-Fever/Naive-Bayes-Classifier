import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Instance {
    private final Map<String, String> data;
    private final String label;
    private final List<String> features;

    public Instance(List<String> attributes, String label, List<String> values){
        this.features = attributes;
        this.label = label;
        this.data = new HashMap<>();

        for(int i = 0; i < attributes.size(); i++){
            data.put(attributes.get(i), values.get(i));
        }
    }

    public String getValue(String feature) {
        return data.get(feature);
    }

    public List<String> getValues() {
        return new ArrayList<>(data.values());
    }

    public String getLabel() {
        return label;
    }

    public List<String> getFeatures() {
        return features;
    }
}
