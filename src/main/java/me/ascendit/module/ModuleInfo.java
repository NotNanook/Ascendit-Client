package me.ascendit.module;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface ModuleInfo {

	Category category();

	public String description();

	public String name();

	boolean silent() default false;
}