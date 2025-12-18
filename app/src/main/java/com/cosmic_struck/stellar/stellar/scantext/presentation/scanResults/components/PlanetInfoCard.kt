package com.cosmic_struck.stellar.stellar.scantext.presentation.scanResults.components


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cosmic_struck.stellar.stellar.models.domain.model.Planet
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.coil.CoilImage

@Composable
fun PlanetInfoCard(
    planet: Planet,
    modifier: Modifier = Modifier) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(280.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(Color.Black)
    ) {
        CoilImage(
            imageModel = {planet.planet_thumbnail},
            imageOptions = ImageOptions(
                contentScale = ContentScale.Crop,
            ),
            modifier = Modifier.fillMaxSize()
        )
        // Replace with your actual planet image
        // AsyncImage(
        //     model = "https://your-image-url.jpg",
        //     contentDescription = "Jupiter",
        //     modifier = Modifier.fillMaxSize(),
        //     contentScale = ContentScale.Crop
        // )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
        )

        // Gas Giant Badge
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(12.dp)
                .background(
                    color = Color(0xFFAB47BC),
                    shape = RoundedCornerShape(20.dp)
                )
                .padding(horizontal = 12.dp, vertical = 6.dp)
        ) {
            Text(
                "Gas Giant",
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                color = Color.White
            )
        }
    }

    Spacer(modifier = Modifier.height(16.dp))

    // Planet Info Card
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            // Planet Title
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    planet.planet_name,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    "✨",
                    fontSize = 20.sp
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Description
            Text(
                "Jupiter is the largest planet in our solar system, known for its Great Red Spot and numerous moons including the four Galilean satellites.",
                fontSize = 13.sp,
                color = Color.Gray,
                lineHeight = 18.sp
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Diameter Info
            PlanetInfoRow(
                icon = "🌍",
                label = "Diameter",
                value = "142,984 km"
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Temperature Info
            PlanetInfoRow(
                icon = "🌡️",
                label = "Temperature",
                value = "-108°C"
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Distance Info
            PlanetInfoRow(
                icon = "📍",
                label = "Distance from Sun",
                value = "778.5 million km"
            )
        }
    }

    Spacer(modifier = Modifier)
}

@Composable
fun PlanetInfoRow(
    icon: String,
    label: String,
    value: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .background(
                    color = Color(0xFFF3E5F5),
                    shape = RoundedCornerShape(8.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(icon, fontSize = 20.sp)
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column {
            Text(
                label,
                fontSize = 12.sp,
                color = Color.Gray,
                fontWeight = FontWeight.Medium
            )
            Text(
                value,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.Black
            )
        }
    }

}