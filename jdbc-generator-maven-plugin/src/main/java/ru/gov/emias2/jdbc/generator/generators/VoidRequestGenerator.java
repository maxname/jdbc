package ru.gov.emias2.jdbc.generator.generators;

import ru.gov.emias2.jdbc.generator.helper.JavaBuilder;
import ru.gov.emias2.jdbc.generator.jaxb.RequestModel;

/**
 * @author mkomlev
 */
public class VoidRequestGenerator extends AbstractGenerator {

    @Override
    protected void generate(RequestModel model, JavaBuilder b, String className) {
        putClassSignature(model, b, "ru.gov.emias2.jdbc.VoidRequest", className, null);
        putConstructor(model, b, className);
        putParametersMethod(model, b);
        putQueryMethod(model, b);
        putParametersGettersAndSetters(model, b);
        b.put("}");
    }
}
