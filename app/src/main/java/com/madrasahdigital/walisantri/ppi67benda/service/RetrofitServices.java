package com.madrasahdigital.walisantri.ppi67benda.service;

/**
 * Created by Alhudaghifari on 13:29 12/05/19
 */
public class RetrofitServices {
    public static ApiCall sendRequest(){
        return RetrofitClientUtils.client().create(ApiCall.class);
    }
}
