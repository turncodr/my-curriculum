package rocks.turncodr.mycurriculum.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 *
 * @author rotkodr3d
 *
 * Configuration class for AreaOfStudies, currently it only maps the yml config to a java class and makes the values accessible as beans.
 */

@Configuration
@ConfigurationProperties(prefix = "area-of-studies")
public class AreaOfStudiesConfiguration {
    private int minColorOffset;
    private int redWeight;
    private int greenWeight;
    private int blueWeight;

    @Bean(name = "minColorOffset")
    public int getMinColorOffset() {
        return minColorOffset;
    }

    public void setMinColorOffset(int minColorOffset) {
        this.minColorOffset = minColorOffset;
    }

    @Bean(name = "redWeight")
    public int getRedWeight() {
        return redWeight;
    }

    public void setRedWeight(int redWeight) {
        this.redWeight = redWeight;
    }

    @Bean(name = "greenWeight")
    public int getGreenWeight() {
        return greenWeight;
    }

    public void setGreenWeight(int greenWeight) {
        this.greenWeight = greenWeight;
    }

    @Bean(name = "blueWeight")
    public int getBlueWeight() {
        return blueWeight;
    }

    public void setBlueWeight(int blueWeight) {
        this.blueWeight = blueWeight;
    }
}
