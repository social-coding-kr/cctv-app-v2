package com.socialcoding.models;

/**
 * Created by yoon on 2016. 10. 24..
 */
public class Law {
    private int singleLawLayoutId;
    private int singleLawTitleTxtId;
    private int singleLawContentTxtId;

    public Law(int singleLawLayoutId, int singleLawTitleTxtId, int singleLawContentTxtId) {
        this.singleLawLayoutId = singleLawLayoutId;
        this.singleLawTitleTxtId = singleLawTitleTxtId;
        this.singleLawContentTxtId = singleLawContentTxtId;
    }

    public int getSingleLawLayoutId() {
        return singleLawLayoutId;
    }
    public int getSingleLawTitleTxtId() {
        return singleLawTitleTxtId;
    }
    public int getSingleLawContentTxtId() {
        return singleLawContentTxtId;
    }
}
