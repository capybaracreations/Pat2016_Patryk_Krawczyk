package com.blstreampatronage.patrykkrawczyk.events;

public class LoadNewImagesEvent {
    public final boolean isError;
    public final String message;
    public LoadNewImagesEvent(String m, boolean e) { message = m; isError = e;}
}
