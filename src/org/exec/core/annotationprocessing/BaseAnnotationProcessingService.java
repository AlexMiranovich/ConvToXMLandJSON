package org.exec.core.annotationprocessing;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.exec.core.nature.BaseNature;

public class BaseAnnotationProcessingService implements AnnotationProcessingService {
  private Map<Class<? extends Annotation>, AnnotationProcessor> _annotationProcessorMap = new HashMap();
  private AnnotatedElement target;

  public void addAnnotationProcessor(AnnotationProcessor annotationProcessor) {
    if (annotationProcessor != null) {
      this._annotationProcessorMap.put(annotationProcessor.forAnnotationClass(), annotationProcessor);
    }
  }
  public Set<AnnotationProcessor> getAnnotationProcessors() {
    return new HashSet(this._annotationProcessorMap.values());
  }
  public <I extends BaseNature> Annotation[] processAnnotations(I info, Annotation[] annotations) throws AnnotationTargetException {
    ArrayList<Annotation> unprocessed = new ArrayList();
    for (int i = 0; i < annotations.length; i++) {
      if (!processAnnotation(info, annotations[i])) {
        unprocessed.add(annotations[i]);
      }
    }
    Annotation[] arrReturn = new Annotation[unprocessed.size()];
    return (Annotation[])unprocessed.toArray(arrReturn);
  }
  public <I extends BaseNature, A extends Annotation> boolean processAnnotation(I info, A annotation) throws AnnotationTargetException {
    boolean processed = false;
    AnnotationProcessor annotationProcessor = (AnnotationProcessor)this._annotationProcessorMap.get(annotation.annotationType());
    if (annotationProcessor != null) {
      processed = annotationProcessor.processAnnotation(info, annotation, target );
    }
    return processed;
  }
}
