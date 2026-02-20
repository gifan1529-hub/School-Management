package com.example.schoolmanagement.DI

import android.annotation.SuppressLint
import androidx.compose.animation.core.copy
import androidx.compose.foundation.gestures.snapping.SnapPosition.Center.position
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.Circle
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.overlay.Polygon

@SuppressLint("ClickableViewAccessibility")
@Composable
actual fun MapView (
    modifier: Modifier,
    latitude: Double,
    longitude: Double,
    title: String,
    radius: Double
) {
    val context = LocalContext.current

    remember {
        Configuration.getInstance().load(
            context,
            context.getSharedPreferences("osmdroid", 0)
        )
        Configuration.getInstance().userAgentValue = context.packageName
        true
    }

    AndroidView(
        factory = { ctx ->
            org.osmdroid.views.MapView(ctx).apply {
                // Set sumber peta
                setTileSource(TileSourceFactory.MAPNIK)

                // Kontrol Zoom
                setMultiTouchControls(true)
                controller.setZoom(16.0)

                // biar ga bentrok sama scroll
                setOnTouchListener { view, event ->
                    when (event.action) {
                        android.view.MotionEvent.ACTION_DOWN -> {
                            view.parent.requestDisallowInterceptTouchEvent(true)
                        }
                        android.view.MotionEvent.ACTION_UP -> {
                            view.parent.requestDisallowInterceptTouchEvent(false)
                        }
                    }
                    false
                }

                // Lokasi Anak
                val childPoint = GeoPoint(latitude, longitude)
                controller.setCenter(childPoint)

                // Marker Anak
                val childMarker = org.osmdroid.views.overlay.Marker(this)
                childMarker.position = childPoint
                childMarker.setAnchor(org.osmdroid.views.overlay.Marker.ANCHOR_CENTER, org.osmdroid.views.overlay.Marker.ANCHOR_BOTTOM)
                childMarker.title = title
                overlays.add(childMarker)

                val schoolPoint = GeoPoint(-6.355486047031499, 106.99211768069286)
                val schoolMarker = org.osmdroid.views.overlay.Marker(this)
                schoolMarker.position = schoolPoint
                schoolMarker.title = "Sekolah"
                overlays.add(schoolMarker)

                val circle = Polygon(this)
                circle.points = Polygon.pointsAsCircle(schoolPoint, 300.0) // 300 meter
                circle.fillPaint.color = android.graphics.Color.parseColor("#1A0066FF")
                circle.outlinePaint.color = android.graphics.Color.parseColor("#0066FF")
                circle.outlinePaint.strokeWidth = 2f
                overlays.add(circle)
            }
        },
        modifier = modifier,
        update = { view ->
            val newPoint = GeoPoint(latitude, longitude)
            view.controller.animateTo(newPoint)
        }
    )
}