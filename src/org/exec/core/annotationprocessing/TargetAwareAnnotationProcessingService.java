package org.exec.core.annotationprocessing;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.util.Set;
import org.exec.core.nature.BaseNature;

public  interface TargetAwareAnnotationProcessingService extends AnnotationProcessingService {
    <I extends BaseNature> Annotation[] processAnnotations(I paramI, Annotation[] paramArrayOfAnnotation, AnnotatedElement paramAnnotatedElement)
    throws AnnotationTargetException;
    <I extends BaseNature, A extends Annotation> boolean processAnnotation(I paramI, A paramA, AnnotatedElement paramAnnotatedElement)
    throws AnnotationTargetException;
    void addAnnotationProcessor(TargetAwareAnnotationProcessor paramTargetAwareAnnotationProcessor);
    Set<TargetAwareAnnotationProcessor> getTargetAwareAnnotationProcessors();
    Set<AnnotationProcessor> getAllAnnotationProcessors();
}
