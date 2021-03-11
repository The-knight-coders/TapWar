package com.nir.apk.library;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
//import com.sun.jndi.toolkit.url.Uri;

import java.io.IOException;

public class MyClass {
	public static void main(String[] args) throws JsonProcessingException {
		System.out.println("Hello word");

		UserDetail u = new UserDetail();
		u.setPersonName("nirbhay");
		u.setPersonEmail("nk@hskdjfl");
		u.setPersonId("sdfdsfdsfd");
		String json = u.toJson();
		System.out.println( UserDetail.toObject(json) );

		ServerResponseBody s = new ServerResponseBody("nirbhay",null,ServerResponseBody.REQUEST_CREATE_GAME);

		System.out.println(s.toJson());
	}
}

class ServerResponseBody {
	public static final int REQUEST_LOGIN = 1;
	public static final int REQUEST_CREATE_GAME = 2;
	public static final int REQUEST_JOIN_GAME = 3;

	public static final String LOGIN = "login";
	public static final String JOIN_GAME = "join_game";
	public static final String CREATE_GAME = "create_game";

	private String user_name;
	private String type;
	private String game_id;

	public ServerResponseBody(@Nullable String userName, @Nullable String gameId, @NonNull int reqCode ) {
		this.user_name = userName;
		this.game_id = gameId;
		findType(reqCode);
	}
	private void findType(int reqCode) {
		if (reqCode == 1) {
			this.type =  LOGIN;
		} else if (reqCode == 2) {
			this.type =  CREATE_GAME;
		} else if (reqCode == 3) {
			this.type =  JOIN_GAME;
		} else {
			this.type = null;
		}
	}

	public String toJson() {
		return new GsonBuilder().create().toJson(this, ServerResponseBody.class);
	}

	public static ServerResponseBody toObject(String json) {
		Gson gson = new GsonBuilder().create();
		ServerResponseBody body= gson.fromJson(json, ServerResponseBody.class);
		return body;
	}


}

class UserDetail {


	private String personName;
	private String personEmail;
	private String personId;


	UserDetail(){

	}
//	public String getPersonName() {
//		return personName;
//	}

	public void setPersonName(String personName) {
		this.personName = personName;
	}

	public String getPersonEmail() {
		return personEmail;
	}

	public void setPersonEmail(String personEmail) {
		this.personEmail = personEmail;
	}

	public String getPersonId() {
		return personId;
	}

	public void setPersonId(String personId) {
		this.personId = personId;
	}

//	public Uri getPersonPhoto() {
//		return personPhoto;
//	}

//	public void setPersonPhoto(Uri personPhoto) {
//		this.personPhoto = personPhoto;
//	}

	public String toJson() {
		return new GsonBuilder().create().toJson(this, UserDetail.class);
	}

	public static UserDetail toObject(String json) {
		Gson gson = new GsonBuilder().create();
		UserDetail user= gson.fromJson(json, UserDetail.class);
		return user;
	}

	public String toString() {
		return "UserDetail{" +
				"personName='" + personName + '\'' +
				", personEmail='" + personEmail + '\'' +
				", personId='" + personId + '\'' +
				'}';
	}
}

abstract class JSON {
	public String toJson() {
		return new GsonBuilder().create().toJson(this, JSON.class);
	}

	public static JSON toObject(String json) {
		Gson gson = new GsonBuilder().create();
		JSON user= gson.fromJson(json, JSON.class);
		return user;
	}
}