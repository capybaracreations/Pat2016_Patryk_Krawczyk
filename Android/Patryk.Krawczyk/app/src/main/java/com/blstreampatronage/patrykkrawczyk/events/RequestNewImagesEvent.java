package com.blstreampatronage.patrykkrawczyk.events;

public class RequestNewImagesEvent {
    public final String message;

    /**
     * Used to signal that we want to download JSON containing next page of images
     * @param strMessage should contain URL at which we will point GET request to retrieve JSON
     */
    public RequestNewImagesEvent(String strMessage) { message = strMessage; }
}
