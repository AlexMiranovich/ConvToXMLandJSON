package org.exec.core.annotationprocessing;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import org.exec.core.nature.BaseNature;

public  interface AnnotationProcessor {
    Class<? extends Annotation> forAnnotationClass();
    <I extends BaseNature, A extends Annotation> boolean processAnnotation(I paramI, A paramA, AnnotatedElement target) throws AnnotationTargetException;
}
