package de.nexible.gauge.testrail.sync.model;

import com.google.common.collect.ImmutableList;
import com.thoughtworks.gauge.Spec;
import de.nexible.gauge.testrail.config.TestRailUtil;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public final class GaugeSpec extends Tagged {
    private Path specFile;
    private List<GaugeScenario> scenarios = new ArrayList<>();

    private GaugeSpec() {
        // static constructor
    }

    public static GaugeSpec newInstance(Spec.ProtoSpec spec) {
        GaugeSpec gaugeSpec = new GaugeSpec();
        gaugeSpec.specFile = Paths.get(spec.getFileName());
        gaugeSpec.tag = spec.getTagsList().stream().filter(TestRailUtil::isSectionTag).findAny();
        gaugeSpec.heading = spec.getSpecHeading();
        gaugeSpec.setSteps(spec.getItemsList());
        return gaugeSpec;
    }

    public Tagged addScenario(Spec.ProtoScenario scenario) {
        GaugeScenario tagged = GaugeScenario.newInstance(scenario);
        scenarios.add(tagged);
        return tagged;
    }

    public List<GaugeScenario> getScenarios() {
        return ImmutableList.copyOf(scenarios);
    }

    public Path getSpecFile() {
        return specFile;
    }

    public Optional<GaugeScenario> findScenarioByName(String line) {
        String onlyHeading = line.substring(3).trim();
        return scenarios.stream().filter(t -> t.getHeading().equals(onlyHeading)).findFirst();
    }

    @Override
    public String toString() {
        return "GaugeSpec{" +
                "specFile=" + specFile +
                ", scenarios=" + scenarios +
                ", heading='" + heading + '\'' +
                ", tag=" + tag +
                '}';
    }
}