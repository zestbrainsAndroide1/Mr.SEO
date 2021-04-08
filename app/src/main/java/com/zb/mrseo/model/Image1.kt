package com.zb.mrseo.model



import java.io.Serializable

class Image1(var imageId: String, var imageName: String) : Serializable {

    override fun toString(): String {
        return "Image{" +
                "imageId='" + imageId + '\'' +
                ", imageName='" + imageName + '\''
        '}'
    }

}