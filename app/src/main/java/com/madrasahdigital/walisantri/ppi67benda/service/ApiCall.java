package com.madrasahdigital.walisantri.ppi67benda.service;

import com.madrasahdigital.walisantri.ppi67benda.model.loginmodel.BodyLoginModel;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by Alhudaghifari on 13:37 12/05/19
 */
public interface ApiCall {
    @POST("api/user/auth/login")
    Call<JSONObject> callLogin(@Body BodyLoginModel bodyLoginModel);
}
