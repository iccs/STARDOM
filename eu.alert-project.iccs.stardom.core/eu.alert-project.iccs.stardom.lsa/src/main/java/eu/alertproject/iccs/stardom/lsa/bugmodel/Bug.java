/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.alertproject.iccs.stardom.lsa.bugmodel;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author kostas
 */
public class Bug {


    private String short_desc;
    private String bug_id;
    private String creation_ts;
    private String delta_ts;
    private String reporter_accessible;
    private String cclist_accessible;
    private String classification_id;
    private String classification;
    private String product;
    private String component;
    private String version;
    private String rep_platform;
    private String op_sys;
    private String bug_status;
    private String resolution;
    private String priority;
    private String bug_severity;
    private String target_milestone;
    private String everconfirmed;
    private String reporter;
    private String assigned_to;
    private ArrayList<String> ccs;
    private ArrayList<LongDescType> long_descs;

    public ArrayList<LongDescType> getLong_descs() {
        return long_descs;
    }

    


}
