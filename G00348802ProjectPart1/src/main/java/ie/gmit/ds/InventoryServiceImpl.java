package ie.gmit.ds;

import com.google.protobuf.BoolValue;
import com.google.protobuf.ByteString;

import io.grpc.stub.StreamObserver;

public class InventoryServiceImpl extends InventoryServiceGrpc.InventoryServiceImplBase {

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
	public void validate(Validate request, StreamObserver<BoolValue> responseObserver) {
		// TODO Auto-generated method stub
		char[] password = request.getPassword().toCharArray();
		byte[] paswordHashed = request.getHashedPassword().toByteArray();
		byte[] salt = request.getSalt().toByteArray();

		//responseObserver.equals(Passwords.isExpectedPassword(password, salt, paswordHashed));
		// Validate the hash matches and return true
        if(Passwords.isExpectedPassword(password, salt, paswordHashed)){
            responseObserver.onNext(BoolValue.newBuilder().setValue(true).build());
        }
        // Else return false
        else{
            responseObserver.onNext(BoolValue.newBuilder().setValue(false).build());
        }
	}
}
