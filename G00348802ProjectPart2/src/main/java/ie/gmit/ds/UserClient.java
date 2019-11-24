package ie.gmit.ds;

import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.protobuf.BoolValue;
import com.google.protobuf.ByteString;

import ie.gmit.ds.InventoryServiceGrpc.InventoryServiceBlockingStub;
import ie.gmit.ds.InventoryServiceGrpc.InventoryServiceStub;

import java.util.Scanner;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;
import io.grpc.stub.StreamObserver;

public class UserClient {

	private static final Logger logger = Logger.getLogger(UserClient.class.getName());
	private final ManagedChannel channel;
	private final InventoryServiceBlockingStub syncInventoryService;
	private final InventoryServiceStub asyncInventoryService;
	private int userId;
	private String userPassword;
	private ByteString hashedPassword;
	private ByteString salt;
	Scanner in = new Scanner(System.in);

	public void getUserInput() {
		System.out.println("Enter ID:");
		userId = in.nextInt();
		System.out.println("Enter Password:");
		userPassword = in.next();
	}

	public static void main(String[] args) throws Exception {
		UserClient client = new UserClient("localhost", 50551);
		client.getUserInput();
		try {
			client.hashPassword();
			client.validatePassword();
		} finally

		{
			// Don't stop process, keep alive to receive async response
			Thread.currentThread().join();
		}

	}

	public UserClient(String host, int port) {
		channel = ManagedChannelBuilder.forAddress(host, port).usePlaintext().build();
		syncInventoryService = InventoryServiceGrpc.newBlockingStub(channel);
		asyncInventoryService = InventoryServiceGrpc.newStub(channel);
	}

	public void shutdown() throws InterruptedException {
		channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
	}

	public void hashPassword() {
		logger.info("User ID: " + userId + "\nPassword: " + userPassword);
		Hash newItem = Hash.newBuilder().setUserId(userId).setPassword(userPassword).build();
		HashResponse hashItem;
		try {
			hashItem = syncInventoryService.hash(newItem);
			hashedPassword=hashItem.getHashPassword();
			salt=hashItem.getSalt();
			logger.info(hashItem.toString());
		} catch (StatusRuntimeException ex) {
			logger.log(Level.WARNING, "Failed:{0}", ex.getStatus());
			// return
		}
	}

	public void validatePassword() {
		StreamObserver<BoolValue> responseObserver = new StreamObserver<BoolValue>() {

			@Override
			public void onNext(BoolValue value) {
				if (value.getValue()) {
					logger.info("Correct Values");
				} else {
					logger.info("Username or Password is incorrect");
				}
			}

			@Override
			public void onError(Throwable t) {
				System.out.println("An Error has occurred.. " + t.getLocalizedMessage());
			}

			@Override
			public void onCompleted() { // TODO Auto-generated method stub

			}
		};
		try { 
			logger.info("requesting validation");
			asyncInventoryService.validate(Validate.newBuilder().setPassword(userPassword)
					.setHashedPassword(hashedPassword).setSalt(salt).build(), responseObserver);
			logger.info("returning validation");
		} catch (StatusRuntimeException ex) {
			logger.log(Level.WARNING, "RPC failed: {0}", ex.getStatus());
			return;
		}
	}
}