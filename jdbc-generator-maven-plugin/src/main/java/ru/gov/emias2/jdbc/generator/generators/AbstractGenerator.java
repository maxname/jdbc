package ru.gov.emias2.jdbc.generator.generators;

import ru.gov.emias2.jdbc.generator.helper.BasePath;
import ru.gov.emias2.jdbc.generator.helper.Fields;
import ru.gov.emias2.jdbc.generator.helper.JavaBuilder;
import ru.gov.emias2.jdbc.generator.helper.Strings;
import ru.gov.emias2.jdbc.generator.jaxb.*;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author mkomlev
 */
public abstract class AbstractGenerator {
    protected abstract void generate(final RequestModel model, final JavaBuilder b, final String className);

    public void generate(final RequestModel requestModel, final File root, String className) {
        MetaModel metaModel = requestModel.getMeta();
        String outputDirectory = BasePath.get(root, metaModel.getPackageName());

        JavaBuilder b = JavaBuilder.get(className, metaModel.getPackageName(), outputDirectory);
        this.generate(requestModel, b, className);
        b.save();
    }

    String getType(ParameterType type, boolean isCollection) {
        String result = PARAMETER_TYPE_STRING_MAP.get(type);
        if (isCollection) {
            result = "java.util.List<" + result + ">";
        }
        return result;
    }

    private final static Map<ParameterType, String> PARAMETER_TYPE_STRING_MAP = new HashMap<ParameterType, String>() {{
        put(ParameterType.STRING, "String");
        put(ParameterType.INT, "Integer");
        put(ParameterType.LONG, "Long");
        put(ParameterType.NUMBER, "Decimal");
        put(ParameterType.DATE, "java.util.Date");
    }};

    private String getConstructorSignature(List<ParameterModel> params) {
        StringBuilder sb = new StringBuilder();
        boolean separate = false;
        for (ParameterModel parameterModel : params) {
            if (separate) {
                sb.append(", ");
            }
            sb.append(getType(parameterModel.getType(), parameterModel.isIsCollection()));
            sb.append(" ");
            sb.append(Fields.name(parameterModel.getName()));
            separate = true;
        }
        return sb.toString();
    }

    void putConstructor(RequestModel requestModel, JavaBuilder b, String className) {
        b.fold("Конструктор").put();
        b.doc("Конструктор по-умолчанию для «%s»", requestModel.getMeta().getDescription());
        b.put("public %s() {", className).put("}").put();

        if (requestModel.isSetParameters() && requestModel.getParameters().isSetParam()) {
            b.put("/**");
            b.put(" *  Конструктор с параметрами для «%s»", requestModel.getMeta().getDescription());
            for (ParameterModel param : requestModel.getParameters().getParam()) {
                b.put(" *  @param %s %s", Fields.name(param.getName()), param.getDescription());
            }
            b.put(" */");
            b.put("public %s(%s) {", className, getConstructorSignature(requestModel.getParameters().getParam()));
            b.put("this();");
            for (ParameterModel param : requestModel.getParameters().getParam()) {
                b.put("set%s(%s);", Strings.capitalize(param.getName()), Fields.name(param.getName()));
            }
            b.put("}").put();
        }
        b.unfold();
    }

    void putResultClassMethod(RequestModel requestModel, JavaBuilder b, String resultClass) {
        b.fold("Результирующий класс").put();
        b.put("/**");
        b.put(" *  Получить класс результирующего объекта");
        b.put(" *  @return Результирующий класс");
        b.put(" */");
        b.put("public Class<%s> getResultClass() {", resultClass);
        b.put("return %s.class;", resultClass);
        b.put("}").put();
        b.unfold();
    }

    void putMapper(RequestModel requestModel, JavaBuilder b, ClassResponseType response) {
        b.fold("Маппер").put();
        if (response.isSetCustomRowMapperClass()) {
            b.put("private static final org.springframework.jdbc.core.RowMapper<%s> MAPPER = new %s();",
                    response.getResultClass(), response.getCustomRowMapperClass()).put();
        } else {
            b.put("private static final org.springframework.jdbc.core.RowMapper<%s> MAPPER = org.springframework.jdbc.core.BeanPropertyRowMapper.newInstance(%s.class);",
                    response.getResultClass(), response.getResultClass()).put();
        }

        b.put("/**");
        b.put(" *  Получить маппер источника данных в объекты");
        b.put(" *  @return Маппер данных из таблицы БД в объект Java");
        b.put(" */");
        b.put("public org.springframework.jdbc.core.RowMapper<%s> getMapper() {", response.getResultClass());
        b.put("return MAPPER;");
        b.put("}").put();
        b.unfold();
    }

    void putQueryMethod(RequestModel requestModel, JavaBuilder b) {
        b.fold("Запрос").put();
        String sql = requestModel.getSql().getValue();
        sql = sql.replace("\"", "\\\"").replace("\r", "\\r").replace("\n", "\\n");
        b.put("private static final String QUERY = \"%s\";", sql).put();
        b.put("/**");
        b.put(" *  Получить текст запроса");
        b.put(" *  @return SQL-строка запроса к БД");
        b.put(" */");
        b.put("public String getQuery() {");
        b.put("return QUERY;");
        b.put("}").put();
        b.unfold();
    }

    void putParametersMethod(RequestModel requestModel, JavaBuilder b) {
        b.fold("Параметры").put();
        b.put("private final java.util.Map<String, Object> parameters = new java.util.HashMap<String, Object>();").put();

        b.put("/**");
        b.put(" *  Получить все параметры");
        b.put(" *  @return Мапа со значениями всех параметров, которые используются при запросе");
        b.put(" */");
        b.put("public java.util.Map<String, Object> getParameters() {");
        b.put("return parameters;");
        b.put("}").put();
        b.unfold();
    }

    void putParametersGettersAndSetters(RequestModel requestModel, JavaBuilder b) {
        if (requestModel.isSetParameters() && requestModel.getParameters().isSetParam()) {
            for (ParameterModel param : requestModel.getParameters().getParam()) {
                String type = getType(param.getType(), param.isIsCollection());
                String name = param.getName();
                String capName = Strings.capitalize(name);

                b.fold("Параметр «%s» (%s)", name, param.getDescription()).put();

                b.put("/**");
                b.put(" *  Получить значение параметра «%s» (%s)", name, param.getDescription());
                b.put(" *  @return Значение параметра «%s» (%s)", name, param.getDescription());
                b.put(" */");
                b.put("public %s get%s() {", type, capName);
                b.put("return (%s)parameters.get(\"%s\");", type, name);
                b.put("}").put();

                b.put("/**");
                b.put(" *  Задать значение параметра «%s» (%s)", name, param.getDescription());
                b.put(" *  @param %s %s", name, param.getDescription());
                b.put(" */");
                b.put("public void set%s(%s %s) {", capName, type, name);
                b.put("parameters.put(\"%s\", %s);", name, name);
                b.put("}").put();

                b.unfold();
            }
        }
    }

    void putClassSignature(RequestModel requestModel, JavaBuilder b, String interfaceName, String className, String responseClass) {
        b.doc(requestModel.getMeta().getDescription());
        if (responseClass != null && !"".equals(responseClass)) {
            b.put("public final class %s implements %s<%s> {", className, interfaceName, responseClass).put();
        } else {
            b.put("public final class %s implements %s {", className, interfaceName).put();
        }
    }


}
