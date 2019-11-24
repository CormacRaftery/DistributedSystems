package ie.gmit.ds;

import io.dropwizard.Application;
import io.dropwizard.setup.Environment;

public class UserRunner extends Application<UserConfig> {

	public static void main(String[] args) throws Exception {
		new UserRunner().run(args);
	}

	public void run(UserConfig userConfiguration, Environment environment) throws Exception {
		final UserApi resource = new UserApi();

		final UserHealthCheck hc = new UserHealthCheck();
		environment.healthChecks().register("healthcheck", hc);

		environment.jersey().register(resource);
	}
}
