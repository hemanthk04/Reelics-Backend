package com.projects.reelics.config;

import java.time.Duration;

public final class RetryPolicy {

    public static final int MAX_RETRIES = 3;
    public static final Duration RETRY_BACKOFF = Duration.ofMinutes(2);

    private RetryPolicy() {}
}
