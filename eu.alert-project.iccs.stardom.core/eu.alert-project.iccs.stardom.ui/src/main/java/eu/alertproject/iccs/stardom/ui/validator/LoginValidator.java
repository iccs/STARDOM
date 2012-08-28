package eu.alertproject.iccs.stardom.ui.validator;

import eu.alertproject.iccs.stardom.ui.beans.ProfileBean;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

/**
 * Created with IntelliJ IDEA.
 * User: fotis
 * Date: 8/28/12
 * Time: 11:15 PM
 * To change this template use File | Settings | File Templates.
 */
public class LoginValidator implements Validator {


    @Override
    public boolean supports(Class<?> aClass) {
        return ProfileBean.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {

        ValidationUtils.rejectIfEmpty(errors, "email","email.empty");
        ValidationUtils.rejectIfEmpty(errors, "code","code.empty");

    }
}

