package lsit;

import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import com.google.cloud.storage.Bucket;
import com.google.cloud.storage.Blob;

public class GCSService {
    private final Storage storage;
    private final String bucketName = "your-gcs-bucket-name"; // Replace with your GCS bucket name

    public GCSService() {
        // Initialize the storage client using default credentials
        this.storage = StorageOptions.getDefaultInstance().getService();
    }

    // Get the GCS client
    public Storage getStorage() {
        return storage;
    }

    // Upload a file to Google Cloud Storage
    public void uploadFile(String fileName, String content) {
        // Get a bucket
        Bucket bucket = storage.get(bucketName);

        // Upload file as an object
        bucket.create(fileName, content.getBytes());
    }

    // Download a file from Google Cloud Storage
    public String downloadFile(String fileName) {
        Bucket bucket = storage.get(bucketName);
        Blob blob = bucket.get(fileName);

        return new String(blob.getContent());
    }
}

