package com.dhht.http.annotation;

import androidx.annotation.IntDef;

/**
 *
 */
@IntDef({RequestMethod.Get, RequestMethod.Post})
public @interface RequestMethod {
    int Get = 1;
    int Post = 2;
    int Upload = 3;
}
