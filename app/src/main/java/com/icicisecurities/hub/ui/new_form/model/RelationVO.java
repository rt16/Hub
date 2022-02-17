package com.icicisecurities.hub.ui.new_form.model;

import java.io.Serializable;

/**
 * Created by yagneshparikh on 18/11/16.
 */
public class RelationVO implements Serializable {

    private String entityCode;
    private String entityDescription;
    private String entityKRAMapCode;

    public String getEntityCode() {
        return entityCode;
    }

    public void setEntityCode(String entityCode) {
        this.entityCode = entityCode;
    }

    public String getEntityDescription() {
        return entityDescription;
    }

    public void setEntityDescription(String entityDescription) {
        this.entityDescription = entityDescription;
    }

    public String getEntityKRAMapCode() {
        return entityKRAMapCode;
    }

    public void setEntityKRAMapCode(String entityKRAMapCode) {
        this.entityKRAMapCode = entityKRAMapCode;
    }
}
