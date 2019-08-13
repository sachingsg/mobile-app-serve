package com.gsg.mongo.repository;

import java.util.List;

import org.springframework.data.mongodb.core.geo.Sphere;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.gsg.mongo.model.AppUser;

public interface AppUserRepository extends MongoRepository<AppUser, String>,AppUserRepositoryCustom  {

	AppUser findByEmail(String emailId);

	AppUser findByUserId(String id);
	
	AppUser findByContactNbr(String contactNbr);
	
//	@Query(value = "{}", fields = "{name : 1}")
//    List<AppUser> findNameAndId();

	@Query(value = "{'roles' : { $in : [ ?0] }}}", fields = "{ userId:1, firstName:1, middleName:1, lastName:1, contactNbr:1,email:1, serviceArea:1,wsStatus:1,wName:1  }" )
	List<AppUser> findUsersByroleDetails(String role);

	@Query(value = "{}", fields = "{ userId:1, firstName:1, middleName:1, lastName:1, roles:1 }" )
	List<AppUser> getAllUserMinimum();
	
	@Query(value = "{'userId' : ?0}", fields = "{ userId:1, firstName:1, middleName:1, lastName:1, roles:1 }" )
	AppUser getBasicDetailsById(String userId);
	
	@Query(value = "{'userId' : { $in : [ ?0] } }", fields = "{ userId:1, firstName:1, middleName:1, lastName:1, roles:1 }" )
	List<AppUser> getBasicDetailsByIds(String[] userIds);
	
	List<AppUser> findByRolesInAndServiceAreaMapLocationWithin(String role, Sphere circle);
	
	@Query(value = "{'roles' : { $in : ['ROLE_WORK_SHOP'] }, 'wsStatus':?0}", fields = "{ userId:1, firstName:1, middleName:1, lastName:1, contactNbr:1,email:1, serviceArea:1,wsStatus:1  }")
	List<AppUser> getWorkShopByStatus(String wsStatus);
	@Query(value = "{ 'coordinates': { '$nearSphere': { '$geometry': { 'type': 'Point', 'coordinates': ?1 }, '$maxDistance': ?0 } }, 'wsStatus':'completed' }")
	List<AppUser> getWorkShopByLocation(int distance, double[] location);
	
}
