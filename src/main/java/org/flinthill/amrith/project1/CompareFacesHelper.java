/**
 * 
 */
package org.flinthill.amrith.project1;


import java.util.List;

import org.flinthill.independentstudy.amrith.SearchFaces;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.auth.profile.ProfilesConfigFile;
import com.amazonaws.regions.Regions; 

import com.amazonaws.services.rekognition.AmazonRekognition;
import com.amazonaws.services.rekognition.AmazonRekognitionClientBuilder;
import com.amazonaws.services.rekognition.model.*; 

/**
 * 
 * @author Amrith
 *
 */
public class CompareFacesHelper {
	
	String photo = "Zoo2.jpeg";

	String facesPhoto = "GroupPicture1.jpg";

	String bucket = "amrithtest";
//
//	String compareFace1 = "Dylan1.jpg";
//
//	String compareFace2 = "AmrithSad.jpg";

    String AccessKey ="";
    
    String AccessSecereteKey = "";
    
	
	public String compareFaces (String bucketName , String compareFace1, String compareFace2) {
		String result = "";
		
		try {
		
			ProfilesConfigFile configFile = new ProfilesConfigFile("credential"); 
				AWSCredentials credentials = new ProfileCredentialsProvider(configFile,"default").getCredentials();

			//AWSCredentials credentials = new ProfileCredentialsProvider("default").getCredentials();
			System.out.println(credentials.getAWSSecretKey());
			AmazonRekognition rekognitionClient = AmazonRekognitionClientBuilder
			.standard()
			  .withRegion(Regions.US_EAST_1)
			.withCredentials(new AWSStaticCredentialsProvider(credentials))
			.build();
			CompareFacesRequest compareFacesRequest = new CompareFacesRequest()
			.withSimilarityThreshold(70F)
			.withSourceImage(new com.amazonaws.services.rekognition.model.Image()
					.withS3Object(new com.amazonaws.services.rekognition.model.S3Object()
			.withBucket(bucketName)
			.withName(compareFace1)))
			.withTargetImage(new com.amazonaws.services.rekognition.model.Image() 
	         .withS3Object(new S3Object()
			.withBucket(bucketName)
			.withName(compareFace2)));
			CompareFacesResult compareResult = rekognitionClient.compareFaces(compareFacesRequest);
			List <CompareFacesMatch>  compareFaceMatch= compareResult.getFaceMatches();
			
			System.out.println("Matching Size : "+ compareFaceMatch.size());
			
				if (compareFaceMatch.size() == 0) {
	
					result ="You are NOT a FlintHill Student.  Go away !!!  ";
				}
	
		
		
		
			for (CompareFacesMatch comparingFace: compareFaceMatch ) {
				System.out.println("Similarity: " + comparingFace.getSimilarity());
				ComparedFace comparedface = comparingFace.getFace();
				if (comparedface.getConfidence() > 70F){
					result ="You are a FlintHill Student with Confidance level : "+comparedface.getConfidence();
					return result;
				}
				System.out.println ("Compared Face confidence lvl: " + comparedface.getConfidence());
	
			  }

		}catch(Exception e) {
		System.out.println(e.getMessage());

		}
		
		return result;

		}
	 
