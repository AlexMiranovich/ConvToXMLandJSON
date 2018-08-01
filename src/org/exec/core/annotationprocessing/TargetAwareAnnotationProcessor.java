package org.exec.core.annotationprocessing;

import org.exec.core.nature.BaseNature;
import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;

public interface TargetAwareAnnotationProcessor extends AnnotationProcessor {
    <I extends BaseNature, A extends Annotation> boolean processAnnotation(I var1, A var2, AnnotatedElement var3) throws AnnotationTargetException;
}
