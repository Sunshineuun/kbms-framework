package com.winning.mybatis.builder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.apache.ibatis.builder.BaseBuilder;
import org.apache.ibatis.builder.BuilderException;
import org.apache.ibatis.builder.CacheRefResolver;
import org.apache.ibatis.builder.IncompleteElementException;
import org.apache.ibatis.builder.MapperBuilderAssistant;
import org.apache.ibatis.builder.ResultMapResolver;
import org.apache.ibatis.builder.xml.XMLStatementBuilder;
import org.apache.ibatis.mapping.ResultFlag;
import org.apache.ibatis.mapping.ResultMapping;

import com.winning.mybatis.annotation.support.AnnotationConfiguration;
import com.winning.mybatis.support.ClassMap;
import com.winning.mybatis.support.ClassMap.Property;

public class AnnoatationMapperBuilder extends BaseBuilder {
	private MapperBuilderAssistant builderAssistant;
	private AnnotationConfiguration annotationConfiguration;
	private final String RESOURCE = "annotations";
	private final String namespace = "DefaultMapper";

	public AnnoatationMapperBuilder(AnnotationConfiguration annotationConfiguration) {
		super(annotationConfiguration.getConfiguration());
		this.builderAssistant = new MapperBuilderAssistant(configuration, RESOURCE);
		this.annotationConfiguration = annotationConfiguration;
	}

	public void parse() {
		if (!configuration.isResourceLoaded(RESOURCE)) {
			configurationAnnotation(annotationConfiguration.getClassMapList());
			configuration.addLoadedResource(RESOURCE);
		}

		parsePendingResultMaps();
		parsePendingChacheRefs();
		parsePendingStatements();
	}

	private void configurationAnnotation(List<ClassMap> classMaps) {
		try {
			builderAssistant.setCurrentNamespace(namespace);
			resultMapAnnotations(classMaps);
			buildStatementFromContext(classMaps);
		} catch (Exception e) {
			throw new BuilderException("Error parsing Mapper XML. Cause: " + e, e);
		}
	}

	private void buildStatementFromContext(List<ClassMap> classMaps) {
		buildStatementFromContext(classMaps, configuration.getDatabaseId());
	}

	private void buildStatementFromContext(List<ClassMap> classMaps, String requiredDatabaseId) {
		List<Statement> statements = createStatements(classMaps);
		for (Statement statement : statements) {
			final AnnotationStatementBuilder statementBuilder = new AnnotationStatementBuilder(configuration,
					builderAssistant, statement, requiredDatabaseId);
			statementBuilder.parseStatementNode();
		}
	}

	private List<Statement> createStatements(List<ClassMap> classMaps) {
		List<Statement> statements = new ArrayList<Statement>();
		for (ClassMap classMap : classMaps) {
			Statement selectStatement = new Statement();
			selectStatement.setId(StatementKeyGenerator.generateSelectStatementKey(classMap.getType()));
			selectStatement.setResultMap(classMap.getResultMapId());
			selectStatement.setSqlCommandType("select");
			statements.add(selectStatement);
		}
		return statements;
	}

	private void parsePendingResultMaps() {
		Collection<ResultMapResolver> incompleteResultMaps = configuration.getIncompleteResultMaps();
		synchronized (incompleteResultMaps) {
			Iterator<ResultMapResolver> iter = incompleteResultMaps.iterator();
			while (iter.hasNext()) {
				try {
					iter.next().resolve();
					iter.remove();
				} catch (IncompleteElementException e) {
					// ResultMap is still missing a resource...
				}
			}
		}
	}

	private void parsePendingChacheRefs() {
		Collection<CacheRefResolver> incompleteCacheRefs = configuration.getIncompleteCacheRefs();
		synchronized (incompleteCacheRefs) {
			Iterator<CacheRefResolver> iter = incompleteCacheRefs.iterator();
			while (iter.hasNext()) {
				try {
					iter.next().resolveCacheRef();
					iter.remove();
				} catch (IncompleteElementException e) {
					// Cache ref is still missing a resource...
				}
			}
		}
	}

	private void parsePendingStatements() {
		Collection<XMLStatementBuilder> incompleteStatements = configuration.getIncompleteStatements();
		synchronized (incompleteStatements) {
			Iterator<XMLStatementBuilder> iter = incompleteStatements.iterator();
			while (iter.hasNext()) {
				try {
					iter.next().parseStatementNode();
					iter.remove();
				} catch (IncompleteElementException e) {
					// Statement is still missing a resource...
				}
			}
		}
	}

	private void resultMapAnnotations(List<ClassMap> classMaps) throws Exception {
		for (ClassMap classMap : classMaps) {
			List<ResultMapping> resultMappings = new ArrayList<ResultMapping>();
			for (Property property : classMap.getProperties()) {
				ResultMapping.Builder resultMappingBuilder = new ResultMapping.Builder(configuration,
						property.getName(), property.getColumn(), property.getType());
				resultMappings.add(resultMappingBuilder.build());
			}

			for (Property property : classMap.getIdProperties()) {
				ResultMapping.Builder resultMappingBuilder = new ResultMapping.Builder(configuration,
						property.getName(), property.getColumn(), property.getType());
				List<ResultFlag> flags = new ArrayList<ResultFlag>();
				flags.add(ResultFlag.ID);
				resultMappingBuilder.flags(flags);
				resultMappings.add(resultMappingBuilder.build());
			}

			ResultMapResolver resultMapResolver = new ResultMapResolver(builderAssistant, classMap.getResultMapId(),
					classMap.getType(), null, null, resultMappings, null);
			try {
				resultMapResolver.resolve();
			} catch (IncompleteElementException e) {
				configuration.addIncompleteResultMap(resultMapResolver);
				throw e;
			}

		}
	}

}
