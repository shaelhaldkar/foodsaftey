package com.itgc.foodsafety.utils;

import android.content.Context;
import android.util.Log;

import com.amazonaws.mobile.client.AWSMobileClient;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;

import java.io.File;

/**
 * Created by anil on 13/04/18.
 */

public class S3FileUploadHelper {

    private static final String TAG = S3FileUploadHelper.class.getSimpleName();

    private Context context;
    private FileTransferListener transferListener;
    String fileName;


    public S3FileUploadHelper(Context context) {
        this.context = context;
    }

    public void upload(String sourceFilePath, String fileName1)
    {
        this.fileName=fileName1;
        TransferUtility transferUtility =
                TransferUtility.builder()
                        .context(context.getApplicationContext())
                        .awsConfiguration(AWSMobileClient.getInstance().getConfiguration())
                        .s3Client(new AmazonS3Client(AWSMobileClient.getInstance().getCredentialsProvider()))
                        .build();

        TransferObserver uploadObserver =
                transferUtility.upload(
                        fileName,
                        new File(sourceFilePath), CannedAccessControlList.PublicRead);

        // Attach a listener to the observer to get state update and progress notifications
        uploadObserver.setTransferListener(new TransferListener() {

            @Override
            public void onStateChanged(int id, TransferState state) {
                if (TransferState.COMPLETED == state) {
                    // Handle a completed upload.
                   // AppLogger.d("YourActivity", "s3 file transfer completed");

                    if (transferListener != null){
                        transferListener.onSuccess(id, state, fileName);
                    }


                }
            }

            @Override
            public void onProgressChanged(int id, long bytesCurrent, long bytesTotal) {
                float percentDonef = ((float) bytesCurrent / (float) bytesTotal) * 100;
                int percentDone = (int) percentDonef;

                Log.d("YourActivity", "ID:" + id + " bytesCurrent: " + bytesCurrent
                        + " bytesTotal: " + bytesTotal + " " + percentDone + "%");

                if (transferListener != null){
                    transferListener.onProgressChanged(id, bytesCurrent, bytesTotal);
                }
            }

            @Override
            public void onError(int id, Exception ex) {

                if (transferListener != null){
                    transferListener.onError(id, ex);
                }
                // Handle errors
                ex.printStackTrace();
           //     AppLogger.d("YourActivity", "s3 file transfer error");

            }

        });
    }

    public interface FileTransferListener {

        void onSuccess(int id, TransferState state, String fileName);

        void onProgressChanged(int id, long bytesCurrent, long bytesTotal);

        void onError(int id, Exception ex);
    }

    public void setFileTransferListener(FileTransferListener transferListener) {
        this.transferListener = transferListener;
    }
}
