package com.example.android_assignment.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SideMenu(
    isOpen: Boolean,
    onClose: () -> Unit,
    modifier: Modifier = Modifier
) {
    val alpha by animateFloatAsState(
        targetValue = if (isOpen) 0.4f else 0f,
        animationSpec = spring(dampingRatio = 0.8f),
        label = "overlay_alpha"
    )

    val scale by animateFloatAsState(
        targetValue = if (isOpen) 1f else 0.97f,
        animationSpec = spring(dampingRatio = 0.8f),
        label = "menu_scale"
    )

    Box(modifier = modifier.fillMaxSize()) {

        // Dim background overlay
        if (isOpen) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = alpha))
                    .clickable { onClose() }
            )
        }

        // The menu panel
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .width(280.dp)
                .offset(x = if (isOpen) 0.dp else (-300).dp)
                .scale(scale)
                .background(Color(0xFF1C1A2E)) // your chat bubble dark color
                .padding(24.dp)
        ) {

            Column(modifier = Modifier.fillMaxSize()) {

                Spacer(modifier = Modifier.height(32.dp))

                // Profile section
                Row(verticalAlignment = Alignment.CenterVertically) {

                    Box(
                        modifier = Modifier
                            .size(60.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF6B39F4)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Profile",
                            tint = Color.White,
                            modifier = Modifier.size(30.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(14.dp))

                    Column {
                        Text(
                            "User",
                            color = Color(0xFFE4E2F6),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium
                        )
                        Text(
                            "user@example.com",
                            color = Color(0xFF9C9AB0),
                            fontSize = 13.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(28.dp))
                Divider(color = Color(0xFF2D2A3E))
                Spacer(modifier = Modifier.height(20.dp))

                MenuItem("Settings")
                MenuItem("About")
                MenuItem("Help")
                MenuItem("Logout")
            }
        }
    }
}
@Composable
fun MenuItem(
    text: String,
    onClick: () -> Unit = {}
) {
    Text(
        text = text,
        color = Color(0xFFE4E2F6),
        fontSize = 16.sp,
        fontWeight = FontWeight.Medium,
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 14.dp)
    )
}
