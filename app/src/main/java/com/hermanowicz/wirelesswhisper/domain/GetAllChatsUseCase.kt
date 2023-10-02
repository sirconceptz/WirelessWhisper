package com.hermanowicz.wirelesswhisper.domain

import android.content.Context
import com.hermanowicz.wirelesswhisper.R
import com.hermanowicz.wirelesswhisper.data.model.Chat
import com.hermanowicz.wirelesswhisper.data.model.Device
import com.hermanowicz.wirelesswhisper.data.model.Message
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class GetAllChatsUseCase @Inject constructor(
    @ApplicationContext private val context: Context,
    private val getDeviceAddressUseCase: GetDeviceAddressUseCase
) : (List<Message>, List<Device>) -> List<Chat> {
    override fun invoke(
        allMessages: List<Message>,
        allDevices: List<Device>
    ): List<Chat> {
        val currentDeviceAddress = getDeviceAddressUseCase()
        val allChats: MutableList<Chat> = mutableListOf()
        val addressList: MutableList<String> = mutableListOf()
        allMessages.forEach { message ->
            val address =
                if (message.senderAddress == currentDeviceAddress) {
                    message.receiverAddress
                } else {
                    message.senderAddress
                }
            var device: Device? = null
            allDevices.forEach { mDevice ->
                if (mDevice.macAddress == address) {
                    device = mDevice
                }
            }
            if (device != null) {
                if (!addressList.contains(address)) {
                    allChats.add(
                        Chat(
                            deviceName = device!!.name,
                            macAddress = address,
                            unreadMessages = if (message.readOut) 0 else 1,
                            deviceConnectionStatus = device!!.connected
                        )
                    )
                    addressList.add(address)
                } else {
                    if (!message.readOut) {
                        val oldChat = getChatForAddress(address, allChats)
                        val newChat = oldChat!!.copy(unreadMessages = oldChat.unreadMessages + 1)
                        allChats.remove(oldChat)
                        allChats.add(newChat)
                    }
                }
            } else {
                if (!addressList.contains(address) && currentDeviceAddress?.isNotEmpty() == true) {
                    allChats.add(
                        Chat(
                            deviceName = context.getString(R.string.unnamed),
                            macAddress = address,
                            unreadMessages = if (message.readOut) 0 else 1,
                            deviceConnectionStatus = false
                        )
                    )
                    addressList.add(address)
                } else {
                    if (!message.readOut) {
                        val oldChat = getChatForAddress(address, allChats)
                        val newChat = oldChat!!.copy(unreadMessages = oldChat.unreadMessages + 1)
                        allChats.remove(oldChat)
                        allChats.add(newChat)
                    }
                }
            }
        }
        return allChats.toList()
    }

    private fun getChatForAddress(address: String, chatList: List<Chat>): Chat? {
        chatList.forEach { chat ->
            if (chat.macAddress == address) {
                return chat
            }
        }
        return null
    }
}
