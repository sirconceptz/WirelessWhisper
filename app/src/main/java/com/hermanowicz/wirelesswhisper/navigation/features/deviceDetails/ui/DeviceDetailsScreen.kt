package com.hermanowicz.wirelesswhisper.navigation.features.deviceDetails.ui

import android.content.Context
import android.content.Intent
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.hermanowicz.wirelesswhisper.R
import com.hermanowicz.wirelesswhisper.bluetooth.MyBluetoothService
import com.hermanowicz.wirelesswhisper.components.button.ButtonPrimary
import com.hermanowicz.wirelesswhisper.components.card.CardPrimary
import com.hermanowicz.wirelesswhisper.components.dialog.DialogPermissions
import com.hermanowicz.wirelesswhisper.components.dialog.DialogPrimary
import com.hermanowicz.wirelesswhisper.components.divider.DividerCardInside
import com.hermanowicz.wirelesswhisper.components.permissions.permissionChecker
import com.hermanowicz.wirelesswhisper.components.spacer.SpacerLarge
import com.hermanowicz.wirelesswhisper.components.textfield.TextFieldAndLabel
import com.hermanowicz.wirelesswhisper.components.topBarScoffold.TopBarScaffoldLazyColumn
import com.hermanowicz.wirelesswhisper.data.model.Device
import com.hermanowicz.wirelesswhisper.domain.GoToPermissionSettingsUseCase
import com.hermanowicz.wirelesswhisper.navigation.features.deviceDetails.state.DeviceDetailsUiState
import com.hermanowicz.wirelesswhisper.ui.theme.LocalSpacing
import com.hermanowicz.wirelesswhisper.utils.Permissions

@Composable
fun DeviceDetailsScreen(
    bottomBar: @Composable () -> Unit,
    bluetoothService: Intent,
    navBack: () -> Unit,
    viewModel: DeviceDetailsViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val uiState by viewModel.state.collectAsState()

    val launcherPermissionsConnectDevice = permissionChecker(
        onGranted = { connectDevice(uiState.device, bluetoothService, context) },
        showDialogPermissionNeeded = { viewModel.onGoToPermissionSettings(true) }
    )

    val launcherPermissionsDisconnectDevice = permissionChecker(
        onGranted = { disconnectDevice(uiState.device, bluetoothService, context) },
        showDialogPermissionNeeded = { viewModel.onGoToPermissionSettings(true) }
    )

    LaunchedEffect(key1 = uiState.goToPermissionSettings) {
        if (uiState.goToPermissionSettings) {
            GoToPermissionSettingsUseCase.invoke(context)
            viewModel.onGoToPermissionSettings(false)
        }
    }

    LaunchedEffect(key1 = uiState.navBack) {
        if (uiState.navBack) {
            navBack()
        }
    }

    Dialogs(
        uiState,
        viewModel,
        launcherPermissionsConnectDevice,
        launcherPermissionsDisconnectDevice
    )

    TopBarScaffoldLazyColumn(
        topBarText = stringResource(id = R.string.device_details),
        bottomBar = bottomBar
    ) {
        item {
            DeviceDetailsCard(
                uiState = uiState,
                onChangeDeviceName = { viewModel.onChangeDeviceName(it) },
                onConfirmChangeName = { viewModel.onConfirmChangeDeviceName() }
            )
        }
        item {
            Buttons(
                viewModel,
                context,
                bluetoothService
            )
        }
    }
}

