package moe.ingstar.ee.config.probability;

import com.google.gson.annotations.SerializedName;

public class ProbabilityConfig {
    @SerializedName("absolute_immunity_probability")
    private double absoluteImmunityProbability;

    public double getAbsoluteImmunityProbability() {
        return absoluteImmunityProbability;
    }

    public void setAbsoluteImmunityProbability(double probability) {
        this.absoluteImmunityProbability = probability;
    }
}
