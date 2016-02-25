package ru.gov.emias2.jdbc.generator.generators;

import ru.gov.emias2.jdbc.generator.jaxb.RequestModel;
import ru.gov.emias2.jdbc.generator.helper.JavaBuilder;
import ru.gov.emias2.jdbc.generator.jaxb.InsertResponseEnumType;

/**
 * @author mkomlev
 */
public class InsertRequestGenerator extends AbstractGenerator {

    @Override
    protected void generate(RequestModel model, JavaBuilder b, String className) {
        putClassSignature(model, b, "ru.gov.emias2.jdbc.InsertRequest", className,
                getType(model.getResponse().getInsert().getResultClass()));
        putConstructor(model, b, className);
        putParametersMethod(model, b);
        putResultClassMethod(model, b, getType(model.getResponse().getInsert().getResultClass()));
        putQueryMethod(model, b);
        putGetKeyMethod(model, b);
        putParametersGettersAndSetters(model, b);
        b.put("}");
    }

    private void putGetKeyMethod(RequestModel model, JavaBuilder b) {
        b.fold("Приведение полученного идентификатора к нужному типу").put();
        b.put("/**");
        b.put(" *  Приведение полученного идентификатора к нужному типу");
        b.put(" *  @param key Полученный key в формате Number");
        b.put(" *  @return Идентификатор, приведенный к нужному типу");
        b.put(" */");
        b.put("public %s getKeyValue(Number key) {", getType(model.getResponse().getInsert().getResultClass()));
        b.put("return key.%sValue();", getValueMethodSuffix(model.getResponse().getInsert().getResultClass()));
        b.put("}").put();
        b.unfold();
    }

    private String getType(InsertResponseEnumType type) {
        switch (type) {
            case INT:
                return "Integer";
            case LONG:
                return "Long";
        }
        return null;
    }

    private String getValueMethodSuffix(InsertResponseEnumType type) {
        switch (type) {
            case INT:
                return "int";
            case LONG:
                return "long";
        }
        return null;
    }
}
