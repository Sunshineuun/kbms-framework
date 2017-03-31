package com.winning.mybatis.annotation.support;

import java.io.IOException;
import java.util.List;

import org.apache.ibatis.executor.ErrorContext;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.NestedIOException;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternUtils;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.core.type.filter.TypeFilter;
import org.springframework.util.ClassUtils;

import com.winning.annotations.mybatis.Table;
import com.winning.mybatis.builder.AnnoatationMapperBuilder;
import com.winning.mybatis.exceptions.MappingException;

public class AnnotationSqlSessionFactoryBean extends SqlSessionFactoryBean implements ResourceLoaderAware {
	private static final String RESOURCE_PATTERN = "/**/*.class";

	private String[] packagesToScan;
	private ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
	private AnnotationConfiguration annotationConfiguration;
	private List<Interceptor> interceptors;

	private TypeFilter[] entityTypeFilters = new TypeFilter[] { new AnnotationTypeFilter(Table.class, false) };

	public String[] getPackagesToScan() {
		return packagesToScan;
	}

	public void setPackagesToScan(String[] packagesToScan) {
		this.packagesToScan = packagesToScan;
	}

	@Override
	protected SqlSessionFactory buildSqlSessionFactory() throws IOException {
		SqlSessionFactory sqlSessionFactory = super.buildSqlSessionFactory();
		annotationConfiguration = new AnnotationConfiguration(sqlSessionFactory.getConfiguration());
		scanPackages(annotationConfiguration);

		try {
			AnnoatationMapperBuilder annoatationMapperBuilder = new AnnoatationMapperBuilder(annotationConfiguration);
			annoatationMapperBuilder.parse();
		} catch (Exception e) {
			throw new NestedIOException("error", e);
		} finally {
			ErrorContext.instance().reset();
		}

		addInterceptors(sqlSessionFactory.getConfiguration());

		return sqlSessionFactory;
	}

	protected void scanPackages(AnnotationConfiguration config) {
		if (this.packagesToScan != null) {
			try {
				for (String pkg : this.packagesToScan) {
					String pattern = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX
							+ ClassUtils.convertClassNameToResourcePath(pkg) + RESOURCE_PATTERN;
					Resource[] resources = this.resourcePatternResolver.getResources(pattern);
					MetadataReaderFactory readerFactory = new CachingMetadataReaderFactory(this.resourcePatternResolver);
					for (Resource resource : resources) {
						if (resource.isReadable()) {
							MetadataReader reader = readerFactory.getMetadataReader(resource);
							String className = reader.getClassMetadata().getClassName();
							if (matchesFilter(reader, readerFactory)) {
								config.addAnnotatedClass(this.resourcePatternResolver.getClassLoader().loadClass(
										className));
							}
						}
					}
				}
			} catch (IOException ex) {
				throw new MappingException("Failed to scan classpath for unlisted classes", ex);
			} catch (ClassNotFoundException ex) {
				throw new MappingException("Failed to load annotated classes from classpath", ex);
			} catch (Exception ex) {
				throw new MappingException("Failed to addAnnotatedClass", ex);
			}
		}
	}

	private void addInterceptors(Configuration configuration) {
		if (interceptors == null)
			return;

		for (int i = 0; i < interceptors.size(); i++) {
			configuration.addInterceptor(interceptors.get(i));
		}
	}

	public AnnotationConfiguration getAnnotationConfiguration() {
		return annotationConfiguration;
	}

	private boolean matchesFilter(MetadataReader reader, MetadataReaderFactory readerFactory) throws IOException {
		if (this.entityTypeFilters != null) {
			for (TypeFilter filter : this.entityTypeFilters) {
				if (filter.match(reader, readerFactory)) {
					return true;
				}
			}
		}
		return false;
	}

	public void setResourceLoader(ResourceLoader resourceLoader) {
		this.resourcePatternResolver = ResourcePatternUtils.getResourcePatternResolver(resourceLoader);
	}

	public List<Interceptor> getInterceptors() {
		return interceptors;
	}

	public void setInterceptors(List<Interceptor> interceptors) {
		this.interceptors = interceptors;
	}
}
