package com.gsg.config;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.convert.CustomConversions;
import org.springframework.data.mongodb.core.convert.DbRefResolver;
import org.springframework.data.mongodb.core.convert.DefaultDbRefResolver;
import org.springframework.data.mongodb.core.convert.DefaultMongoTypeMapper;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;

@Configuration
@EnableMongoRepositories(basePackages = { "com.gsg.mongo.repository" },mongoTemplateRef="mongoTemplate")
@EnableMongoAuditing(modifyOnCreate=true)
public class MongoDBConfig {

	@Bean
	public MappingMongoConverter mappingMongoConverter(MongoDbFactory factory, MongoMappingContext mctx,
			BeanFactory beanFactory) {

		DbRefResolver dbRefResolver = new DefaultDbRefResolver(factory);
		MappingMongoConverter mappingMongoConverter = new MappingMongoConverter(dbRefResolver, mctx);
		try {
			mappingMongoConverter.setCustomConversions(beanFactory.getBean(CustomConversions.class));
		} catch (NoSuchBeanDefinitionException ex) {

		}
		mappingMongoConverter.setTypeMapper(new DefaultMongoTypeMapper(null));
		return mappingMongoConverter;
	}

	
//	 public @Bean MongoClient mongoClient() {
//	       return new MongoClient(
//	    		   //Collections.singletonList(new ServerAddress("localhost", 27017)),
//	    		   //Collections.singletonList(MongoCredential.createScramSha1Credential("myUserAdmin", "gsg", "abc123".toCharArray())));
////	    		   new MongoClientURI("mongodb://gsgadmin:gsgadmin@ds131137.mlab.com:31137/gsg")); // prod
////	    		   new MongoClientURI("mongodb://gsgadmin:gsgadmin@ds225028.mlab.com:25028/gsg-test")); // test
//	    		   new MongoClientURI("mongodb://localhost:27017/")); // local
//	       
//	   }
	 public @Bean MongoClient mongoClient(String env) {
		 MongoClient mongoClient = null;
		 MongoClientURI mongoClientURI;
		 switch (env) {
		case "local":
			 mongoClientURI = new MongoClientURI("mongodb://localhost:27017/"); // local
			break;
		case "test":
			 mongoClientURI = new MongoClientURI("mongodb://gsgadmin:gsgadmin@ds225028.mlab.com:25028/gsg-test"); // test
			break;
		
		case "prod":
			 mongoClientURI = new MongoClientURI("mongodb://gsgadmin:gsgadmin@ds131137.mlab.com:31137/gsg"); // prod
			break;
		

		default:
			mongoClientURI = new MongoClientURI("mongodb://gsgadmin:gsgadmin@ds225028.mlab.com:25028/gsg-test"); // test
			break;
		}
		mongoClient = new MongoClient( mongoClientURI);
		return mongoClient;
	       
	   }
	 public @Bean MongoTemplate mongoTemplate() {
	      return new MongoTemplate(mongoClient("prod"), "gsg"); // prod
//		 return new MongoTemplate(mongoClient("test"), "gsg-test"); // test
//		 return new MongoTemplate(mongoClient("local"), "gsg-test"); // local
	  }
}
