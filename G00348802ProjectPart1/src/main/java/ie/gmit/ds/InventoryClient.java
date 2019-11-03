package ie.gmit.ds;

import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Scanner;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;

public class InventoryClient {

	private static final Logger logger = Logger.getLogger(InventoryClient.class.getName());
	private final ManagedChannel channel;
	//private final InventoryServiceGrpc.InventoryServiceStub asyncInventoryService;
	private final InventoryServiceGrpc.InventoryServiceBlockingStub syncInventoryService;

	public static void main(String[] args) throws Exception {
		int userId=1;
		String userPassword="1";
		Scanner in = new Scanner(System.in);
		System.out.println("Please enter usedId(int):");
		userId = in.nextInt();
        System.out.println("Please enter userPassword(String):");
        in.nextLine();
        userPassword = in.nextLine();
		InventoryClient client = new InventoryClient("localhost", 50551);
		try {
			client.hashPassword(userId,userPassword);

		} finally

		{
			// Don't stop process, keep alive to receive async response
			Thread.currentThread().join();
		}

	}

	public InventoryClient(String host, int port) {
		channel = ManagedChannelBuilder.forAddress(host, port).usePlaintext().build();
		syncInventoryService = InventoryServiceGrpc.newBlockingStub(channel);
		//asyncInventoryService = InventoryServiceGrpc.newStub(channel);
	}

	public void shutdown() throws InterruptedException {
		channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
	}
	public void hashPassword(int id, String password) {
		logger.info("User ID: "+id+"\nPassword: "+password);
		Hash newItem = Hash.newBuilder().setUserId(id).setPassword(password).build();
		HashResponse hashItem; 
		try {
			hashItem = syncInventoryService.hash(newItem);
			logger.info(hashItem.toString());
		} catch
			(StatusRuntimeException ex){
				logger.log(Level.WARNING, "Failed:{0}", ex.getStatus());
				//return
			}
	
	}
}
