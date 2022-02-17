package com.icicisecurities.hub.utils;

public interface FilterLeadsListener {
    /**
     * @param startFilter
     *
     * When User clicks "OK/YES" => i.e. When user wants to Filter the list using To & Form Date then we get true here...
     *
     * Else when User clicks "Cancel/NO", then we get False here....
     */
    void filterLeadsListener(Boolean startFilter);
}
