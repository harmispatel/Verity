package com.certified.verityscanningOne.beans;

import java.io.Serializable;

/**
 * Created by Nakul Sheth on 01-08-2016.
 */
public class Responcebean implements Serializable {

    public String getJsonResponceContent() {
        return jsonResponceContent;
    }

    public void setJsonResponceContent(String jsonResponceContent) {
        this.jsonResponceContent = jsonResponceContent;
    }

    public boolean isServiceSuccess() {
        return isServiceSuccess;
    }

    public void setIsServiceSuccess(boolean isServiceSuccess) {
        this.isServiceSuccess = isServiceSuccess;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    String errorMessage;
    boolean isServiceSuccess=false;
    String jsonResponceContent=null;


    public boolean isExceptionError() {
        return exceptionError;
    }

    public void setExceptionError(boolean exceptionError) {
        this.exceptionError = exceptionError;
    }

    boolean exceptionError=false;

    public int getIsProductExist() {
        return isProductExist;
    }

    public void setIsProductExist(int isProductExist) {
        this.isProductExist = isProductExist;
    }

    int isProductExist=-1;





}
