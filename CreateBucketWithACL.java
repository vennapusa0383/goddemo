import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.AccessControlList;
import com.amazonaws.services.s3.model.CanonicalGrantee;
import com.amazonaws.services.s3.model.Grant;
import com.amazonaws.services.s3.model.GroupGrantee;
import com.amazonaws.services.s3.model.Permission;

public class CreateBucketWithACL {

	public static void main(String[] args) throws IOException {
		Regions clientRegion = Regions.US_WEST_1;
		String bucketName = "com-elsevier-mhub-nonprod";
		System.out.println("cmd args :" + args[0]);
		try {
			AmazonS3 s3Client = AmazonS3ClientBuilder.standard().withRegion(clientRegion).build();
			/*
			 * @param bucketName The name of an existing bucket, to which you have {@link
			 * Permission#Write} permission.
			 * 
			 * @param key The key under which to store the specified file.
			 * 
			 * @param file The file containing the data to be uploaded to Amazon S3.
			 */
			s3Client.putObject(bucketName, "orgDB/items", new File(args[0]));

			// Create a collection of grants to add to the bucket.
			ArrayList<Grant> grantCollection = new ArrayList<Grant>();

			// Grant the LogDelivery group permission to write to the bucket.
			Grant grant2 = new Grant(GroupGrantee.LogDelivery, Permission.Write);
			grantCollection.add(grant2);

			// Save grants by replacing all current ACL grants with the two we just created.
			AccessControlList bucketAcl = new AccessControlList();
			bucketAcl.grantAllPermissions(grantCollection.toArray(new Grant[0]));
			s3Client.setBucketAcl(bucketName, bucketAcl);

		} catch (AmazonServiceException e) {
			e.printStackTrace();
			System.out.println("Stack Trace " + e);
		} catch (SdkClientException e) {
			e.printStackTrace();
			System.out.println("Stack Trace " + e);
		}
	}
}
