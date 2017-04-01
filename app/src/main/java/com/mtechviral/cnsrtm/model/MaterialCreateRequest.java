package com.mtechviral.cnsrtm.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by pawankumar on 01/04/17.
 */

public class MaterialCreateRequest {
        @SerializedName("token")
        @Expose
        private String token;
        @SerializedName("mat_id")
        @Expose
        private Integer matId;
        @SerializedName("req_type")
        @Expose
        private Integer reqType;

        public MaterialCreateRequest(String token, Integer matid, Integer reqType){
            this.token = token;
            this.matId = matid;
            this.reqType = reqType;
        }

        public Integer getReqType() {
            return reqType;
        }

        public void setReqType(Integer reqType) {
            this.reqType = reqType;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public Integer getMatId() {
            return matId;
        }

        public void setMatId(Integer matId) {
            this.matId = matId;
        }
    }


