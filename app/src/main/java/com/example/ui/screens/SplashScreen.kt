package com.example.ui.screens

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun SplashScreen(
    onSplashFinished: () -> Unit,
    modifier: Modifier = Modifier
) {
    val cs = MaterialTheme.colorScheme
    val logoScale = remember { Animatable(0.72f) }
    val contentAlpha = remember { Animatable(0f) }

    val infiniteTransition = rememberInfiniteTransition(label = "splashGlow")
    val glowPulse by infiniteTransition.animateFloat(
        initialValue = 0.85f,
        targetValue = 1.18f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glowPulse"
    )

    LaunchedEffect(Unit) {
        launch {
            logoScale.animateTo(
                targetValue = 1f,
                animationSpec = spring(dampingRatio = 0.62f, stiffness = 260f)
            )
        }
        launch {
            contentAlpha.animateTo(
                targetValue = 1f,
                animationSpec = tween(durationMillis = 550)
            )
        }
        delay(1750L)
        onSplashFinished()
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(cs.background)
            .drawBehind {
                val center = Offset(size.width / 2f, size.height * 0.43f)
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            cs.primary.copy(alpha = 0.28f),
                            cs.primaryContainer.copy(alpha = 0.12f),
                            Color.Transparent
                        ),
                        center = center,
                        radius = size.minDimension * 0.58f * glowPulse
                    ),
                    center = center,
                    radius = size.minDimension * 0.58f * glowPulse
                )
            }
            .testTag("splash_screen"),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .padding(32.dp)
                .alpha(contentAlpha.value)
        ) {
            Box(
                modifier = Modifier
                    .scale(logoScale.value)
                    .size(116.dp)
                    .clip(RoundedCornerShape(30.dp))
                    .border(
                        width = 2.dp,
                        brush = Brush.linearGradient(
                            colors = listOf(cs.primary, cs.secondary, cs.onSecondaryContainer)
                        ),
                        shape = RoundedCornerShape(30.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.img_sn_launcher_logo),
                    contentDescription = stringResource(R.string.splash_logo_desc),
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }

            Spacer(modifier = Modifier.height(26.dp))

            Text(
                text = stringResource(R.string.app_name),
                style = MaterialTheme.typography.displaySmall.copy(fontWeight = FontWeight.Bold),
                color = cs.onBackground
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = stringResource(R.string.splash_tagline),
                style = MaterialTheme.typography.bodyMedium,
                color = cs.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(34.dp))

            LinearProgressIndicator(
                modifier = Modifier
                    .width(136.dp)
                    .height(4.dp)
                    .clip(RoundedCornerShape(50)),
                color = cs.primary,
                trackColor = cs.secondaryContainer,
                strokeCap = StrokeCap.Round
            )
        }
    }
}
