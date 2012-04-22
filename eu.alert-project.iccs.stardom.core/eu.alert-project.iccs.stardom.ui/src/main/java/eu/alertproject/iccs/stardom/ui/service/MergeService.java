package eu.alertproject.iccs.stardom.ui.service;

import org.springframework.transaction.annotation.Transactional;

/**
 * User: fotis
 * Date: 22/04/12
 * Time: 17:52
 */
public interface MergeService {
    @Transactional
    void merge(Integer... ids);
}
