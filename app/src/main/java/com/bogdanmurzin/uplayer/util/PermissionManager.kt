package com.bogdanmurzin.uplayer.util

import android.content.Context
import android.content.pm.PackageManager
import androidx.annotation.StringRes
import androidx.fragment.app.FragmentActivity
import com.bogdanmurzin.uplayer.R
import com.bogdanmurzin.uplayer.util.permission.BottomSheetMenu
import com.bogdanmurzin.uplayer.util.permission.PermissionUtil
import java.lang.ref.WeakReference
import javax.inject.Inject

class PermissionManager @Inject constructor() {

    @StringRes
    private var descRes: Int = R.string.pmx_default_description

    private lateinit var bottomSheetMenu: BottomSheetMenu
    private var permissions: ArrayList<String> = arrayListOf()
    private var callback: PermissionCallback? = null

    companion object {
        private const val REQUEST_CODE = 300

        private var contextWeakRef: WeakReference<FragmentActivity>? = null
        private var permissionManagerWeakRef: WeakReference<PermissionManager>? = null


        fun with(context: FragmentActivity): PermissionManager {
            contextWeakRef = WeakReference(context)
            permissionManagerWeakRef = WeakReference(PermissionManager())

            return getPermissionManager()
        }

        private fun getPermissionManager(): PermissionManager {
            return permissionManagerWeakRef?.get()!!
        }

        private fun getActivity(): FragmentActivity {
            return contextWeakRef?.get()!!
        }

    }

    fun request(permission: String): PermissionManager {
        permissions.add(permission)
        return getPermissionManager()
    }

    fun request(permissionArr: Array<String>): PermissionManager {
        permissions.addAll(permissionArr)
        return getPermissionManager()
    }

    fun setDescription(@StringRes descRes: Int): PermissionManager {
        this.descRes = descRes
        return getPermissionManager()
    }

    fun ask() {

        bottomSheetMenu =
            BottomSheetMenu(getActivity() as Context) {
                handlePermission()
                bottomSheetMenu.hide()
            }

        bottomSheetMenu.setDescription(descRes)

        if (!PermissionUtil.shouldAskForPermission(getActivity(), permissions.toTypedArray())) {
            return
        }

        bottomSheetMenu.show()
    }

    fun setCallbacks(callback: PermissionCallback): PermissionManager {
        this.callback = callback
        return getPermissionManager()
    }

    fun onRequestPermissionResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: Array<Int>
    ) {
        if (requestCode != REQUEST_CODE) {
            return
        }

        permissions.forEachIndexed { index, perm ->
            if (grantResults[index] == PackageManager.PERMISSION_GRANTED) callback?.onPermissionAccepted(
                perm
            ) else callback?.onPermissionRejected(perm)
        }
    }

    private fun handlePermission() {
        PermissionUtil.requestPermission(getActivity(), permissions.toTypedArray(), REQUEST_CODE)
    }

    interface PermissionCallback {
        fun onPermissionAccepted(permission: String)
        fun onPermissionRejected(permission: String)
    }


}