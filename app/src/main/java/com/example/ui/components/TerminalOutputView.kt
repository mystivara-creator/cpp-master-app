package com.example.ui.components

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Terminal
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun TerminalOutputView(
    output: String,
    executionTimeMs: Long? = null,
    isSuccess: Boolean = true,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val clipboardManager = LocalClipboardManager.current
    val scrollState = rememberScrollState()
    val colorScheme = MaterialTheme.colorScheme

    OutlinedCard(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.outlinedCardColors(
            containerColor = colorScheme.surfaceContainerLow
        ),
        border = BorderStroke(
            width = 1.dp,
            color = if (isSuccess) colorScheme.outlineVariant else colorScheme.error.copy(alpha = 0.5f)
        )
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            // Minimalist Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(colorScheme.surfaceContainer)
                    .padding(horizontal = 12.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Terminal,
                        contentDescription = null,
                        tint = if (isSuccess) colorScheme.primary else colorScheme.error,
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = "Keluaran Terminal",
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Medium,
                            color = colorScheme.onSurfaceVariant
                        )
                    )
                    if (executionTimeMs != null) {
                        Text(
                            text = "(${executionTimeMs} ms)",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontFamily = FontFamily.Monospace,
                                color = colorScheme.onSurfaceVariant
                            )
                        )
                    }
                }

                IconButton(
                    onClick = {
                        clipboardManager.setText(AnnotatedString(output))
                        Toast.makeText(context, "Output disalin", Toast.LENGTH_SHORT).show()
                    },
                    modifier = Modifier
                        .size(28.dp)
                        .testTag("terminal_copy_btn")
                ) {
                    Icon(
                        imageVector = Icons.Default.ContentCopy,
                        contentDescription = "Salin Output",
                        tint = colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(14.dp)
                    )
                }
            }

            HorizontalDivider(color = colorScheme.outlineVariant.copy(alpha = 0.6f))

            // Output Text Area
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 72.dp, max = 220.dp)
                    .verticalScroll(scrollState)
                    .padding(12.dp)
            ) {
                if (output.isBlank()) {
                    Text(
                        text = "$ ./a.out\n(Menunggu program dijalankan...)",
                        style = TextStyle(
                            fontFamily = FontFamily.Monospace,
                            fontSize = 12.sp,
                            lineHeight = 18.sp,
                            color = colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                        )
                    )
                } else {
                    Text(
                        text = output,
                        style = TextStyle(
                            fontFamily = FontFamily.Monospace,
                            fontSize = 13.sp,
                            lineHeight = 18.sp,
                            color = if (isSuccess) colorScheme.onSurface else colorScheme.error
                        ),
                        modifier = Modifier.testTag("terminal_output_text")
                    )
                }
            }
        }
    }
}
