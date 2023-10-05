package com.hermanowicz.wirelesswhisper.navigation.features.scanDevices.ui

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.hilt.navigation.compose.hiltViewModel
import com.hermanowicz.wirelesswhisper.R
import com.hermanowicz.wirelesswhisper.bluetooth.MyBluetoothService
import com.hermanowicz.wirelesswhisper.components.broadcastReceiver.ScanDevicesBroadcastReceiver
import com.hermanowicz.wirelesswhisper.components.button.ButtonPrimary
import com.hermanowicz.wirelesswhisper.components.card.CardPrimary
import com.hermanowicz.wirelesswhisper.components.dialog.DialogPermissions
import com.hermanowicz.wirelesswhisper.components.dialog.DialogPrimary
import com.hermanowicz.wirelesswhisper.components.divider.DividerCardInside
import com.hermanowicz.wirelesswhisper.components.permissions.permissionChecker
import com.hermanowicz.wirelesswhisper.components.topBarScoffold.TopBarScaffoldLazyColumn
import com.hermanowicz.wirelesswhisper.domain.GoToPermissionSettingsUseCase
import com.hermanowicz.wirelesswhisper.navigation.features.scanDevices.state.ScanDevicesState
import com.hermanowicz.wirelesswhisper.ui.theme.LocalSpacing
import com.hermanowicz.wirelesswhisper.utils.Permissions
import timber.log.Timber

@Composable
fun ScanDevicesScreen(
    bottomBar: @Composable () -> Unit,
    viewModel: ScanDevicesViewModel = hiltViewModel(),
    bluetoothService: Intent
) {
    val context = LocalContext.current
    val state by viewModel.state.collectAsState()

    val launcherPermissionsScanDevices = permissionChecker(
        onGranted = { viewModel.scanDevices() },
        showDialogPermissionNeeded = { viewModel.showDialogPermissionsScanDevices(true) }
    )

    val launcherPermissionsOnDiscoverableMode =
        permissionChecker(
            onGranted = { viewModel.turnOnDiscoverable(true) },
            showDialogPermissionNeeded = { viewModel.showDialogPermissionsOnDiscoverable(true) }
        )

    val launcherPermissionsOnPair =
        permissionChecker(
            onGranted = { connectDevice(bluetoothService, state, context) },
            showDialogPermissionNeeded = { viewModel.showDialogPermissionsOnPair(true) }
        )

    LaunchedEffect(key1 = state.goToPermissionSettings) {
        if (state.goToPermissionSettings) {
            GoToPermissionSettingsUseCase.invoke(context)
            viewModel.onGoToPermissionSettings(false)
        }
    }

    LaunchedEffect(key1 = state.turnOnDiscoverableMode) {
        if (state.turnOnDiscoverableMode) {
            val bluetoothManager =
                context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
            val btAdapter = bluetoothManager.adapter
            try {
                if (btAdapter.isDiscovering) {
                    btAdapter.cancelDiscovery()
                    Timber.d("Bluetooth: Cancel discovery new devices")
                }
                val intent = Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE)
                intent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300)
                context.startActivity(intent)
            } catch (e: SecurityException) {
                Timber.e(context.getString(R.string.error_bluetooth_is_not_active))
                Toast.makeText(context, context.getString(R.string.error_bluetooth_is_not_active), Toast.LENGTH_LONG).show()
            }
        }
    }

    Dialogs(state, viewModel, launcherPermissionsOnPair)

    TopBarScaffoldLazyColumn(
        topBarText = stringResource(id = R.string.scan_devices),
        actions = {
            Button(
                modifier = Modifier.padding(end = LocalSpacing.current.small),
                onClick = { launcherPermissionsScanDevices.launch(Permissions.btPermissions) }
            ) {
                Text(
                    text = stringResource(id = R.string.scan)
                )
            }
        },
        bottomBar = bottomBar
    ) {
        if (state.foundDevices.isEmpty()) {
            item {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = stringResource(id = R.string.no_found_devices),
                    textAlign = TextAlign.Center
                )
            }
        }
        state.foundDevices.forEach { device ->
            item {
                CardPrimary {
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { viewModel.onClickFoundDevice(device) },
                        text = device.name,
                        color = Color.White,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
        item {
            DividerCardInside()
            ButtonPrimary(text = stringResource(id = R.string.turn_on_discoverable)) {
                launcherPermissionsOnDiscoverableMode.launch(Permissions.btPermissions)
            }
        }
    }

    ScanDevicesBroadcastReceiver(onDeviceFound = {
        viewModel.setFoundDevice(it)
    }, onStartDiscovery = { viewModel.onStartScanning() })
}

@Composable
private fun Dialogs(
    state: ScanDevicesState,
    viewModel: ScanDevicesViewModel,
    launcherPermissionsOnPair: ManagedActivityResultLauncher<Array<String>, Map<String, @JvmSuppressWildcards Boolean>>
) {
    if (state.showDialogPermissionsScanDevices) {
        DialogPermissions(
            onPositiveLabel = stringResource(id = android.R.string.ok),
            onPositiveRequest = {
                viewModel.onGoToPermissionSettings(true)
                viewModel.showDialogPermissionsScanDevices(false)
            },
            onDismissRequest = { viewModel.showDialogPermissionsScanDevices(false) }
        )
    }

    if (state.showDialogPermissionsOnDiscoverable) {
        DialogPermissions(
            onPositiveLabel = stringResource(id = android.R.string.ok),
            onPositiveRequest = {
                viewModel.onGoToPermissionSettings(true)
                viewModel.showDialogPermissionsOnDiscoverable(false)
            },
            onDismissRequest = { viewModel.showDialogPermissionsOnDiscoverable(false) }
        )
    }

    if (state.showDialogPermissionsOnPair) {
        DialogPermissions(
            onPositiveLabel = stringResource(id = android.R.string.ok),
            onPositiveRequest = {
                viewModel.onGoToPermissionSettings(true)
                viewModel.showDialogPermissionsOnPair(false)
            },
            onDismissRequest = { viewModel.showDialogPermissionsOnPair(false) }
        )
    }

    if (state.showDialogOnPairDeviceConfirmation) {
        DialogPrimary(
            onPositiveLabel = stringResource(id = android.R.string.ok),
            onPositiveRequest = {
                launcherPermissionsOnPair.launch(Permissions.btPermissions)
                viewModel.showDialogOnPairDeviceConfirmation(false)
            },
            onDismissRequest = { viewModel.showDialogOnPairDeviceConfirmation(false) }
        ) {
            Text(text = stringResource(id = R.string.do_you_want_to_pair_this_device))
        }
    }
}

private fun connectDevice(
    bluetoothService: Intent,
    state: ScanDevicesState,
    context: Context
) {
    bluetoothService.putExtra(
        MyBluetoothService.ACTION_CONNECT,
        state.deviceDuringPairing!!.macAddress
    )
    bluetoothService.action = MyBluetoothService.ACTION_CONNECT
    context.startForegroundService(bluetoothService)
}
