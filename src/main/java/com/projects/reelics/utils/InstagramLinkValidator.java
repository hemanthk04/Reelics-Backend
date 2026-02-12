package com.projects.reelics.utils;

import java.util.regex.Pattern;

public class InstagramLinkValidator {

    private static final Pattern INSTAGRAM_URL_PATTERN = Pattern.compile(
            "^https?://(www\\.)?instagram\\.com/(reel|p|tv)/[A-Za-z0-9_-]+/?(\\?.*)?$",
            Pattern.CASE_INSENSITIVE
    );

    private InstagramLinkValidator() {}

    public static boolean isLinkValid(String url) {
        return url != null && INSTAGRAM_URL_PATTERN.matcher(url).matches();
    }
}