@Composable
private fun Dialogs(
    uiState: DeviceDetailsUiState,
    viewModel: DeviceDetailsViewModel,
    launcherPermissionsConnectDevice: ManagedActivityResultLauncher<Array<String>, Map<String, @JvmSuppressWildcards Boolean>>,
    launcherPermissionsDisconnectDevice: ManagedActivityResultLauncher<Array<String>, Map<String, @JvmSuppressWildcards Boolean>>
) {
    if (uiState.showDialogPermissionsConnectDevice) {
        DialogPermissions(
            onPositiveLabel = stringResource(id = android.R.string.ok),
            onPositiveRequest = {
                viewModel.showDialogPermissionsConnectDevice(false)
                launcherPermissionsConnectDevice.launch(Permissions.btPermissions)
            },
            onDismissRequest = { viewModel.showDialogPermissionsConnectDevice(false) }
        )
    }

    if (uiState.showDialogPermissionsDisconnectDevice) {
        DialogPermissions(
            onPositiveLabel = stringResource(id = android.R.string.ok),
            onPositiveRequest = {
                viewModel.showDialogPermissionsDisconnectDevice(false)
                launcherPermissionsDisconnectDevice.launch(Permissions.btPermissions)
            },
            onDismissRequest = { viewModel.showDialogPermissionsDisconnectDevice(false) }
        )
    }

    if (uiState.showDeleteDeviceDialog) {
        DialogPrimary(
            onPositiveLabel = stringResource(id = android.R.string.ok),
            onPositiveRequest = {
                viewModel.deleteDeviceConfirmed()
                viewModel.showDeleteDeviceDialog(false)
            },
            onDismissRequest = { viewModel.showDeleteDeviceDialog(false) }
        ) {
            Text(text = stringResource(id = R.string.are_you_sure_you_want_to_delete_this_device))
        }
    }
}

@Composable
private fun DeviceDetailsCard(
    uiState: DeviceDetailsUiState,
    onChangeDeviceName: (String) -> Unit,
    onConfirmChangeName: () -> Unit
) {
    CardPrimary {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            TextFieldAndLabel(
                labelText = stringResource(id = R.string.name),
                placeholder = stringResource(id = R.string.name),
                textfieldText = uiState.deviceName,
                textEvent = onChangeDeviceName,
                keyboardActions = KeyboardActions(
                    onDone = {
                        onConfirmChangeName()
                    }
                )
            )
        }
        DividerCardInside(
            modifier = Modifier.padding(vertical = LocalSpacing.current.small),
            color = Color.White
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = stringResource(id = R.string.mac_address),
                color = Color.White
            )
            Text(
                text = uiState.device?.macAddress ?: "",
                color = Color.White
            )
        }
    }
}

@Composable
private fun Buttons(
    viewModel: DeviceDetailsViewModel,
    context: Context,
    bluetoothService: Intent
) {
    val uiState by viewModel.state.collectAsState()

    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            ButtonPrimary(
                modifier = Modifier.weight(1f),
                text = stringResource(id = R.string.connect)
            ) {
                if (viewModel.isNeededPermissionsGranted())
                    connectDevice(uiState.device, bluetoothService, context)
                else
                    viewModel.showDialogPermissionsConnectDevice(true)
            }
            SpacerLarge()
            ButtonPrimary(
                modifier = Modifier.weight(1f),
                text = stringResource(id = R.string.disconnect)
            ) {
                if (viewModel.isNeededPermissionsGranted())
                    disconnectDevice(uiState.device, bluetoothService, context)
                else
                    viewModel.showDialogPermissionsDisconnectDevice(true)
            }
        }
        ButtonPrimary(
            text = stringResource(id = R.string.delete_device)
        ) {
            viewModel.showDeleteDeviceDialog(true)
        }
    }
}

fun connectDevice(device: Device?, bluetoothService: Intent, context: Context) {
    if (device != null) {
        bluetoothService.putExtra(
            MyBluetoothService.ACTION_CONNECT,
            device.macAddress
        )
        bluetoothService.action = MyBluetoothService.ACTION_CONNECT
        context.startForegroundService(bluetoothService)
    }
}

fun disconnectDevice(device: Device?, bluetoothService: Intent, context: Context) {
    if (device != null) {
        bluetoothService.action = MyBluetoothService.ACTION_DISCONNECT
        bluetoothService.putExtra(MyBluetoothService.ACTION_DISCONNECT, device.macAddress)
        context.startForegroundService(bluetoothService)
    }
}
