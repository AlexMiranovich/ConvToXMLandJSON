package org.exec.core.annotationprocessing;

import java.lang.annotation.Annotation;
import java.util.Set;
import org.exec.core.nature.BaseNature;

public interface AnnotationProcessingService {
   <I extends BaseNature> Annotation[] processAnnotations(I paramI, Annotation[] paramArrayOfAnnotation) throws AnnotationTargetException;
   <I extends BaseNature, A extends Annotation> boolean processAnnotation(I paramI, A paramA) throws AnnotationTargetException;
   void addAnnotationProcessor(AnnotationProcessor paramAnnotationProcessor);
   Set<AnnotationProcessor> getAnnotationProcessors();
}
