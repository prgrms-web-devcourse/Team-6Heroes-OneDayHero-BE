package com.sixheroes.onedayheroapplication.user.response;

public record ProfileCitizenResponse(
    UserBasicInfoResponse basicInfo,
    UserImageResponse image,
    Integer heroScore
) {
}
