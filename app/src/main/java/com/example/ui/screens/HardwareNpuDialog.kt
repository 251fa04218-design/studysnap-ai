package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BatteryChargingFull
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.CloudOff
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Memory
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.model.NpuTelemetry
import com.example.ui.theme.HexagonCyan
import com.example.ui.theme.NpuGreen
import com.example.ui.theme.SnapdragonRed
import com.example.ui.theme.SnapdragonRedBright
import com.example.ui.theme.TechNavyBorder
import com.example.ui.theme.TechNavyCard
import com.example.ui.theme.TechNavySurface
import com.example.ui.theme.TextSecondaryDark

@Composable
fun HardwareNpuDialog(
    telemetry: NpuTelemetry,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .border(
                    1.dp,
                    Brush.linearGradient(
                        listOf(SnapdragonRed, HexagonCyan, TechNavyBorder)
                    ),
                    RoundedCornerShape(20.dp)
                )
                .testTag("hardware_npu_dialog"),
            color = TechNavyCard,
            tonalElevation = 8.dp
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(SnapdragonRed),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Memory,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "Snapdragon® X NPU Engine",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Text(
                                text = "HP PC On-Device AI Architecture",
                                style = MaterialTheme.typography.bodySmall,
                                color = HexagonCyan
                            )
                        }
                    }

                    IconButton(onClick = onDismiss) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = TextSecondaryDark
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Primary NPU metrics grid
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    NpuMetricBadge(
                        title = "Compute Capacity",
                        value = "45 TOPS",
                        subtext = "Hexagon™ NPU",
                        icon = Icons.Default.Bolt,
                        tint = SnapdragonRedBright,
                        modifier = Modifier.weight(1f)
                    )
                    NpuMetricBadge(
                        title = "Inference Speed",
                        value = "${telemetry.localTokensPerSec} t/s",
                        subtext = "Sub-20ms latency",
                        icon = Icons.Default.Speed,
                        tint = HexagonCyan,
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    NpuMetricBadge(
                        title = "Air-Gapped Privacy",
                        value = "0 KB Upload",
                        subtext = "100% On-Device",
                        icon = Icons.Default.Lock,
                        tint = NpuGreen,
                        modifier = Modifier.weight(1f)
                    )
                    NpuMetricBadge(
                        title = "Power Envelope",
                        value = "${telemetry.powerEfficiencyWatts}W",
                        subtext = "All-Day Battery",
                        icon = Icons.Default.BatteryChargingFull,
                        tint = Color(0xFFFFB703),
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = "Why Snapdragon On-Device AI Wins for Students",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )

                Spacer(modifier = Modifier.height(10.dp))

                ComparisonRow(
                    label = "Educational Privacy",
                    onDevice = "Air-gapped local memory. Unpublished papers & lecture audio stay private.",
                    cloud = "Uploaded to remote third-party servers with telemetry logging.",
                    isAdvantage = true
                )

                Spacer(modifier = Modifier.height(8.dp))

                ComparisonRow(
                    label = "Offline Reliability",
                    onDevice = "Works 100% offline in lecture halls, planes, or campus dead-zones.",
                    cloud = "Requires active broadband; fails when Wi-Fi is spotty or overloaded.",
                    isAdvantage = true
                )

                Spacer(modifier = Modifier.height(8.dp))

                ComparisonRow(
                    label = "Thermal & Battery",
                    onDevice = "Snapdragon Hexagon draws <3W without fans or heat throttling.",
                    cloud = "Continuous socket polling drains mobile battery prematurely.",
                    isAdvantage = true
                )

                Spacer(modifier = Modifier.height(16.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .background(TechNavySurface)
                        .padding(12.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.CloudOff,
                            contentDescription = null,
                            tint = HexagonCyan,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "Model: Quantized 4-Bit Neural Model running directly on Snapdragon Hexagon Tensor Accelerator with hardware vector instructions.",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextSecondaryDark,
                            lineHeight = 16.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                Button(
                    onClick = onDismiss,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(46.dp)
                        .testTag("dismiss_npu_dialog_button"),
                    colors = ButtonDefaults.buttonColors(containerColor = SnapdragonRed),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text("Got It", fontWeight = FontWeight.Bold, color = Color.White)
                }
            }
        }
    }
}

@Composable
private fun NpuMetricBadge(
    title: String,
    value: String,
    subtext: String,
    icon: ImageVector,
    tint: Color,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(TechNavySurface)
            .border(1.dp, TechNavyBorder, RoundedCornerShape(12.dp))
            .padding(12.dp)
    ) {
        Column {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = title,
                    fontSize = 11.sp,
                    color = TextSecondaryDark
                )
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = tint,
                    modifier = Modifier.size(16.dp)
                )
            }
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = value,
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Text(
                text = subtext,
                fontSize = 10.sp,
                color = tint
            )
        }
    }
}

@Composable
private fun ComparisonRow(
    label: String,
    onDevice: String,
    cloud: String,
    isAdvantage: Boolean
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(TechNavySurface.copy(alpha = 0.6f))
            .padding(10.dp)
    ) {
        Text(
            text = label,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
            color = HexagonCyan
        )
        Spacer(modifier = Modifier.height(4.dp))
        Row(verticalAlignment = Alignment.Top) {
            Text(
                text = "✓ On-Device:",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = NpuGreen,
                modifier = Modifier.width(80.dp)
            )
            Text(
                text = onDevice,
                fontSize = 11.sp,
                color = Color.White
            )
        }
        Spacer(modifier = Modifier.height(2.dp))
        Row(verticalAlignment = Alignment.Top) {
            Text(
                text = "✗ Cloud AI:",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFEF5350),
                modifier = Modifier.width(80.dp)
            )
            Text(
                text = cloud,
                fontSize = 11.sp,
                color = TextSecondaryDark
            )
        }
    }
}
