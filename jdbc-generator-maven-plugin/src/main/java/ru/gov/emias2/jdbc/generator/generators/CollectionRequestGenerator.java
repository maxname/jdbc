package ru.gov.emias2.jdbc.generator.generators;

import ru.gov.emias2.jdbc.generator.helper.JavaBuilder;
import ru.gov.emias2.jdbc.generator.jaxb.RequestModel;

/**
 * @author mkomlev
 */
public class CollectionRequestGenerator extends AbstractGenerator {

    @Override
    protected void generate(RequestModel model, JavaBuilder b, String className) {
        putClassSignature(model, b, "ru.gov.emias2.jdbc.CollectionRequest", className,
                model.getResponse().getCollection().getResultClass());
        putConstructor(model, b, className);
        putMapper(model, b, model.getResponse().getCollection());
        putParametersMethod(model, b);
        putResultClassMethod(model, b, model.getResponse().getCollection().getResultClass());
        putQueryMethod(model, b);
        putParametersGettersAndSetters(model, b);
        b.put("}");
    }

}
