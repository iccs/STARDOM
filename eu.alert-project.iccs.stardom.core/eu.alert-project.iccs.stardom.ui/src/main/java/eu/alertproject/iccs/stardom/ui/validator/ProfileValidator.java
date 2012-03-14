package eu.alertproject.iccs.stardom.ui.validator;

import eu.alertproject.iccs.stardom.domain.api.Profile;
import eu.alertproject.iccs.stardom.ui.beans.ProfileBean;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

/**
 * User: fotis
 * Date: 14/03/12
 * Time: 23:18
 */
public class ProfileValidator implements Validator{


    @Override
    public boolean supports(Class<?> aClass) {
        return ProfileBean.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {

        ValidationUtils.rejectIfEmpty(errors, "name","name.empty");
        ValidationUtils.rejectIfEmpty(errors, "lastname","lastname.empty");
        ValidationUtils.rejectIfEmpty(errors, "username","username.empty");
        ValidationUtils.rejectIfEmpty(errors, "email","email.empty");
        ValidationUtils.rejectIfEmpty(errors, "description","description.empty");

    }
}
