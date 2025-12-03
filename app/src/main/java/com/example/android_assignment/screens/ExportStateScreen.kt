package com.example.android_assignment.screens

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.VideoView
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.FileProvider
import androidx.navigation.NavController
import com.example.android_assignment.R
import com.example.android_assignment.ui.theme.SoundversePrimary
import java.io.File
import java.io.FileOutputStream

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExportStateScreen(navController: NavController) {

    val context = LocalContext.current
    var animated by remember { mutableStateOf(false) }

    val previewScale by animateFloatAsState(
        targetValue = if (animated) 1f else 0.95f,
        animationSpec = spring(dampingRatio = 0.7f),
        label = ""
    )

    LaunchedEffect(Unit) { animated = true }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent
                )
            )
        },
        containerColor = Color.Transparent
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        listOf(
                            Color(0xFF101026),
                            Color(0xFF0A0A1A),
                            Color(0xFF060611)
                        )
                    )
                )
                .padding(padding)
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.height(12.dp))

            // Title
            Text(
                text = "Ready to share",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )
            Spacer(modifier = Modifier.height(10.dp))

            // Preview Card with Video
            Box(
                modifier = Modifier
                    .width(210.dp)
                    .height(200.dp)
                    .scale(previewScale)
                    .clip(RoundedCornerShape(22.dp))
                    .background(Color(0xFF15152E)),
                contentAlignment = Alignment.Center
            ) {

                val context = LocalContext.current

                AndroidView(
                    modifier = Modifier.fillMaxSize(),
                    factory = { ctx ->
                        VideoView(ctx).apply {
                            setVideoURI(
                                Uri.parse("android.resource://${context.packageName}/${R.raw.insta_story}")
                            )
                            setOnPreparedListener { mp ->
                                mp.isLooping = true
                                mp.setVolume(0f, 0f)   // mute preview
                                start()
                            }
                        }
                    }
                )
            }

            Spacer(modifier = Modifier.height(28.dp))

            // Instagram Button
            ActionShareButton(
                label = "Share to Instagram Stories",
                icon = R.drawable.ic_instagram,
                onClick = {
                    try {
                        val videoUri = copyRawVideoToCache(context, R.raw.insta_story)

                        val intent = Intent("com.instagram.share.ADD_TO_STORY").apply {
                            setDataAndType(videoUri, "video/*")
                            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                        }

                        context.grantUriPermission(
                            "com.instagram.android",
                            videoUri,
                            Intent.FLAG_GRANT_READ_URI_PERMISSION
                        )

                        context.startActivity(intent)

                    } catch (e: Exception) {
                        val fallback = context.packageManager
                            .getLaunchIntentForPackage("com.instagram.android")
                            ?: Intent(Intent.ACTION_VIEW, Uri.parse("https://www.instagram.com"))
                        context.startActivity(fallback)
                    }
                }

            )

            Spacer(modifier = Modifier.height(12.dp))

            // WhatsApp Button
            ActionShareButton(
                label = "Share to Whatsapp",
                icon = R.drawable.ic_whatsapp,
                onClick = {}
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Bottom Row of Icons
            Row(
                horizontalArrangement = Arrangement.spacedBy(32.dp)
            ) {
                ShareIcon(R.drawable.ic_instagram)
                ShareIcon(R.drawable.ic_whatsapp)
                ShareIcon(R.drawable.ic_facebook)
                ShareIcon(R.drawable.ic_more)
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Done Button
            Spacer(modifier = Modifier.weight(1f))  // Push everything upward

            Button(
                onClick = {
                    navController.navigate("notifications") {
                        popUpTo("notifications") { inclusive = false }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 54.dp)  // adaptive height, supports all phones
                    .padding(bottom = 12.dp)
                    .navigationBarsPadding(),
                shape = RoundedCornerShape(18.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = SoundversePrimary
                )
            ) {
                Text(
                    text = "Done",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }



        }
    }
}

@Composable
fun ActionShareButton(label: String, icon: Int, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp),
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFF784BFF)
        )
    ) {
        Image(
            painter = painterResource(id = icon),
            contentDescription = null,
            modifier = Modifier.size(22.dp)
        )
        Spacer(modifier = Modifier.width(10.dp))
        Text(
            text = label,
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
fun ShareIcon(icon: Int) {
    val context = LocalContext.current

    val packageName = when (icon) {
        R.drawable.ic_instagram -> "com.instagram.android"
        R.drawable.ic_facebook -> "com.facebook.katana"
        R.drawable.ic_whatsapp -> "com.google.android.youtube"
        R.drawable.ic_more -> null  // system share
        else -> null
    }

    Box(
        modifier = Modifier
            .size(55.dp)
            .clip(CircleShape)
            .background(Color(0xFF151428))
            .clickable {
                try {
                    if (packageName == null) {
                        // Show Android share sheet for "More"
                        val shareIntent = Intent(Intent.ACTION_SEND).apply {
                            type = "text/plain"
                            putExtra(Intent.EXTRA_TEXT, "Check this out!")
                        }
                        context.startActivity(
                            Intent.createChooser(shareIntent, "Share using")
                        )
                    } else {
                        // App launch intent
                        val intent = context.packageManager.getLaunchIntentForPackage(packageName)
                        if (intent != null) {
                            context.startActivity(intent)
                        } else {
                            // App not installed → open website fallback
                            val url = when (icon) {
                                R.drawable.ic_instagram -> "https://www.instagram.com"
                                R.drawable.ic_facebook -> "https://www.facebook.com"
                                R.drawable.ic_whatsapp -> "https://www.whatsapp.com"
                                else -> "https://google.com"
                            }
                            context.startActivity(
                                Intent(Intent.ACTION_VIEW, Uri.parse(url))
                            )
                        }
                    }
                } catch (e: Exception) {
                    // Safety fallback
                    context.startActivity(
                        Intent(Intent.ACTION_VIEW, Uri.parse("https://google.com"))
                    )
                }
            },
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = icon),
            contentDescription = null,
            modifier = Modifier.size(26.dp)
        )
    }
}

fun copyRawVideoToCache(context: Context, rawId: Int): Uri {
    val inputStream = context.resources.openRawResource(rawId)
    val file = File(context.cacheDir, "insta_story.mp4")

    FileOutputStream(file).use { output ->
        inputStream.copyTo(output)
    }

    return FileProvider.getUriForFile(
        context,
        "${context.packageName}.provider",
        file
    )
}