	public String searchFaces(String collectionId, String bucketName, String fileName){
		   
		   String result ="";
		   SearchFaces searchfaces = new SearchFaces(); 
		   
		   searchfaces.setAmazonRekognition();
		   
		  // fileName = "AmrithSad.jpg";
		   Float threshold = 92F;
 	       int maxFaces = 25;
 	      
	      Image image = getImageUtil(bucketName, fileName);
	
	      //5. Search collection for faces similar to the largest face in the image.
	      SearchFacesByImageResult searchFacesByImageResult =
	    	searchfaces.callSearchFacesByImage(
	    			collectionId, image, threshold, maxFaces, searchfaces.getAmazonRekognition()
	    			);
	      
	      //System.out.println("Faces matching largest face in image  " + fileName);
	      List < FaceMatch > faceImageMatches = searchFacesByImageResult.getFaceMatches();
	      System.out.println("Result set size :"+ faceImageMatches.size());
	      
	      if (faceImageMatches.size() == 0){
	    	  System.out.println("You are not a FlintHill student");
	    	  result = "You are not a FlintHill student." ;
	      }else{
	    	  System.out.println(" You are a FlintHill Student, Access Granted ");
	    	  result = "You are a FlintHill Student, Access Granted";
	      }
	      for (FaceMatch face: faceImageMatches) {
	         System.out.println(face.getFace().toString());
	         result = result+ "You are Name is : "+face.getFace().getExternalImageId();
	         
	      }
	      
	      return result;		
	}
	
	
	 private static Image getImageUtil(String bucket, String key) {
	      return new Image()
	         .withS3Object(new S3Object()
	            .withBucket(bucket)
	            .withName(key));
	  }
	 
	
	public void detectFaces() {

		try {

			AWSCredentials credentials = new ProfileCredentialsProvider("default").getCredentials();
	
	
			System.out.println(credentials.getAWSSecretKey());
	
	
			//AmazonS3 s3client =  AmazonS3ClientBuilder.defaultClient();
	
			System.out.println("After S3 build");
	
			AmazonRekognition rekognitionClient = AmazonRekognitionClientBuilder
	
	
			.standard()
	
			  .withRegion(Regions.US_EAST_1)
	
			.withCredentials(new AWSStaticCredentialsProvider(credentials))
	
			.build();
	
	
			DetectFacesRequest faceRequest =  new DetectFacesRequest().withImage(new com.amazonaws.services.rekognition.model.Image()
	
			.withS3Object(new S3Object()
	
			.withName(facesPhoto)
	
			.withBucket(bucket)))
	
			.withAttributes(Attribute.ALL);


			try {
	
			DetectFacesResult result = rekognitionClient.detectFaces(faceRequest);
	
			List <FaceDetail> faceDetails = result.getFaceDetails();
	
			for (FaceDetail face: faceDetails) {
	
	
			if (faceRequest.getAttributes().contains("ALL")){
	
			AgeRange age = face.getAgeRange(); 
	
			System.out.println ("Your age range is: " + age.getLow() + "-" + age.getHigh());
	
			List<Emotion> emotionList = face.getEmotions();
	
	
			for (Emotion emotion: emotionList) {
	
			System.out.println(emotion.getType());
	
			}
	
	
	
			Gender gender = face.getGender();
	
			System.out.println (gender.getValue());
	
	
			Smile smile = face.getSmile();
	
			System.out.println ("Smiling?: " + smile.getValue());
	
			}
	
			}
	
	
	
			}catch(AmazonRekognitionException e) {
	
			e.printStackTrace();
	
	
			}





		}catch(Exception e) {


		System.out.println(e.getMessage());

		}


		}
	
	
	public void detectLabels () {

		try {
		
		ProfilesConfigFile configFile = new ProfilesConfigFile("credential"); 
		//configFile.

		AWSCredentials credentials = new ProfileCredentialsProvider(configFile,"default").getCredentials();


		System.out.println(credentials.getAWSSecretKey());


		//AmazonS3 s3client =  AmazonS3ClientBuilder.defaultClient();

		System.out.println("After S3 build");

		AmazonRekognition rekognitionClient = AmazonRekognitionClientBuilder


		.standard()

		  .withRegion(Regions.US_EAST_1)

		.withCredentials(new AWSStaticCredentialsProvider(credentials))

		//.withClientConfiguration(new ClientConfiguration().)
		.build();
		
		//rekognitionClient.
		 

		//Region reg = new Region(regionImpl)*/


		System.out.println("After Rekognition build");


		//rekognitionClient.setRegion();


		DetectLabelsRequest request = new DetectLabelsRequest()

		.withImage(new com.amazonaws.services.rekognition.model.Image()

		.withS3Object(new S3Object()

		.withName(photo).withBucket(bucket)))

		.withMaxLabels(100)

		.withMinConfidence(40F);


		DetectLabelsResult result = rekognitionClient.detectLabels(request);

		List <com.amazonaws.services.rekognition.model.Label> labels = result.getLabels();



		for(int i = 0 ; i<labels.size(); i++) {


		System.out.println(labels);



		}



		}catch (Exception e) {


		System.out.println("In deterct Error message " +e.getMessage()); 

		}



		}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
			
	}


	public String getAccessKey() {
		return AccessKey;
	}


	public void setAccessKey(String accessKey) {
		AccessKey = accessKey;
	}


	public String getAccessSecereteKey() {
		return AccessSecereteKey;
	}


	public void setAccessSecereteKey(String accessSecereteKey) {
		AccessSecereteKey = accessSecereteKey;
	}

}
