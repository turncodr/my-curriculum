package rocks.turncodr.mycurriculum.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import rocks.turncodr.mycurriculum.model.AreaOfStudies;

/**
 *
 * @author rotkodr3d
 *
 * Utility class for AreaOfStudies
 *
 */

@Component
public class AreaOfStudiesUtil {
    //weight definition of the colors, change them in the config if you want different results
    @Autowired
    private int redWeight;
    @Autowired
    private int greenWeight;
    @Autowired
    private int blueWeight;

    /**
     * Calculates if black or white has the highest contrast to the color of the given area of studies. <br>
     * E.g if the area of studies color is black white is returned and vice-versa.
     * @return Either "black" or "white" as a String
     */
    @SuppressWarnings("checkstyle:magicnumber")
    public String calcTextColor(AreaOfStudies areaOfStudies) {
        int color = areaOfStudies.getColor();
        int b = color & 0xFF, g = (color & 0xFF00) >>> 8, r = (color & 0xFF0000) >>> 16;
        final int divisor = redWeight + greenWeight + blueWeight;
        int yiq = ((r * redWeight) + (g * greenWeight) + (b * blueWeight)) / divisor; //division is needed so that we stay in the rgb value range
        return (yiq >= 128) ? "black" : "white";
    }
}
