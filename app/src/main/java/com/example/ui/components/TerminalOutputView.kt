package com.example.ui.components

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Terminal
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.CppCodeBackground
import com.example.ui.theme.CppEmeraldTertiary
import com.example.ui.theme.CppNavyCard
import com.example.ui.theme.CppRoseError
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

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

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(CppCodeBackground)
    ) {
        // Console Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(CppNavyCard)
                .padding(horizontal = 12.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Terminal,
                    contentDescription = null,
                    tint = if (isSuccess) CppEmeraldTertiary else CppRoseError,
                    modifier = Modifier.size(16.dp)
                )
                Text(
                    text = "Terminal Output",
                    style = TextStyle(
                        fontFamily = FontFamily.Monospace,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = TextPrimary
                    )
                )
                if (executionTimeMs != null) {
                    Text(
                        text = "(${executionTimeMs}ms)",
                        style = TextStyle(
                            fontFamily = FontFamily.Monospace,
                            fontSize = 11.sp,
                            color = TextSecondary
                        )
                    )
                }
            }

            IconButton(
                onClick = {
                    clipboardManager.setText(AnnotatedString(output))
                    Toast.makeText(context, "Output disalin", Toast.LENGTH_SHORT).show()
                },
                modifier = Modifier.size(28.dp).testTag("terminal_copy_btn")
            ) {
                Icon(
                    imageVector = Icons.Default.ContentCopy,
                    contentDescription = "Salin Output",
                    tint = TextSecondary,
                    modifier = Modifier.size(14.dp)
                )
            }
        }

        // Output Text Area
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 70.dp, max = 220.dp)
                .verticalScroll(scrollState)
                .padding(12.dp)
        ) {
            if (output.isBlank()) {
                Text(
                    text = "$ ./a.out\n(Menunggu program dijalankan...)",
                    style = TextStyle(
                        fontFamily = FontFamily.Monospace,
                        fontSize = 12.sp,
                        color = TextSecondary.copy(alpha = 0.6f)
                    )
                )
            } else {
                Text(
                    text = output,
                    style = TextStyle(
                        fontFamily = FontFamily.Monospace,
                        fontSize = 13.sp,
                        lineHeight = 18.sp,
                        color = if (isSuccess) Color(0xFF6EE7B7) else CppRoseError
                    ),
                    modifier = Modifier.testTag("terminal_output_text")
                )
            }
        }
    }
}
