package com.example.arsen.sololearntask.model


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey


open class NewsResult : RealmObject() {
        @PrimaryKey
        @Expose
        @SerializedName("id")
        var id = String()
        @Expose
        @SerializedName("sectionName")
        var category = String()
        @Expose
        @SerializedName("fields")
        var fields : NewsFields? = null
        var isPinned :Boolean = false
}
open class NewsFields : RealmObject() {
        @Expose
        @SerializedName("headline")
        var title =  String()
        @Expose
        @SerializedName("thumbnail")
        var image = String()

}