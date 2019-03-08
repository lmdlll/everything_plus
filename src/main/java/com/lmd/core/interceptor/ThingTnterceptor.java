package com.lmd.core.interceptor;

import com.lmd.core.Model.Thing;

@FunctionalInterface
public interface ThingTnterceptor {

    void apply(Thing thing);
}
