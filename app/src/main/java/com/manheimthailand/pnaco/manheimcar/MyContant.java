package com.manheimthailand.pnaco.manheimcar;

import org.jibble.simpleftp.SimpleFTP;

/**
 * Created by man on 23/10/2559.
 */

public class MyContant {
    // Explicit
    private String hostString = "ftp.swiftcodingthai.com";
    private int portAnInt = 21;
    private String userFTPString = "Man@swiftcodingthai.com";
    private String passFTPwordString = "Abc12345";
    private String urlImageString = "http://swiftcodingthai.com/Man/images";
    private String urlAddUserString = "http://swiftcodingthai.com/Man/add_user_naco.php";
    private String urlJSONString = "http://swiftcodingthai.com/Man/get_data_naco.php";
    private String testTitleString = "User find not founded.";
    private String testMessageString = "User find not founded in DataBase.";
    private String urlEditLocationString = "http://swiftcodingthai.com/Man/edit_location_naco.php";

    public String getUrlEditLocationString() {
        return urlEditLocationString;
    }

    public String getTestTitleString() {
        return testTitleString;
    }

    public String getTestMessageString() {
        return testMessageString;
    }

    public String getUrlJSONString() {
        return urlJSONString;
    }

    public String getUrlAddUserString() {
        return urlAddUserString;
    }

    public String getUrlImageString() {
        return urlImageString;
    }

    public String getHostString() {
        return hostString;
    }

    public int getPortAnInt() {
        return portAnInt;
    }

    public String getUserFTPString() {
        return userFTPString;
    }

    public String getPassFTPwordString() {
        return passFTPwordString;
    }
}   // Main Class
