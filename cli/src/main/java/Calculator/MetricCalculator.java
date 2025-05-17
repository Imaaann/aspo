package Calculator;

import java.util.Map;
import com.aspodev.SCAR.Model;

public interface MetricCalculator {

    public abstract Map<String, Double> calculate(Model SCAR);
}