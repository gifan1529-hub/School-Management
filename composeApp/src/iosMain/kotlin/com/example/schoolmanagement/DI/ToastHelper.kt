package com.example.schoolmanagement.DI

import platform.UIKit.UIAlertController
import platform.UIKit.UIAlertControllerStyleAlert
import platform.UIKit.UIApplication
import platform.darwin.DISPATCH_TIME_NOW
import platform.darwin.NSEC_PER_SEC
import platform.darwin.dispatch_after
import platform.darwin.dispatch_get_main_queue
import platform.darwin.dispatch_time

actual class ToastHelper {
    actual fun Toast (message: String) {
        val alert = UIAlertController.alertControllerWithTitle(
            title = null,
            message = message,
            preferredStyle = UIAlertControllerStyleAlert
        )

        val rootViewController = UIApplication.sharedApplication.keyWindow?.rootViewController
        rootViewController?.presentViewController(alert, animated = true, completion = null)

        val delay = 2.0 * NSEC_PER_SEC.toDouble()
        val time = dispatch_time(DISPATCH_TIME_NOW, delay.toLong())

        dispatch_after(time, dispatch_get_main_queue()) {
            alert.dismissViewControllerAnimated(true, completion = null)
        }
    }
}