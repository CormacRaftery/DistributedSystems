package ie.gmit.ds;

import org.hibernate.validator.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonProperty;

// Object for the UserLogin
public class UserLogin {
	@NotBlank
	private int userId;
	@NotBlank
	private String userPassword;
	
	public UserLogin(int id, String password) {
		this.userId = id;
		this.userPassword = password;
	}

	public UserLogin() {
	}
	
	@JsonProperty
	public int getId() {
		return userId;
	}

	@JsonProperty
	public String getPassword() {
		return userPassword;
	}
}
