package com.blstreampatronage.patrykkrawczyk.events;

public class LoadNewImagesEvent {
    public final boolean isError;
    public final String  message;

    /**
     * Used to signal that new images are ready to be loaded
     * @param strMessage should contain JSON with urls to iamges that we want to feed to recyclerView
     * @param boolError defines wheter this event signals an error during downloading of JSON
     */
    public LoadNewImagesEvent(String strMessage, boolean boolError) { message = strMessage; isError = boolError;}
}
