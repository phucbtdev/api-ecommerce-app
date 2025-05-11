package com.ecommerce_app.util;

import java.text.Normalizer;
import java.util.Locale;
import java.util.regex.Pattern;

/**
 * Utility class for generating URL-friendly slugs from strings
 */
public class SlugUtil {

    private static final Pattern NONLATIN = Pattern.compile("[^\\w-]");
    private static final Pattern WHITESPACE = Pattern.compile("[\\s]");
    private static final Pattern MULTIPLE_HYPHENS = Pattern.compile("-{2,}");

    /**
     * Generate a URL-friendly slug from a string
     *
     * @param input The string to convert to a slug
     * @return A URL-friendly slug
     */
    public static String generateSlug(String input) {
        if (input == null || input.isEmpty()) {
            return "";
        }

        String result = input.toLowerCase(Locale.ENGLISH);

        // Normalize the string (converts accented characters like é to e)
        result = Normalizer.normalize(result, Normalizer.Form.NFD);

        // Remove all non-Latin characters
        result = NONLATIN.matcher(result).replaceAll("");

        // Replace whitespace with hyphens
        result = WHITESPACE.matcher(result).replaceAll("-");

        // Replace multiple consecutive hyphens with a single hyphen
        result = MULTIPLE_HYPHENS.matcher(result).replaceAll("-");

        // Remove leading and trailing hyphens
        result = result.replaceAll("^-", "").replaceAll("-$", "");

        return result;
    }
}