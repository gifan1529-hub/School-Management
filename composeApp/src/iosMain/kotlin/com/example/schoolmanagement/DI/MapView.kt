package com.example.schoolmanagement.DI


import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.interop.UIKitView
import kotlinx.cinterop.ExperimentalForeignApi
import platform.MapKit.MKMapView
import platform.MapKit.MKPointAnnotation
import platform.MapKit.MKCoordinateRegionMakeWithDistance
import platform.CoreLocation.CLLocationCoordinate2DMake


@OptIn(ExperimentalForeignApi::class)
@Composable
actual fun MapView(
    modifier: Modifier,
    latitude: Double,
    longitude: Double,
    title: String,
    radius: Double
) {
    val coordinate = remember(latitude, longitude) {
        CLLocationCoordinate2DMake(latitude, longitude)
    }

    UIKitView(
        factory = {
            MKMapView().apply {
                val annotation = MKPointAnnotation().apply {
                    setCoordinate(coordinate)
                    setTitle(title)
                }
                addAnnotation(annotation)

                val region = MKCoordinateRegionMakeWithDistance(coordinate, 1000.0, 1000.0)
                setRegion(region, animated = true)
            }
        },
        modifier = modifier,
        update = { view ->
            val newCoordinate = CLLocationCoordinate2DMake(latitude, longitude)
            val region = MKCoordinateRegionMakeWithDistance(newCoordinate, 1000.0, 1000.0)
            view.setRegion(region, animated = true)
        }
    )
}