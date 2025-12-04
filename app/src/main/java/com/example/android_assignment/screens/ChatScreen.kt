package com.example.android_assignment.screens

import android.graphics.drawable.shapes.Shape
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.android_assignment.R
import com.example.android_assignment.navigation.Screen
import com.example.android_assignment.ui.components.SideMenu
import com.example.android_assignment.viewmodel.ChatViewModel
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.GenericShape
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.LayoutDirection


data class Message(
    val id: String,
    val text: String,
    val isUser: Boolean,
    val timestamp: Long = System.currentTimeMillis()
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(
    navController: NavController,
    viewModel: ChatViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    val messages by viewModel.messages.collectAsState()
    val isMenuOpen by viewModel.isMenuOpen.collectAsState()

    val menuOffset by animateFloatAsState(
        targetValue = if (isMenuOpen) 1f else 0f,
        animationSpec = spring(dampingRatio = 0.85f, stiffness = 300f),
        label = "menu_animation"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF000000))
    ) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.35f)
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFF6B4BCC),
                            Color(0xFF5928A3),
                            Color(0xFF3D2873),
                            Color(0xFF1A1133),
                            Color(0xFF000000).copy(alpha = 0.8f),
                            Color(0xFF000000).copy(alpha = 0.3f),
                            Color(0xFF000000).copy(alpha = 0f)
                        ),
                        startY = -200f,
                        endY = 1400f
                    )
                )
        )

        SideMenu(
            isOpen = isMenuOpen,
            onClose = { viewModel.closeMenu() },
            modifier = Modifier.fillMaxSize()
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .offset(x = (menuOffset * 280f).dp)
        ) {

            TopAppBar(
                title = {},
                navigationIcon = {
                    IconButton(onClick = { viewModel.toggleMenu() }) {
                        Box(
                            modifier = Modifier
                                .size(34.dp)
                                .clip(CircleShape)
                                .background(Color(0xFF1C1A2E)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                Icons.Default.Menu,
                                contentDescription = "Menu",
                                tint = Color.White,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                    }
                },
                actions = {
                    IconButton(onClick = { navController.navigate(Screen.Notifications.route) }) {
                        Icon(
                            Icons.Default.Notifications,
                            contentDescription = "Notifications",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent
                )
            )
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 18.dp, vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                state = rememberLazyListState()
            ) {
                item {
                    Spacer(modifier = Modifier.height(20.dp))
                }


                itemsIndexed(messages) { index, msg ->
                    when (index) {
                        0 -> FirstMessageWithLogo(msg)
                        2 -> MessageBubbleWithButton(msg)
                        else -> MessageBubble(msg)
                    }
                }
            }
        }
        BottomInputBar(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .windowInsetsPadding(WindowInsets.navigationBars)
                .padding(bottom = 8.dp)
        )

    }
}
@Composable
fun BottomInputBar(
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 10.dp)
            .background(Color.Transparent),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {

        IconButton(onClick = {}, modifier = Modifier.size(44.dp)) {
            Icon(
                Icons.Default.Add,
                contentDescription = "Attach",
                tint = Color(0xFF9B8FCF),
                modifier = Modifier.size(22.dp)
            )
        }

        Box(
            modifier = Modifier
                .weight(1f)
                .height(46.dp)
                .clip(RoundedCornerShape(23.dp))
                .background(Color(0xFF221D35)),
            contentAlignment = Alignment.CenterStart
        ) {
            Text(
                text = "What would you like to create?",
                color = Color(0xFF6B657F),
                fontSize = 9.sp,
                modifier = Modifier.padding(horizontal = 18.dp)
            )
        }

        IconButton(onClick = {}, modifier = Modifier.size(44.dp)) {
            Icon(
                Icons.Default.MoreVert,
                contentDescription = "More",
                tint = Color(0xFF9B8FCF),
                modifier = Modifier.size(22.dp)
            )
        }

        Box(
            modifier = Modifier
                .size(44.dp)
                .clip(CircleShape)
                .background(
                    Brush.linearGradient(
                        listOf(Color(0xFF9B5CF6), Color(0xFF8B4CF4))
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                Icons.AutoMirrored.Filled.Send,
                contentDescription = "Send",
                tint = Color.White,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}


@Composable
fun FirstMessageWithLogo(message: Message) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(LeftTailBubbleShape())
            .padding(horizontal = 12.dp, vertical = 10.dp)
    ) {

        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "Logo",
            modifier = Modifier
                .size(60.dp)
                .padding(top = 4.dp, end = 6.dp)
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .background(Color(0xFF181A1C))
                .padding(horizontal = 9.dp, vertical = 9.dp)
        ) {
            Text(
                text = message.text,
                color = Color.White,
                fontSize = 14.sp,
                fontWeight = FontWeight.Normal,
                lineHeight = 22.sp
            )
        }
    }
}


@Composable
fun MessageBubble(message: Message) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(LeftTailBubbleShape())
            .padding(
                start = if (!message.isUser) 70.dp else 0.dp,
                bottom = 4.dp
            ),
        horizontalArrangement = if (message.isUser) Arrangement.End else Arrangement.Start
    )    {

        val bubbleColor = if (message.isUser)
            Color(0xFF6C38FF)
        else
            Color(0XFF181A1C)

        val textColor = Color(0xFFFFFFFF)

        Box(
            modifier = Modifier
                .fillMaxWidth(0.90f)
                .clip(RoundedCornerShape(20.dp))
                .background(bubbleColor)
                .padding(horizontal = 9.dp, vertical = 8.dp)
        ) {
            Text(
                text = message.text,
                color = textColor,
                fontSize = 14.sp,
                lineHeight = 22.sp,
                fontWeight = FontWeight.Normal
            )
        }
    }
}

@Composable
fun MessageBubbleWithButton(message: Message) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(LeftTailBubbleShape())
            .padding(
            start = if (!message.isUser) 70.dp else 0.dp,
    bottom = 4.dp
    ),
        horizontalArrangement = Arrangement.Start
    ) {

        Box(
            modifier = Modifier
                .fillMaxWidth(0.90f)
                .clip(RoundedCornerShape(20.dp))
                .background(Color(0xFF181A1C))
                .padding(horizontal = 9.dp, vertical = 8.dp)
        ) {

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {

                // message text
                Text(
                    text = message.text,
                    color = Color.White,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal,
                    lineHeight = 22.sp
                )

                Button(
                    onClick = {  },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF9C3AFF)
                    ),
                    shape = RoundedCornerShape(40),
                    modifier = Modifier
                        .height(38.dp)
                        .align(Alignment.Start)
                ) {
                    Text(
                        text = "Try Now",
                        color = Color.White,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}

@Composable
fun LeftTailBubbleShape(): GenericShape {
    return GenericShape { size: Size, layoutDirection: LayoutDirection ->

        val tailSize = 18f
        val cornerRadius = 40f

        moveTo(tailSize, 0f)
        lineTo(size.width - cornerRadius, 0f)

        // top-right corner
        quadraticTo(
            size.width, 0f,
            size.width, cornerRadius
        )

        lineTo(size.width, size.height - cornerRadius)

        quadraticTo(
            size.width, size.height,
            size.width - cornerRadius, size.height
        )

        lineTo(cornerRadius, size.height)

        quadraticTo(
            0f, size.height,
            0f, size.height - cornerRadius
        )

        lineTo(0f, cornerRadius)

        quadraticTo(
            0f, 0f,
            tailSize, 0f
        )
        close()
    }
}