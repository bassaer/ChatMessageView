package com.github.bassaer.chatmessageview.model

import android.graphics.Bitmap

class ChatUser(internal var id: Int?, internal var name: String, internal var icon: Bitmap) : IChatUser {

    override fun getId(): String {
        return this.id!!.toString()
    }

    override fun getName(): String? {
        return this.name
    }

    override fun getIcon(): Bitmap? {
        return this.icon
    }

    override fun setIcon(bmp: Bitmap) {
        this.icon = bmp
    }
}
