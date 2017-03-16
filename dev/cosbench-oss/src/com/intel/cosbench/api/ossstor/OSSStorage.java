package com.intel.cosbench.api.S3Stor;

import static com.intel.cosbench.client.S3Stor.S3Constants.*;

import java.io.*;

import org.apache.http.HttpStatus;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.DeleteObjectsRequest;
import com.aliyun.oss.model.DeleteObjectsResult;
import com.aliyun.oss.model.ListObjectsRequest;
import com.aliyun.oss.model.OSSObjectSummary;
import com.aliyun.oss.model.ObjectListing;

import com.intel.cosbench.api.storage.*;
import com.intel.cosbench.api.context.*;
import com.intel.cosbench.config.Config;
import com.intel.cosbench.log.Logger;

public class OSSStorage extends NoneStorage {
    private int timeout;

    private String accessKey;
    private String secretKey;
    private String endpoint;

    private OSSClient client;

    @Override
    public void init(Config config, Logger logger) {
        super.init(config, logger);

        timeout = config.getInt(CONNN_TIMEOUT_KEY, CONN_TIMEOUT_DEFAULT);

        parms.put(CONN_TIMEOUT_KEY, timeout);

        endpoint = config.get(ENDPOINT_KEY, ENDPOINT_DEFAULT);
        accessKey = config.get(AUTH_USERNAME_KEY, AUTH_USERNAME_DEFAULT);
        secretKey = config.get(AUTH_PASSWORD_KEY, AUTH_PASSWORD_DEFAULT);

        parms.put(ENDPOINT_KEY, endpoint);
        parms.put(AUTH_USERNAME_KEY, accessKey);
        parms.put(AUTH_PASSWORD_KEY, secretKey);
        parms.put(PATH_STYLE_ACCESS_KEY, pathStyleAccess);

        logger.debug("using storage config: {}", parms);

        client = new OSSClient(endpoint, accessKey, secretKey);

        logger.debug("OSS client has been initialized");
    }

    @Override
    public void setAuthContext(AuthContext info) {
        super.setAuthContext(info);
    }

    @Override
    public void dispose() {
        super.dispose();
        client = null;
    }

    @Override
    public InputStream getObject(String container, String object, Config config) {
        super.getObject(container, object, config);
        InputStream stream;
        try {

            OSSObject obj = client.getObject(container, object);
            stream = obj.getObjectContent();

        } catch (Exception e) {
            throw new StorageException(e);
        }
        return stream;
    }

    @Override
    public void createContainer(String container, Config config) {
        super.createContainer(container, config);
        try {
                if(!client.doesBucketExist(container)) {

                    client.createBucket(container);
                }
        } catch (Exception e) {
            throw new StorageException(e);
        }
    }

    @Override
    public void createObject(String container, String object, InputStream data,
            long length, Config config) {
        super.createObject(container, object, data, length, config);
        try {
                ObjectMetadata metadata = new ObjectMetadata();
                metadata.setContentLength(length);
                metadata.setContentType("application/octet-stream");

                client.putObject(container, object, data, metadata);
        } catch (Exception e) {
            throw new StorageException(e);
        }
    }

    @Override
    public void deleteContainer(String container, Config config) {
        super.deleteContainer(container, config);
        try {
                if(client.doesBucketExist(container)) {
                        client.deleteBucket(container);
                }
        } catch(OSSException oe) {
                if(oe.getErrorCode() != HttpStatus.SC_NOT_FOUND) {
                        throw new StorageException(oe);
                }
        } catch (Exception e) {
            throw new StorageException(e);
        }
    }

    @Override
    public void deleteObject(String container, String object, Config config) {
        super.deleteObject(container, object, config);
        try {
            client.deleteObject(container, object);
        } catch(OSSException oe) {
                if(oe.getStatusCode() != HttpStatus.SC_NOT_FOUND) {
                        throw new StorageException(oe);
                }
        } catch (Exception e) {
            throw new StorageException(e);
        }
    }

}

