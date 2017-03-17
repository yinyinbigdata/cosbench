package com.intel.cosbench.api.ossstor;

import com.intel.cosbench.api.storage.*;

public class OSSStorageFactory implements StorageAPIFactory {

    @Override
    public String getStorageName() {
        return "oss";
    }

    @Override
    public StorageAPI getStorageAPI() {
        return new OSSStorage();
    }

}
