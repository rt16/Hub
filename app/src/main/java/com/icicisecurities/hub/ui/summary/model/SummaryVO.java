package com.icicisecurities.hub.ui.summary.model;

public class SummaryVO {

    private String formNumber;
    private String status;
    private String rejectedReason;
    private String rejectedFlag;
    private String submittedDate;
    private String updatedDate;

    public String getSubmittedDate() {
        return submittedDate;
    }

    public void setSubmittedDate(String submittedDate) {
        this.submittedDate = submittedDate;
    }

    public String getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(String updatedDate) {
        this.updatedDate = updatedDate;
    }

    public String getRejectedFlag() {
        return rejectedFlag;
    }

    public void setRejectedFlag(String rejectedFlag) {
        this.rejectedFlag = rejectedFlag;
    }

    public String getRejectedReason() {
        return rejectedReason;
    }

    public void setRejectedReason(String rejectedReason) {
        this.rejectedReason = rejectedReason;
    }

    public String getFormNumber() {
        return formNumber;
    }

    public void setFormNumber(String formNumber) {
        this.formNumber = formNumber;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
