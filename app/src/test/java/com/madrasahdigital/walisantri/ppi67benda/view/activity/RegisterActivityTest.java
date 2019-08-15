package com.madrasahdigital.walisantri.ppi67benda.view.activity;

import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by Alhudaghifari on 14:03 13/08/19
 */
public class RegisterActivityTest {

    @Test
    public void registerData() {
        String json = "{\"status\":\"failed\",\"message\":\"Bidang Password harus setidaknya 6 panjang karakter.\\n\"}";

        try {
            JSONObject jsonObject = new JSONObject(json);
            String message = jsonObject.getString("message");
            Assert.assertNotNull(message);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}