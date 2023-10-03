package com.hermanowicz.wirelesswhisper.components.permissions

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable

@Composable
fun permissionChecker(
    onGranted: () -> Unit,
    showDialogPermissionNeeded: () -> Unit
) =
    rememberLauncherForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { requestedPermissions ->
        var isGranted = true
        for (permission in requestedPermissions) {
            if (!permission.value) {
                isGranted = false
            }
        }
        if (isGranted) {
            onGranted()
        } else {
            showDialogPermissionNeeded()
        }
    }
