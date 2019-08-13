package com.gsg.services;

import com.gsg.error.GenericException;
import com.gsg.mongo.model.AppUser;

public interface DashBoardService {


	AppUser createNewUserByCC(AppUser usr) throws GenericException;

}
