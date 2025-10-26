package com.grupo10.levelupgamer.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.grupo10.levelupgamer.R
import com.grupo10.levelupgamer.ui.theme.*
import kotlinx.coroutines.delay
import kotlin.random.Random

@Composable
fun AnimatedSplashScreen(
    onSplashFinished: () -> Unit
) {
    var startAnimation by remember { mutableStateOf(false) }

    // Animación principal de escala
    val scale by animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "scale"
    )

    // Animación de alpha para el texto
    val alpha by animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0f,
        animationSpec = tween(
            durationMillis = 1000,
            delayMillis = 800
        ),
        label = "alpha"
    )

    // Animación infinita para efectos
    val infiniteTransition = rememberInfiniteTransition(label = "infinite")

    // Rotación de anillos
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(4000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "rotation"
    )

    // Pulso del brillo
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.3f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse"
    )

    // Partículas flotantes
    val particleOffset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(3000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "particles"
    )

    LaunchedEffect(key1 = true) {
        startAnimation = true
        delay(3500)
        onSplashFinished()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        GamerDarkBlue,
                        GamerNavyBlue,
                        GamerDeepBlue
                    )
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        // Partículas de fondo
        AnimatedParticles(offset = particleOffset)

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Contenedor del logo con efectos
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.size(250.dp)
            ) {
                // Anillo exterior rotando
                Canvas(
                    modifier = Modifier
                        .fillMaxSize()
                        .rotate(rotation)
                        .alpha(0.3f)
                ) {
                    val center = Offset(size.width / 2, size.height / 2)
                    val radius = size.minDimension / 2

                    // Dibujar anillo
                    drawCircle(
                        brush = Brush.sweepGradient(
                            colors = listOf(
                                GamerPurple,
                                GamerTeal,
                                GamerGold,
                                GamerPurple
                            )
                        ),
                        center = center,
                        radius = radius,
                        alpha = 0.5f
                    )
                }

                // Anillo interior rotando en dirección opuesta
                Canvas(
                    modifier = Modifier
                        .size(200.dp)
                        .rotate(-rotation * 0.7f)
                        .alpha(0.4f)
                ) {
                    val center = Offset(size.width / 2, size.height / 2)
                    val radius = size.minDimension / 2

                    drawCircle(
                        brush = Brush.sweepGradient(
                            colors = listOf(
                                GamerTeal,
                                GamerPurple,
                                GamerTeal
                            )
                        ),
                        center = center,
                        radius = radius,
                        alpha = 0.3f
                    )
                }

                // Brillo pulsante
                Box(
                    modifier = Modifier
                        .size(180.dp)
                        .scale(scale * pulseScale)
                        .background(
                            brush = Brush.radialGradient(
                                colors = listOf(
                                    GamerPurple.copy(alpha = 0.6f),
                                    GamerPurple.copy(alpha = 0.3f),
                                    Color.Transparent
                                )
                            ),
                            shape = androidx.compose.foundation.shape.CircleShape
                        )
                )

                // Logo principal
                Image(
                    painter = painterResource(id = R.drawable.logo_levelup_gamer),
                    contentDescription = "Level Up Gamer Logo",
                    modifier = Modifier
                        .size(150.dp)
                        .scale(scale)
                )
            }

            Spacer(modifier = Modifier.height(40.dp))

            // Nombre de la app
            Text(
                text = "LEVEL UP",
                fontSize = 36.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color.White,
                letterSpacing = 4.sp,
                modifier = Modifier.alpha(alpha)
            )

            Text(
                text = "GAMER",
                fontSize = 36.sp,
                fontWeight = FontWeight.ExtraBold,
                color = GamerTeal,
                letterSpacing = 4.sp,
                modifier = Modifier.alpha(alpha)
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Subtítulo
            Text(
                text = "Tu tienda gaming favorita",
                fontSize = 16.sp,
                fontWeight = FontWeight.Light,
                color = GamerGold,
                modifier = Modifier.alpha(alpha)
            )

            Spacer(modifier = Modifier.height(60.dp))

            // Indicador de carga animado
            LoadingDots(alpha = alpha)
        }
    }
}

@Composable
private fun AnimatedParticles(offset: Float) {
    val particles = remember {
        List(20) {
            Particle(
                x = Random.nextFloat(),
                y = Random.nextFloat(),
                size = Random.nextFloat() * 4f + 2f,
                speed = Random.nextFloat() * 0.5f + 0.2f
            )
        }
    }

    Canvas(modifier = Modifier.fillMaxSize()) {
        particles.forEach { particle ->
            val x = size.width * particle.x
            val y = (size.height * particle.y + offset * size.height * particle.speed) % size.height

            drawCircle(
                color = Color(0xFFFFD700).copy(alpha = 0.3f),
                radius = particle.size,
                center = Offset(x, y)
            )
        }
    }
}

@Composable
private fun LoadingDots(alpha: Float) {
    val infiniteTransition = rememberInfiniteTransition(label = "dots")

    Row(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.alpha(alpha)
    ) {
        repeat(3) { index ->
            val dotScale by infiniteTransition.animateFloat(
                initialValue = 0.5f,
                targetValue = 1f,
                animationSpec = infiniteRepeatable(
                    animation = tween(
                        durationMillis = 600,
                        delayMillis = index * 200,
                        easing = FastOutSlowInEasing
                    ),
                    repeatMode = RepeatMode.Reverse
                ),
                label = "dot_$index"
            )

            Box(
                modifier = Modifier
                    .size(14.dp)
                    .scale(dotScale)
                    .background(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                GamerGold,
                                GamerGold.copy(alpha = 0.5f)
                            )
                        ),
                        shape = androidx.compose.foundation.shape.CircleShape
                    )
            )
        }
    }
}

private data class Particle(
    val x: Float,
    val y: Float,
    val size: Float,
    val speed: Float
)

