package com.yatoufang.complete.model;

import java.util.concurrent.ScheduledFuture;
import java.util.function.Supplier;

/**
 * @author hse
 * @since 2023/1/23 0023
 */
public class CompletionCache {

    private String[] predictions;
    private int offset;
    private String verifyToken;
    private boolean empty;
    private Supplier<ScheduledFuture<?>> timeoutSupplier;

    public CompletionCache() {
        this.predictions = new String[0];
        this.offset = -1;
        this.verifyToken = "";
        this.empty = true;
        this.timeoutSupplier = null;

    }

    public void setPredictions(String[] predictions) {
        this.predictions = predictions;
    }

    public String[] getPredictions() {
        return predictions;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public int getOffset() {
        return offset;
    }

    public void setVerifyToken(String verifyToken) {
        this.verifyToken = verifyToken;
    }

    public String getVerifyToken() {
        return verifyToken;
    }

    public void setEmpty(boolean empty) {
        this.empty = empty;
    }

    public boolean isEmpty() {
        return empty;
    }

    public void setTimeoutSupplier(Supplier<ScheduledFuture<?>> timeoutSupplier) {
        this.timeoutSupplier = timeoutSupplier;
    }

    public Supplier<ScheduledFuture<?>> getTimeoutSupplier() {
        return timeoutSupplier;
    }
}
