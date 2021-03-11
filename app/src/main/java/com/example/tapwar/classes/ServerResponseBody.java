package com.example.tapwar.classes;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.gson.GsonBuilder;

public class ServerResponseBody {

	public static final int REQUEST_LOGIN = 1;
	public static final int REQUEST_CREATE_GAME = 2;
	public static final int REQUEST_JOIN_GAME = 3;

	public static final String LOGIN = "login";
	public static final String JOIN_GAME = "join_game";
	public static final String CREATE_GAME = "create_game";

	private String user_name;
	private String type;
	private String game_id;

	public ServerResponseBody(@Nullable String userName, @Nullable String gameId,@NonNull int reqCode ) {
		this.user_name = userName;
		this.game_id = gameId;
		findType(reqCode);
	}
	private void findType(int reqCode) {
		if (reqCode == REQUEST_LOGIN) {
			this.type =  LOGIN;
		} else if (reqCode == REQUEST_CREATE_GAME) {
			this.type =  CREATE_GAME;
		} else if (reqCode == REQUEST_JOIN_GAME) {
			this.type =  JOIN_GAME;
		} else {
			this.type = null;
		}
	}

	public String getUser_name() {
		return user_name;
	}

	public String getType() {
		return type;
	}

	public String getGame_id() {
		return game_id;
	}

	public String toJson(int reqCode) {
		return new GsonBuilder().create().toJson(this, ServerResponseBody.class);
	}

	public static ServerResponseBody toObject(String json) {
		return new GsonBuilder().create().fromJson(json, ServerResponseBody.class);
	}

	@Override
	public String toString() {
		return "ServerResponseBody{" +
				"user_name='" + user_name + '\'' +
				", type='" + type + '\'' +
				", game_id='" + game_id + '\'' +
				'}';
	}
}
