package com.cosmic_struck.stellar.classroom.presentation.screens

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.cosmic_struck.stellar.R
import com.cosmic_struck.stellar.classroom.classroomModel.presentation.ClassroomModelScreenViewModel
import com.cosmic_struck.stellar.classroom.presentation.components.SceneViewCard
import com.cosmic_struck.stellar.common.util.Rajdhani
import com.cosmic_struck.stellar.ui.theme.ButtonPressed

@Composable
fun ClassroomModelScreen(
    viewModel: ClassroomModelScreenViewModel = hiltViewModel(),
    modifier: Modifier = Modifier) {

    val state = viewModel.state.value
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ){
        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            SceneViewCard()

            Box(
                modifier = Modifier
                    .fillMaxWidth()
            ){
                Button(
                    onClick = {},
                    colors = ButtonDefaults.buttonColors(
                        containerColor = ButtonPressed,
                        contentColor = Color.White
                    ),
                    modifier = Modifier
                        .align(Alignment.Center)
                ) {
                    Text(text = "Take Quiz")
                }

                IconButton(
                    onClick = {},
                    colors = IconButtonDefaults.iconButtonColors(
                        containerColor = ButtonPressed,
                        contentColor = Color.White
                    )
                ) {
                    Icon(
                        painter = painterResource(R.drawable.scan),
                        contentDescription = "Full Screen"
                    )
                }
            }

            Card(
                modifier = Modifier
                    .shadow(
                        elevation = 20.dp,
                        spotColor = Color(0x40000000),
                        ambientColor = Color(0x40000000)
                    )
                    .border(
                        width = 1.dp,
                        color = Color(0x331E0063),
                        shape = RoundedCornerShape(size = 36.dp)
                    )
                .padding(20.dp)
                .fillMaxWidth(),
                colors = CardDefaults
                    .cardColors(
                        containerColor = Color(0x331E0063),
                        contentColor = Color.Black
                    )
            ) {
                Text(
                    text = state.modelDescription,
                    modifier = Modifier
                        .padding(10.dp),
                    fontFamily = Rajdhani,
                    fontSize = 24.sp
                )
            }
        }
    }

}