package org.exec.core.annotationprocessing;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.util.*;

import org.exec.core.nature.BaseNature;

public abstract class BaseTargetAwareAnnotationProcessingService
        extends BaseAnnotationProcessingService implements TargetAwareAnnotationProcessingService {
  private Map<Class<? extends Annotation>, TargetAwareAnnotationProcessor> _taAnnotationProcessorMap = new HashMap();
    private AnnotatedElement target;

    public void addAnnotationProcessor(TargetAwareAnnotationProcessor taAnnotationProcessor) {
    if (taAnnotationProcessor != null) {
      this._taAnnotationProcessorMap.put(taAnnotationProcessor.forAnnotationClass(), taAnnotationProcessor);
    }
  }
  public Set<AnnotationProcessor> getAllAnnotationProcessors() {
    Set<AnnotationProcessor> result = new HashSet(super.getAnnotationProcessors());
    result.addAll( (Collection<? extends AnnotationProcessor>) this._taAnnotationProcessorMap.values() );
    return result;
  }
  public Set<TargetAwareAnnotationProcessor> getTargetAwareAnnotationProcessors() {
    Set<TargetAwareAnnotationProcessor> result = new HashSet(this._taAnnotationProcessorMap.values());
    return result;
  }
  public <I extends BaseNature, A extends Annotation> boolean processAnnotation(I info, A annotation, AnnotatedElement target)
    throws AnnotationTargetException {
    boolean processed = false;
    TargetAwareAnnotationProcessor annotationProcessor = (TargetAwareAnnotationProcessor)this._taAnnotationProcessorMap.get(annotation.annotationType());
    if (annotationProcessor != null) {
      processed = annotationProcessor.processAnnotation(info, annotation, target);
    }
    return processed;
  }
  public <I extends BaseNature> Annotation[] processAnnotations(I info, Annotation[] annotations, AnnotatedElement target)
    throws AnnotationTargetException {
    ArrayList<Annotation> unprocessed = new ArrayList();
    for (int i = 0; i < annotations.length; i++) {
      if (!processAnnotation(info, annotations[i], target)) {
        unprocessed.add(annotations[i]);
      }
    }
    Annotation[] arrReturn = new Annotation[unprocessed.size()];
    return (Annotation[])unprocessed.toArray(arrReturn);
  }
  public <I extends BaseNature, A extends Annotation> boolean processAnnotation(I info, A annotation) {
      boolean superReturn = false;
      try {
          superReturn = super.processAnnotation(info, annotation);
      } catch (AnnotationTargetException e) {
          e.printStackTrace();
      }
      if (!superReturn) {
      boolean processed = false;
      AnnotationProcessor annotationProcessor = (AnnotationProcessor)this._taAnnotationProcessorMap.get(annotation.annotationType());
      if (annotationProcessor != null) {
          try {
              processed = annotationProcessor.processAnnotation(info, annotation, target );
          } catch (AnnotationTargetException e) {
              e.printStackTrace();
          }
      }
      return processed;
    }
    return superReturn;
  }
  public <I extends BaseNature> Annotation[] processAnnotations(I info, Annotation[] annotations) {
      Annotation[] superUnprocessed = new Annotation[0];
      try {
          superUnprocessed = super.processAnnotations(info, annotations);
      } catch (AnnotationTargetException e) {
          e.printStackTrace();
      }
      ArrayList<Annotation> unprocessed = new ArrayList();
    for (int i = 0; i < superUnprocessed.length; i++) {
      if (!processAnnotation(info, superUnprocessed[i])) {
        unprocessed.add(superUnprocessed[i]);
      }
    }
    Annotation[] arrReturn = new Annotation[unprocessed.size()];
    return (Annotation[])unprocessed.toArray(arrReturn);
  }
}
