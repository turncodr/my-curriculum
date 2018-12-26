package rocks.turncodr.mycurriculum.validation;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import rocks.turncodr.mycurriculum.model.AreaOfStudies;
import rocks.turncodr.mycurriculum.services.AreaOfStudiesJpaRepository;

/**
*
* Custom validation class for area of studies.
*
*/
@Component("areaOfStudiesValidator")
public class AreaOfStudiesValidator implements Validator {

    @Autowired
    private AreaOfStudiesJpaRepository areaOfStudiesJpaRepository;

    private static final int MIN_COLOR_OFFSET = 40;
    private static final int SQUARE = 2;
    private static final double SQUARE_ROOT = 0.5;
    @Override
    public boolean supports(Class<?> clazz) {
        return AreaOfStudies.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        List<AreaOfStudies> areaOfStudiesList = areaOfStudiesJpaRepository.findAll();
        AreaOfStudies areaOfStudies = (AreaOfStudies) target;
        int[] newColor = intColorToIntRGB(areaOfStudies.getColor());

        for (AreaOfStudies aos : areaOfStudiesList) {
            String existingName = aos.getName().toLowerCase();
            String inputName = areaOfStudies.getName().toLowerCase().trim();
            if (existingName.equals(inputName)) {
                errors.rejectValue("name", "areaOfStudiesCreate.NameError");
                break;
            }
            int[] existingColor = intColorToIntRGB(aos.getColor());
            int colorDifference = 0;

            for (int i = 0; i < existingColor.length && i < newColor.length; i++) {
                colorDifference += Math.pow((existingColor[i] - newColor[i]), SQUARE);
            }
            colorDifference = (int) Math.pow(colorDifference, SQUARE_ROOT); //Calculates the 3D offset to an existing color
            if (colorDifference <= MIN_COLOR_OFFSET) {
                errors.rejectValue("color", "areaOfStudiesCreate.ColorError");
                break;
            }
        }
    }

    @SuppressWarnings("checkstyle:magicnumber")
    private int[] intColorToIntRGB(int color) {
        color >>>= 0;
        int[] rgb = {color & 0xFF, (color & 0xFF00) >>> 8, (color & 0xFF0000) >>> 16};
        return rgb;
    }
}
