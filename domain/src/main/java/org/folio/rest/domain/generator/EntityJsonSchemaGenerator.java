package org.folio.rest.domain.generator;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AnnotationTypeFilter;

import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.kjetland.jackson.jsonSchema.JsonSchemaGenerator;

public class EntityJsonSchemaGenerator {

  private static final String JSON_SCHEMA_DIRECTORY = "schema";

  public static void main(String[] args) {

    String path = EntityJsonSchemaGenerator.class.getClassLoader().getResource(".").getPath();

    File directory = new File(path + File.separator + ".." + File.separator + JSON_SCHEMA_DIRECTORY);
    if (!directory.exists()) {
      directory.mkdir();
    }

    ObjectMapper objectMapper = new ObjectMapper();
    ObjectWriter objectWriter = objectMapper.writer(new DefaultPrettyPrinter());

    JsonSchemaGenerator jsonSchemaGenerator = new JsonSchemaGenerator(objectMapper);

    for (Class<?> entity : getEntities()) {
      JsonNode jsonSchema = jsonSchemaGenerator.generateJsonSchema(entity);
      String entityName = entity.getSimpleName().toLowerCase();
      String jsonFilePath = directory.getAbsolutePath() + File.separator + entityName + ".json";
      File jsonFile = new File(jsonFilePath);
      try {
        objectWriter.writeValue(jsonFile, jsonSchema);
      } catch (IOException e) {
        throw new RuntimeException("Unable to write jsonschema for " + entity.getName(), e);
      }
    }
  }

  private static Set<Class<?>> getEntities() {
    Set<Class<?>> entities = new HashSet<Class<?>>();
    ClassPathScanningCandidateComponentProvider provider = new ClassPathScanningCandidateComponentProvider(false);
    provider.addIncludeFilter(new AnnotationTypeFilter(Entity.class));
    Set<BeanDefinition> beanDefinitions = provider.findCandidateComponents("org.folio");
    for (BeanDefinition beanDefinition : beanDefinitions) {
      try {
        entities.add(Class.forName(beanDefinition.getBeanClassName()));
      } catch (ClassNotFoundException e) {
        throw new RuntimeException("Unable to find class for " + beanDefinition.getBeanClassName(), e);
      }
    }
    return entities;
  }

}
