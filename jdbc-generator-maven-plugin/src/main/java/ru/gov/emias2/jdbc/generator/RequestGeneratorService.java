package ru.gov.emias2.jdbc.generator;

import org.apache.maven.plugin.logging.Log;
import ru.gov.emias2.jdbc.generator.generators.*;
import ru.gov.emias2.jdbc.generator.jaxb.RequestModel;
import ru.gov.emias2.jdbc.generator.jaxb.ResponseType;

import javax.xml.bind.JAXB;
import java.io.File;
import java.util.List;


/**
 * @author maxname
 */
class RequestGeneratorService {

    void generate(List<File> files, File root, Log log) {
        for(File file : files) {
            log.info("[Request Generator Maven Plugin] Generate file " + file.getAbsolutePath());
            generateRequest(JAXB.unmarshal(file, RequestModel.class), root, file.getName().split("\\.")[0]);
        }
    }

    private void generateRequest(final RequestModel requestModel, File root, String className) {
        AbstractGenerator generator = getGenerator(requestModel.getResponse());
        if (generator != null) {
            generator.generate(requestModel, root, className);
        }
    }



    private AbstractGenerator getGenerator(ResponseType type) {
        if (type.isSetCollection()) {
            return new CollectionRequestGenerator();
        }

        if (type.isSetObject()) {
            return new ObjectRequestGenerator();
        }

        if (type.isSetPrimitive()) {
            return new PrimitiveRequestGenerator();
        }

        if (type.isSetInsert()) {
            return new InsertRequestGenerator();
        }

        if (type.isSetVoid()) {
            return new VoidRequestGenerator();
        }

        return null;
    }

}
