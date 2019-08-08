package online.iizvv.utils;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.*;
import online.iizvv.core.config.Config;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * @author ：iizvv
 * @date ：Created in 2019-07-24 10:24
 * @description：TODO
 * @version: 1.0
 */
@Component
public class FileManager {

    /**
      * create by: iizvv
      * description: 上传临时文件
      * create time: 2019-07-24 10:32

      * @return
      */
    public void uploadFile(File file, String objName) {
        uploadFile(file, objName, true);
    }

    public void uploadFile(byte[] bytes, String objName) {
        uploadFile(bytes, objName, true);
    }

    /**
      * create by: iizvv
      * description: 上传文件
      * create time: 2019-07-24 10:27
      
      * @return 
      */
    public void uploadFile(File file, String objName, Boolean isTemp) {
        String bucket = isTemp==true?Config.aliTempBucket:Config.aliMainBucket;
        OSS ossClient = new OSSClientBuilder().build(Config.vpcEndpoint, Config.accessKeyID, Config.accessKeySecret);
        InitiateMultipartUploadRequest request = new InitiateMultipartUploadRequest(bucket, objName);
        InitiateMultipartUploadResult result = ossClient.initiateMultipartUpload(request);
        String uploadId = result.getUploadId();
        List<PartETag> partETags =  new ArrayList<PartETag>();
        final long partSize = 4 * 1024 * 1024L;
        long fileLength = file.length();
        int partCount = (int) (fileLength / partSize);
        if (fileLength % partSize != 0) {
            partCount++;
        }
        for (int i = 0; i < partCount; i++) {
            long startPos = i * partSize;
            long curPartSize = (i + 1 == partCount) ? (fileLength - startPos) : partSize;
            InputStream instream = null;
            try {
                instream = new FileInputStream(file);
                // 跳过已经上传的分片。
                instream.skip(startPos);
                UploadPartRequest uploadPartRequest = new UploadPartRequest();
                uploadPartRequest.setBucketName(bucket);
                uploadPartRequest.setKey(objName);
                uploadPartRequest.setUploadId(uploadId);
                uploadPartRequest.setInputStream(instream);
                // 设置分片大小。除了最后一个分片没有大小限制，其他的分片最小为100KB。
                uploadPartRequest.setPartSize(curPartSize);
                // 设置分片号。每一个上传的分片都有一个分片号，取值范围是1~10000，如果超出这个范围，OSS将返回InvalidArgument的错误码。
                uploadPartRequest.setPartNumber( i + 1);
                // 每个分片不需要按顺序上传，甚至可以在不同客户端上传，OSS会按照分片号排序组成完整的文件。
                UploadPartResult uploadPartResult = ossClient.uploadPart(uploadPartRequest);
                // 每次上传分片之后，OSS的返回结果会包含一个PartETag。PartETag将被保存到partETags中。
                partETags.add(uploadPartResult.getPartETag());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Collections.sort(partETags, new Comparator<PartETag>() {
            public int compare(PartETag p1, PartETag p2) {
                return p1.getPartNumber() - p2.getPartNumber();
            }
        });
        CompleteMultipartUploadRequest completeMultipartUploadRequest =
                new CompleteMultipartUploadRequest(bucket, objName, uploadId, partETags);
        ossClient.completeMultipartUpload(completeMultipartUploadRequest);
        ossClient.shutdown();
    }

    /**
      * create by: iizvv
      * description: 字节数组方式上传文件
      * create time: 2019-07-24 11:14

      * @return void
      */
    public void uploadFile(byte[] bytes, String objName, Boolean isTemp) {
        String bucket = isTemp==true?Config.aliTempBucket:Config.aliMainBucket;
        OSS ossClient = new OSSClientBuilder().build(Config.vpcEndpoint, Config.accessKeyID, Config.accessKeySecret);
        ossClient.putObject(bucket, objName, new ByteArrayInputStream(bytes));
        ossClient.shutdown();
    }

    /**
      * create by: iizvv
      * description: 获取文件后缀名
      * create time: 2019-07-24 10:53

      * @return 后缀名
      */
    public String getSuffixName(File file) {
        String fileName = file.getName();
        String suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
        return suffix;
    }

}
