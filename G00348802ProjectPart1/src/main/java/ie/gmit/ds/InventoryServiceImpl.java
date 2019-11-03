package ie.gmit.ds;

import java.util.logging.Logger;
import com.google.protobuf.ByteString;

import io.grpc.stub.StreamObserver;

public class InventoryServiceImpl extends InventoryServiceGrpc.InventoryServiceImplBase {
	private static final Logger logger = Logger.getLogger(InventoryServiceImpl.class.getName());

	public InventoryServiceImpl() {
	}

	@Override
	public void hash(Hash item, StreamObserver<HashResponse> responseObserver) {
		String password = item.getPassword();
		char[] hashString = password.toCharArray();
		
		byte[] addSalt = Passwords.getNextSalt();
		
		byte[] passwordHashed = Passwords.hash(hashString, addSalt);
		
		responseObserver.onNext(HashResponse.newBuilder().setUserId(item.getUserId()).setHashPassword(ByteString.copyFrom(passwordHashed)).setSalt(ByteString.copyFrom(addSalt)).build());
		responseObserver.onCompleted();
	}

	@Override
	public void validate(Validate request, StreamObserver responseObserver) {
		// TODO Auto-generated method stub
		char[] password = request.getPassword().toCharArray();
		byte[] paswordHashed = request.getHashedPassword().toByteArray();
		byte[] salt = request.getSalt().toByteArray();

		responseObserver.equals(Passwords.isExpectedPassword(password, salt, paswordHashed));

	}
}
