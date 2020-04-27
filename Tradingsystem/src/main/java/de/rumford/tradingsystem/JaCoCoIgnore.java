package de.rumford.tradingsystem;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * Indicates that a method declaration is to be ignored by JaCoCo test coverage
 * calculation.
 */
@Target(ElementType.METHOD)
public @interface JaCoCoIgnore {

}
