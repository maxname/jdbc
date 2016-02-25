package ru.gov.emias2.jdbc.generator.generators;

import ru.gov.emias2.jdbc.generator.helper.JavaBuilder;
import ru.gov.emias2.jdbc.generator.jaxb.RequestModel;

/**
 * @author mkomlev
 */
public class PrimitiveRequestGenerator extends AbstractGenerator {

    @Override
    protected void generate(RequestModel model, JavaBuilder b, String className) {
        putClassSignature(model, b, "ru.gov.emias2.jdbc.PrimitiveRequest", className,
                getType(model.getResponse().getPrimitive().getResultClass(), false));
        putConstructor(model, b, className);
        putParametersMethod(model, b);
        putResultClassMethod(model, b, getType(model.getResponse().getPrimitive().getResultClass(), false));
        putQueryMethod(model, b);
        putParametersGettersAndSetters(model, b);
        b.put("}");
    }
}
