package com.hermanowicz.wirelesswhisper.domain

import android.content.Context
import com.hermanowicz.wirelesswhisper.R
import com.hermanowicz.wirelesswhisper.data.model.Device
import com.hermanowicz.wirelesswhisper.data.model.Message
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class GetAllChatsUseCase @Inject constructor(
    @ApplicationContext private val context: Context,
    private val getDeviceAddressUseCase: GetDeviceAddressUseCase
) : (List<Message>, List<Device>) -> List<Pair<String, String>> {
    override fun invoke(
        allMessages: List<Message>,
        allDevices: List<Device>
    ): List<Pair<String, String>> {
        val currentDeviceAddress = getDeviceAddressUseCase()
        val allChats: MutableList<Pair<String, String>> = mutableListOf()
        allMessages.forEach {
            val address =
                if (it.senderAddress == currentDeviceAddress) it.receiverAddress else it.senderAddress
            var device: Device? = null
            allDevices.forEach { mDevice ->
                if (mDevice.macAddress == address) {
                    device = mDevice
                }
            }
            if (device != null) {
                if (!allChats.contains(Pair(device!!.name, device!!.macAddress))) {
                    allChats.add((Pair(device!!.name, device!!.macAddress)))
                }
            } else {
                if (!allChats.contains(Pair(context.getString(R.string.unnamed), address))) {
                    allChats.add((Pair(context.getString(R.string.unnamed), address)))
                }
            }
        }
        return allChats.toList()
    }
}
