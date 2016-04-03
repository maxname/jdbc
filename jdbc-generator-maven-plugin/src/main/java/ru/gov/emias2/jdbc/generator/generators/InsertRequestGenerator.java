package ru.gov.emias2.jdbc.generator.generators;

import ru.gov.emias2.jdbc.generator.helper.JavaBuilder;
import ru.gov.emias2.jdbc.generator.jaxb.RequestModel;

/**
 * @author mkomlev
 */
public class InsertRequestGenerator extends AbstractGenerator {

    @Override
    protected void generate(RequestModel model, JavaBuilder b, String className) {
        putClassSignature(model, b, "ru.gov.emias2.jdbc.InsertRequest", className,
                getType(model.getResponse().getInsert().getResultClass(), false));
        putConstructor(model, b, className);
        putParametersMethod(model, b);
        putResultClassMethod(model, b, getType(model.getResponse().getInsert().getResultClass(), false));
        putQueryMethod(model, b);
        putGetKeyFieldMethod(model, b);
        putParametersGettersAndSetters(model, b);
        b.put("}");
    }

    private void putGetKeyFieldMethod(RequestModel model, JavaBuilder b) {
        b.fold("Получение идентификатора поля первичного ключа").put();
        b.put("/**");
        b.put(" *  Получение идентификатора поля первичного ключа");
        b.put(" *  @param key Полученный key в формате Number");
        b.put(" *  @return Идентификатор поля первичного ключа");
        b.put(" */");
        b.put("public String getKeyColumn() {");
        b.put("return \"%s;\"", model.getResponse().getInsert().getKeyField());
        b.put("}").put();
        b.unfold();
    }
}
