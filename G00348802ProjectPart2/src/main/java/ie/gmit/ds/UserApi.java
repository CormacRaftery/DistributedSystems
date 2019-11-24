package ie.gmit.ds;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserApi {

	private HashMap<Integer, UserAccount> userMap = new HashMap<>();

	public UserApi() {
		UserAccount test = new UserAccount(1, "cormac", "cormacraftery@gmail.com", "password");
		userMap.put(test.getUserID(), test);
	}

	//Gets current users
	@GET
	public List<UserAccount> getUsers() {

		return new ArrayList<UserAccount>(userMap.values());
	}

	//Gets individual user
	@Path("{userId}")
	@GET
	public UserAccount getUserById(@PathParam("userId") int userId) {
		return userMap.get(userId);
	}

	//Adds new user
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response add(UserAccount newUser) {
		userMap.put(newUser.getUserID(), newUser);

		String response = "user id: " + newUser.getUserID();
		return Response.status(200).entity(response).build();
	}

	//Updates a user
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	public Response update(UserAccount user) {
		userMap.put(user.getUserID(), user);

		String response = "user id: " + user.getUserID();
		return Response.status(200).entity(response).build();
	}

	//Deletes a user
	@DELETE
	@Consumes(MediaType.APPLICATION_JSON)
	public Response delete(UserAccount user) {
		userMap.remove(user.getUserID());
		String response = "Deleted user id: " + user.getUserID();
		return Response.status(200).entity(response).build();
	}

	// Login
	@Path("/login")
	@POST
	public Response login(UserLogin login) {
		if ((userMap.get(login.getId()) != null)
				&& (userMap.get(login.getId()).getPassword().equals(login.getPassword()))) {
			String response = "Login Successful, for user: " + login.getId();
			return Response.status(200).entity(response).build();
		}
		// or unsuccessful
		else {
			String response = "Login Unsuccessful";
			return Response.status(400).entity(response).build();
		}
	}
}