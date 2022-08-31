package com.mike.lunchvoter.util;

import lombok.experimental.UtilityClass;

import java.time.LocalTime;

@UtilityClass
public class Constants {

    public static final int NON_EXISTING_ID = -1;
    public static final LocalTime TIME_AFTER_WHICH_USER_CANT_CHANGE_VOTE_FOR_TODAY
            = LocalTime.of(11, 0, 0);

}
