package com.example.schoolmanagement.UI.Component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.schoolmanagement.UI.Theme.getPoppinsFontFamily

@Composable
fun ScheduleItem(time: String, subject: String, teacher: String) {
    val poppins = getPoppinsFontFamily()

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFF8FAFC), RoundedCornerShape(12.dp))
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.width(80.dp)) {
            Text(time.split("-")[0],fontFamily = poppins, fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
            Text(time.split("-")[1],fontFamily = poppins, color = Color.Gray, fontSize = 12.sp)
        }

        Box(modifier = Modifier.width(1.dp).height(40.dp).background(Color.LightGray))

        Spacer(modifier = Modifier.width(16.dp))

        Column {
            Text(text = subject, fontWeight = FontWeight.SemiBold, fontFamily = poppins, fontSize = 15.sp)
            Row(verticalAlignment = Alignment.CenterVertically) {
                Spacer(modifier = Modifier.width(8.dp))
                Icon(Icons.Default.Person,  null, Modifier.size(12.dp), Color.Gray)
                Text(text = " $teacher", fontFamily = poppins, fontSize = 12.sp, color = Color.Gray)
            }
        }
    }
}