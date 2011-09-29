package eu.alertproject.iccs.stardom.constructor.internal;

import eu.alertproject.iccs.stardom.constructor.api.CiCalculatorService;
import org.springframework.stereotype.Service;

/**
 * Created by IntelliJ IDEA.
 * User: fotis
 * Date: 29/09/11
 * Time: 15:59
 * To change this template use File | Settings | File Templates.
 */
@Service("ciCalculatorService")
public class DefaultCiCalculatorService implements CiCalculatorService{

    @Override
    public double ci(int scm, int iam, int mlam){

        double scmWeight =0.5638;
        double iamWeight =0.3807;
        double mlamWeight =0.2723;
        double offset =0.1129;

        //0.5638×SCM+0.3807× IAM+ 0.2723×mlam-0.1129

        return ((double)scm*scmWeight)+((double)iam*iamWeight)+((double)mlam*mlamWeight)-offset;

    }
}
