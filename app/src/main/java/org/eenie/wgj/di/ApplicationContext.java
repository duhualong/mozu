package org.eenie.wgj.di;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Qualifier;

/**
 * Application Context annotation

 */
@Qualifier @Retention(RetentionPolicy.RUNTIME) public @interface ApplicationContext {
}
