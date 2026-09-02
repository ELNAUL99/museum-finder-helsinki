package com.museumfinder.web.dto;

import com.museumfinder.domain.Museum;

/**
 * Attribution travelling with a museum photograph. Wikimedia Commons images are
 * mostly CC BY-SA, which requires naming the photographer and the licence wherever
 * the picture appears - so this rides along with every image, not just the detail page.
 */
public record ImageCreditDto(String photographer, String license, String licenseUrl, String sourceUrl) {

    public static ImageCreditDto of(Museum museum) {
        if (museum.getImageUrl() == null || museum.getImageCredit() == null) {
            return null;
        }
        return new ImageCreditDto(museum.getImageCredit(), museum.getImageLicense(),
                museum.getImageLicenseUrl(), museum.getImageSourceUrl());
    }
}
